package org.pngyul.jwt.filter.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * 每次非登录请求来到这个filter,在请求头中获取token
 * 并解析，SecurityContextHolder.getContext().setAuthentication(token);
 * 供后续的过滤器获取信息
 */

public class JwtFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String jwtToken = req.getHeader("authorization");
        if(jwtToken != null){
            Jws<Claims> jws = Jwts.parser().setSigningKey("pngyul@123")
                    .parseClaimsJws(jwtToken.replace("Bearer", ""));
            Claims claims = jws.getBody();
            String username = claims.getSubject();
            List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList((String) claims.get("authorities"));
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(token);
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
