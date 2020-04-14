package org.pngyul.jwt.config.learn;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.pngyul.jwt.filter.jwt.JwtFilter;
import org.pngyul.jwt.filter.jwt.JwtLoginFilter;
import org.pngyul.jwt.model.RespBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 经过测试，Spring security默认使用session+cookie的方式认证用户
 * 登录后，删除cookie,便没有了api访问权限，需要重新登录
 */

//@Configuration
public class SecurityConfigWithStudy extends WebSecurityConfigurerAdapter {
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //角色继承
    //user拥有的权限，admin也可以
    @Bean
    RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("ROLE_admin > ROLE_user");
        return hierarchy;
    }

    //@Configuration比application.yml优先级更高，所以在这里配置了user信息，application.yml失效
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("pngyul")
                .password("$2a$10$0WjZpkSCAHKrcQ/a3.CwOuWP3arJOloYs2oecrC9oPny9HWdnZ.Ti")
                .roles("user")
                .and()
                .withUser("admin")
                .password("$2a$10$NRYzZTFPSDorqpIVXJaSxuOwITR6NJmFTJZUBkLcBK7JyRNxdfxTy")
                .roles("admin");
    }

    //静态资源等不需要拦截
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/js/**", "/css/**", "/images/**","/verifyCode");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //.antMatchers("/**").authenticated(); 下面权限配置全部失效
                //上面的覆盖下面的
                .antMatchers("/hello/**").hasRole("user")
                .antMatchers("/admin/**").hasRole("admin")
                //注意，anyRequest().authenticated()一般写在最后，之后不要再配权限信息
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginProcessingUrl("/doLogin")
                //默认情况下，登录页面和登录接口都是一样的/login
                //配置了的话，登录接口同样也是/login.html,是一个post请求
                //注意，自定义指明了loginPage，系统就不会自动生成登录页
                .loginPage("/login.html")
                .usernameParameter("name") //更改表单字段
                .passwordParameter("pswd")
                .defaultSuccessUrl("/index") //默认跳转/index,如果你之前是访问其它页面跳转到登录页，那跳转到之前的页面
                .successForwardUrl("/index") // 登录成功就跳转/index，不管你之前在哪里来

                //登录成功返回JSon
                .successHandler((req, resp, authentication) -> {
                    Object principal = authentication.getPrincipal();
                    resp.setContentType("application/json;charset=utf-8");
                    PrintWriter out = resp.getWriter();
                    out.write(new ObjectMapper().writeValueAsString(principal));
                    out.flush();
                    out.close();
                })
                //登录失败返回json
                .failureHandler((req, resp, e) -> {
                    resp.setContentType("application/json;charset=utf-8");
                    PrintWriter out = resp.getWriter();
                    out.write(e.getMessage());
                    out.flush();
                    out.close();
                })
                .permitAll()
                .and()
                .logout()
                .logoutSuccessHandler((req, resp, authentication) -> {
                    resp.setContentType("application/json;charset=utf-8");
                    PrintWriter out = resp.getWriter();
                    out.write("注销成功");
                    out.flush();
                    out.close();
                })
                .logoutSuccessUrl("/index")
                .deleteCookies()
                .clearAuthentication(true) //默认都是trued
                .invalidateHttpSession(true)

                .and()
                .addFilterBefore(new JwtLoginFilter("/doLogin", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class)
                .csrf().disable()
                //尽管你让系统生成登录页，但是你重写了该方法，那么登录页也无法访问
                //这时候，你必须自定义登录页
                //所以本案例已自定义登录页
                //前后端分离登录，非法请求直接返回 JSON
                .exceptionHandling()
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest req, HttpServletResponse resp, AuthenticationException authentication) throws IOException, ServletException {
                        resp.setContentType("application/json;charset=utf-8");
                        PrintWriter out = resp.getWriter();
                        RespBean respBean = new RespBean("访问失败！");
                        if (authentication instanceof InsufficientAuthenticationException) {
                            respBean.setMessage("尚未登录，请先登录!");
                        }
                        out.write(new ObjectMapper().writeValueAsString(respBean));
                        out.flush();
                        out.close();
                    }
                });
        ;

    }


}
