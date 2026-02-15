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
    TaiKhoanDao taikhoandao;

    // ================= PASSWORD ENCODER =================
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ================= USER DETAILS SERVICE =================
    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {

            TaiKhoan taikhoan = taikhoandao.findByEmail(email)
                    .orElseThrow(() ->
                            new UsernameNotFoundException(email + " not found!")
                    );

            String matkhau = taikhoan.getMatKhau();

            // Lấy quyền từ enum (VD: QUAN_LY, NHAN_VIEN, KHACH_HANG)
            String role = taikhoan.getQuyen().name();

            return User.withUsername(email)
                    .password(matkhau)
                    .roles(role)
                    .build();
        };
    }

    // ================= SECURITY FILTER CHAIN =================
    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            CustomAccessDeniedHandler accessDeniedHandler
    ) throws Exception {

        return http

                // ===== CSRF & CORS =====
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())

                // ===== PHÂN QUYỀN =====
                .authorizeHttpRequests(auth -> auth

                        // "Quản lý" và "Nhân viên" không được vào TrangChu
                        .requestMatchers("/TrangChu")
                        .not()
                        .hasAnyRole("QUAN_LY", "NHAN_VIEN")

                        // Chỉ QUAN_LY truy cập /admin/**
                        .requestMatchers("/admin/**")
                        .hasRole("QUAN_LY")

                        // QUAN_LY và NHAN_VIEN truy cập /employee/**
                        .requestMatchers("/employee/**")
                        .hasAnyRole("QUAN_LY", "NHAN_VIEN")

                        // Chỉ KHACH_HANG truy cập /customer/**
                        .requestMatchers("/customer/**")
                        .hasRole("KHACH_HANG")

                        // Các request khác cho phép truy cập
                        .anyRequest().permitAll()
                )

                // ===== XỬ LÝ ACCESS DENIED =====
                .exceptionHandling(e -> e
                        .accessDeniedHandler(accessDeniedHandler)
                )

                // ===== FORM LOGIN =====
                .formLogin(f -> f
                        .loginPage("/views/DangNhap")
                        .loginProcessingUrl("/views/DangNhap/submit")
                        .defaultSuccessUrl("/views/DangNhap/success", false)
                        .failureUrl("/views/DangNhap?error=true")
                        .usernameParameter("email")
                        .passwordParameter("matKhau")
                )

                // ===== REMEMBER ME =====
                .rememberMe(r -> r
                        .rememberMeParameter("remember")
                        .key("uniqueAndSecret")
                        .tokenValiditySeconds(86400) // 1 ngày
                )

                // ===== GOOGLE LOGIN =====
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/views/DangNhap")
                        .successHandler((request, response, authentication) -> {
                            response.sendRedirect("/login/success");
                        })
                        .failureHandler((request, response, exception) -> {
                            response.sendRedirect("/login?error");
                        })
                )

                // ===== LOGOUT =====
                .logout(l -> l
                        .logoutUrl("/views/logoff")
                        .logoutSuccessUrl("/views/logoff/success")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )

                .build();
    }
}
