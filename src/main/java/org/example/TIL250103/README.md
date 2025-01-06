- 코드 컨벤션 추가 제안
    - Controller 컨벤션
- Http 입력값 사용
    - 사용 요소들
        - @PathVariable
            - **URL 경로의 일부를 변수로 받아올 때 사용**합니다.
            - 동적인 URL을 구성할 때 활용됩니다.
            - RESTful API에서 리소스를 식별하는 데 주로 사용됩니다.

            ```java
            @GetMapping("/users/{userId}")
            public ResponseEntity<User> getUserById(@PathVariable("userId") Long id) {
                // 로직 처리
            }
            ```

        - @RequestParam
            - **URL 쿼리 파라미터로 전달되는 데이터를 받아올 때 사용**합니다.
            - 주로 검색 조건이나 페이징 정보 등 선택적인 파라미터를 처리할 때 활용됩니다.
            - URL에서 **`?`** 다음의 쿼리 스트링을 통해 전달되는 값을 받아옵니다.

            ```java
            @GetMapping("/users")
            public ResponseEntity<List<User>> getUsers(@RequestParam("page") int page,
                                                       @RequestParam("size") int size) {
                // 로직 처리
            }
            ```

        - @RequestMapping의 Params 속성
            - **요청 매핑 시 특정한 요청 파라미터가 포함된 경우에만 매핑되도록 설정**할 수 있습니다.
            - 동일한 URL이라도 요청 파라미터에 따라 다른 메소드를 호출하고자 할 때 사용됩니다.

            ```java
            @GetMapping(value = "/users", params = "type=admin")
            public ResponseEntity<List<User>> getAdminUsers() {
                // 관리자 사용자 목록 처리
            }
                    
            @GetMapping("/users")
            public ResponseEntity<List<User>> getAllUsers() {
                // 모든 사용자 목록 처리
            }
            ```

        - @RequestHeader

            ```java
            @GetMapping("/resource")
            public ResponseEntity<Resource> getResource(@RequestHeader("User-Agent") String userAgent) {
                // 로직 처리
            }
            ```

        - @CookieValue

            ```java
            @GetMapping("/welcome")
            public String welcome(@CookieValue(value = "sessionId", required = false) String sessionId) {
                // 로직 처리
            }
            ```

        - @RequestBody
            - **HTTP 요청의 본문(body)에 담긴 데이터를 받아올 때 사용**합니다.
            - 주로 POST나 PUT 요청에서 JSON이나 XML 데이터를 처리할 때 활용됩니다.

            ```java
            @PostMapping("/users")
            public ResponseEntity<User> createUser(@RequestBody User user) {
                // 사용자 생성 로직
            }
            ```

### **@PathVariable vs @RequestParam**
        
- **@PathVariable**은 **리소스를 식별**하는 데 사용됩니다.
    - 예: 특정 사용자 조회 **`/users/{userId}`**
- **@RequestParam**은 **리소스를 조회하거나 동작을 수행할 때 필요한 추가적인 옵션이나 필터링 조건**을 전달할 때 사용합니다.
    - 예: 사용자 목록 조회 시 페이지 정보 **`/users?page=1&size=10`**
- **@RequestMapping의 params 속성**
    - 동일한 URL 경로에서 **요청 파라미터의 존재나 값에 따라 다른 메소드를 매핑**하고자 할 때 사용합니다.
    - 이는 **오버로딩된 URL**을 처리하거나, 특정 조건에 따라 **다른 로직을 실행**하고자 할 때 유용합니다.
    - 하지만 복잡성을 증가시킬 수 있으므로 필요한 경우에만 사용합니다.
- 한방 예시
            
    ```java
    @RestController
    @RequestMapping("/api/users")
    public class UserController {
            
        // 특정 사용자 조회
        @GetMapping("/{userId}")
        public ResponseEntity<User> getUser(@PathVariable Long userId) {
            // 로직 처리
        }
            
        // 사용자 목록 조회 (검색 및 페이징)
        @GetMapping
        public ResponseEntity<List<User>> getUsers(@RequestParam(required = false) String name,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
            // 로직 처리
        }
            
        // 사용자 생성
        @PostMapping
        public ResponseEntity<User> createUser(@RequestBody User user) {
            // 로직 처리
        }
            
        // 사용자 수정
        @PutMapping("/{userId}")
        public ResponseEntity<User> updateUser(@PathVariable Long userId,
                                               @RequestBody User user) {
            // 로직 처리
        }
            
        // 사용자 삭제
        @DeleteMapping("/{userId}")
        public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
            // 로직 처리
        }
    }
    ```
            
