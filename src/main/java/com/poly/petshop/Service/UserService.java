package com.poly.petshop.Service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.poly.petshop.Dao.TaiKhoanDao;
import com.poly.petshop.Entity.TaiKhoan;

@Service
public class UserService {

    private final TaiKhoanDao taiKhoanDao;
    private final PasswordEncoder passwordEncoder;

    public UserService(TaiKhoanDao taiKhoanDao,
            PasswordEncoder passwordEncoder) {
        this.taiKhoanDao = taiKhoanDao;
        this.passwordEncoder = passwordEncoder;
    }

    // ========================
    // Tìm theo ID
    // ========================
    public TaiKhoan getTaiKhoanById(Integer id) {
        return taiKhoanDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));
    }

    // ========================
    // Tìm theo email
    // ========================
    public TaiKhoan findByEmail(String email) {
        return taiKhoanDao.findByEmail(email)
                .orElseThrow(()
                        -> new UsernameNotFoundException("Không tìm thấy người dùng với email: " + email));
    }

    // ========================
    // Cập nhật thông tin
    // ========================
    public void update(TaiKhoan taiKhoan) {

        TaiKhoan existing = taiKhoanDao.findById(taiKhoan.getTaiKhoanId())
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));

        existing.setHoTen(taiKhoan.getHoTen());
        existing.setEmail(taiKhoan.getEmail());
        existing.setSoDienThoai(taiKhoan.getSoDienThoai());
        existing.setGioiTinh(taiKhoan.getGioiTinh());

        taiKhoanDao.save(existing);
    }

    // ========================
    // Kiểm tra mật khẩu cũ
    // ========================
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    // ========================
    // Cập nhật mật khẩu mới
    // ========================
    public void updatePassword(TaiKhoan taiKhoan, String newPassword) {
        taiKhoan.setMatKhau(passwordEncoder.encode(newPassword));
        taiKhoanDao.save(taiKhoan);
    }
}
