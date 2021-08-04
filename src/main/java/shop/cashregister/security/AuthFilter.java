package shop.cashregister.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthFilter extends OncePerRequestFilter{
    @Autowired
    private JwtTokenManager tokenManager;

    @Autowired
    private CashierDetailsService cashierDetailsService;

    private final String authorisationType = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
        String authorisationHeader = request.getHeader("Authorization");

        // attempt to extract the jwt token from the header
        if(authorisationHeader != null && authorisationHeader.startsWith(authorisationType)){
            String token = authorisationHeader.substring(authorisationHeader.length());
            if(tokenManager.isTokenValid(token)){
                // parse the token for the username & password
                UserDetails requestSender = cashierDetailsService.loadUserByUsername(tokenManager.extractUsernameFromToken(token));
                // authenticate them
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                                            requestSender , null, requestSender.getAuthorities());
                // store in the the security context for future reuse
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);    // invoke further security filters
    }

}