- 입력값 검증
- 사용 요소들
    - @ModelAttribute
        - **요청 파라미터나 폼 데이터를 객체에 바인딩하고, 모델에 데이터를 추가할 때 사용**합니다.
        - 컨트롤러 메소드의 **파라미터**나 **클래스의 메소드**에 적용됩니다.
        - **데이터 바인딩**과 **모델 속성 추가** 두 가지 역할을 수행합니다.
                
        ```java
        @PostMapping("/submit")
        public String submitForm(@ModelAttribute("user") User user) {
            // 요청 데이터가 User 객체에 바인딩됨
            return "result";
        }
        ```
                
    - @Valid와 @Validated
                
        ### **@Valid**
                
        - **자바 표준**인 **Bean Validation**에서 제공하는 검증 애노테이션입니다.
        - 검증 그룹을 지원하지 않습니다.
                
        ```java
        @PostMapping("/register")
        public String registerUser(@Valid @ModelAttribute User user, BindingResult result) {
            if (result.hasErrors()) {
                return "registerForm";
            }
            // 처리 로직
            return "success";
        }
        ```
                
        ### **@Validated**
                
        - **Spring Framework**에서 제공하는 검증 애노테이션입니다.
        - **검증 그룹(Grouping Validation)을 지원**하여, 상황에 따라 다른 검증 규칙을 적용할 수 있습니다.
                
        ```java
        public interface CreateGroup {}
        public interface UpdateGroup {}
                
        @PostMapping("/create")
        public String createUser(@Validated(CreateGroup.class) @ModelAttribute User user, BindingResult result) {
            // CreateGroup에 대한 검증 수행
        }
                
        @PutMapping("/update")
        public String updateUser(@Validated(UpdateGroup.class) @ModelAttribute User user, BindingResult result) {
            // UpdateGroup에 대한 검증 수행
        }
        ```
                
    - BindingResult
        - **검증 오류 정보를 담는 객체**입니다.
        - **`@Valid`** 또는 **`@Validated`** 애노테이션과 함께 사용하여 **검증 결과를 확인**할 수 있습니다.
        - **검증 대상 객체 바로 다음 파라미터**로 선언해야 합니다.
                
        ```java
        @PostMapping("/submit")
        public String submitForm(@Valid @ModelAttribute User user, BindingResult result) {
            if (result.hasErrors()) {
                // 검증 실패 시 처리 로직
                return "form";
            }
            // 검증 성공 시 처리 로직
            return "success";
        }
        ```
                
- 규칙
    - REST API에서는 **`@RequestBody`**를 사용하여 JSON 형식의 요청 본문을 객체에 바인딩합니다.
    - **`@Valid`** 또는 **`@Validated`**를 사용하여 데이터의 **유효성 검증**을 수행합니다.
    - **글로벌 예외 처리기**를 통해 **검증 실패 시 일관된 에러 응답을 반환**합니다.
            
    ```java
    @RestController
    @RequestMapping("/api/users")
    public class UserController {
            
        // 사용자 생성
        @PostMapping
        public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
            // 사용자 생성 로직
            return ResponseEntity.ok("사용자가 생성되었습니다.");
        }
    }
    ```

## **I. 메인 코딩 표준**

1. 패키지 이름은 모두 소문자로 작성한다.

    ```java
     package com.awesome.math;
    
    ```

2. `import`를 할 때는 전체 이름을 다 적는다. (*를 사용하지 않음)

   **틀린 방식:**

    ```java
     import com.awesome.*;
    
    ```

   **올바른 방식:**

    ```java
     import foo.bar;
    
    ```

3. 클래스와 열거형을 선언할 때는 파스칼 표기법을 따른다.

    ```java
     public class PlayerManager {
    // 코드 생략}
    
    ```

    ```java
     public enum AccountType {
    // 열거형 멤버 생략}
    
    ```

