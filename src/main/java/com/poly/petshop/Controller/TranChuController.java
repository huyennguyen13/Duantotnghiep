package com.poly.petshop.Controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.poly.petshop.Dao.SanPhamDao;
import com.poly.petshop.Entity.SanPhamEntity;
import com.poly.petshop.Service.SreachService;

import jakarta.servlet.http.HttpSession;

@Controller
public class TranChuController {
	
	@Autowired
	SanPhamDao sanphamDAO;
    
	@GetMapping("/customer/TrangChu")
	public String Trangchu(@RequestParam(required = false) String error, Model model) {
		if (error != null) {
			model.addAttribute("error","bạn không có quyền truy cập");
		}
		// Lấy tất cả các sản phẩm ngẫu nhiên
        List<SanPhamEntity> randomProducts = sanphamDAO.findRandomLowStockProducts();

        // Giới hạn số lượng sản phẩm trả về là 8
        List<SanPhamEntity> limitedRandomProducts = randomProducts.stream()
                .limit(8) // Chỉ lấy 8 sản phẩm đầu tiên
                .collect(Collectors.toList());

        // Truyền danh sách sản phẩm ngẫu nhiên vào model
        model.addAttribute("lowStockProducts", limitedRandomProducts);
		return "views/TrangChu";
	}
	
	@GetMapping("/employee/TrangQuanTri")
	public String Trangquantri(@RequestParam(required = false) String error, Model model) {
		if (error != null) {
			model.addAttribute("error", "Bạn không có quyền truy cập");
		}
		return "views/TrangQuanTri";
	}
}
