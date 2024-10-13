package com.medicalink.MedicaLink_backend.config;

import com.medicalink.MedicaLink_backend.services.JwtService;
import com.medicalink.MedicaLink_backend.utils.HttpExceptions.UnAuthorizedException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.annotation.Nonnull;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserDetailsService userDetailsService,
            HandlerExceptionResolver handlerExceptionResolver
    ) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }


    /**
     * Validates the user token and sets the security context
     * @param request the incoming request
     * @param response the outgoing response
     * @param filterChain the filter chain the request is going through
     */
    @Override
    protected void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        final String requestUri = request.getRequestURI();
        if(requestUri.startsWith("/auth")) {
            System.out.println("PUBLIC ENDPOINT");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                System.out.println("AUTHENTICATION ERROR doesn't contain the bearer");
                throw new UnAuthorizedException("Doesn't contain the bearer");
            }

            final String jwt = authHeader.substring(7);
            final String sessionId = jwtService.extractClaim(jwt, Claims::getId);
            final String userName = jwtService.extractUsername(jwt);
            SessionData.setToken(jwt);
            SessionData.setSessionId(sessionId);
            System.out.println("Token: " + jwt + " sessionId: " + sessionId);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if(userName != null && authentication == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("AUTHENTICATION LOG" + userDetails.getAuthorities().toString());
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            System.out.println("AUTHENTICATION ERROR exception thrown");
            e.printStackTrace();
            handlerExceptionResolver.resolveException(request, response, null, e);
        } finally {
            SessionData.clearAll();
        }
    }
}
