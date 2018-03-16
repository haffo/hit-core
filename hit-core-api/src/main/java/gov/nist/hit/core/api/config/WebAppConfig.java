/**
 * This software was developed at the National Institute of Standards and Technology by employees of
 * the Federal Government in the course of their official duties. Pursuant to title 17 Section 105
 * of the United States Code this software is not subject to copyright protection and is in the
 * public domain. This is an experimental system. NIST assumes no responsibility whatsoever for its
 * use by other parties, and makes no guarantees, expressed or implied, about its quality,
 * reliability, or any other characteristic. We would appreciate acknowledgement if the software is
 * used. This software can be redistributed and/or modified freely provided that any derivative
 * works bear some notice that they are derived from it, and any modified versions bear some notice
 * that they have been modified.
 */
package gov.nist.hit.core.api.config;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

@Configuration
@EnableWebMvc
// @ComponentScan({ "gov.nist.hit", "gov.nist.auth.hit" })
public class WebAppConfig extends WebMvcConfigurerAdapter {

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	/*
	 * Here we register the Hibernate4Module into an ObjectMapper, then set this
	 * custom-configured ObjectMapper to the MessageConverter and return it to
	 * be added to the HttpMessageConverters of our application
	 */
	public MappingJackson2HttpMessageConverter jacksonMessageConverter() {

		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
		MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter(builder.build());
		ObjectMapper objectMapper = messageConverter.getObjectMapper();
		// SimpleModule module = new SimpleModule("Stream");
		// module.addSerializer(Stream.class, new JsonSerializer<Stream>() {
		// @Override
		// public void serialize(Stream value, JsonGenerator gen,
		// SerializerProvider serializers)
		// throws IOException, JsonProcessingException {
		// serializers.findValueSerializer(Iterator.class,
		// null).serialize(value.iterator(), gen, serializers);
		//
		// }
		// });
		// objectMapper.registerModule(module);

		//
		// ObjectMapper mapper = new ObjectMapper();
		// mapper.disable(SerializationFeature.INDENT_OUTPUT);
		// mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		// // mapper.disable(SerializationFeature.FLUSH_AFTER_WRITE_VALUE);
		// // mapper.enable(SerializationFeature.USE_EQUALITY_FOR_OBJECT_ID);
		// mapper.setSerializationInclusion(Include.NON_NULL);

		// Registering Hibernate4Module to support lazy objects
		objectMapper.registerModule(new Hibernate5Module());

		return messageConverter;

	}

	// public GsonHttpMessageConverter gsonHttpMessageConverter() {
	// Gson gson =
	// new GsonBuilder().setExclusionStrategies(new
	// AnnotationExclusionStrategy()).create();
	// GsonHttpMessageConverter converter = new GsonHttpMessageConverter(gson);
	// converter.setGson(gson);
	// return converter;
	// }

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		// Here we add our custom-configured HttpMessageConverter
		converters.add(jacksonMessageConverter());
		StringHttpMessageConverter stringConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
		stringConverter.setSupportedMediaTypes(Arrays.asList( //
				MediaType.TEXT_PLAIN, //
				MediaType.TEXT_HTML, //
				MediaType.APPLICATION_JSON, MediaType.TEXT_XML, MediaType.APPLICATION_FORM_URLENCODED));
		converters.add(stringConverter);
		super.configureMessageConverters(converters);
	}

	@Bean
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(100000000);
		multipartResolver.setDefaultEncoding("utf-8");
		return multipartResolver;
	}

	// @Override
	// public void addResourceHandlers(ResourceHandlerRegistry registry) {
	// registry.addResourceHandler("/docs/**").addResourceLocations("classpath*:/docs/");
	// }
	//
	// @Bean
	// public InternalResourceViewResolver getInternalResourceViewResolver() {
	// InternalResourceViewResolver resolver = new
	// InternalResourceViewResolver();
	// resolver.setPrefix("classpath*:/docs/");
	// resolver.setSuffix("*.html");
	// return resolver;
	// }

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addRedirectViewController("/apidocs/v2/api-docs", "/v2/api-docs");
		registry.addRedirectViewController("/apidocs/configuration/ui", "/configuration/ui");
		registry.addRedirectViewController("/apidocs/configuration/security", "/configuration/security");
		registry.addRedirectViewController("/apidocs/swagger-resources", "/swagger-resources");
		registry.addRedirectViewController("/apidocs", "/apidocs/swagger-ui.html");
		registry.addRedirectViewController("/apidocs/", "/apidocs/swagger-ui.html");

	}

	// @Override
	// public void addResourceHandlers(ResourceHandlerRegistry registry) {
	// registry.addResourceHandler("swagger-ui.html").addResourceLocations(
	// "classpath:/META-INF/resources/");
	//
	// registry.addResourceHandler("/webjars/**").addResourceLocations(
	// "classpath:/META-INF/resources/webjars/");
	// }

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/apidocs/**").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

	@Bean
	public ConversionService conversionService() {
		return new DefaultConversionService();
	}

	// @Bean
	// public static PropertyPlaceholderConfigurer ppc() throws IOException {
	// PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
	// Properties properties = new Properties();
	// properties.setProperty("nullValue", "@null");
	// ppc.setProperties(properties);
	// return ppc;
	// }

}
