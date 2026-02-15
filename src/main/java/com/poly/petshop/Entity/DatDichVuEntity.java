package com.poly.petshop.Entity;

import java.util.Date;

import jakarta.persistence.*;

@Entity
@Table(name = "DatDichVu")
public class DatDichVuEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DatDichVuId")
    private int datDichVuId;

    @ManyToOne
    @JoinColumn(name = "DichVuId", nullable = false)
    private DichVu dichVu;  

    @ManyToOne
    @JoinColumn(name = "TaiKhoanId", nullable = false)
    private TaiKhoan taiKhoan; 

    @ManyToOne
    @JoinColumn(name = "ThuCungId", nullable = false)
    private ThuCungEntity thuCung; 

    @Column(name = "NgayDat", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayDat;

    @Column(name = "NgayThucHien")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayThucHien;

    @Column(name = "TrangThai", nullable = false)
    private boolean trangThai;

    // Getters and Setters

    public int getDatDichVuId() {
        return datDichVuId;
    }

    public void setDatDichVuId(int datDichVuId) {
        this.datDichVuId = datDichVuId;
    }

    public DichVu getDichVu() {
        return dichVu;
    }

    public void setDichVu(DichVu dichVu2) {
        this.dichVu = dichVu2;
    }

    public TaiKhoan getTaiKhoan() {
        return taiKhoan;
    }

    public void setTaiKhoan(TaiKhoan taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    public ThuCungEntity getThuCung() {
        return thuCung;
    }

    public void setThuCung(ThuCungEntity thuCung) {
        this.thuCung = thuCung;
    }

    public Date getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(Date ngayDat) {
        this.ngayDat = ngayDat;
    }

    public Date getNgayThucHien() {
        return ngayThucHien;
    }

    public void setNgayThucHien(Date ngayThucHien) {
        this.ngayThucHien = ngayThucHien;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return "DatDichVuEntity{" +
                "datDichVuId=" + datDichVuId +
                ", dichVu=" + (dichVu != null ? dichVu.getTenDichVu() : "null") +
                ", taiKhoan=" + (taiKhoan != null ? taiKhoan.getHoTen() : "null") +
                ", thuCung=" + (thuCung != null ? thuCung.getTenThuCung() : "null") +
                ", ngayDat=" + ngayDat +
                ", ngayThucHien=" + ngayThucHien +
                ", trangThai=" + trangThai +
                '}';
    }
}
//package com.poly.petshop.Entity;
//
//import java.time.LocalDate;
//import java.util.Date;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.Table;
//import lombok.Data;
//
//@Data
//@Entity
//@Table(name = "DatDichVu")
//public class DatDichVuEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int datDichVuId;  // ID của yêu cầu dịch vụ
//
//    // Liên kết với bảng TaiKhoan
//    @ManyToOne
//    @JoinColumn(name = "taiKhoanId", nullable = false)
//    private TaiKhoan taiKhoan;
//
//    // Liên kết với bảng ThuCung
//    @ManyToOne
//    @JoinColumn(name = "thuCungId", nullable = false)
//    private ThuCungEntity thuCung;
//
//    // Liên kết với bảng DichVu
//    @ManyToOne
//    @JoinColumn(name = "dichVuId", nullable = false)
//    private DichVu dichVu;
//
//    private LocalDate ngayDat;  // Ngày yêu cầu dịch vụ được đặt
//
//    private LocalDate ngayThucHien;  // Ngày yêu cầu dịch vụ được thực hiện
//
//    private boolean trangThai;  // Trạng thái yêu cầu dịch vụ: false (0) là chưa nhận, true (1) là đã nhận
//
//    @Override
//    public String toString() {
//        return "DatDichVuEntity{" +
//                "datDichVuId=" + datDichVuId +
//                ", dichVu=" + (dichVu != null ? dichVu.getTenDichVu() : "null") +
//                ", taiKhoan=" + (taiKhoan != null ? taiKhoan.getHoTen() : "null") +
//                ", thuCung=" + (thuCung != null ? thuCung.getTenThuCung() : "null") +
//                ", ngayDat=" + ngayDat +
//                ", ngayThucHien=" + ngayThucHien +
//                ", trangThai=" + trangThai +
//                '}';
//    }
//}

         


