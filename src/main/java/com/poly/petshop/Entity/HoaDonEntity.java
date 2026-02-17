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
	int hoaDonId;

	int phuongThucThanhToan;
	boolean trangThai;
	int choXacNhan;

	@Temporal(TemporalType.DATE)
	@Column(name = "ngayTao")
	Date ngayTao = new Date();

	double tongTien;
	String diaChi;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "taiKhoanId")
	TaiKhoan taiKhoans;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "khuyenMaiId")
	KhuyenMaiEntity khuyenMais;

	// 🔥 QUAN TRỌNG: phải trùng với hoaDons bên CthdEntity
	@OneToMany(mappedBy = "hoaDons", fetch = FetchType.LAZY)
	List<CthdEntity> cthds;

	@Transient
	String trangThaiMoTa;
}
