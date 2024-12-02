package org.example.TIL241202.src.xml;


public class MockServlet {
    public void service(String request, StringBuilder response) {
        // 간단한 요청 처리 모사
        if ("/example".equals(request)) {
            response.append("<html><body><h1>Hello, this is a mock servlet response!</h1></body></html>");
        } else {
            response.append("<html><body><h1>404 Not Found</h1></body></html>");
        }
    }
}
