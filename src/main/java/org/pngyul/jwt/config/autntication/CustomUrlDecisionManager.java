package org.pngyul.jwt.config.autntication;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class CustomUrlDecisionManager implements AccessDecisionManager {
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        //configAttributes当前请求需要的所以权限，遍历每一个权限
        for (ConfigAttribute configAttribute : configAttributes) {
            //当前请求需要的所以权限
            String needRole = configAttribute.getAttribute();
            //ROLE_LOGIN，意思是登录了就可以访问
            if ("ROLE_LOGIN".equals(needRole)) {
                if (authentication instanceof AnonymousAuthenticationToken) {//没有登陆
                    throw new AccessDeniedException("尚未登录，请登录!");
                }else {
                    return;  //登录了就直接返回，请求将被成功执行
                }
            }
            //遍历collection，同时查看当前用户的角色列表中是否具备需要的权限，如果具备就直接返回
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equals(needRole)) {
                    return;
                }
            }
        }
        //登录了，但是权限不足
        throw new AccessDeniedException("权限不足，请联系管理员!");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
