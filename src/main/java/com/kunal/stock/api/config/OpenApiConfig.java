package com.kunal.stock.api.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Kumar.Kunal
 * @project Stock API
 */

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Stock Application API")
                        .description("Stock API Application Documented on OpenApi 3")
                        .termsOfService("terms")
                        .contact(new Contact().email("kkunal2005@gmail.com"))
                        .license(new License().name("GNU"))
                        .version("1.0")
                );
    }
}
