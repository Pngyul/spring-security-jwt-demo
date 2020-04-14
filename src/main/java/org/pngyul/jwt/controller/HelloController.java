package org.pngyul.jwt.controller;

import org.pngyul.jwt.model.Hr;
import org.pngyul.jwt.service.HrService;
import org.pngyul.jwt.utils.VerificationCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;


@RestController
public class HelloController {

    @Autowired
    HrService hrService;

    @GetMapping("/hello")
    public String hello() {
        //SecurityContextHolder.getContext()获取了一个SecurityContext
        //用户认证信息Authentication被封装成一个SecurityContext
        //而实际存储的类是SecurityContextHolderStrategy的实现类
        //ThreadLocalSecurityContextHolderStrategy
        //它使用了ThreadLocal<SecurityContext> contextHolder = new ThreadLocal();
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        return "hello";
    }

    @GetMapping("/admin/hello")
    public String admin() {
        return "admin";
    }

    @GetMapping("/user/hello")
    public String user() {
        return "user";
    }

    @GetMapping("/verifyCode")
    public void verifyCode(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        VerificationCode code = new VerificationCode();
        BufferedImage image = code.getImage();
        String text = code.getText();
        HttpSession session = req.getSession(true);
        session.setAttribute("verify_code", text);
        VerificationCode.output(image,resp.getOutputStream());
    }

    //postman中获取验证码
    @GetMapping("/code")
    public String testCerifyCode(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        VerificationCode code = new VerificationCode();
        BufferedImage image = code.getImage();
        String text = code.getText();
        HttpSession session = req.getSession(true);
        session.setAttribute("verify_code", text);
        return text;
    }

    @GetMapping("/test")
    public Hr test() {
        Hr pngyul = (Hr)hrService.loadUserByUsername("pngyul");
        return pngyul;
    }
}
