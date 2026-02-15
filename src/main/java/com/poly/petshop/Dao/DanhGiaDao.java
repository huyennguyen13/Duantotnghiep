package com.poly.petshop.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.petshop.Entity.DanhGia;

import java.util.List;

@Repository
public interface DanhGiaDao extends JpaRepository<DanhGia, Integer> {
    boolean existsBySanPhamSanPhamIdAndTaiKhoanTaiKhoanId(int sanPhamId, int taiKhoanId);

    List<DanhGia> findBySanPhamSanPhamId(int sanPhamId);
}
