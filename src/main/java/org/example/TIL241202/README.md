# 어노테이션 사용의 장점

## 목차
- [1. 간결함과 명확성](#1-간결함과-명확성)
- [2. 유지보수 용이성](#2-유지보수-용이성)
- [3. 기본값 제공](#3-기본값-제공)
- [4. 의존성 주입과 DI 프레임워크의 통합](#4-의존성-주입과-di-프레임워크의-통합)
- [5.코드 예시](#5-코드-예시)

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

### 5. 코드 예시

[코드 예시](./src/xml)는 로컬 수준에서 간단하게 돌아가도록 작성된 예시이다. 이는 어노테이션이 아닌 xml.web 사용을 모사했다.
어노테이션을 사용한다면 필요 없어 지는 XML 파싱 관련 코드이다.

```java
// XML 파일 읽기
File xmlFile = new File("src/com/example/web.xml");
DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
DocumentBuilder builder = factory.newDocumentBuilder();
Document doc = builder.parse(xmlFile);
doc.getDocumentElement().normalize();

// 서블릿 클래스 찾기
NodeList servletNodes = doc.getElementsByTagName("servlet");
Node servletNode = servletNodes.item(0); // 첫 번째 서블릿 사용
String servletClass = null;

if (servletNode.getNodeType() == Node.ELEMENT_NODE) {
    Element element = (Element) servletNode;
    servletClass = element.getElementsByTagName("servlet-class").item(0).getTextContent();
}

// URL 매핑 찾기
NodeList mappingNodes = doc.getElementsByTagName("servlet-mapping");
Node mappingNode = mappingNodes.item(0);
String urlPattern = null;

if (mappingNode.getNodeType() == Node.ELEMENT_NODE) {
    Element element = (Element) mappingNode;
    urlPattern = element.getElementsByTagName("url-pattern").item(0).getTextContent();
}
```

해당 코드는 아래와 같이 대체된다.
```java
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/example")
public class MockServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        resp.getWriter().println("<h1>Hello, this is an annotated servlet response!</h1>");
    }
}
```

### 6.그러면 annotation과 Java Reflect의 무엇이 이걸 가능하게 하는데?

[코드 예시](./src/annotation)는 Reflect를 통해 Annotation을 활용하는 예제이다.


