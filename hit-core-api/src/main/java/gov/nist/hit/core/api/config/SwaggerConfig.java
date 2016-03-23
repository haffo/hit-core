package gov.nist.hit.core.api.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.configuration.ObjectMapperConfigured;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Predicate;



@Configuration
@EnableSwagger2
@ComponentScan({"gov.nist.hit"})
// @Import({BeanValidatorPluginsConfiguration.class})
public class SwaggerConfig {

  @Bean
  public Docket swaggerSpringMvcPlugin() {
    return new Docket(DocumentationType.SWAGGER_2)
            .groupName("business-api")
            .select()
               //Ignores controllers annotated with @CustomIgnore
              .apis(not(withClassAnnotation(CustomIgnore.class)) //Selection by RequestHandler
              .paths(paths()) // and by paths
              .build()
            .apiInfo(apiInfo())
            .securitySchemes(securitySchemes())
            .securityContext(securityContext());
  }

  // Here is an example where we select any api that matches one of these paths
  private Predicate<String> paths() {
    return or(regex("/business.*"), regex("/some.*"), regex("/contacts.*"), regex("/pet.*"),
        regex("/springsRestController.*"), regex("/test.*"));
  }



  //
  //
  // @Bean
  // public SwaggerSpringMvcPlugin customImplementation() {
  // return new SwaggerSpringMvcPlugin(this.springSwaggerConfig).apiInfo(apiInfo()).includePatterns(
  // ".*api.*");
  // }
  //
  // private ApiInfo apiInfo() {
  // ApiInfo apiInfo =
  // new ApiInfo("NIST Meaningful Use and beyond Validation Suite API", "NIST Validation tool",
  // "", "", "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0.html");
  // return apiInfo;
  // }



}
