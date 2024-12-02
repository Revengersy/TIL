package org.example.TIL241202.src.annotation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ClassScanner {
    public static List<Class<?>> getAllClassesInPackage(String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        String path = packageName.replace('.', '/');

        File directory = new File("src/" + path); // Adjust path as necessary
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    String fileName = file.getName();
                    if (fileName.endsWith(".class")) {
                        String className = packageName + '.' + fileName.replace(".class", "");
                        classes.add(Class.forName(className));
                    }
                }
            }
        }
        return classes;
    }
}
