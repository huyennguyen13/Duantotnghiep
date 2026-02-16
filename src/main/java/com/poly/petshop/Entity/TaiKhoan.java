package com.poly.petshop.Entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poly.petshop.Classer.Quyen;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "taikhoan")
public class TaiKhoan implements Serializable {
private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TaiKhoanId")
    private Integer taiKhoanId;

    @NotBlank
    @Column(name = "Email", nullable = false, unique = true, length = 500)
    private String email;

    @NotBlank
    @Column(name = "MatKhau", nullable = false, length = 500)
    private String matKhau;

    // ===== ROLE tinyint dùng QuyenConverter (autoApply=true) =====
    @Column(name = "Quyen", nullable = false)
    private Quyen quyen = Quyen.KHACH_HANG;

    @Column(name = "MaThongBao", length = 60)
    private String maThongBao;

    // ===== PostgreSQL Timestamp =====
    @Column(name = "NgayTao", nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime ngayTao;

    @Column(name = "TrangThai")
    private Boolean trangThai = true;

    @Column(name = "HoTen", nullable = false, length = 100)
    private String hoTen;

    @Column(name = "SoDienThoai", unique = true, length = 10)
    private String soDienThoai;

    @Column(name = "GioiTinh")
    private Boolean gioiTinh;

    @Column(name = "NgaySinh")
    private LocalDate ngaySinh;

    @Column(name = "Hinh", length = 255)
    private String hinh;

    @Column(name = "NgayHetHan")
    private LocalDateTime ngayHetHan;

    @Column(name = "Provider", length = 15)
    private String provider;

    // ===== Quan hệ hóa đơn =====
    @JsonIgnore
    @OneToMany(mappedBy = "taiKhoans")
    private List<HoaDonEntity> hoaDons;

    // ===== Helper cho Spring Security =====
    public String getRoleName() {
        return quyen != null ? quyen.name() : null;
    }
}
