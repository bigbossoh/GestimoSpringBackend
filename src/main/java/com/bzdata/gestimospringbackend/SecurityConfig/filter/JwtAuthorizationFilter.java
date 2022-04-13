package com.bzdata.gestimospringbackend.SecurityConfig.filter;

import com.bzdata.gestimospringbackend.utility.JWTTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.bzdata.gestimospringbackend.constant.SecurityConstant.TOKEN_PREFIX;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import static com.bzdata.gestimospringbackend.constant.SecurityConstant.OPTIONS_HTTP_METHOD;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JWTTokenProvider jwtTokenProvider;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if(request.getMethod().equalsIgnoreCase(OPTIONS_HTTP_METHOD)){
            response.setStatus(OK.value());
        }else{
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if (authorizationHeader == null || !authorizationHeader.startsWith(TOKEN_PREFIX)) {
                filterChain.doFilter(request, response);
                return;
            }
            String token=authorizationHeader.substring(TOKEN_PREFIX.length());
            String username=jwtTokenProvider.getSubject(token);
            if(jwtTokenProvider.isTokenValid(username,token) &&
                    SecurityContextHolder.getContext().getAuthentication()==null){
                List<GrantedAuthority> authorities=jwtTokenProvider.getAuthorities(token);
                Authentication authentication =jwtTokenProvider.getAuthentication(username,authorities,request);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }else {
                log.error("Token is not valid");
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request,response);
    }

}
