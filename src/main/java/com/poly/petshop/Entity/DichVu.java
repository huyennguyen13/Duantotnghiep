package com.poly.petshop.Entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "dichvu")
public class DichVu implements Serializable{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DichVuId")
	 int dichVuId;
	
	@Column(name = "TenDichVu")
	 String tenDichVu;
	
	@Column(name = "GiaDichVu")
	 float giaDichVu;
	
	@Column(name = "MoTa")
	 String moTa;
	
	@Column(name = "Hinh")
	 String hinh;
}
