package vn.feature.filters;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.feature.components.JwtTokenUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;

    @Value("${api.prefix}")
    private String apiPrefix;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            if (isByPassToken(request)) {
                filterChain.doFilter(request, response); // enable bypass
                return;
            }

            String authHeader = request.getHeader("Authorization");
            String token;
            String phoneNumber;
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }

            token = authHeader.substring(7);
            phoneNumber = jwtTokenUtil.extractPhoneNumber(token);

            if (phoneNumber != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(phoneNumber);
                if (jwtTokenUtil.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities() // check xem role là gi
                            );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }
    }

    // isByPass: không yêu cầu token
    private boolean isByPassToken(@NotNull HttpServletRequest request) {
        final List<Pair<String, String>> bypassTokens = Arrays.asList(
                Pair.of(String.format("%s/health_check/health", apiPrefix), "GET"),
                Pair.of(String.format("%s/actuator/**", apiPrefix), "GET"),

                Pair.of(String.format("%s/roles**", apiPrefix), "GET"),
                Pair.of(String.format("%s/products**", apiPrefix), "GET"),
                Pair.of(String.format("%s/categories**", apiPrefix), "GET"),
                Pair.of(String.format("%s/comments**", apiPrefix), "GET"),
                Pair.of(String.format("%s/users/login", apiPrefix), "POST"),
                Pair.of(String.format("%s/users/register", apiPrefix), "POST"),
                Pair.of(String.format("%s/users/refresh-token", apiPrefix), "POST"),

                // sagger-ui
                Pair.of("/v2/api-docs", "GET"),
                Pair.of("/v3/api-docs", "GET"),
                Pair.of("/v3/api-docs/**", "GET"),
                Pair.of("/swagger-resources/**", "GET"),
                Pair.of("/swagger-ui.html", "GET"),
                Pair.of("/webjars/**", "GET"),
                Pair.of("/swagger-resources/configuration/ui", "GET"),
                Pair.of("/swagger-resources/configuration/security", "GET"),
                Pair.of("/swagger-ui.html/**", "GET"),
                Pair.of("/swagger-ui/**", "GET"),
                Pair.of("/swagger-ui.html/**", "GET")
        );

        String requestPath = request.getServletPath();
        String requestMethod = request.getMethod();
        // check ở method get của order
        if (requestPath.equals(String.format("%s/orders", apiPrefix))
                && requestMethod.equals("GET")) {
            // check if the requestPath matches the desired pattern
            if (requestPath.matches(String.format("/%s/orders/\\d+", apiPrefix))) {
                return true;
            }
            // if the requestPath is just
            if (requestPath.matches(String.format("/%s/orders", apiPrefix))) {
                return true;
            }
        }

        for (Pair<String, String> bypassToken : bypassTokens) {
            String path = bypassToken.getFirst();
            String method = bypassToken.getSecond();

            if (requestPath.matches(path.replace("**", ".*")) &&
            requestMethod.equalsIgnoreCase(method)) {
                return true;
            }
        }

        return false;
    }

}
