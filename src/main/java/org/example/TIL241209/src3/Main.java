package org.example.TIL241209.src3;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException {
        // Create an HTTP server
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Map to store URL and corresponding handler method
        Map<String, Method> urlToMethodMap = new HashMap<>();
        Map<String, Object> urlToInstanceMap = new HashMap<>();

        // Scan controller class
        Class<?> controllerClass = MyController.class;
        Object controllerInstance;
        try {
            controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
            System.out.println("Controller instance created: " + controllerInstance);
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate controller", e);
        }

        // Register methods annotated with @GetMapping
        for (Method method : controllerClass.getDeclaredMethods()) {
            System.out.println("Method: " + method);
            if (method.isAnnotationPresent(GetMapping.class)) {
                GetMapping getMapping = method.getAnnotation(GetMapping.class);
                String url = getMapping.value();
                urlToMethodMap.put(url, method);
                urlToInstanceMap.put(url, controllerInstance);
                System.out.println("Mapped URL [" + url + "] to method: " + method.getName());
            }
        }

        // Create a handler to process requests
        HttpHandler handler = exchange -> {
            String path = exchange.getRequestURI().getPath();
            Method method = urlToMethodMap.get(path);
            Object instance = urlToInstanceMap.get(path);

            if (method != null && instance != null) {
                try {
                    // Invoke the method and get the response
                    String response = (String) method.invoke(instance);
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    exchange.sendResponseHeaders(500, 0);
                }
            } else {
                String response = "404 Not Found";
                exchange.sendResponseHeaders(404, response.getBytes().length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
        };

        // Register the handler for all paths
        server.createContext("/", handler);
        server.start();
        System.out.println("Server started on port 8080");
    }
}