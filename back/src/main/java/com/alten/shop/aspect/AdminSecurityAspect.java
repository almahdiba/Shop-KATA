package com.alten.shop.aspect;

import com.alten.shop.config.AppProperties;
import com.alten.shop.util.GlobalConstants;

import lombok.RequiredArgsConstructor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Aspect
@Component
@RequiredArgsConstructor
public class AdminSecurityAspect {

    private final AppProperties appProperties;


    @Around("@annotation(com.alten.shop.annotation.IsAdmin)")
    public Object checkAdminAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails securityUser = (UserDetails) authentication.getPrincipal();
            if (authentication.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(GlobalConstants.ROLE_ADMIN))) {
                 if (appProperties.getAdminEmail().equalsIgnoreCase(securityUser.getUsername())) {
                    return joinPoint.proceed();
                 }
            }
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied. You do not have permission to access this resource.");
    }
}
