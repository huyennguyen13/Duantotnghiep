package com.poly.petshop.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.poly.petshop.Classer.Quyen;
import com.poly.petshop.Dao.HoaDonDao;
import com.poly.petshop.Dao.TaiKhoanDao;
import com.poly.petshop.Entity.CthdEntity;
import com.poly.petshop.Entity.HoaDonEntity;
import com.poly.petshop.Entity.TaiKhoan;

@Service
public class UserService {

	@Autowired
	TaiKhoanDao taikhoanDao;
	
	@Autowired
	HoaDonDao hoadonDAO;
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@SuppressWarnings("unused")
	private UserDetails toDetails(TaiKhoan user) {
		String username = user.getEmail();
		String password = user.getMatKhau();
		// Mã hóa mật khẩu
		String encodedPassword = bCryptPasswordEncoder.encode(password);
		// Lấy quyền từ đối tượng TaiKhoan (giả sử quyen là đối tượng enum Quyen)
		Quyen role = user.getQuyen();
		String[] roles = new String[] { "ROLE_" + role.name() }; // Lấy tên của enum dưới dạng chuỗi

		return org.springframework.security.core.userdetails.User.withUsername(username).password(encodedPassword)
				.roles(roles).build();
	}

	public TaiKhoan getTaiKhoanById(int id) {
		return taikhoanDao.findById(id).orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));
	}

	public TaiKhoan findByEmail(String email) {
		return taikhoanDao.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng với email: " + email));
	}

	public void update(TaiKhoan taiKhoan) {
		TaiKhoan existingTaiKhoan = taikhoanDao.findById(taiKhoan.getTaiKhoanId())
				.orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));

		existingTaiKhoan.setHoTen(taiKhoan.getHoTen());
		existingTaiKhoan.setEmail(taiKhoan.getEmail());
		existingTaiKhoan.setSoDienThoai(taiKhoan.getSoDienThoai());
		existingTaiKhoan.setGioiTinh(taiKhoan.getGioiTinh());

		taikhoanDao.save(existingTaiKhoan);
	}

	public TaiKhoan findByUsername(String hoTen) {
		return taikhoanDao.findByHoTen(hoTen);
	}

	// Kiểm tra mật khẩu cũ
	public boolean checkPassword(String matKhauCu, String MKLuutru) {
		return bCryptPasswordEncoder.matches(matKhauCu, MKLuutru);
	}

	// Cập nhật mật khẩu mới
	public void updatePassword(TaiKhoan taiKhoan, String matKhauMoi) {
		String encodedPassword = bCryptPasswordEncoder.encode(matKhauMoi);
		taiKhoan.setMatKhau(encodedPassword);
		taikhoanDao.save(taiKhoan);
	}
}
