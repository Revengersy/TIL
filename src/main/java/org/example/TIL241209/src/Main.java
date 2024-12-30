package org.example.TIL241209.src;

@MyBean
class ServiceA {
    public String doSomething() {
        return "ServiceA is working!";
    }
}

@MyBean
class ServiceB {
    @Inject
    private ServiceA serviceA;

    public String callServiceA() {
        return serviceA.doSomething();
    }
}

public class Main {
    public static void main(String[] args) {
        MyContainer container = new MyContainer();

        // 1. Bean 등록
        container.registerBeans(ServiceA.class, ServiceB.class);

        // 2. 의존성 주입
        container.injectDependencies();

        // 3. Bean 사용
        ServiceB serviceB = container.getBean(ServiceB.class);
        System.out.println(serviceB.callServiceA()); // 출력: ServiceA is working!
    }
}
