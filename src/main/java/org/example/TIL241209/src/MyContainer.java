package org.example.TIL241209.src;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class MyContainer {
    private final Map<Class<?>, Object> beanRegistry = new HashMap<>();

    // Bean 등록
    public void registerBeans(Class<?>... classes) {
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(MyBean.class)) {
                try {
                    Object instance = clazz.getDeclaredConstructor().newInstance();
                    beanRegistry.put(clazz, instance);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to create bean for " + clazz, e);
                }
            }
        }
    }

    // 의존성 주입
    public void injectDependencies() {
        for (Object bean : beanRegistry.values()) {
            for (Field field : bean.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Inject.class)) {
                    Object dependency = beanRegistry.get(field.getType());
                    if (dependency != null) {
                        field.setAccessible(true);
                        try {
                            field.set(bean, dependency);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException("Failed to inject dependency for " + field.getName(), e);
                        }
                    }
                }
            }
        }
    }

    // Bean 가져오기
    public <T> T getBean(Class<T> clazz) {
        return clazz.cast(beanRegistry.get(clazz));
    }
}