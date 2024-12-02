package org.example.TIL241202.src.annotation.configs;

import org.example.TIL241202.src.annotation.Component;
import org.example.TIL241202.src.annotation.Init;

@Component
public class ServiceConfig {
    @Init
    public void setupService() {
        System.out.println("Service setup complete");
    }
}