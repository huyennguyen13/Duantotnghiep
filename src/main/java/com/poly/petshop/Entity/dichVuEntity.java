package com.poly.petshop.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "DichVu")
public class dichVuEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DichVuId")
    private int dichVuId;

    @Column(name = "TenDichVu", nullable = false, length = 255)
    private String tenDichVu;

    @Column(name = "GiaDichVu", nullable = false)
    private double giaDichVu;

    @Column(name = "MoTa", length = 255)
    private String moTa;

	public int getDichVuId() {
		return dichVuId;
	}

	public void setDichVuId(int dichVuId) {
		this.dichVuId = dichVuId;
	}

	public String getTenDichVu() {
		return tenDichVu;
	}

	public void setTenDichVu(String tenDichVu) {
		this.tenDichVu = tenDichVu;
	}

	public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}

   
  
}
