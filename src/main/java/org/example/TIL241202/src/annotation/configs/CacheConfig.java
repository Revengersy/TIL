package org.example.TIL241202.src.annotation.configs;

import org.example.TIL241202.src.annotation.Component;
import org.example.TIL241202.src.annotation.Init;

@Component
public class CacheConfig {
    @Init
    public void setupCache() {
        System.out.println("Cache setup complete");
    }
}