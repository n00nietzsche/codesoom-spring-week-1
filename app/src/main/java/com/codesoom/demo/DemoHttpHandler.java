package com.codesoom.demo;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class DemoHttpHandler implements HttpHandler {

    /**
     * 1주차는 HTTP 통신에 대한 이해를 위주로 코드가 진행되는듯 보인다.
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        // 요청의 3요소.
        // 1. METHOD - GET, POST, PUT/PATCH, DELETE, ...
        // 2. Path - "/", "/tasks", "/tasks/1", ...
        // 3. Headers, Body(Content)

        // METHOD 얻어보기
        String method = exchange.getRequestMethod();

        // URI(Uniform Resource Identifier) 를 통해 PATH 얻어보기
        // 참고: URI 의 한 종류가 URL 이다.
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        // 기본으로 뿌려줄 메세지
        String content = "Hello, world!";

        // 메서드와 PATH 가 일치하면 뿌려줄 메세지들
        if (method.equals("GET") && path.equals("/tasks")) {
            content = "We have no task.";
        }
        if (method.equals("POST") && path.equals("/tasks")) {
            content = "Create a new task.";
        }

        // 영어가 아닌 언어(한국어) 경우, 글자수와 바이트가 일치하지 않을 수 있으니 조심해야 한다.
        // 상태코드가 있어야 브라우저는 통신이 정상적으로 마무리됐다고 인식한다
        exchange.sendResponseHeaders(200, content.getBytes().length);

        // OutputStream 타입의 ResponseBody 얻어서 직접 Byte 화 시킨 스트링 넣어 출력해보기
        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(content.getBytes());
        // Flush 는 왜 해줘야 하는가? -> 내 문서 내 wiki 에 (JAVA -> iostream -> flush())에 있다.
        responseBody.flush();
        // iostream 은 닫아주어야 한다.
        responseBody.close();

        System.out.println(method + " " + path);
    }
}
