package com.poly.petshop.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.poly.petshop.Classer.CustomerNotFoundException;
import com.poly.petshop.Dao.TaiKhoanDao;
import com.poly.petshop.Entity.TaiKhoan;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class TaiKhoanService {
	@Autowired
	TaiKhoanDao taikhoanDao;
	
	private final BCryptPasswordEncoder passwordEncoder;

    public TaiKhoanService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
    //Mã hóa mật khẩu chạy một lần
    @PostConstruct
    public void init() {
        try {
            for (TaiKhoan user : taikhoanDao.findAll()) {
                if (!user.getMatKhau().matches("^\\$2[aby]\\$.*")) {
                    user.setMatKhau(passwordEncoder.encode(user.getMatKhau()));
                    taikhoanDao.save(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi chi tiết
        }
    }


    public void CapNhatMaThongBao(String maThongBao, String email) 
            throws CustomerNotFoundException {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email không được bỏ trống!");
        }

        Optional<TaiKhoan> optionalTaiKhoan = taikhoanDao.findByEmail(email);

        if (optionalTaiKhoan.isPresent()) { // Kiểm tra nếu tài khoản tồn tại
            TaiKhoan taikhoan = optionalTaiKhoan.get(); // Lấy đối tượng bên trong Optional
            taikhoan.setMaThongBao(maThongBao);

            Calendar calendar = Calendar.getInstance();
            calendar.add(calendar.MINUTE, 10);

            Date ngayHetHan = calendar.getTime();
            taikhoan.setNgayHetHan(ngayHetHan);

            taikhoanDao.save(taikhoan);
        } else {
            throw new CustomerNotFoundException("Không tìm thấy bất kỳ tài khoản nào có Email: " + email);
        }
    }

	
	public TaiKhoan getByResetPasswordToken(String maThongBao) {
		return taikhoanDao.findByresetpasswordtoken(maThongBao);
	}
//Cập nhật mật khẩu
	public void CapNhatMatKhau(TaiKhoan taikhoan, String newPassword) {
		if (newPassword == null || newPassword.isEmpty()) {
			throw new IllegalArgumentException("mật khẩu mới không dược bỏ trống");
		}
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encoderpassword = passwordEncoder.encode(newPassword);
		taikhoan.setMatKhau(encoderpassword);
//		
		taikhoan.setMaThongBao(null);
		taikhoanDao.save(taikhoan);
	}
	public boolean existsByEmail(String email) {
		return taikhoanDao.existsByEmail(email);
	}
	public TaiKhoan findByEmailAndPassword(String email, String password) {
	    return taikhoanDao.findByEmailAndPassword(email, password);
	}
	
	//Phú nhượng quyền
	public Optional<TaiKhoan> findTaiKhoanById(int taiKhoanId) {
        return taikhoanDao.findById(taiKhoanId);
    }
	
	public void updateTaiKhoan(TaiKhoan taiKhoan) {
	    taikhoanDao.save(taiKhoan);
	}
	
	public Optional<TaiKhoan> findTaiKhoanByEmail(String email) {
	     return taikhoanDao.findByEmail(email);
	 }
}

