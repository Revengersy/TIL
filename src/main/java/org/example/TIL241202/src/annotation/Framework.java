package org.example.TIL241202.src.annotation;
import java.lang.reflect.Method;
import java.util.List;

public class Framework {
    public static void main(String[] args) throws Exception {
        System.out.println("Current working directory: " + System.getProperty("user.dir"));
        initialize("src/main/java/org/example/TIL241202/src/annotation/configs");
    }

    public static void initialize(String packageName) throws Exception {
        // Load all classes in the package
        List<Class<?>> classes = ClassScanner.getAllClassesInPackage(packageName);

        System.out.println("test1");

        for (Class<?> clazz : classes) {
            if (!clazz.isAnnotationPresent(Component.class)) {
                continue;
            }

            // Create an instance of the class
            Object instance = clazz.getDeclaredConstructor().newInstance();
            System.out.println("test2");

            // Find and invoke methods annotated with @Init
            for (Method method : clazz.getDeclaredMethods()) {
                System.out.println("test2");
                if (method.isAnnotationPresent(Init.class)) {
                    method.invoke(instance);
                }
            }
        }
    }
}
