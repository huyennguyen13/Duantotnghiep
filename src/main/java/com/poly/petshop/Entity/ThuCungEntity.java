package com.poly.petshop.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "ThuCung")
public class ThuCungEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int thuCungId;

    @ManyToOne
    @JoinColumn(name = "taiKhoanId", nullable = false)
    private TaiKhoan taiKhoan;

    private String tenThuCung;
    private int loaiThuCung; // 1.Gâu gâu, 2.Meo meo
    private boolean gioiTinh;
    private int thangTuoi;
    private float canNang;
    private String ghiChu;
}
