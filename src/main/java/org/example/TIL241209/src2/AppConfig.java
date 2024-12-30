package org.example.TIL241209.src2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AppConfig {
    @Bean
    public MyBean helloBean() {
        return new MyBean();
    }
}