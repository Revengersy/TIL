# 마크다운을 이용한 @Configuration과 @Component의 비교 및 예제

## 목차

- [1.소개](#1-소개)
- [2. @Component와 @Configuration의 차이점](#2-component와-configuration의-차이점)
    - [2.1. @Component](#21-component)
    - [2.2. @Configuration](#22-configuration)
- [3. ApplicationContext 직접 생성 및 비교 예제](#3-applicationcontext-직접-생성-및-비교-예제)
    - [3.1. 예제 코드](#31-예제-코드)
        - [ComponentBean.java](#componentbeanjava)
        - [ConfigBean.java](#configbeanjava)
        - [AppConfig.java](#appconfigjava)
        - [MainApp.java](#mainappjava)
    - [3.2. 실행 결과](#32-실행-결과)
    - [3.3. 분석 및 비교](#33-분석-및-비교)
- [4. 결론](#4-결론)
- [5. 추가 정보](#5-추가-정보)

---

## 1. 소개

Spring Boot에서 `@Component`와 `@Configuration`은 모두 스프링 컨테이너에 `빈(Bean)`을 등록하는 데 사용되지만, 그 동작 방식과 사용 목적에는 차이가 있다. 이 글에서는 `ApplicationContext`를 직접 생성하여 두 애노테이션의 빈 등록 과정을 비교하고 이해할 수 있는 예제를 소개한다.

---

## 2. @Component와 @Configuration의 차이점

### 2.1. @Component

- **빈 등록 방법**: 클래스에 `@Component`를 부여하여 컴포넌트 스캔을 통해 자동으로 빈으로 등록된다.
- **사용 목적**: 개발자가 작성한 일반 클래스(예: 서비스, 리포지토리 등)를 빈으로 등록할 때 사용한다.
- **필요 조건**: 컴포넌트 스캔이 활성화되어 있어야 하며, 스캔 대상 패키지 내에 위치해야 한다.

### 2.2. @Configuration

- **빈 등록 방법**: 클래스에 `@Configuration`을 부여하고, 내부에 `@Bean` 애노테이션이 붙은 메서드를 통해 빈을 등록한다.
- **사용 목적**: 외부 라이브러리의 클래스나 복잡한 설정이 필요한 빈을 등록할 때 사용한다.
- **특징**: 컴포넌트 스캔 없이도 `@Configuration` 클래스를 직접 등록하면 내부의 `@Bean` 메서드들이 빈으로 등록된다.

---

## 3. ApplicationContext 직접 생성 및 비교 예제

`ApplicationContext`를 직접 생성하여 `@Component`와 `@Configuration`의 빈 등록 방식을 비교한다.

### 3.1. 예제 코드

#### ComponentBean.java
```java
package com.example;

import org.springframework.stereotype.Component;

@Component
public class ComponentBean {
    public void sayHello() {
        System.out.println("Hello from ComponentBean!");
    }
}
```
#### ConfigBean.java

```java
package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigBean {

    @Bean
    public ConfiguredBean configuredBean() {
        return new ConfiguredBean();
    }
}

class ConfiguredBean {
    public void sayHello() {
        System.out.println("Hello from ConfiguredBean!");
    }
}
```

#### AppConfig.java

```java
package com.example;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.example")
public class AppConfig {
    // 컴포넌트 스캔을 통해 빈을 등록하므로 별도의 빈 설정이 필요하지 않다.
}
```

#### MainApp.java

```java
package com.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainApp {
    public static void main(String[] args) {
        // 1. 컴포넌트 스캔 없이 ApplicationContext 생성
        AnnotationConfigApplicationContext contextWithoutScan = new AnnotationConfigApplicationContext();
        contextWithoutScan.register(ConfigBean.class);
        contextWithoutScan.refresh();

        // ComponentBean은 스캔되지 않았으므로 빈으로 등록되지 않음
        try {
            ComponentBean componentBean = contextWithoutScan.getBean(ComponentBean.class);
            componentBean.sayHello();
        } catch (Exception e) {
            System.out.println("ComponentBean은 빈으로 등록되지 않았습니다 (스캔 없음).");
        }

        // ConfiguredBean은 @Configuration을 통해 빈으로 등록되었음
        ConfiguredBean configuredBean = contextWithoutScan.getBean(ConfiguredBean.class);
        configuredBean.sayHello();

        // 2. 컴포넌트 스캔을 포함하여 ApplicationContext 생성
        ApplicationContext contextWithScan = new AnnotationConfigApplicationContext(AppConfig.class);

        // ComponentBean은 스캔되어 빈으로 등록됨
        ComponentBean componentBean = contextWithScan.getBean(ComponentBean.class);
        componentBean.sayHello();

        // ConfiguredBean은 @Configuration을 통해 빈으로 등록됨
        ConfiguredBean configuredBean2 = contextWithScan.getBean(ConfiguredBean.class);
        configuredBean2.sayHello();
    }
}
```
### 3.2. 실행 결과

    ComponentBean은 빈으로 등록되지 않았습니다 (스캔 없음).
    Hello from ConfiguredBean!
    Hello from ComponentBean!
    Hello from ConfiguredBean!

---

## 4. 분석 및 비교

### 4.1. 컴포넌트 스캔 없이 ApplicationContext 생성한 경우

- `contextWithoutScan`을 생성하여 `ConfigBean`만 등록하고, `refresh()`를 호출한다.
- `ComponentBean`은 컴포넌트 스캔이 없으므로 빈으로 등록되지 않아 예외가 발생하고, 해당 메시지를 출력한다.
- `ConfiguredBean`은 `@Configuration` 클래스의 `@Bean` 메서드를 통해 빈으로 등록되었으므로 정상적으로 사용 가능하다.

### 4.2. 컴포넌트 스캔을 포함하여 ApplicationContext 생성한 경우

- `AppConfig` 클래스에 `@ComponentScan`을 설정하여 `com.example` 패키지를 스캔한다.
- `ComponentBean`이 빈으로 등록되어 정상적으로 `sayHello()` 메서드를 호출할 수 있다.
- `ConfiguredBean`도 이전과 동일하게 빈으로 등록되어 사용 가능하다.

### 4.3. 비교 결과

- **컴포넌트 스캔이 없는 경우**: `@Component`는 빈으로 등록되지 않지만, `@Configuration` 클래스를 직접 등록하면 내부의 `@Bean` 메서드를 통해 빈이 생성된다.
- **컴포넌트 스캔이 있는 경우**: `@Component`와 `@Configuration` 모두 자동으로 빈으로 등록되어 사용할 수 있다.

---

## 5. 결론

- `@Component`는 **컴포넌트 스캔이 필요**하며, 스캔 대상 패키지 내에 위치해야 빈으로 등록된다.
- `@Configuration` 클래스는 **컨텍스트에 등록되기만 하면** 컴포넌트 스캔 여부와 관계없이 내부의 `@Bean` 메서드를 통해 빈이 등록된다.
- `ApplicationContext`를 직접 생성하여 사용하는 경우, 컴포넌트 스캔을 활성화하지 않으면 `@Component`는 빈으로 등록되지 않는다.
- 실제 개발에서는 Spring Boot의 자동 설정과 컴포넌트 스캔을 통해 이러한 과정을 자동화하지만, 내부 동작 원리를 이해하기 위해서는 직접 설정을 해보는 것이 도움이 된다.

---

## 추가 정보

- **컴포넌트 스캔의 이점**: 컴포넌트 스캔을 사용하면 클래스에 애노테이션을 부여하는 것만으로 빈 등록이 가능하여 개발 생산성이 향상된다.
- **명시적 빈 등록의 이점**: 외부 라이브러리나 수정할 수 없는 클래스의 경우 `@Configuration`과 `@Bean`을 사용하여 빈으로 등록할 수 있다.
- 이러한 원리를 이해하고 상황에 맞게 애노테이션을 적용하면 Spring Framework를 더욱 효과적으로 사용할 수 있다.