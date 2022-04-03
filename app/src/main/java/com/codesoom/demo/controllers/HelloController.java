package com.codesoom.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {
    // DispatcherServlet 은 스프링이 실행될 때 가장 먼저 실행되는 앱이다.
    // RequestMapping 을 통해 DispatcherServlet 에 연결해야 한다.
    @RequestMapping("/")
    public String sayHello() {
        return "Hello, world!!!";
    }
}
