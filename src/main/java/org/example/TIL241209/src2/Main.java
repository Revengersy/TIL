package org.example.TIL241209.src2;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
//
//@SpringBootApplication
//public class SpringTrainingApplication {
//    public static void main(String[] args) {
//        SpringApplication.run(SpringTrainingApplication.class, args);
//    }
//}


public class Main {
    public static void main(String[] args) {
        // Spring 컨테이너 생성
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // Bean 가져오기
        MyBean helloBean = context.getBean(MyBean.class);

        // Bean 메서드 호출
        System.out.println(helloBean.sayHello());

        // 컨테이너 닫기
        context.close();
    }

}