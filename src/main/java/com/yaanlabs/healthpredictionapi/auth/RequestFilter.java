package com.yaanlabs.healthpredictionapi.auth;

import com.yaanlabs.healthpredictionapi.HealthPredictionAppException;
import com.yaanlabs.healthpredictionapi.model.User;
import com.yaanlabs.healthpredictionapi.service.UserService;
import com.yaanlabs.healthpredictionapi.util.CommonUtils;
import com.yaanlabs.healthpredictionapi.util.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RequestFilter extends OncePerRequestFilter {

    private final Log logger = LogFactory.getLog(RequestFilter.class);

    private static final String TOKEN_START_PREFIX = "Bearer ";
    private static final String REQUEST_HEADER_AUTHORIZATION = "Authorization";

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader(REQUEST_HEADER_AUTHORIZATION);
        String username = null;
        String jwtToken = null;

        if (requestTokenHeader != null) {
            if (requestTokenHeader.startsWith(TOKEN_START_PREFIX)) {
                jwtToken = requestTokenHeader.substring(TOKEN_START_PREFIX.length());
            } else {
                logger.warn(String.format("JWT Token does not begin with %s prefix. It will be tried to validate with full %s header",
                        TOKEN_START_PREFIX, REQUEST_HEADER_AUTHORIZATION));
                jwtToken = requestTokenHeader;
            }

            try {
                username = jwtTokenUtil.getSubjectFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                logger.error("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                logger.error("JWT Token has expired");
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                User user = this.userService.findUserByEmail(username);
                UserDetails userDetails = this.userService.getUserDetailsFromUser(user);

                if (jwtTokenUtil.validateToken(jwtToken, user)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    request.setAttribute(CommonUtils.REQUEST_ATTRIBUTE_TOKEN_USER_ID, user.getUserID());
                }
            } catch (HealthPredictionAppException appException) {
                logger.warn(String.format("User with email %s may not authenticated.", username), appException);
            }
        }
        chain.doFilter(request, response);
    }

}