4. 클래스, 멤버 변수, 메서드에는 언제나 접근 제어자를 붙인다. 단, 기본 패키지 접근 권한이 필요할 경우에는 그렇지 않는다.

    ```java
     public class Person {
         int mHeight;// 기본 (패키지) 접근 권한private int age;
    
         public int getAge() {
    // 메서드 구현 생략}
    
         private void doSomething() {
    // 메서드 구현 생략}
     }
    
    ```

5. 접근 제어자는 다른 수정자(modifier)앞에 붙인다.

   **틀린 방식:**

    ```java
     static public void doSomething() {
    // 메서드 구현 생략}
    
    ```

   **올바른 방식:**

    ```java
     public static void doSomething() {
    // 메서드 구현 생략}
    
    ```

6. 모든 메서드 이름은 카멜 표기법을 따라 짓는다.

    ```java
     public int getAge() {
    // 메서드 구현 생략}
    
    ```

7. 지역 변수와 메서드 매개변수 이름은 카멜 표기법을 따라 짓는다.

    ```java
     int age = 10;
    
     public void someMethod(int someParameter) {
         int someNumber;
     }
    
    ```

8. 메서드 이름은 동사로 시작한다.

    ```java
     public int getAge() {
    // 메서드 구현 생략}
    
    ```

9. 단, 단순히 부울(boolean) 상태를 반환하는 메서드의 동사 부분은 최대한 `is`, `can`, `has`, `should`를 사용하되 그러는 것이 부자연스러울 경우에는 상태를 나타내는 다른 3인칭 단수형 동사를 사용한다.

    ```java
    public boolean isAlive(Person person);
    public boolean hasChild(Person person);
    public boolean canAccept(Person person);
    public boolean shouldDelete(Person person);
    public boolean exists(Person person);
    
    ```

10. 상수로 사용하는 `final` 필드의 변수명은 모두 대문자로 하되 밑줄로 각 단어를 분리한다.

    ```java
     final int SOME_CONSTANT = 1;
    
    ```

11. 인터페이스의 이름?

    ```java
     
    ```

12. 열거형 멤버의 이름은 모두 대문자로 하되 밑줄로 각 단어를 분리한다.

    ```java
    public enum MyEnum {
        FUN,
        MY_AWESOME_VALUE
    }
    
    ```

13. 멤버 변수의 이름은 카멜 표기법을 따른다.

    ```java
    public class Employee {
        public String nickName;
        protected String familyName;
        private int age;
    }
    
    ```

14. 값을 반환하는 메서드의 이름은 무엇을 반환하는지 알 수 있게 짓는다.

    ```java
    public int getAge();
    
    ```

15. 단순히 반복문에 사용되는 변수가 아닌 경우엔 `i`, `e` 같은 변수명 대신 `index`, `employee` 처럼 변수에 저장되는 데이터를 한 눈에 알아볼 수 있는 변수명을 사용한다.

    ```java
    int i;// BADint a;// BADint index;// GOODint age;// GOOD
    ```

    ```java
    // GOODfor (int i = 0; i < 10; ++i) {
    
    }
    
    ```

16. 줄임말(축약어)를 변수 및 메서드 명에 사용할 때는 기타 단어들과 동일하게 사용한다. 즉, 파스칼 표기법을 따르는 경우에는 오직 첫 번째 글자만 대문자로 바꾸며, 카멜 표기법을 따르는 경우에는 두 번째 단어부터 첫 번째 글자만 대문자로 바꾼다.

    ```java
    int orderId
    String httpAddress;
    String myHttp;
    
    ```

17. `public` 멤버 변수 대신 getter와 setter 메서드를 사용한다.

    **틀린 방식:**

    ```java
    public class Employee {
        public String Name;
    }
    
    ```

    **올바른 방식:**

    ```java
    public class Employee {
        private String name;
    
        public String getName();
        public String setName(String name);
    }
    
    ```

18. (Lombok) @Data는 쓰지 않는다. @Getter를 쓰고, Setter는 필요한 경우만 직접 구현한다.

    **틀린 방식:**

    ```java
    
    ```

    **올바른 방식:**

    ```java
    
    ```

