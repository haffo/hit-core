package gov.nist.hit.core.api.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.mangofactory.swagger.plugin.EnableSwagger;



@Configuration
@EnableSwagger
@ComponentScan("gov.nist.hit")
public class SwaggerConfig {


  // @Autowired
  // private SpringSwaggerConfig springSwaggerConfig;
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
  // new ApiInfo("NIST HIT API", "API Documentation of the NIST HIT Application",
  // "My Apps API terms of service", "My Apps API Contact Email",
  // "My Apps API Licence Type", "My Apps API License URL");
  // return apiInfo;
  // }

}
