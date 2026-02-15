package com.poly.petshop.Entity;

import java.util.Date;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
	@ManyToOne @JoinColumn(name = "hoaDonId")
	HoaDonEntity hoaDons;
	@ManyToOne @JoinColumn(name = "sanPhamId")
	SanPhamEntity sanPhams;
}
