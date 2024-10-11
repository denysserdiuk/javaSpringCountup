package ua.denysserdiuk.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    @Value("${API_KEY}")
    private String apiKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Get the API key from the request header
        String requestApiKey = request.getHeader("X-API-KEY");

        // Validate the API key
        if (requestApiKey != null && requestApiKey.equals(apiKey)) {
            // Proceed with the request if the API key is valid
            filterChain.doFilter(request, response);
        } else {
            // Respond with an unauthorized status if the API key is invalid
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid API Key");
        }
    }
}