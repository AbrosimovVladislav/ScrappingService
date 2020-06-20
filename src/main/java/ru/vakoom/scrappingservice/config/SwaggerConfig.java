package ru.vakoom.scrappingservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.ant("/*"))
                .apis(basePackage("ru.vakoom.scrappingservice.web"))
                .build()
                .apiInfo(apiDetails());
    }

    private ApiInfo apiDetails() {
        return new ApiInfo(
                "Scrapping service API",
                "Scrapping Service BackEnd app. Service for Hockey Equipment Aggregator 'Your Hockey'",
                "0.1",
                "No terms of use",
                new Contact("Your Hockey", "http://yourhockey.ru", "-"),
                "API License",
                "-",
                Collections.emptyList()
        );
    }

}
