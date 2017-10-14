package thirdstage.exercise.akka.wordstats3;

import java.net.InetAddress;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import thirdstage.exercise.akka.wordstats.StatsMessages.StatsJob;

public class StatsHttpServer2ClientTest{

   private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

   private ObjectMapper jacksonMapper;

   private HttpClient httpClient;

   private String baseUri;

   @BeforeTest
   public void beforeTest() throws Exception{
      this.jacksonMapper = new ObjectMapper();
      this.jacksonMapper.registerModule(new JaxbAnnotationModule())
      .configure(MapperFeature.AUTO_DETECT_FIELDS, false)
      .configure(MapperFeature.AUTO_DETECT_CREATORS, false)
      .configure(MapperFeature.AUTO_DETECT_GETTERS, true)
      .configure(MapperFeature.AUTO_DETECT_IS_GETTERS, true)
      .configure(MapperFeature.AUTO_DETECT_SETTERS, true);

      String addr = InetAddress.getLocalHost().getHostAddress();
      this.httpClient = HttpClients.createDefault();
      this.baseUri = "http://" + addr + ":" + StatsHttpServer2.HTTP_PORT_DEFAULT + "/" + StatsHttpServer2.APPL_NAME_DEFAULT;
   }


   @Test
   public void testSingleJsonRequest() throws Exception{

      HttpPost req = new HttpPost(this.baseUri);
      req.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);

      String id = "100";
      String text = "She's got a smile that it seems to me.";
      StatsJob job = new StatsJob(id, text);
      String body = this.jacksonMapper.writeValueAsString(job);
      HttpEntity entity = new StringEntity(body);
      req.setEntity(entity);

      HttpResponse resp = this.httpClient.execute(req);
      ProtocolVersion ver = resp.getStatusLine().getProtocolVersion();
      int statusCode = resp.getStatusLine().getStatusCode();

      Assert.assertEquals(200, statusCode);
   }

}