19. (Spring) 의존성 주입을 받는 경우, 해당 멤버변수를 final로 선언하고 `@RequiredArgsConstructor`로 선언한다.

    **틀린 방식:**

    ```java
    
    public class UserController {
    	@Autowired
    	private final UserService userService;
    }
    
    @Injected
    ```

    **올바른 방식:**

    ```java
    @RequiredArgsConstructor
    public class UserController {
    	private final UserService userService;
    }
    ```

20. 지역 변수를 선언할 때는 그 지역 변수를 처음 사용하는 코드와 최대한 가까이 선언하는 것을 원칙으로 한다.
21. `double`이 반드시 필요한 경우가 아닌 이상 부동 소수점 값에 `f`를 붙여준다.

    ```java
    float f = 0.5f;
    
    ```

22. `switch` 문에 언제나 `default:` 케이스를 넣는다.

    ```java
    switch (number) {
        case 0:
            ...
            break;
        default:
            break;
    }
    
    ```

23. 향상된 switch문 lambda 식으로 사용한다

    **한 줄 방식:**

    ```java
    // java 14부터 추가된 기능임에 유의
    
    switch (val) {
    	case 1 -> System.out.println("One");
    	case 2 -> System.out.println("Two");
    	default -> System.out.println("Any");
    }
    ```

    **여러 줄 방식:**

    ```java
    
    ```

24. `switch` 문에서 `default:` 케이스가 절대 실행될 일이 없는 경우, `default:` 안에 `assert (false)` 란 코드를 추가하거나 예외를 던진다.

    ```java
    switch (type) {
        case 1:
            ...
            break;
        default:
            assert (false) : "unknown type";
            break;
    }
    
    ```

    또는

    ```java
    switch (type) {
        case 1:
            ...
            break;
        default:
            throw new IllegalArgumentException("unknown type");
            break;
    }
    
    ```

25. 재귀 메서드는 이름 뒤에 `Recursive`를 붙인다.

    ```java
    public void fibonacciRecursive();
    
    ```

26. 클래스 안에서 멤버 변수와 메서드의 등장 순서는 다음을 따른다.

    ```
    
    a. public 멤버 변수
    b. default 멤버 변수
    c. protected 멤버 변수
    d. private 멤버 변수
    e. 생성자
    f. public 메서드
    g. default 메서드
    h. protected 메서드
    i. private 메서드
    
    ```

27. 대부분의 경우 메서드 오버로딩을 피한다.

    **틀린 방식:**

    ```java
    public Anim getAnim(int index);
    public Anim getAnim(String name);
    
    ```

    **올바른 방식:**

    ```java
    public Anim getAnimByIndex(int index);
    public Anim getAnimByName(String name);
    
    ```

28. 클래스는 각각 독립된 소스 파일에 있어야 한다. 단, 작은 클래스 몇 개를 한 파일 안에 같이 넣어두는 것이 상식적일 경우 예외를 허용한다.
29. 파일 이름은 대소문자까지 포함해서 반드시 클래스 이름과 일치해야 한다.

    ```java
    // file: PlayerAnimation.javapublic class PlayerAnimation {
    
        private class InnerClass1 {
        }
    
        private class InnerClass2 {
        }
    }
    
    ```


