# MySQL 데이터베이스 연결 및 테스트 코드 간단히 따라하기


## 목차

1. [DBConnectionUtil 클래스 구현](#1-dbconnectionutil-클래스-구현)
2. [DBConnectionUtilTest 클래스 작성](#2-dbconnectionutiltest-클래스-작성)
3. [테스트 실행](#3-테스트-실행)
4. [결론](#4-결론)


## 1. DBConnectionUtil 클래스 구현

MySQL 데이터베이스에 연결하는 유틸리티 클래스를 작성한다.

- **src/main/java/com/example/util/DBConnectionUtil.java**

```java
package com.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/testdb?serverTimezone=Asia/Seoul&characterEncoding=UTF-8";
    private static final String USERNAME = "your_username";
    private static final String PASSWORD = "your_password";

    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException("데이터베이스 연결 실패", e);
        }
    }
}
```

**설명**:

- **URL**: `localhost`의 `3306` 포트에 위치한 `testdb` 데이터베이스에 접속한다.
- **USERNAME**과 **PASSWORD**: 실제 MySQL 사용자명과 비밀번호로 변경해야 한다.
- **serverTimezone**과 **characterEncoding** 파라미터는 시간대와 인코딩 설정이다.

## 2. DBConnectionUtilTest 클래스 작성

JUnit을 사용하여 데이터베이스 연결을 테스트하는 클래스를 작성한다.

- **src/test/java/com/example/util/DBConnectionUtilTest.java**

```java
package com.example.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class DBConnectionUtilTest {

    @Test
    void connection() {
        Connection connection = DBConnectionUtil.getConnection();
        assertThat(connection).isNotNull();
        log.info("데이터베이스 연결 성공: {}", connection);
    }
}
```

**설명**:

- `@Slf4j` 어노테이션을 사용하여 로깅 기능을 제공한다.
- `connection()` 테스트 메서드는 데이터베이스 연결이 성공적으로 이루어지는지 확인한다.

## 3. 테스트 실행

### 3.1. Lombok 설정

IntelliJ IDEA에서 Lombok을 사용하기 위해 설정한다.

- **Settings** > **Plugins**에서 Lombok 플러그인 설치
- **Settings** > **Build, Execution, Deployment** > **Compiler** > **Annotation Processors**에서 **Enable annotation processing** 체크

### 3.2. MySQL 데이터베이스 설정

- MySQL 서버가 실행 중인지 확인한다.
- `testdb` 데이터베이스와 해당 사용자 계정이 존재하는지 확인한다.

```sql
CREATE DATABASE testdb;
CREATE USER 'your_username'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON testdb.* TO 'your_username'@'localhost';
FLUSH PRIVILEGES;
```

### 3.3. 테스트 실행 방법

- **방법 1**: `DBConnectionUtilTest.java` 파일에서 `Run` 버튼 클릭
- **방법 2**: Gradle 탭에서 `verification` > `test` 실행
- **방법 3**: 터미널에서 `./gradlew test` 명령어 실행

**예상 결과**:

- 테스트가 성공적으로 완료되면 데이터베이스 연결 성공 로그가 출력된다.
- 실패 시 에러 메시지를 확인하고 원인을 파악한다.

## 4. 결론

이번 포스팅에서는 Java에서 MySQL 데이터베이스에 연결하고, JUnit을 사용하여 연결 테스트를 수행하는 방법을 알아보았다. 프로젝트 설정부터 의존성 추가, 클래스 구현, 테스트 실행까지의 과정을 따라 하면서 데이터베이스 연결을 이해할 수 있다

