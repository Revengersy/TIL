package org.example.TIL241209.src3;

class MyController {
    @GetMapping("/hello")
    public String hello() {
        return "Hello from Reflection!";
    }

    @GetMapping("/greet")
    public String greet() {
        return "Greetings!";
    }
}