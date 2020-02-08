package app.example.store.jwt;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements Serializable,AuthenticationEntryPoint {

    private static final long serialVersionUID = -4910865087240032030L;

    @Override
    public void commence(
        HttpServletRequest req, 
        HttpServletResponse res, 
        AuthenticationException ae) 
        throws IOException, ServletException 
    {
        res.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Not Allowed.");
    }
    
}