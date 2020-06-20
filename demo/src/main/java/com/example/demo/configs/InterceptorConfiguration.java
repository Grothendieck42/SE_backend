package com.example.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.example.demo.interceptors.*;
import com.example.demo.repositories.AuthorityRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.repositories.SqlSessionRepository;
import com.example.demo.services.AuthorizationService;

import java.util.List;

@Configuration
public class InterceptorConfiguration {
    private final UserRepository userRepository;
    private final SqlSessionRepository sqlSessionRepository;
    private final AuthorizationService authorizationService;

    @Autowired
    public InterceptorConfiguration(UserRepository userRepository, SqlSessionRepository sqlSessionRepository, 
                                    AuthorizationService authorizationService) {
        this.userRepository = userRepository;
        this.sqlSessionRepository = sqlSessionRepository;
        this.authorizationService = authorizationService;
    }

    @Bean
    public WebMvcConfigurer addAuthorizationInterceptor() {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new AuthorizationInterceptor(sqlSessionRepository, userRepository, authorizationService));
            }
        };
    }

    @Bean
    public WebMvcConfigurer addUserArgumentSolver() {
        return new WebMvcConfigurer() {
            @Override
            public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
                resolvers.add(new CurrentUserInterceptor(userRepository));
            }
        };
    }
}
