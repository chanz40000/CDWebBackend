package com.example.cdwebbackend.filter;

import com.example.cdwebbackend.components.JwtTokenUtil;
import com.example.cdwebbackend.entity.UserEntity;
import com.example.cdwebbackend.security.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    //sua lai
    @Value("${api.prefix}")
    private String apiPrefix;

    private final ApplicationContext context;
    private final CustomUserDetailsService customUserDetailsService;
    @Autowired
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            if(isByPassToken(request)){
                filterChain.doFilter(request, response);  //enable bypass
                System.out.println("pass token roi nha");
                return;
            }
            final String authHeader = request.getHeader("Authorization");
            if(authHeader==null ||!authHeader.startsWith("Bearer ")){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }
            if(authHeader.startsWith("Bearer ")){
                System.out.println("lay ra token de kiem tra: "+authHeader);
                final String token = authHeader.substring(7);
                final String username = jwtTokenUtil.extractUsername(token);
                System.out.println("usename ne: "+ username);
                System.out.println("Auth hiện tại trong context: " + SecurityContextHolder.getContext().getAuthentication());
                if(username !=null && SecurityContextHolder.getContext().getAuthentication()==null){
                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                    if(jwtTokenUtil.validateToken(token, userDetails)){
                        System.out.println("?????");
                        System.out.println("vo toi day roi: "+ userDetails.getAuthorities());
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        System.out.println("✅ Đã set Authentication vào context: " + SecurityContextHolder.getContext().getAuthentication());

                    }
                }
                filterChain.doFilter(request, response);
                System.out.println("Sau khi đi qua filter chain, context auth là: " + SecurityContextHolder.getContext().getAuthentication());

            }
        }catch (Exception e){
            e.printStackTrace();
            // Kiểm tra nếu response chưa commit trước khi gửi lỗi
            if (!response.isCommitted()) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT Token");
            }
            return;
        }


    }

//    private boolean isByPassToken(@NonNull HttpServletRequest request){
//        if (apiPrefix == null || apiPrefix.isEmpty()) {
//            return false; // Tránh lỗi NullPointerException
//        }
//        final List<Pair<String, String>> bypassTokens= Arrays.asList(
//                Pair.of(String.format("%s/products", apiPrefix), "GET"),
//                Pair.of(String.format("%s/categories", apiPrefix), "GET"),
//                Pair.of(String.format("%s/users", apiPrefix), "GET"),
//                Pair.of(String.format("%s/users", apiPrefix), "POST"),
//                Pair.of(String.format("%s/users/register", apiPrefix), "POST"),
//                Pair.of(String.format("%s/users/login", apiPrefix), "POST")
//        );
//        for(Pair<String, String>bypassToken: bypassTokens){
//            if(request.getServletPath().contains(bypassToken.getFirst())
//                    && request.getMethod().equals(bypassToken.getSecond())){
//               return true;
//            }
//        }
//        return false;
//    }
private boolean isByPassToken(@NonNull HttpServletRequest request) {
    if (apiPrefix == null || apiPrefix.isEmpty()) {
        return false;
    }

    final List<Pair<String, String>> bypassTokens = Arrays.asList(
            Pair.of(String.format("%s/products", apiPrefix), "GET"),
            Pair.of(String.format("%s/categories", apiPrefix), "GET"),
            Pair.of(String.format("%s/users/register", apiPrefix), "POST"),
            Pair.of(String.format("%s/users/login", apiPrefix), "POST"),
            Pair.of(String.format("%s/users/forgotPass", apiPrefix), "POST"),
            Pair.of(String.format("%s/users/newPassword", apiPrefix), "PUT"),
            Pair.of(String.format("%s/users/validateOtp", apiPrefix), "POST"),
            Pair.of(String.format("%s/reviews/rating-summary", apiPrefix), "GET")


    );

    // Bỏ qua các URL nếu chúng khớp với danh sách bypassTokens
    for (Pair<String, String> bypassToken : bypassTokens) {
        if (request.getServletPath().startsWith(bypassToken.getFirst())
                && request.getMethod().equals(bypassToken.getSecond())) {
            System.out.println("Bypass Token Matched!");
            return true;
        }
    }


    return false;
}




}
