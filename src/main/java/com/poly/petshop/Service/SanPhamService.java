package com.poly.petshop.Service;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.poly.petshop.Dao.SanPhamDao;
import com.poly.petshop.Entity.SanPhamEntity;

public interface SanPhamService {
    List<SanPhamEntity> getAllSanPham();
    Page<SanPhamEntity> getAllSanPhamPaginated(int page, int size, String sortOrder);
    SanPhamEntity getSanPhamById(Integer id);
    Page<SanPhamEntity> filterSanPham(String tenSanPham, Integer loaiSanPham, Integer loaiThuCung, Double giaMin, Double giaMax, int page, int size);
    Page<SanPhamEntity> filterSanPhamWithSort(
            String tenSanPham,
            Integer loaiSanPham,
            Integer loaiThuCung,
            Double giaMin,
            Double giaMax,
            int page,
            int size,
            String sortOrder
        );
}
