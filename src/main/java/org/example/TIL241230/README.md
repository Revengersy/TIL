# Spring에서 @Bean 애너테이션 사용의 차이점

## 목차
- [1. 서론](#1-서론)
- [2. Bean이란 무엇인가?](#2-bean이란-무엇인가)
- [3. @Configuration 클래스에서의 @Bean 애너테이션](#3-configuration-클래스에서의-bean-애너테이션)
    - [3.1. @Bean이 있는 메서드](#31-bean이-있는-메서드)
    - [3.2. @Bean이 없는 메서드](#32-bean이-없는-메서드)
- [4. @Bean의 유무에 따른 차이점 테스트](#4-bean의-유무에-따른-차이점-테스트)
    - [4.1. 설정 클래스 작성](#41-설정-클래스-작성)
    - [4.2. 메인 클래스 작성](#42-메인-클래스-작성)
    - [4.3. 실행 결과](#43-실행-결과)
- [5. 결과 분석](#5-결과-분석)
- [6. 결론](#6-결론)
- [7. 추가 정보](#7-추가-정보)

---

## 1. 서론

Spring 프레임워크에서 빈(Bean)은 애플리케이션의 핵심 구성 요소로, `@Configuration` 클래스 내에서 `@Bean` 애너테이션을 통해 정의됩니다. 그러나 `@Bean` 애너테이션의 유무에 따라 메서드의 동작 방식과 스프링 컨테이너에 등록되는지 여부가 달라집니다. 이번 포스팅에서는 `@Bean` 애너테이션이 붙은 메서드와 그렇지 않은 메서드의 차이점을 예제 코드를 통해 알아보겠습니다.

---

## 2. Bean이란 무엇인가?

**Bean**은 Spring의 IoC(Inversion of Control) 컨테이너가 관리하는 객체를 의미합니다. 스프링 컨테이너는 애플리케이션 실행 시 필요한 빈을 생성하고, 의존성을 주입하며, 생명주기를 관리합니다.

---

## 3. @Configuration 클래스에서의 @Bean 애너테이션

`@Configuration`으로 애너테이션된 클래스는 빈 정의를 제공하는 설정 클래스입니다. 이 클래스 내에서 메서드에 `@Bean` 애너테이션을 붙이면, 해당 메서드의 반환 객체는 스프링 컨테이너에 빈으로 등록됩니다.

### 3.1. @Bean이 있는 메서드

- **빈 등록**: 메서드의 반환값이 스프링 컨테이너에 빈으로 등록됩니다.
- **의존성 주입 가능**: 다른 빈에서 의존성 주입을 통해 해당 빈을 사용할 수 있습니다.
- **싱글톤 보장**: 스프링은 기본적으로 빈을 싱글톤으로 관리합니다.

### 3.2. @Bean이 없는 메서드

- **일반 메서드**: 빈으로 등록되지 않는 일반 메서드로 취급됩니다.
- **내부 사용 목적**: 설정 클래스 내부에서만 사용되며, 스프링 컨테이너가 관리하지 않습니다.
- **의존성 주입 불가**: 다른 빈에서 의존성 주입을 통해 사용할 수 없습니다.

---

## 4. @Bean의 유무에 따른 차이점 테스트

### 4.1. 설정 클래스 작성

```java
package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    // @Bean이 붙은 메서드
    @Bean
    public String beanMethod() {
        return "I'm a bean!";
    }

    // @Bean이 없는 메서드
    public String regularMethod() {
        return "I'm just a regular method.";
    }
}
```

- `beanMethod()`: `@Bean` 애너테이션이 있으므로 반환값이 빈으로 등록됩니다.
- `regularMethod()`: 일반 메서드로, 빈으로 등록되지 않습니다.

### 4.2. 메인 클래스 작성

```java
package com.example.demo;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class BeanTestApplication {

    public static void main(String[] args) {
        // 애플리케이션 컨텍스트 생성 및 설정 클래스 등록
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(AppConfig.class);
        context.refresh();

        // 1. @Bean이 붙은 메서드로부터 생성된 빈을 가져오기
        String beanResult = context.getBean("beanMethod", String.class);
        System.out.println("Bean Method Result: " + beanResult);

        // 2. @Bean이 없는 메서드를 빈으로 가져오려고 시도하기
        try {
            String regularResult = context.getBean("regularMethod", String.class);
            System.out.println("Regular Method Result: " + regularResult);
        } catch (Exception e) {
            System.out.println("Failed to retrieve 'regularMethod' as a bean.");
            System.out.println("Exception: " + e.getMessage());
        }

        // 3. 설정 클래스 자체를 빈으로 가져오기
        AppConfig appConfig = context.getBean(AppConfig.class);

        // 설정 클래스의 메서드 직접 호출
        String directResult = appConfig.regularMethod();
        System.out.println("Direct Method Call Result: " + directResult);

        context.close();
    }
}
```

### 4.3. 실행 결과

    Bean Method Result: I'm a bean!
    Failed to retrieve 'regularMethod' as a bean.
    Exception: No bean named 'regularMethod' available
    Direct Method Call Result: I'm just a regular method.

---

## 5. 결과 분석

1. **`@Bean`이 있는 메서드 (`beanMethod`)**

    - `context.getBean("beanMethod", String.class)`를 통해 빈을 성공적으로 가져옵니다.
    - 출력: `Bean Method Result: I'm a bean!`

2. **`@Bean`이 없는 메서드 (`regularMethod`)**

    - `context.getBean("regularMethod", String.class)`를 호출하면 예외가 발생합니다.
    - 예외 메시지: `No bean named 'regularMethod' available`
    - 이는 `regularMethod`가 빈으로 등록되지 않았기 때문입니다.

3. **설정 클래스의 메서드 직접 호출**

    - `AppConfig` 설정 클래스를 빈으로 가져와 `regularMethod()`를 직접 호출합니다.
    - 출력: `Direct Method Call Result: I'm just a regular method.`
    - 설정 클래스는 `@Configuration`으로 인해 빈으로 등록되므로, 이를 통해 메서드 호출이 가능합니다.

---

## 6. 결론

- `@Bean` 애너테이션이 있는 메서드는 스프링 컨테이너에 빈으로 등록되며, `getBean()`을 통해 인스턴스를 가져올 수 있습니다.
- `@Bean`이 없는 메서드는 일반 메서드로 취급되어 컨테이너에 등록되지 않으며, 직접 호출해야 합니다.
- 설정 클래스 자체는 빈으로 등록되므로, 필요시 설정 클래스의 인스턴스를 가져와 메서드를 호출할 수 있습니다.
- 그러나 일반적으로 설정 클래스의 메서드를 직접 호출하는 것은 권장되지 않으며, 빈을 통해 의존성을 관리하는 것이 좋습니다.

---

## 7. 추가 정보

### 빈 등록 여부 확인

애플리케이션 컨텍스트에 등록된 빈의 이름을 확인하여 어떤 빈이 등록되었는지 알 수 있습니다.

```java
// 컨텍스트에 등록된 빈 이름 출력
System.out.println("\nRegistered Bean Names:");
for (String beanName : context.getBeanDefinitionNames()) {
    System.out.println(beanName);
}
```

**실행 결과 (일부):**

    Registered Bean Names:
    appConfig          // 설정 클래스의 빈 이름
    beanMethod         // @Bean 메서드로 등록된 빈 이름

- `regularMethod`는 빈으로 등록되지 않았음을 확인할 수 있습니다.

### 권장사항

- 빈으로 관리되어야 하는 객체는 반드시 `@Bean` 애너테이션을 사용하여 정의합니다.
- 설정 클래스의 메서드를 직접 호출하기보다는 빈을 통해 의존성을 주입받아 사용하는 것이 바람직합니다.
- `@Bean`이 없는 메서드는 빈 생성 시 필요한 부가적인 로직을 처리하는 데 사용될 수 있습니다.

---
