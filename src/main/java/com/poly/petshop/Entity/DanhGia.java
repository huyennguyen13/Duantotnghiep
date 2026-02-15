package com.poly.petshop.Entity;


import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "DanhGia")
public class DanhGia{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int danhGiaId;

    @ManyToOne
    @JoinColumn(name = "SanPhamId", nullable = false)
    private SanPhamEntity sanPham;

    @ManyToOne
    @JoinColumn(name = "TaiKhoanId", nullable = false)
    private TaiKhoan taiKhoan;

    @Column(name = "Diem", nullable = false)
    private int diem;

    @Column(name = "NoiDung", length = 255)
    private String noiDung;

    @Temporal(TemporalType.DATE)
    @Column(name = "NgayDanhGia", nullable = false)
    private Date ngayDanhGia = new Date();

    @Column(name = "Hinh", length = 255)
    private String hinh; // Lưu đường dẫn tới ảnh


}
