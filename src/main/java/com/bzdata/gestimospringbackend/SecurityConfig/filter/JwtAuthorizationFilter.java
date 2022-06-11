package com.bzdata.gestimospringbackend.SecurityConfig.filter;

import com.auth0.jwt.interfaces.Claim;
import com.bzdata.gestimospringbackend.utility.JWTTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
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
      //  log.info("incomming request {}",request.getRequestURI());

        Claim idAgence = null;
        if(request.getMethod().equalsIgnoreCase(OPTIONS_HTTP_METHOD)){
            response.setStatus(OK.value());
        }else{
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if (authorizationHeader == null || !authorizationHeader.startsWith(TOKEN_PREFIX)) {
                filterChain.doFilter(request, response);
               // log.info("we are adding the jwt into the request {}",request);
                return;
            }
            String token=authorizationHeader.substring(TOKEN_PREFIX.length());
            String username=jwtTokenProvider.getSubject(token);
            log.info("we are take the token {}",token);
            idAgence = jwtTokenProvider.extractIdAgnece(token);
           log.info("idAgence {}",idAgence);
            if(jwtTokenProvider.isTokenValid(username,token) &&
                    SecurityContextHolder.getContext().getAuthentication()==null){
                log.info("token is valid");
                List<GrantedAuthority> authorities=jwtTokenProvider.getAuthorities(token);
                Authentication authentication =jwtTokenProvider.getAuthentication(username,authorities,request);
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }else {
                log.error("Token is not valid");
                SecurityContextHolder.clearContext();
            }
        }
        MDC.put("idAgence",idAgence.toString());
//        log.info("get MDC {} and {}",MDC.get("idAgence"),idAgence.toString());
       // log.info("nothing has been done {}, {}", request,response);
        filterChain.doFilter(request,response);
    }

}
