package com.poly.petshop.Entity;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "sanpham")
public class SanPhamEntity implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int sanPhamId;
	String tenSanPham;
	double giaBan;
	double giaNhap;
	double khoiLuong;
	int donVi;
	int loaiSanPham;
	int loaiThuCung;
	int soLuongKho;
	String moTa;
	String hinh;
	String hinhHover;
	String xuatXu;
	String baoBi;
	String thanhPhan;
	
	@OneToMany(mappedBy = "sanPhams")
	List<CthdEntity> cthds;
}
