package in.nit.config;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Bean
	public Docket createDocket() {
		return new Docket(DocumentationType.SWAGGER_2)//full Screen(empty);
						.select()//select rest controller
						.apis(RequestHandlerSelectors.basePackage("in.nit.controller.rest"))//using Base packege
						.build()//create Screen
						.apiInfo(getApiInfo());//description
	}
	
	@SuppressWarnings("rawtypes")
	private ApiInfo getApiInfo() {
		return new ApiInfo(
				"WAREHOUSE APP", 
				"NIT WH APP", 
				"3.2", 
				"https://nareshit.in/", 
				new Contact("Mr. SAURABH", "https://nareshit.in/", "saurabh23patre@gmail.com"), 
				"NIT LICENCE", "https://nareshit.in/", new ArrayList<VendorExtension>());
	}
}
