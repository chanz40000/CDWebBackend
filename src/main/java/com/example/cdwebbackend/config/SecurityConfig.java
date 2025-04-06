//package com.example.cdwebbackend.config;
//
//import com.example.cdwebbackend.components.JwtTokenUtil;
//import com.example.cdwebbackend.entity.UserEntity;
//import com.example.cdwebbackend.filter.JwtTokenFilter;
//import com.example.cdwebbackend.repository.UserRepository;
//import com.example.cdwebbackend.service.impl.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import lombok.RequiredArgsConstructor;
//
//@Configuration
//@RequiredArgsConstructor
//public class SecurityConfig {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private AuthenticationConfiguration configuration;
//    @Autowired
//    private final UserDetailsService userDetailsService;
//    @Autowired
//    private final JwtTokenUtil jwtTokenUtil;
//
//
//
//    //user's detail object
//    @Bean
//    public UserDetailsService userDetailsService(){
//        return username ->{
//            return userRepository
//                    .findOneByUsername(username)
//                    .orElseThrow(() -> new UsernameNotFoundException("Cannot find user with username ="+ username));
//        };
//    }
//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
//    @Bean
//    public AuthenticationProvider authenticationProvider(){
//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(userDetailsService());
//        authenticationProvider.setPasswordEncoder(passwordEncoder());
//        return authenticationProvider;
//    }
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration){
//        this.configuration = configuration;
//        try {
//            return configuration.getAuthenticationManager();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//
//}
package com.example.cdwebbackend.config;

import com.example.cdwebbackend.components.JwtTokenUtil;
import com.example.cdwebbackend.filter.JwtTokenFilter;
import com.example.cdwebbackend.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig{

    private final UserRepository userRepository;
    private final AuthenticationConfiguration configuration;
    private final JwtTokenFilter jwtFilter;

    // UserDetailsService bean để load thông tin user từ cơ sở dữ liệu
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> (UserDetails) userRepository
                .findOneByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cannot find user with username =" + username));
    }

    // PasswordEncoder bean để mã hóa mật khẩu người dùng
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationProvider bean sử dụng UserDetailsService và PasswordEncoder để xác thực
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    // AuthenticationManager bean dùng để quản lý và xác thực người dùng
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return configuration.getAuthenticationManager();
    }

    // JwtTokenFilter bean sử dụng để lọc các yêu cầu có token
//    @Bean
//    public JwtTokenFilter jwtTokenFilter(UserDetailsService userDetailsService) {
//        return new JwtTokenFilter(userDetailsService, jwtTokenUtil);
//    }
    // Cấu hình bảo mật và các URL cho phép truy cập
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//                .authorizeRequests()
//                .antMatchers("/api/v1/users/login", "/api/v1/users/register").permitAll()  // Cho phép những endpoint này không cần xác thực
//                .anyRequest().authenticated() // Tất cả các yêu cầu khác đều cần phải xác thực
//                .and()
//                .addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class); // Thêm filter JWT vào trước filter xác thực mặc định
//    }
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http.csrf(customizer -> customizer.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/users/login", "/api/v1/users/register").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
