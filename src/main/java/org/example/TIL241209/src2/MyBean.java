package org.example.TIL241209.src2;

import org.springframework.stereotype.Component;

@Component
public class MyBean {
    public String sayHello() {
        return "Hello, I am a Bean!";
    }
}