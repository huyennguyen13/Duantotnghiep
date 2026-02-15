package com.poly.petshop.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.poly.petshop.Dao.SanPhamDao;
import com.poly.petshop.Dao.SreachDAO;
import com.poly.petshop.Entity.SanPhamEntity;
import com.poly.petshop.Service.SanPhamService;
import com.poly.petshop.Service.SreachService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/customer/sanpham")
public class SanPhanKhachHangController {

	@Autowired
	private SanPhamService sanPhamService;

	@Autowired
	private SanPhamDao sanPhamDao;

	@Autowired
	private SreachService sreachService;

	// Fetch all products with pagination and sorting
	@GetMapping
	public String getAllSanPhams(@RequestParam(defaultValue = "1") int page, // Start at 1 in the frontend
			@RequestParam(defaultValue = "8") int size, @RequestParam(defaultValue = "ASC") String sortOrder,
			Model model) {

		// Convert 1-based page to 0-based for Spring's Pageable
		int zeroPage = Math.max(page - 1, 0);

		Pageable pageable = PageRequest.of(zeroPage, size);
		Page<SanPhamEntity> sanPhamPage = sanPhamService.getAllSanPhamPaginated(zeroPage, size, sortOrder);

		// Add attributes for Thymeleaf
		model.addAttribute("sanPhamList", sanPhamPage.getContent());
		model.addAttribute("page", sanPhamPage);
		model.addAttribute("currentPage", page); // Keep 1-based for frontend
		model.addAttribute("sortOrder", sortOrder);

		return "views/sanpham";
	}
//		@GetMapping
//		public String getAllSanPhams(Model model) {
//			List<SanPhamEntity> sanPhamPage = sanPhamDao.findAll();
//			// Add attributes for Thymeleaf
//			model.addAttribute("sanPhamList", sanPhamPage);
//
//			return "views/sanpham";
//		}

	// Fetch product details by ID
	@GetMapping("/{sanPhamId}")
	public String getSanPhamById(@PathVariable Integer sanPhamId, Model model) {
		SanPhamEntity sanPham = sanPhamService.getSanPhamById(sanPhamId);
		model.addAttribute("sanpham", sanPham); // Đảm bảo đối tượng sanpham được thêm vào model
		return "views/chiTietSP";
	}

	// Filter and sort products
	@GetMapping("/filter")
	public String filterSanPham(@RequestParam(required = false) String tenSanPham,
			@RequestParam(required = false) Integer loaiSanPham, @RequestParam(required = false) Integer loaiThuCung,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "8") int size,
			@RequestParam(defaultValue = "ASC") String sortOrder, Model model) {

		// Đặt trang hiện tại (0-based index)
		int zeroBasedPage = Math.max(page - 1, 0);

		// Lấy dữ liệu từ service
		Page<SanPhamEntity> sanPhamPage = sanPhamService.filterSanPhamWithSort(tenSanPham, loaiSanPham, loaiThuCung,
				null, null, zeroBasedPage, size, sortOrder);

		// Gắn dữ liệu vào model để hiển thị
		model.addAttribute("sanPhamList", sanPhamPage.getContent());
		model.addAttribute("page", sanPhamPage);
		model.addAttribute("currentPage", page);
		model.addAttribute("tenSanPham", tenSanPham);
		model.addAttribute("loaiSanPham", loaiSanPham);
		model.addAttribute("loaiThuCung", loaiThuCung);
		model.addAttribute("sortOrder", sortOrder);

		return "views/sanpham"; // Tên view hiển thị danh sách sản phẩm
	}

	@GetMapping("/filterAjax")
	public List<Map<String, Object>> filterSanPhamAjax(@RequestParam(required = false) String tenSanPham) {

		// Gọi service để tìm sản phẩm
		Page<SanPhamEntity> sanPhamPage = sanPhamService.filterSanPham(tenSanPham, null, null, null, null, 0, 20);

		// Chuyển dữ liệu entity thành JSON-friendly format
		List<Map<String, Object>> response = sanPhamPage.getContent().stream().map(product -> {
			Map<String, Object> productMap = new HashMap<>();
			productMap.put("id", product.getSanPhamId());
			productMap.put("tenSanPham", product.getTenSanPham());
			productMap.put("giaBan", product.getGiaBan());
			productMap.put("imageUrl", product.getHinh()); // Thêm thuộc tính URL hình ảnh nếu có
			return productMap;
		}).collect(Collectors.toList());

		return response;
	}

	// Xử lý tìm kiếm và hiển thị kết quả trên một trang khác
	@GetMapping("/search")
	public String searchSanPham(@RequestParam(required = false) String query, Model model) {

		List<SanPhamEntity> sanPhamList;
		int totalProducts = 0; // Biến đếm số lượng sản phẩm

		if (query != null && !query.trim().isEmpty()) {
			sanPhamList = sreachService.searchSanPhamWithoutPaging(query);
			totalProducts = sanPhamList.size(); // Đếm số lượng sản phẩm
		} else {
			sanPhamList = sanPhamService.getAllSanPham();
			totalProducts = sanPhamList.size(); // Đếm số lượng sản phẩm
		}

		model.addAttribute("sanPhamList", sanPhamList);
		model.addAttribute("query", query);
		model.addAttribute("totalProducts", totalProducts); // Thêm số lượng sản phẩm vào model

		return "views/sreach"; // Giữ nguyên layout và thêm kết quả tìm kiếm
	}
}
