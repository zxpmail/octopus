package org.zhouxp.octopus.framework.test.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zhouxp.octopus.framework.test.web.model.User;
import org.zhouxp.octopus.framework.web.annotations.NoApiResult;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2025-07-11 17:47:12
 *
 * @author zhouxp
 */
@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {
    @GetMapping("/user")
    public User getUser() {
        return new User().setAge(25).setName("zhouxp").setEmail("zhouxp@163.com");
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello world";
    }

    @GetMapping("/hello1")
    @NoApiResult
    public String hello1() {
        return "hello world";
    }

    @GetMapping("/hello2")
    @NoApiResult
    public void hello2() {
        System.out.println("hello world");
    }

    @GetMapping("/hello3")
    public void hello3() {
        throw new RuntimeException("hello world");
    }
}
