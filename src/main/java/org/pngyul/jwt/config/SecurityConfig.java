package org.pngyul.jwt.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.pngyul.jwt.config.autntication.CustomFilterInvocationSecurityMetadataSource;
import org.pngyul.jwt.config.autntication.CustomUrlDecisionManager;
import org.pngyul.jwt.filter.jwtwithcode.JwtFilter;
import org.pngyul.jwt.filter.jwtwithcode.LoginFilter;
import org.pngyul.jwt.model.RespBean;
import org.pngyul.jwt.service.HrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 一个JWT+验证码的demo
 */

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    HrService hrService;
    @Autowired
    CustomFilterInvocationSecurityMetadataSource customFilterInvocationSecurityMetadataSource;
    @Autowired
    CustomUrlDecisionManager customUrlDecisionManager;

   /* @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource druid() {
        return new DruidDataSource();
    }*/

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(hrService);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/js/**", "/css/**", "/images/**","/verifyCode","/code","/test");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        object.setAccessDecisionManager(customUrlDecisionManager);
                        object.setSecurityMetadataSource(customFilterInvocationSecurityMetadataSource);
                        return object;
                    }
                })
                .and()
                .formLogin()
                .loginPage("/login.html")
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
                .and()
                .addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class)
                .csrf().disable()
                .exceptionHandling()
                //未登录时，访问失败，不设置重定向到登录页
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest req, HttpServletResponse resp, AuthenticationException authentication) throws IOException, ServletException {
                        resp.setContentType("application/json;charset=utf-8");
                        PrintWriter out = resp.getWriter();
                        RespBean respBean = new RespBean("访问失败！");
                        if (authentication instanceof InsufficientAuthenticationException) {
                            respBean.setMessage("尚未登录，请先登录!!");
                        }
                        out.write(new ObjectMapper().writeValueAsString(respBean));
                        out.flush();
                        out.close();
                    }
                })
                //登录了，访问权限不足
                .accessDeniedHandler(new AccessDeniedHandler() {
                     @Override
                     public void handle(HttpServletRequest req, HttpServletResponse resp, AccessDeniedException e) throws IOException, ServletException {
                         resp.setContentType("application/json;charset=utf-8");
                         PrintWriter out = resp.getWriter();
                         out.write("权限不足，请联系管理员!");
                         out.flush();
                         out.close();
                     }
                });
    }

    @Bean
    LoginFilter loginFilter() throws Exception {
        LoginFilter loginFilter = new LoginFilter();
        loginFilter.setAuthenticationSuccessHandler(new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authResult) throws IOException, ServletException {
                Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();//获取登录用户的角色
                StringBuffer sb = new StringBuffer();
                for (GrantedAuthority authority : authorities) {
                    sb.append(authority.getAuthority()).append(",");
                }
                String jwt = Jwts.builder()
                        .claim("authorities", sb)
                        .setSubject(authResult.getName())
                        .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                        .signWith(SignatureAlgorithm.HS512, "pngyul@123")
                        .compact();
                Map<String, String> map = new HashMap<>();
                map.put("token", jwt);
                map.put("msg", "登录成功");
                resp.setContentType("application/json;charset=utf-8");
                PrintWriter out = resp.getWriter();
                out.write(new ObjectMapper().writeValueAsString(map));
                out.flush();
                out.close();
            }
        });
        loginFilter.setAuthenticationFailureHandler(new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                response.setContentType("application/json;charset=utf-8");
                PrintWriter out = response.getWriter();
                RespBean respBean = new RespBean(exception.getMessage());
                if (exception instanceof LockedException) {
                    respBean.setMessage("账户被锁定，请联系管理员!");
                } else if (exception instanceof CredentialsExpiredException) {
                    respBean.setMessage("密码过期，请联系管理员!");
                } else if (exception instanceof AccountExpiredException) {
                    respBean.setMessage("账户过期，请联系管理员!");
                } else if (exception instanceof DisabledException) {
                    respBean.setMessage("账户被禁用，请联系管理员!");
                } else if (exception instanceof BadCredentialsException) {
                    respBean.setMessage("用户名或者密码输入错误，请重新输入!");
                }
                out.write(new ObjectMapper().writeValueAsString(respBean));
                out.flush();
                out.close();
            }
        });
        loginFilter.setAuthenticationManager(authenticationManagerBean());
        loginFilter.setFilterProcessesUrl("/doLogin");
        return loginFilter;
    }

}