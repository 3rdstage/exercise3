package thirdstage.sirius.support.web3j;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import javax.validation.ValidationProviderResolver;
import javax.validation.Validator;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.http.HttpService;
import org.web3j.quorum.Quorum;

//@TODO Apply concurrency control to client cache
public class QuorumClientFactory{
  
  private static QuorumClientFactory factory;
  
  private MultiKeyMap<String, Quorum> cache = new MultiKeyMap<>();
  
  private Lock cacheLock = new ReentrantLock();
  
  private Logger logger = LoggerFactory.getLogger(this.getClass());
  
  private QuorumClientFactory() {
    
  }
  
  static {
    factory = new QuorumClientFactory();
  }
  
  public static QuorumClientFactory getInstance() {
    return factory;
  }
  
  
  public Quorum getQuroumClient(String addr, int port) {
    Validate.notBlank(addr, "The IP address for the Quorum server should be specified");
    Validate.inclusiveBetween(1, 65000, port, "The port number is not valid.");
    
    //@TODO Apply double checked locking instead of reentrant lock
    cacheLock.lock();
    try {
      Quorum client = this.cache.get(addr, String.valueOf(port));
      if(client == null) {
        client = Quorum.build(new HttpService(String.format("http://%s:%d", addr, port)));
        cache.put(addr, String.valueOf(port), client);
      }
      return client;
    }catch(Exception ex) {
      this.logger.error("Fail to create or cache-back Quorum client", ex);
      throw new RuntimeException(ex);
    }finally {
      cacheLock.unlock();
    }
  }

}
