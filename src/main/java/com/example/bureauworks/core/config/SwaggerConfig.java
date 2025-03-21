package com.example.bureauworks.core.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI api() {
		return new OpenAPI().info(
				new Info()
						.title("Api Documentation").description("bureauworks").version("1.0").termsOfService("urn:tos")
						.license(new License().name("Apache 2.0").url("http://www.apache.org/licenses/LICENSE-2.0"))
						.contact(new Contact().name("BWM").url("https://www.bureauworks.com/")));
	}
}
