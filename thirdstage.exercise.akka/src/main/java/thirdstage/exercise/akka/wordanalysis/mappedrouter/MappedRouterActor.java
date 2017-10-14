package thirdstage.exercise.akka.wordanalysis.mappedrouter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hibernate.validator.constraints.NotBlank;
import akka.actor.ActorSystem;
import akka.actor.Address;
import akka.actor.UntypedActor;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.ClusterEvent.MemberEvent;
import akka.cluster.ClusterEvent.MemberExited;
import akka.cluster.ClusterEvent.MemberUp;
import akka.cluster.ClusterEvent.ReachableMember;
import akka.cluster.ClusterEvent.UnreachableMember;
import akka.cluster.Member;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.Router;

public class MappedRouterActor<T extends java.io.Serializable> extends UntypedActor{

   //@TODO Update to manage member list to double check the timing of router update.

   private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
   private final Cluster cluster;

   /**
    * The name of the routee actor.
    * Full name includes all the elements following the '.../user/' in the path.
    * For example, for 'akka.tcp://SampleSystem@xxx.xxx.xxx.xx/user/mallService/warehouseService/scheduleService',
    * the full name is 'mallService/warehouseService/scheduleService'
    */
   private final String routeeFullName;

   private final NodeIdResolver nodeIdResolver;

   private final KeyNodeMap<T> nodeMap = new SimpleKeyNodeMap<T>();

   @Nonnull protected KeyNodeMap<T> getKeyNodeMap(){ return this.nodeMap; }

   private final List<KeyedRoutee<T>> routees = new ArrayList<KeyedRoutee<T>>();

   @Nonnull protected List<KeyedRoutee<T>> getRoutees(){ return this.routees; }

   private final Lock routeesLock = new ReentrantLock();

   private volatile Router router = null;

   @Nullable protected Router getRouter(){ return this.router; }

   /**
    * @param nodeMap if any, will be shallow-copied, not referenced
    * @param routeeFullName
    * @param nodeIdResolver
    */
   public MappedRouterActor(@Nullable KeyNodeMap<T> nodeMap, @NotBlank String routeeFullName,
         @Nonnull NodeIdResolver nodeIdResolver){

      Validate.isTrue(StringUtils.isNotBlank(routeeFullName), "Routee name should be specified.");
      Validate.isTrue(nodeIdResolver != null, "The Node ID resovler should be provided.");

      if(nodeMap != null){
         for(Map.Entry<Key<T>, String> entry: nodeMap.getEntrySet()){
            this.nodeMap.putNodeId(entry.getKey(), entry.getValue());
         }
      }

      this.nodeIdResolver = nodeIdResolver;
      this.routeeFullName = routeeFullName;

      this.cluster = Cluster.get(this.getContext().system());
      this.cluster.subscribe(getSelf(), ClusterEvent.initialStateAsEvents(),
            MemberEvent.class, UnreachableMember.class, ReachableMember.class);
   }

   /* (non-Javadoc)
    * @see akka.actor.UntypedActor#preStart()
    */
   @Override
   public void preStart() throws Exception{
      super.preStart();
   }

   @Override
   public void onReceive(Object msg) throws Exception{

      if(msg instanceof Keyed<?>){
         if(!(router == null)){
            this.router.route(msg, this.getSender());
            this.logger.debug("Requested to route the keyed message to the router actor.");
         }
      }else if(msg instanceof MemberUp){
         MemberUp ev = (MemberUp) msg;
         this.logger.info("Member(address:{}) is up.", ev.member().address());

         //this.updateRouterWithMemberEvent(ev);
         this.addRouteesForMember(ev.member());
      }else if(msg instanceof MemberExited){
         MemberExited ev = (MemberExited) msg;
         this.logger.info("Member(address:{}) has exited.", ev.member().address());

         this.removeRouteesOfMember(ev.member());
      }else if(msg instanceof UnreachableMember){
         UnreachableMember ev = (UnreachableMember) msg;
         this.logger.info("Member(address:{}) is unreachable.", ev.member().address());

         this.removeRouteesOfMember(ev.member());
      }else if(msg instanceof ReachableMember){
         ReachableMember ev = (ReachableMember) msg;
         this.logger.info("Member(address:{}) is reachable.", ev.member().address());

         this.addRouteesForMember(ev.member());
      }else if(msg instanceof MemberEvent){
         MemberEvent ev = (MemberEvent) msg;
         this.logger.info("Member(address:{}) caused {}.", ev.member().address(), ev.getClass().getSimpleName());
      }else{
         this.unhandled(msg);
      }
   }

