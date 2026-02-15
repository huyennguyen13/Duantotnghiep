package com.poly.petshop.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.poly.petshop.Dao.CthdDao;
import com.poly.petshop.Dao.HoaDonDao;
import com.poly.petshop.Dao.KhuyenMaiDao;
import com.poly.petshop.Dao.SanPhamDao;
import com.poly.petshop.Dao.TaiKhoanDao;
import com.poly.petshop.Entity.CthdEntity;
import com.poly.petshop.Entity.HoaDonEntity;
import com.poly.petshop.Entity.SanPhamEntity;
import com.poly.petshop.Entity.TaiKhoan;
import com.poly.petshop.Service.UserService;

import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/home/profile")
public class ProfileController {
	@Autowired
	TaiKhoanDao taiKhoanDao;
	@Autowired
	SanPhamDao sanPhamDao;
	@Autowired
	CthdDao cthdDao;
	@Autowired
	HoaDonDao hoaDonDao;
	@Autowired
	KhuyenMaiDao khuyenMaiDao;
	@Autowired
	UserService userService;
	private static final Map<Integer, String> TRANG_THAI_MAP = Map.of(1, "Chờ xác nhận", 2, "Đã xác nhận", 3,
			"Đang giao", 4, "Đã giao", 5, "Đã nhận", 6, "Đã hủy");

	@RequestMapping("")
	public String profile(Model model) {

		return "views/trangprofile";
	}

	@RequestMapping("/CDThongBao")
	public String CDThongBao(Model model) {

		return "views/profile/caidatthongbao";
	}

	@GetMapping("/DonHang")
	public String DonHang(Model model) {
		List<HoaDonEntity> hds = hoaDonDao.findAlltrangThai();
		model.addAttribute("hds", hds);
		return "views/profile/donhang";
	}

	@Transactional
	@RequestMapping("/DonMua")
	public String DonMua(@AuthenticationPrincipal UserDetails userDetails, Model model) {
		// Lấy tài khoản đang đăng nhập
		String email = userDetails.getUsername();
		TaiKhoan taiKhoan = userService.findByEmail(email);
		if (taiKhoan == null) {
			return null; // Trả về danh sách rỗng nếu không tìm thấy tài khoản
		}

		// Lấy danh sách hóa đơn của tài khoản
		int taiKhoanId = taiKhoan.getTaiKhoanId();
		List<HoaDonEntity> hoaDons = hoaDonDao.findHoaDonsByTaiKhoanId(taiKhoanId);

		// Truyền map trạng thái vào model
		model.addAttribute("trangThaiMap", TRANG_THAI_MAP);
		// Đưa danh sách hóa đơn và chi tiết hóa đơn vào model
		model.addAttribute("hoaDons", hoaDons);
		return "views/profile/DonMua";
	}

	@RequestMapping("/SanPhamDaMua")
	public String lichSuMuaHang(@AuthenticationPrincipal UserDetails userDetails, Model model) {
	    // Lấy tài khoản đang đăng nhập
	    String email = userDetails.getUsername();
	    TaiKhoan taiKhoan = userService.findByEmail(email);
	    if (taiKhoan == null) {
	        return "redirect:/login"; // Nếu tài khoản không tồn tại, chuyển hướng về trang đăng nhập
	    }

	    // Lấy danh sách hóa đơn của tài khoản
	    List<HoaDonEntity> hoaDons = hoaDonDao.findHoaDonsByTaiKhoanId(taiKhoan.getTaiKhoanId());

	    // Lấy tất cả chi tiết hóa đơn từ danh sách hóa đơn
	    List<CthdEntity> chiTietHoaDons = new ArrayList<>();
	    for (HoaDonEntity hoaDon : hoaDons) {
	        // Sử dụng phương thức findByHoaDonId trong CthdRepository để lấy chi tiết hóa đơn
	        chiTietHoaDons.addAll(cthdDao.findByHoaDons_HoaDonId(hoaDon.getHoaDonId())); 
	    }

	    // Thêm danh sách chi tiết hóa đơn vào model
	    model.addAttribute("chiTietHoaDons", chiTietHoaDons);

	    // Kiểm tra nếu không có dữ liệu thì hiển thị thông báo
	    if (chiTietHoaDons.isEmpty()) {
	        model.addAttribute("message", "Bạn chưa có sản phẩm nào đã mua.");
	    }

	    // Trả về view hiển thị
	    return "views/profile/DonHang";
	}



	@GetMapping("/updateChoXacNhan/{hoaDonId}")
	public String updateChoXacNhan(@PathVariable("hoaDonId") int hoaDonId) {
		// Lấy hóa đơn từ database
		HoaDonEntity hoaDon = hoaDonDao.findById(hoaDonId)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn với ID: " + hoaDonId));

		// Cập nhật trạng thái choXacNhan
		hoaDon.setChoXacNhan(5); // Giả sử trạng thái "Đã nhận" là 5
		hoaDonDao.save(hoaDon);

		return "redirect:/home/profile/DonMua";
	}

	@Transactional
	@GetMapping("/huyChoXacNhan/{hoaDonId}")
	public String huyChoXacNhan(@PathVariable("hoaDonId") int hoaDonId) {
		// Lấy hóa đơn từ database
		HoaDonEntity hoaDon = hoaDonDao.findById(hoaDonId)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn với ID: " + hoaDonId));

		// Lặp qua danh sách chi tiết hóa đơn (cthd) để cập nhật lại số lượng kho
		for (CthdEntity cthd : hoaDon.getCthds()) {
			SanPhamEntity sanPham = cthd.getSanPhams(); // Lấy sản phẩm
			int soLuongHienTai = sanPham.getSoLuongKho(); // Số lượng hiện tại trong kho
			sanPham.setSoLuongKho(soLuongHienTai + cthd.getSoLuong()); // Cộng thêm số lượng từ hóa đơn
			sanPhamDao.save(sanPham); // Lưu lại sản phẩm
		}

		// Cập nhật trạng thái hóa đơn thành "Đã hủy" (giả sử trạng thái 6 là "Đã hủy")
		hoaDon.setChoXacNhan(6);
		hoaDonDao.save(hoaDon);

		return "redirect:/home/profile/DonMua"; // Điều hướng lại trang DonMua
	}

	@RequestMapping("/HoSoKh")
	public String HoSoKh(Model model) {

		return "views/profile/Profile";
	}

	@RequestMapping("/XoaTaiKhoan")
	public String XoaTaiKhoan(Model model) {

		return "views/profile/xoataikhoan";
	}
}
