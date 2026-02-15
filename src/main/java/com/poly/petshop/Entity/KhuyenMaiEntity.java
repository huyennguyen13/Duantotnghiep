package com.poly.petshop.Entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
@Table(name = "khuyenmai")
public class KhuyenMaiEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int khuyenMaiId;
	String tenKhuyenMai;
	int phanTram;
	@Temporal(TemporalType.DATE)
	@Column(name = "ngayBatDau")
	Date ngayBatDau = new Date();
	
	@Temporal(TemporalType.DATE)
	@Column(name = "ngayKetThuc")
	Date ngayKetThuc = new Date();
	String hinh;
	@OneToMany(mappedBy = "khuyenMais")
	List<HoaDonEntity> hoaDons;
}
