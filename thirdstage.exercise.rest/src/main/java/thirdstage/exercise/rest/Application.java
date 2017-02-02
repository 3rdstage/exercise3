package thirdstage.exercise.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@ComponentScan(basePackages = {"thirdstage.exercise.rest.roll"})
public class Application{

   public static void main(String[] args){
      ApplicationContext cntx = SpringApplication.run(Application.class, args);
   }

   @Bean
   public Docket api(){
      return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any()).paths(PathSelectors.any()).build().pathMapping("/")
            .apiInfo(metadata());

   }

   @Bean
   public UiConfiguration uiConfig(){
      return new UiConfiguration("validationUrl", "none", "alpha", "schema", UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS, false, true, 6000L);
   }

   private ApiInfo metadata(){
      return new ApiInfoBuilder().title("Sample API").description("Sample API").version("0.7").contact("halfface@chollian.net").build();
   }
}
