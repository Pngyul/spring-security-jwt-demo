package org.pngyul.jwt.filter.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.pngyul.jwt.model.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import javax.servlet.FilterChain;
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
 * Spring security本质就是一堆的过滤器，这一点很重要
 * 必须清楚他的一个执行流程原理
 * 注意 AbstractAuthenticationProcessingFilter
 * 是UsernamePasswordAuthenticationFilter的父类
 * 如果是登录请求，进入了这个过滤器，那么在它下面的 Filter不再执行
 *
 * 我们实现自己的 AbstractAuthenticationProcessingFilter(也就是LoginFilter)
 * 如果没有重写他的doFilter方法，那么我们要重写 Authentication attemptAuthentication()方法
 * 另外，不一定重写他的successfulAuthentication()和unsuccessfulAuthentication()
 * 但是最好去重新设置该filter的setAuthenticationSuccessHandler(*)
 * 应为 successfulAuthentication()内部调用了该 handler
 */

public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {

    public JwtLoginFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher(defaultFilterProcessesUrl));
        setAuthenticationManager(authenticationManager);
    }

    //默认的必须通过key/value的形式传递登录信息
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException {
        //表单接收不了，会报错？为什么
        User user = new ObjectMapper().readValue(req.getInputStream(), User.class);
        return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse resp, FilterChain chain, Authentication authResult) throws IOException, ServletException {
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

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest req, HttpServletResponse resp, AuthenticationException failed) throws IOException, ServletException {
        Map<String, String> map = new HashMap<>();
        map.put("msg", "登录失败");
        resp.setContentType("application/json;charset=utf-8");
        PrintWriter out = resp.getWriter();
        out.write(new ObjectMapper().writeValueAsString(map));
        out.flush();
        out.close();
    }
}
