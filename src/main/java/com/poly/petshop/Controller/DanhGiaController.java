package com.poly.petshop.Controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.poly.petshop.Dao.*;
import com.poly.petshop.Entity.DanhGia;
import com.poly.petshop.Entity.SanPhamEntity;
import com.poly.petshop.Entity.TaiKhoan;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/danhgia")
public class DanhGiaController {

	@Autowired
	private Cloudinary cloudinary;

	@Autowired
	private SanPhamDao sanPhamDao;

	@Autowired
	private TaiKhoanDao taiKhoanDao;

	@Autowired
	private DanhGiaDao danhGiaDao;

	// Hiển thị form đánh giá
	@GetMapping("/form/{sanPhamId}")
	public String showDanhGiaForm(@PathVariable("sanPhamId") int sanPhamId, Model model, Principal principal) {
		if (principal == null) {
			return "redirect:/login";
		}

		Optional<TaiKhoan> taiKhoanOpt = taiKhoanDao.findByEmail(principal.getName());
		if (taiKhoanOpt.isEmpty()) {
			model.addAttribute("error", "Không tìm thấy tài khoản.");
			return "views/ErrorPage";
		}

		TaiKhoan taiKhoan = taiKhoanOpt.get();

		// Kiểm tra nếu tài khoản đã đánh giá sản phẩm này
		boolean daDanhGia = danhGiaDao.existsBySanPhamSanPhamIdAndTaiKhoanTaiKhoanId(sanPhamId,
				taiKhoan.getTaiKhoanId());

		Optional<SanPhamEntity> sanPhamOpt = sanPhamDao.findById(sanPhamId);
		if (sanPhamOpt.isEmpty()) {
			model.addAttribute("error", "Không tìm thấy sản phẩm.");
			return "views/ErrorPage";
		}

		SanPhamEntity sanPham = sanPhamOpt.get();

		// Gửi thông tin sản phẩm về giao diện
		model.addAttribute("sanPham", sanPham);
		model.addAttribute("daDanhGia", daDanhGia);
		model.addAttribute("taiKhoanId", taiKhoan.getTaiKhoanId());

		return "views/DanhGia";
	}

	// Lưu đánh giá
	@Transactional
	@PostMapping("/save")
	public String saveDanhGia(@RequestParam("sanPhamId") Integer sanPhamId,
			@RequestParam("taiKhoanId") Integer taiKhoanId, @RequestParam("diem") Integer diem,
			@RequestParam("noiDung") String noiDung,
			@RequestParam(value = "hinh", required = false) MultipartFile[] hinhFiles, Model model) {

		if (sanPhamId == null || taiKhoanId == null) {
			model.addAttribute("error", "Sản phẩm hoặc tài khoản không hợp lệ.");
			return "views/DanhGia";
		}

		try {
			// Xử lý tải ảnh lên Cloudinary
			StringBuilder filePaths = new StringBuilder();
			if (hinhFiles != null && hinhFiles.length > 0) {
				for (MultipartFile hinh : hinhFiles) {
					if (!hinh.isEmpty()) {
						try {
							Map uploadResult = cloudinary.uploader().upload(hinh.getBytes(), ObjectUtils
									.asMap("public_id", "danhgia/" + System.currentTimeMillis(), "overwrite", true));
							filePaths.append(uploadResult.get("secure_url")).append(";");
						} catch (IOException e) {
							model.addAttribute("error", "Lỗi khi upload ảnh: " + e.getMessage());
							e.printStackTrace();
							return "views/DanhGia";
						}
					}
				}
			}

			// Tìm sản phẩm và tài khoản
			Optional<SanPhamEntity> sanPhamOpt = sanPhamDao.findById(sanPhamId);
			Optional<TaiKhoan> taiKhoanOpt = taiKhoanDao.findById(taiKhoanId);

			if (sanPhamOpt.isEmpty() || taiKhoanOpt.isEmpty()) {
				model.addAttribute("error", "Không tìm thấy sản phẩm hoặc tài khoản.");
				return "views/DanhGia";
			}

			// Lưu đánh giá
			DanhGia danhGia = new DanhGia();
			danhGia.setSanPham(sanPhamOpt.get());
			danhGia.setTaiKhoan(taiKhoanOpt.get());
			danhGia.setDiem(diem);
			danhGia.setNoiDung(noiDung);

			if (filePaths.length() > 0) {
				danhGia.setHinh(filePaths.toString());
			}

			danhGiaDao.save(danhGia);

			return "redirect:/home/profile/DonMua";

		} catch (Exception e) {
			model.addAttribute("error", "Lỗi không xác định: " + e.getMessage());
			e.printStackTrace();
			return "views/DanhGia";
		}
	}

}
