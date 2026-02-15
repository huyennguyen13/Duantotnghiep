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

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            // Giả sử ta có một dịch vụ để lấy người dùng từ email
            TaiKhoan taikhoan = taikhoandao.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException(email + " not found!"));
            String matkhau = taikhoan.getMatKhau();
            // Lấy quyền từ enum và chuyển thành chuỗi tên quyền của Spring Security
            String role = taikhoan.getQuyen().name();

            return User.withUsername(email).password(matkhau).roles(role).build();
        };
    }

//	
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CustomAccessDeniedHandler accessDeniedHandler) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .authorizeHttpRequests(auth -> auth
                .requestMatchers("/TrangChu").not().hasAnyRole("QUAN_LY", "NHAN_VIEN")//"Quản lý" và "Nhân Viên" không thể vào trang chủ
                .requestMatchers("/admin/**").hasRole("QUAN_LY")// Chỉ có "Quản lý" mới truy cập được// Chỉ có "Nhân viên" mới truy cập được
                .requestMatchers("/employee/**").hasAnyRole("QUAN_LY", "NHAN_VIEN")// "Quản lý" và "Nhân viên" đều truy cập được
                .requestMatchers("/customer/**").hasRole("KHACH_HANG")// Trang dành cho "Khách hàng"
                .anyRequest().permitAll()
                )
                .exceptionHandling(e -> e
                .accessDeniedHandler(accessDeniedHandler)// Sử lý lỗi khi truy cập vào đường dẫn không đúng
                )
                .formLogin(form -> form
                .loginPage("/views/DangNhap")
                .loginProcessingUrl("/views/DangNhap/submit")
                .usernameParameter("email")
                .passwordParameter("matKhau")
                .defaultSuccessUrl("/views/DangNhap/success", true)
                .failureUrl("/views/DangNhap?error=true")
                )
                .rememberMe(r -> r
                .rememberMeParameter("remember")
                .key("uniqueAndSecret")
                .tokenValiditySeconds(86400)
                )
                .oauth2Login(oauth2 -> oauth2
                .loginPage("/views/DangNhap")
                .successHandler((request, response, authentication) -> {
                    response.sendRedirect("/login/success");  // Chuyển hướng đến trang success sau khi đăng nhập thành công
                })
                .failureHandler((request, response, exception) -> {
                    response.sendRedirect("/login?error");  // Xử lý khi thất bại
                })
                )
                .logout(l -> l
                .logoutUrl("/views/logoff")
                .logoutSuccessUrl("/views/logoff/success")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                )
                .build();
    }

}
