package com.poly.petshop.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.poly.petshop.Classer.CustomerNotFoundException;
import com.poly.petshop.Dao.TaiKhoanDao;
import com.poly.petshop.Entity.TaiKhoan;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class TaiKhoanService {

    private final TaiKhoanDao taiKhoanDao;
    private final PasswordEncoder passwordEncoder;

    public TaiKhoanService(TaiKhoanDao taiKhoanDao,
            PasswordEncoder passwordEncoder) {
        this.taiKhoanDao = taiKhoanDao;
        this.passwordEncoder = passwordEncoder;
    }

    // ========================
    // Encode mật khẩu nếu DB còn mật khẩu thuần
    // ========================
    @PostConstruct
    public void init() {
        for (TaiKhoan user : taiKhoanDao.findAll()) {
            if (!user.getMatKhau().matches("^\\$2[aby]\\$.*")) {
                user.setMatKhau(passwordEncoder.encode(user.getMatKhau()));
                taiKhoanDao.save(user);
            }
        }
    }

    // ========================
    // So sánh mật khẩu
    // ========================
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    // ========================
    // Cập nhật mã reset password
    // ========================
    public void capNhatMaThongBao(String maThongBao, String email)
            throws CustomerNotFoundException {

        TaiKhoan taiKhoan = taiKhoanDao.findByEmail(email)
                .orElseThrow(()
                        -> new CustomerNotFoundException("Không tìm thấy tài khoản với email: " + email));

        taiKhoan.setMaThongBao(maThongBao);
        taiKhoan.setNgayHetHan(LocalDateTime.now().plusMinutes(10));

        taiKhoanDao.save(taiKhoan);
    }

    // ========================
    // Lấy theo token reset
    // ========================
    public Optional<TaiKhoan> getByResetPasswordToken(String maThongBao) {
        return taiKhoanDao.findByMaThongBao(maThongBao);
    }

    // ========================
    // Cập nhật mật khẩu mới
    // ========================
    public void capNhatMatKhau(TaiKhoan taiKhoan, String newPassword) {

        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("Mật khẩu mới không được bỏ trống");
        }

        taiKhoan.setMatKhau(passwordEncoder.encode(newPassword));
        taiKhoan.setMaThongBao(null);

        taiKhoanDao.save(taiKhoan);
    }

    // ========================
    // Kiểm tra tồn tại
    // ========================
    public boolean existsByEmail(String email) {
        return taiKhoanDao.existsByEmail(email);
    }

    // ========================
    // Tìm theo ID
    // ========================
    public Optional<TaiKhoan> findTaiKhoanById(Integer taiKhoanId) {
        return taiKhoanDao.findById(taiKhoanId);
    }

    // ========================
    // Update tài khoản
    // ========================
    public void updateTaiKhoan(TaiKhoan taiKhoan) {
        taiKhoanDao.save(taiKhoan);
    }

    public Optional<TaiKhoan> findTaiKhoanByEmail(String email) {
        return taiKhoanDao.findByEmail(email);
    }
}
