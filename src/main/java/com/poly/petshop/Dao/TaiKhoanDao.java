package com.poly.petshop.Dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.petshop.Entity.TaiKhoan;

@Repository
public interface TaiKhoanDao extends JpaRepository<TaiKhoan, Integer> {

    // ===== Dùng cho Spring Security =====
    Optional<TaiKhoan> findByEmail(String email);

    // ===== Reset password =====
    Optional<TaiKhoan> findByMaThongBao(String maThongBao);

    // ===== Kiểm tra tồn tại =====
    boolean existsByEmail(String email);

    boolean existsBySoDienThoai(String soDienThoai);

    // ===== Tìm theo ID =====
    Optional<TaiKhoan> findByTaiKhoanId(Integer taiKhoanId);

    // ===== Tìm theo họ tên =====
    Optional<TaiKhoan> findByHoTen(String hoTen);
}
