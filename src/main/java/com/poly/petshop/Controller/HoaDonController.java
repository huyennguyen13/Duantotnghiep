package com.poly.petshop.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poly.petshop.Dao.HoaDonDao;
import com.poly.petshop.Entity.HoaDonEntity;

@Controller
@RequestMapping("/employee/danhsach")
public class HoaDonController {

	@Autowired
	HoaDonDao hoaDonDao;

	// Danh sách trạng thái
	private static final Map<Integer, String> TRANG_THAI_MAP = Map.of(1, "Chờ xác nhận", 2, "Đã xác nhận", 3,
			"Đang giao", 4, "Đã giao", 6, "Đã hủy");

	@RequestMapping("/danhsachhoadon")
	public String dshd(Model model) {
//		HoaDonEntity hd = new HoaDonEntity();
//		model.addAttribute("hd", hd);
		List<HoaDonEntity> hds = hoaDonDao.findAll();
		model.addAttribute("hds", hds);
		model.addAttribute("trangThaiMap", TRANG_THAI_MAP);
		return "views/danhsachhoadon";
	}

	@PostMapping("/danhsachhoadon/capnhat")
	@ResponseBody
	public String capNhatHoaDon(@RequestParam("hoaDonId") Integer hoaDonId,
			@RequestParam("choXacNhan") Integer choXacNhan) {
		Optional<HoaDonEntity> optionalHoaDon = hoaDonDao.findById(hoaDonId);
		if (optionalHoaDon.isPresent()) {
			HoaDonEntity hoaDon = optionalHoaDon.get();
			hoaDon.setChoXacNhan(choXacNhan);
			hoaDonDao.save(hoaDon);
			return "Cập nhật thành công!";
		} else {
			return "Không tìm thấy hóa đơn";
		}
	}

	// Lọc danh sách hóa đơn theo trạng thái
	@GetMapping("/danhsachhoadon/filter")
	public String filterHoaDon(@RequestParam("status") Integer status, Model model) {
		List<HoaDonEntity> hds;
		if (status != null && status > 0) {
			hds = hoaDonDao.findByChoXacNhan(status); // Lọc theo trạng thái
		} else {
			hds = hoaDonDao.findAll(); // Nếu không lọc thì lấy tất cả
		}
		model.addAttribute("hds", hds);
		model.addAttribute("trangThaiMap", TRANG_THAI_MAP);
		return "views/danhsachhoadon";
	}
}
