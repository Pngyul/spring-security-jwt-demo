package org.pngyul.jwt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestController {

    @GetMapping("/home")
    public String hello() {
        return "/home";
    }

    @GetMapping("/emp/basic")
    public String hello1() {
        return "/emp/basic";
    }

    @GetMapping("/per/train")
    public String hello2() {
        return "/per/train";
    }

    @GetMapping("/emp/adv")
    public String hello3() {
        return "/emp/adv";
    }

    @GetMapping("/sal")
    public String hello4() {
        return "/sal";
    }

    @GetMapping("/sta")
    public String hello5() {
        return "/sta";
    }

    @GetMapping("/sys")
    public String hello6() {
        return "/sys";
    }


}
