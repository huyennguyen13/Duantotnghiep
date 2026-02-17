package com.poly.petshop.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "chitiethoadon")
public class CthdEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;

	int soLuong;
	double donGia;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hoaDonId")
	HoaDonEntity hoaDons;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sanPhamId")
	SanPhamEntity sanPhams;
}
