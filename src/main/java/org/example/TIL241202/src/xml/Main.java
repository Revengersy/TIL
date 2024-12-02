package org.example.TIL241202.src.xml;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        try {
            // XML 파일 읽기
            File xmlFile = new File("src/main/java/org/example/TIL241202/src/web.xml");
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

            // 서블릿 인스턴스화 및 요청 처리 모사
            if (servletClass != null && urlPattern != null) {
                System.out.println("Loading Servlet: " + servletClass);
                System.out.println("Mapping URL: " + urlPattern);

                // 서블릿 호출 모사
                MockServlet servlet = new MockServlet();
                String requestUrl = "/example"; // 가상의 요청 URL
                StringBuilder response = new StringBuilder();

                System.out.println("Processing request for URL: " + requestUrl);
                servlet.service(requestUrl, response);

                // 응답 출력
                System.out.println("Response:");
                System.out.println(response.toString());
            } else {
                System.out.println("Invalid XML configuration!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
