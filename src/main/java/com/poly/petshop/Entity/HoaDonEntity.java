package com.poly.petshop.Entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "hoadon")
public class HoaDonEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int hoaDonId;

    private int phuongThucThanhToan;

    private boolean trangThai;

    private int choXacNhan;

    @Temporal(TemporalType.DATE)
    @Column(name = "ngayTao")
    private Date ngayTao = new Date();

    private double tongTien;

    private String diaChi;

    // ✅ FIX: thêm fetch = EAGER để tránh lỗi lazy loading
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "taiKhoanId")
    private TaiKhoan taiKhoans;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "khuyenMaiId")
    private KhuyenMaiEntity khuyenMais;

    @OneToMany(mappedBy = "hoaDons", fetch = FetchType.EAGER)
    private List<CthdEntity> cthds;

    @Transient
    private String trangThaiMoTa;
}
