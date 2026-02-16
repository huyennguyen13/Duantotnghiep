package com.poly.petshop.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.poly.petshop.Dao.TaiKhoanDao;
import com.poly.petshop.Entity.TaiKhoan;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final TaiKhoanDao taiKhoanDao;

    public CustomUserDetailsService(TaiKhoanDao taiKhoanDao) {
        this.taiKhoanDao = taiKhoanDao;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        TaiKhoan tk = taiKhoanDao.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(tk.getEmail())
                .password(tk.getMatKhau())   // ⚠ KHÔNG encode lại
                .roles(tk.getQuyen().name())
                .build();
    }
}
