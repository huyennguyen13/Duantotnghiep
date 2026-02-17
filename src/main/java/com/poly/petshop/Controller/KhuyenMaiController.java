package com.poly.petshop.Controller;

import org.hibernate.query.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.poly.petshop.Dao.KhuyenMaiDao;
import com.poly.petshop.Entity.KhuyenMaiEntity;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/quanli")
public class KhuyenMaiController {

	@Autowired
	private KhuyenMaiDao khuyenMaiDao;

	// Hiển thị danh sách các chương trình khuyến mãi
	@RequestMapping("/quanlikhuyenmai")
	public String getAllKhuyenMai(Model model, @RequestParam("page") Optional<Integer> page) {
		Pageable paging = PageRequest.of(page.orElse(0), 5);
		List<KhuyenMaiEntity> kms = khuyenMaiDao.findAll();
		model.addAttribute("kms", kms);
		model.addAttribute("km", new KhuyenMaiEntity()); // Đối tượng mới cho form thêm
		return "views/quanli/quanlikhuyenmai";
	}

	// Thêm hoặc cập nhật khuyến mãi
	@PostMapping("/quanlikhuyenmai/luu")
	public String saveOrUpdateKhuyenMai(@RequestParam(value = "khuyenMaiId", required = false) Integer khuyenMaiId,
			@RequestParam("tenKhuyenMai") String tenKhuyenMai, @RequestParam("phanTram") int phanTram,
			@RequestParam("ngayBatDau") @DateTimeFormat(pattern = "yyyy-MM-dd") Date ngayBatDau,
			@RequestParam("ngayKetThuc") @DateTimeFormat(pattern = "yyyy-MM-dd") Date ngayKetThuc, Model model) {

		// Kiểm tra ID để phân biệt thêm mới hay cập nhật
		if (khuyenMaiId == null || khuyenMaiId == 0) {
			// Thêm mới
			KhuyenMaiEntity newKm = new KhuyenMaiEntity();
			newKm.setTenKhuyenMai(tenKhuyenMai);
			newKm.setPhanTram(phanTram);
			newKm.setNgayBatDau(ngayBatDau);
			newKm.setNgayKetThuc(ngayKetThuc);
			khuyenMaiDao.save(newKm);
		} else {
			// Cập nhật
			Optional<KhuyenMaiEntity> existingKm = khuyenMaiDao.findById(khuyenMaiId);
			if (existingKm.isPresent()) {
				KhuyenMaiEntity km = existingKm.get();
				km.setTenKhuyenMai(tenKhuyenMai);
				km.setPhanTram(phanTram);
				km.setNgayBatDau(ngayBatDau);
				km.setNgayKetThuc(ngayKetThuc);
				khuyenMaiDao.save(km);
			} else {
				model.addAttribute("error", "Không tìm thấy khuyến mãi để sửa.");
			}
		}

		return "redirect:/admin/quanli/quanlikhuyenmai";
	}



	// Xóa khuyến mãi
	@RequestMapping("/quanlikhuyenmai/xoa/{khuyenMaiId}")
	public String deleteKhuyenMai(@PathVariable("khuyenMaiId") int khuyenMaiId, Model model) {
		if (khuyenMaiDao.existsById(khuyenMaiId)) {
			khuyenMaiDao.deleteById(khuyenMaiId);
		} else {
			model.addAttribute("error", "Không tìm thấy khuyến mãi để xóa.");
		}
		return "redirect:/admin/quanli/quanlikhuyenmai";
	}

	// Sửa khuyến mãi (hiển thị form)
	@RequestMapping("/quanlikhuyenmai/sua/{khuyenMaiId}")
	public String editKhuyenMai(@PathVariable("khuyenMaiId") Integer khuyenMaiId, Model model) {
		Optional<KhuyenMaiEntity> kmOpt = khuyenMaiDao.findById(khuyenMaiId);
		if (kmOpt.isPresent()) {
			model.addAttribute("km", kmOpt.get());
		} else {
			model.addAttribute("error", "Không tìm thấy khuyến mãi.");
			return "redirect:/views/quanli/quanlikhuyenmai";
		}
		model.addAttribute("kms", khuyenMaiDao.findAll());
		return "views/quanli/quanlikhuyenmai";
	}

	// Lam moi khuyến mãi (hiển thị form)
	@RequestMapping("/quanlikhuyenmai/lammoi/{khuyenMaiId}")
	public String resetKhuyenMai(@PathVariable("khuyenMaiId") Integer khuyenMaiId, Model model) {
		
		return "/views/quanli/quanlikhuyenmai";
	}

	// Cập nhật khuyến mãi
	@PostMapping("/quanlikhuyenmai/capnhat")
	public String updateKhuyenMai(@RequestParam("khuyenMaiId") Integer khuyenMaiId,
			@RequestParam("tenKhuyenMai") String tenKhuyenMai, @RequestParam("phanTram") int phanTram,
			@RequestParam("ngayBatDau") @DateTimeFormat(pattern = "yyyy-MM-dd") Date ngayBatDau,
			@RequestParam("ngayKetThuc") @DateTimeFormat(pattern = "yyyy-MM-dd") Date ngayKetThuc, Model model) {

		// Tìm đối tượng Khuyến Mãi để cập nhật
		Optional<KhuyenMaiEntity> kmOpt = khuyenMaiDao.findById(khuyenMaiId);
		if (kmOpt.isPresent()) {
			KhuyenMaiEntity km = kmOpt.get();
			km.setTenKhuyenMai(tenKhuyenMai);
			km.setPhanTram(phanTram);
			km.setNgayBatDau(ngayBatDau);
			km.setNgayKetThuc(ngayKetThuc);
			khuyenMaiDao.save(km); // Lưu lại đối tượng sau khi cập nhật
		} else {
			model.addAttribute("error", "Không tìm thấy khuyến mãi cần cập nhật.");
		}

		// Quay lại trang danh sách
		return "redirect:/admin/quanli/quanlikhuyenmai";
	}

	// Cấu hình định dạng ngày tháng
	@Configuration
	public class WebConfig implements WebMvcConfigurer {
		@Override
		public void addFormatters(FormatterRegistry registry) {
			registry.addFormatter(new DateFormatter("yyyy-MM-dd"));
		}
	}
}
