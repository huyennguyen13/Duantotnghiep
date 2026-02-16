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
                    // Ví dụ: KHACH_HANG -> Spring tự thêm ROLE_
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
                .requestMatchers("/admin/**").hasRole("QUAN_LY")
                .requestMatchers("/employee/**").hasAnyRole("QUAN_LY", "NHAN_VIEN")
                .requestMatchers("/customer/**").hasRole("KHACH_HANG")
                .anyRequest().authenticated()
            )

            .exceptionHandling(e -> e
                .accessDeniedPage("/views/TrangChu")
            )

            // ===== FORM LOGIN =====
            .formLogin(f -> f
                .loginPage("/views/DangNhap")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/TrangChu", true)
                .failureUrl("/views/DangNhap?error=true")
                .usernameParameter("email")
                .passwordParameter("matKhau")
                .permitAll()
            )

            // ===== REMEMBER ME (FIX CHUẨN) =====
            .rememberMe(r -> r
                .rememberMeParameter("remember")
                .key("uniqueAndSecretKey123")
                .tokenValiditySeconds(86400)
                .userDetailsService(userDetailsService()) // 👈 QUAN TRỌNG
            )

            // ===== GOOGLE LOGIN =====
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/views/DangNhap")
                .defaultSuccessUrl("/TrangChu", true)
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
    @Bean
public org.springframework.boot.CommandLineRunner testPassword(BCryptPasswordEncoder encoder) {
    return args -> {
        String rawPassword = "123";
        String encoded = encoder.encode(rawPassword);

        System.out.println("==== TEST ENCODE PASSWORD ====");
        System.out.println("Raw: " + rawPassword);
        System.out.println("Encoded: " + encoded);
        System.out.println("================================");
    };
}

}