참조: [**https://hseungyeon.tistory.com/453**](https://hseungyeon.tistory.com/453)

- 임시
    1. 특정 조건이 반드시 충족되어야 한다고 가정(assertion)하고 짠 코드 모든 곳에 `assert`를 사용한다. `assert`는 복구 불가능한 조건이다.(예: 대부분의 메서드는 다음과 같은 `assert`를 가질 수도… `assert (parameter != null)`)
    2. `assert`를 사용할 때는 표현식을 소괄호로 감싼다.

       **틀린 방식:**

        ```java
        assert x > 5 && x < 0 : "Custom message";
        
        ```

       **올바른 방식:**

        ```java
        assert (x > 5 && x < 0) : "Custom message"
        
        ```

    3. 비트 플래그 열거형은 이름 뒤에 `Flags`를 붙인다.

        ```java
        public enum VisibilityFlags {
        // 플래그들 생략}
        
        ```

    4. 변수 가리기(variable shadowing)는 허용하지 않는다. 이 규칙에 대한 유일한 예외는 멤버 변수와 생성자/setter 매개변수에 동일한 이름을 사용할 때이다.

        ```java
        public class SomeClass {
            private int count = 5;
        
            public void func() {
                for (int count = 0; count != 10; ++count) {
                    System.out.println(count);
                    System.out.println(this.count);
                }
            }
        }
        
        ```

    5. `var` 키워드를 사용하지 않으려 노력한다. 단, 대입문의 우항에서 자료형이 명확하게 드러나는 경우, 또는 데이터형이 중요하지 않은 경우는 예외를 허용한다.. `IIterable`/`ICollection`에 `var`를 사용하거나 우항의 `new` 키워드를 통해 어떤 개체가 생성되는지 알 수 있는 등이 허용되는 경우의 좋은 예이다.

        ```java
        var text = "string obviously";// Okayvar age = 28;// Okayvar employee = new Employee();// Okayvar accountNumber1 = getAccountNumber();// BADint accountNumber2 = getAccountNumber();// GOOD
        ```

    6. 외부로부터 들어오는 데이터의 유효성은 외부/내부 경계가 바뀌는 곳에서 검증(validate)하고 문제가 있을 경우 내부 메서드로 전달하기 전에 반환해 버린다. 이는 경계를 넘어 내부로 들어온 모든 데이터는 유효하다고 가정한다는 뜻이다.
    7. 따라서 내부 메서드에서 예외(익셉션)을 던지지 않으려 노력한다. 예외는 경계에서만 처리하는 것을 원칙으로 한다.
    8. 위 규칙의 예외: `enum`형을 `switch`문에서 처리할 때 실수로 처리 안 한 `enum` 값을 찾기 위해 `default:` 케이스에서 예외를 던지는 것은 허용

        ```java
        switch (accountType) {
            case AccountType.PERSONAL:
                return something;
            case AccountType.BUSINESS:
                return somethingElse;
            default:
                throw new IllegalArgumentException("unknown type");
        }
        
        ```

    9. 메서드의 매개변수로 `null`을 허용하지 않는 것을 추구한다. 특히 `public` 메서드일 경우 더욱 그러하다.
    10. `null` 매개변수를 사용할 경우 변수명 뒤에 `OrNull`를 붙인다.

        ```java
        public Anim getAnim(String nameOrNull) {
            ...
        }
        
        ```

    11. 메서드에서 `null`을 반환하지 않는 것을 추구한다. 특히 `public` 메서드일 경우 더욱 그러하다. 그러나 때로는 예외를 던지는 것을 방지하기 위해 그래야 할 경우도 있다.
    12. 메서드에서 `null`을 반환할 때는 메서드 이름 뒤에 `OrNull`을 붙인다.

        ```java
        public String getNameOrNull();
        
        ```

    13. 메서드를 오버라이딩할 때는 언제나 `@Override` 어노테이션을 붙인다.

  ## **II. 소스 코드 포맷팅**

    1. 탭(tab)은 IntelliJ의 기본 값을 사용한다. 다른 IDE를 사용할 경우에는 실제 탭 문자 대신 띄어쓰기 4칸을 사용한다.
    2. 중괄호 (`{}`) 는 새로운 줄에서 열지 않으나 닫을 때는 새로운 줄에서 닫는다. 단, 다음 항목의 예외는 허용한다.

        ```java
         public void myMethod() {
             while (expression) {
        // 코드 생략}
        
             try {
        // 코드 생략} catch (ExceptionClass ex) {
        // 코드 생략}
         }
        
        ```

    3. `if`, `if-else`, `if-else-if-else` 문은 다음의 중괄호 스타일을 사용한다.

        ```java
         if (expression) {
        // 코드 생략} else if (expression) {
        // 코드 생략} else {
        // 코드 생략}
        
        ```

    4. 중괄호 안(`{}`)에 코드가 한 줄만 있더라도 반드시 중괄호를 사용한다.

        ```java
         if (!alive) {
             return;
         }
        
        ```

    5. 한 줄에 변수 하나만 선언한다.

       **틀린 방식:**

        ```java
         int counter = 0, index = 0;
        
        ```

       **올바른 방식:**

        ```java
         int counter = 0;
         int index = 0;
        ```