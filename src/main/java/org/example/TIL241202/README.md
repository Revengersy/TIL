# 어노테이션 사용의 장점

## 목차
- [1. 간결함과 명확성](#1-간결함과-명확성)
- [2. 유지보수 용이성](#2-유지보수-용이성)
- [3. 기본값 제공](#3-기본값-제공)
- [4. 의존성 주입과 DI 프레임워크의 통합](#4-의존성-주입과-di-프레임워크의-통합)

### 1. 간결함과 명확성

**어노테이션 없는 경우:**

XML 파일이나 별도의 설정 클래스에 작성해야 합니다.

```xml
<!-- web.xml -->
<servlet>
    <servlet-name>exampleServlet</servlet-name>
    <servlet-class>com.example.ExampleServlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>exampleServlet</servlet-name>
    <url-pattern>/example</url-pattern>
</servlet-mapping>
```

**어노테이션 사용:**
```java
@WebServlet("/example")
public class ExampleServlet extends HttpServlet {
    // 서블릿 코드
}
```
### 2. 유지보수 용이성

**어노테이션 없는 경우:**

XML과 코드가 분리되어 있어, 두군데 모두 관리해야 하며, 설정이 분산되어 있어 누락이나 오류가 발생할 가능성이 높습니다.

**어노테이션 사용:**

어노테이션을 통해 코드와 설정이 한 곳에 있어, 수정할 사항이 코드에 직접 반영되므로 유지보수가 용이합니다.

### 3. 기본값 제공

**어노테이션 없는 경우:**

모든 설정을 명시적으로 지정해야 합니다. 기본값이 존재하지 않으므로 설정 파일이 길어질 수 있습니다.

```xml
<!-- XML에서 모든 속성을 설정해야 함 -->
<servlet>
    <servlet-name>example</servlet-name>
    <load-on-startup>1</load-on-startup>
    <!-- 기타 설정 -->
</servlet>
```
**어노테이션 사용:**

어노테이션은 기본값을 제공하므로, 필요한 경우에만 설정을 지정할 수 있습니다.

```java
@WebServlet(urlPatterns = "/example", loadOnStartup = 1)
public class ExampleServlet extends HttpServlet {
    // 서블릿 코드
}
```

### 4. 의존성 주입과 DI 프레임워크의 통합

**어노테이션 없는 경우:**

의존성 주입을 수동으로 처리해야 하며, 설정 파일에서 복잡한 의존성 관계를 관리해야 합니다.

```xml
<!-- XML로 복잡한 빈 설정 관리 -->
<bean id="myBean" class="com.example.MyBean">
    <property name="dependency" ref="dependencyBean" />
</bean>
```

**어노테이션 사용:**

프레임워크가 어노테이션을 통해 자동으로 빈을 관리하고 주입하므로, 코드가 간단해지고 오류가 줄어듭니다.

```java
@Component
public class MyBean {
    @Autowired
    private DependencyBean dependency;
    // 코드 내에서 자동으로 주입됨
}
```