   private void updateRouterWithMemberEvent(@Nullable MemberEvent ev){
      if(ev == null){ return; }

      Address addr = ev.member().address();
      String nodeId = this.nodeIdResolver.resolveNodeId(ev.member());
      if(StringUtils.isBlank(nodeId)){
         this.logger.warning("Can't resolve node ID from member whose address is {}.", addr.toString());
         return;
      }

      String path = new StringBuilder().append(addr.protocol()).append("://")
            .append(addr.hostPort()).append("/user/").append(this.routeeFullName).toString();

      ActorSystem system = this.getContext().system();
      KeyedRoutee<T> routee = null;

      if(ev instanceof MemberUp){
         this.routeesLock.lock();
         try{
            for(Map.Entry<Key<T>, String> entry : this.nodeMap.getEntrySet()){
               if(nodeId.equals(entry.getValue())){
                  routee = new KeyedActorSelectionRoutee(entry.getKey(), system.actorSelection(path));
                  this.routees.add(routee);
               }
            }
         }finally{
            this.routeesLock.unlock();
         }
      }else if(ev instanceof MemberExited){
         String id = null;
         this.routeesLock.lock();
         try{
            for(int i = this.routees.size() - 1; i > -1; i--){
               id = this.nodeMap.getNodeId(this.routees.get(i).getKey());
               if(nodeId.equals(id)){ this.routees.remove(i); }
            }
         }finally{
            this.routeesLock.unlock();
         }
      }

      MappedRouterConfig2 routerFactory = new MappedRouterConfig2(this.routees);
      int n1 = (this.router == null) ? 0 : this.router.routees().size();
      this.router = routerFactory.createRouter(system);
      int n2 = this.router.routees().size();
      this.logger.info("Updated the router. The number of routees become {} from {}.", n2, n1);
   }

   private void addRouteesForMember(@Nullable Member member){
      if(member == null){
         this.logger.warning("Specified member is null, which is not expected.");
         return;
      }

      Address addr = member.address();
      String nodeId = this.nodeIdResolver.resolveNodeId(member);
      if(StringUtils.isBlank(nodeId)){
         this.logger.warning("Can't resolve node ID from member whose address is {}.", addr.toString());
         return;
      }

      String path = new StringBuilder().append(addr.protocol()).append("://")
            .append(addr.hostPort()).append("/user/").append(this.routeeFullName).toString();

      ActorSystem system = this.getContext().system();
      KeyedRoutee<T> routee = null;

      this.routeesLock.lock();
      try{
         for(Map.Entry<Key<T>, String> entry : this.nodeMap.getEntrySet()){
            if(nodeId.equals(entry.getValue())){
               routee = new KeyedActorSelectionRoutee(entry.getKey(), system.actorSelection(path));
               this.routees.add(routee);
            }
         }
      }finally{
         this.routeesLock.unlock();
      }

      this.updateRouter();
   }


   private void removeRouteesOfMember(@Nullable Member member){
      if(member == null){
         this.logger.warning("Specified member is null, which is not expected.");
         return;
      }

      Address addr = member.address();
      String nodeId = this.nodeIdResolver.resolveNodeId(member);
      if(StringUtils.isBlank(nodeId)){
         this.logger.warning("Can't resolve node ID from member whose address is {}.", addr.toString());
         return;
      }

      ActorSystem system = this.getContext().system();

      String id = null;
      this.routeesLock.lock();
      try{
         for(int i = this.routees.size() - 1; i > -1; i--){
            id = this.nodeMap.getNodeId(this.routees.get(i).getKey());
            if(nodeId.equals(id)){ this.routees.remove(i); }
         }
      }finally{
         this.routeesLock.unlock();
      }

      this.updateRouter();
   }

   private void updateRouter(){

      MappedRouterConfig2 routerFactory = new MappedRouterConfig2(this.routees);
      int n1 = (this.router == null) ? 0 : this.router.routees().size();
      this.router = routerFactory.createRouter(this.getContext().system());
      int n2 = this.router.routees().size();
      this.logger.info("Updated the router. The number of routees become {} from {}.", n2, n1);
   }
}
