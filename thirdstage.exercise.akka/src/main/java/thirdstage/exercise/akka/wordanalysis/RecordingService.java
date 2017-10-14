package thirdstage.exercise.akka.wordanalysis;

import javax.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.LoggerFactory;
import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisConnection;
import akka.actor.UntypedActor;

public class RecordingService extends UntypedActor{

   private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

   private final String parentKey;

   private final RedisConnection<String, String> conn;


   public RecordingService(@NotBlank String parentKey, @Nonnull RedisClient redisClient){
      Validate.isTrue(StringUtils.isNotBlank(parentKey), "The non-blank key shoud be specified.");
      Validate.isTrue(redisClient != null, "The redis client should be provided.");

      this.parentKey = parentKey;
      this.conn = redisClient.connect();
   }


   @Override
   public void onReceive(Object message) throws Exception{
      if(message instanceof Record){
         Record rd = (Record)message;

         String word = rd.getWord();
         if(StringUtils.isBlank(word)){
            this.logger.warn("Blank word is not expected.");
            return;
         }

         String key = parentKey + ":" + word;
         try{
            long cnt = this.conn.incr(key);
            this.logger.debug("The count for {} has incresed to {}", word, cnt);
         }catch(Exception ex){
            this.logger.error("Fail to record the word count", ex);
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
