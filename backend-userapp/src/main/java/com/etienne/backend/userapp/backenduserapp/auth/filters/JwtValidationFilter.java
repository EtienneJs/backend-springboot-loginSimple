package com.etienne.backend.userapp.backenduserapp.auth.filters;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.etienne.backend.userapp.backenduserapp.auth.SimpleGrantedAuthorityJsonCreator;
import com.etienne.backend.userapp.backenduserapp.auth.TokenConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtValidationFilter extends BasicAuthenticationFilter {

    public JwtValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
                String header = request.getHeader(TokenConfig.HEADER_TOKEN);

                if(header == null || !header.startsWith(TokenConfig.PREFIX_TOKEN)){
                    chain.doFilter(request, response);
                    return;
                }

                String token = header.replace(TokenConfig.PREFIX_TOKEN, "");
                try{
                   Claims claims =  Jwts.parser().verifyWith(TokenConfig.SECRET_KEY).build().parseSignedClaims(token).getPayload();

                    Object authoritiesClaims = claims.get("authorities");
                    Object username = claims.getSubject();
                    Collection<? extends GrantedAuthority>authorities = Arrays
                    
                    .asList(new ObjectMapper()
                    .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class)
                    .readValue(authoritiesClaims.toString().getBytes(), SimpleGrantedAuthority[].class));
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,null,authorities);

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    chain.doFilter(request, response);
                }catch(JwtException e) {
                    Map<String,String> body = new HashMap<>();
                    body.put("error", e.getMessage());
                    body.put("message", "Él token no es valido");
                    response.getWriter().write(new ObjectMapper().writeValueAsString(body));
                    response.setStatus(403);
                    response.setContentType("application/type");
                }
    }

    
    
}
