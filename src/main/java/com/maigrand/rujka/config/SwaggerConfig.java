package com.maigrand.rujka.config;

import com.maigrand.rujka.payload.SwaggerPageable;
import org.springframework.context.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.sql.Timestamp;
import java.util.*;

import static java.util.Collections.*;

@Configuration
@EnableSwagger2
@Import({
        springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration.class,
        springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration.class
})
public class SwaggerConfig {

    private final Set<String> producesSet = new HashSet<>();

    @Bean
    public Docket adminApi() {
        producesSet.add("application/json");

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.maigrand.rujka.controller"))
                .paths(PathSelectors.ant("/api/v1/**"))
                .build()
                .produces(producesSet)
                .useDefaultResponseMessages(false)
                .directModelSubstitute(Timestamp.class, Long.class)
                .genericModelSubstitutes(ResponseEntity.class)
                .forCodeGeneration(true)
                .securityContexts(singletonList(securityContext()))
                .securitySchemes(singletonList(apiKey()))
                .tags(new Tag("Авторизация", "", 1))
                .directModelSubstitute(Pageable.class, SwaggerPageable.class);
    }

    private ApiKey apiKey() {
        return new ApiKey("mykey", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("/api/.*"))
                .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return singletonList(
                new SecurityReference("mykey", authorizationScopes));
    }
}
