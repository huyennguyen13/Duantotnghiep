package com.poly.petshop.Entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import lombok.Data;

@Data
@Entity
@Table(name = "hoadon")
public class HoaDonEntity implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int hoaDonId;
	int phuongThucThanhToan;
	boolean trangThai;
	int choXacNhan; //1, "Chờ xác nhận", 2, "Đã xác nhận", 3, "Đang giao", 4, "Đã giao", 5, "Đã nhận", 6, "Đã hủy"
	@Temporal(TemporalType.DATE)
	@Column(name = "ngayTao")
	Date ngayTao = new Date();
	
	double tongTien;
	String diaChi;
	
	@ManyToOne @JoinColumn(name = "taiKhoanId")
	TaiKhoan taiKhoans;
	@ManyToOne @JoinColumn(name = "khuyenMaiId")
	KhuyenMaiEntity khuyenMais;
	
	@OneToMany(mappedBy = "hoaDons", fetch = FetchType.EAGER)
	List<CthdEntity> cthds;
	
	@Transient // Không lưu thuộc tính này vào cơ sở dữ liệu
    private String trangThaiMoTa; // Lưu chuỗi mô tả trạng thái
    // Getter và Setter cho trangThaiMoTa
    public String getTrangThaiMoTa() {
        return trangThaiMoTa;
    }

    public void setTrangThaiMoTa(String trangThaiMoTa) {
        this.trangThaiMoTa = trangThaiMoTa;
    }
}
