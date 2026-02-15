package com.poly.petshop.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
public class CartItems {
    Integer sanPhamId;
    String tenSanPham;
    Double giaBan;
    Integer soLuong;
    Integer soLuongKho;
    Double donGia;
    String hinh; // Thêm trường để lưu hình ảnh sản phẩm

    public CartItems(int sanPhamId, String tenSanPham, Double giaBan, int soLuong, int soLuongKho,Double donGia, String hinh) {
        this.sanPhamId = sanPhamId;
        this.tenSanPham = tenSanPham;
        this.giaBan = giaBan;
        this.soLuong = soLuong;
        this.soLuongKho = soLuongKho;
        this.hinh = hinh;
        
        // Tính toán donGia khi khởi tạo đối tượng
        this.donGia = giaBan * soLuong;
    }
    
    public Double getDonGia() {
        return (giaBan != null && soLuong != null) ? giaBan * soLuong : 0.0;
    }
    
    public CartItems() {

    }

}

