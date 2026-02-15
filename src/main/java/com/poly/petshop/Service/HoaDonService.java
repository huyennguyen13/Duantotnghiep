package com.poly.petshop.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.petshop.Dao.CthdDao;
import com.poly.petshop.Dao.HoaDonDao;
import com.poly.petshop.Entity.CthdEntity;
import com.poly.petshop.Entity.HoaDonEntity;
import com.poly.petshop.Entity.TaiKhoan;

import jakarta.transaction.Transactional;

@Service
public class HoaDonService {
    @Autowired
    HoaDonDao hoadonDao;

    @Autowired
    CthdDao cthdDao;

    @Transactional
    public HoaDonEntity createHoaDonWithChiTiet(HoaDonEntity hoaDonEntity, List<CthdEntity> cartItems, TaiKhoan taiKhoan) {
        if (hoaDonEntity == null || cartItems == null || cartItems.isEmpty()) {
            throw new IllegalArgumentException("Hóa đơn hoặc chi tiết hóa đơn không hợp lệ!");
        }

        // Gán taiKhoan vào hóa đơn
        hoaDonEntity.setTaiKhoans(taiKhoan); // Gán TaiKhoan vào HoaDon

        // Lưu HoaDonEntity trước
        HoaDonEntity savedHoaDon = hoadonDao.save(hoaDonEntity);

        // Gán hoaDon cho từng CthdEntity và lưu
        cartItems.forEach(cthd -> {
            cthd.setHoaDons(savedHoaDon); // Gán khóa ngoại
            cthdDao.save(cthd); // Lưu CthdEntity
        });

        return savedHoaDon; // Trả về hoaDonEntity vừa lưu
    }
    
    public void capNhatChoXacNhan(int id, int choXacNhan) {
        HoaDonEntity hoaDon = hoadonDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn với ID: " + id));
        hoaDon.setChoXacNhan(choXacNhan);
        hoadonDao.save(hoaDon);
    }

}

