package thirdstage.exercise.akka.wordanalysis;

import javax.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hibernate.validator.constraints.NotBlank;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisConnection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class TraceLogService extends UntypedActor{

   private final LoggingAdapter logger = Logging.getLogger(this.getContext().system(), this);

   private final String key;

   private final RedisConnection<String, String> conn;

   private final ObjectMapper jacksonMapper;

   public TraceLogService(@NotBlank String key,
         @Nonnull RedisClient redisClient, @Nonnull ObjectMapper jacksonMapper){
      Validate.isTrue(StringUtils.isNotBlank(key), "The key should not be blank.");
      Validate.isTrue(redisClient != null, "The valid redis client should be provided.");
      Validate.isTrue(jacksonMapper != null, "The Jasckson JSON mapper should be provided.");

      this.key = key;
      this.jacksonMapper = jacksonMapper;

      this.conn = redisClient.connect();
   }

   @Override
   public void onReceive(Object message) throws Exception{
      if(message instanceof Trace){

         Trace trace = (Trace)message;
         try{
            String traceStr = this.jacksonMapper.writeValueAsString(trace);
            conn.rpush(key, traceStr);
         }catch(Exception ex){
            this.logger.error(ex, "Fail to log trace.");
            throw ex;
         }
      }else{
         this.unhandled(message);
      }
   }

   @Override
   public void postStop(){
      if(this.conn != null){
         try{ this.conn.close(); }
         catch(Exception ex){ }
      }
   }

}
