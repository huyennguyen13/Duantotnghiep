package com.poly.petshop.Entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.poly.petshop.Classer.Quyen;
import com.poly.petshop.Config.QuyenConverter;
import com.poly.petshop.Dao.TaiKhoanDao;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "taikhoan")
public class TaiKhoan implements Serializable{
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "TaiKhoanId")
	    private int taiKhoanId;
	    
	    @Column(name = "Email", nullable = false, unique = true, length = 50)
	    private String email;

	    @Column(name = "MatKhau", nullable = false, length = 25)
	    private String matKhau;

	    @Convert(converter = QuyenConverter.class)
	    @Column(name = "Quyen", nullable = false)
	    private Quyen quyen;

	    @Column(name = "MaThongBao", length = 60)
	    private String maThongBao;

	    @Column(name = "NgayTao", nullable = false, updatable = false, insertable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
	    @Temporal(TemporalType.TIMESTAMP)
	    private Date ngayTao;

	    @Column(name = "TrangThai")
	    private Boolean trangThai;

	    @Column(name = "HoTen", nullable = false, length = 100)
	    private String hoTen;

	    @Column(name = "SoDienThoai", unique = true, length = 10)
	    private String soDienThoai;

	    @Column(name = "GioiTinh")
	    private Boolean gioiTinh;

	    @Column(name = "NgaySinh")
	    @DateTimeFormat(pattern = "yyyy-MM-dd")
	    @Temporal(TemporalType.DATE)
	    private Date ngaySinh;

	    @Column(name = "Hinh", length = 255)
	    private String hinh;
	    
	    @Column(name = "NgayHetHan")
	    private Date ngayHetHan;

        @Column(name = "Provider")
        private String provider;
	    public String getRoleValue() {
	        if (quyen != null) {
	            return quyen.name();
	        }
	        return null; 
	    }
	    public int getRoleValueInt() {
	        if (quyen != null) {
	            return quyen.getValue(); 
	        }
	        return -1;  
	    }
	    public void setQuyen(Quyen quyen) {
	        if (quyen == null) {
	            this.quyen = Quyen.KHACH_HANG; // Gán giá trị mặc định nếu quyen là null
	        } else {
	            this.quyen = quyen;
	        }
	    }
	    public void setProvider(String provider) {
	        this.provider = provider;
	    }
	    @OneToMany(mappedBy = "taiKhoans")
		List<HoaDonEntity> hoaDons;

}

