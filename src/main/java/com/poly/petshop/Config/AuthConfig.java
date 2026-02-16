package com.poly.petshop.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.poly.petshop.Dao.TaiKhoanDao;
import com.poly.petshop.Entity.TaiKhoan;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class AuthConfig {

    @Autowired
    private TaiKhoanDao taiKhoanDao;

    // ================= PASSWORD ENCODER =================
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ================= USER DETAILS SERVICE =================
    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            TaiKhoan taiKhoan = taiKhoanDao.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Email not found"));

            return User.withUsername(taiKhoan.getEmail())
                    .password(taiKhoan.getMatKhau())
                    .roles(taiKhoan.getQuyen().name()) 
                    .build();
        };
    }

    // ================= SECURITY FILTER =================
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.disable())

            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/views/DangNhap",
                        "/process-signup",
                        "/oauth2/**",
                        "/css/**",
                        "/js/**",
                        "/images/**"
                ).permitAll()

                // Trang quản trị
                .requestMatchers("/employee/**")
                    .hasAnyRole("QUAN_LY", "NHAN_VIEN")

                // Trang khách hàng
                .requestMatchers("/customer/**")
                    .hasAnyRole("KHACH_HANG", "QUAN_LY", "NHAN_VIEN")

                .anyRequest().authenticated()
            )

            // ===== FORM LOGIN =====
            .formLogin(f -> f
                .loginPage("/views/DangNhap")
                .loginProcessingUrl("/login")

                // ✅ ROLE-BASED REDIRECT
                .successHandler((request, response, authentication) -> {

                    var authorities = authentication.getAuthorities();

                    if (authorities.stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_QUAN_LY"))) {
                        response.sendRedirect("/employee/TrangQuanTri");
                    }
                    else if (authorities.stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_NHAN_VIEN"))) {
                        response.sendRedirect("/employee/TrangQuanTri");
                    }
                    else {
                        response.sendRedirect("/customer/TrangChu");
                    }
                })

                .failureUrl("/views/DangNhap?error=true")
                .usernameParameter("email")
                .passwordParameter("matKhau")
                .permitAll()
            )

            // ===== REMEMBER ME =====
            .rememberMe(r -> r
                .rememberMeParameter("remember")
                .key("uniqueAndSecretKey123")
                .tokenValiditySeconds(86400)
                .userDetailsService(userDetailsService())
            )

            // ===== GOOGLE LOGIN =====
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/views/DangNhap")
                .defaultSuccessUrl("/customer/TrangChu", true)
            )

            // ===== LOGOUT =====
            .logout(l -> l
                .logoutUrl("/logout")
                .logoutSuccessUrl("/views/DangNhap?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "remember-me")
                .permitAll()
            );

        return http.build();
    }
}
