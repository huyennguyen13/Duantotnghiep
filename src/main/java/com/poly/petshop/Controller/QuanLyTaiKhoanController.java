package com.poly.petshop.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.annotations.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Window;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.poly.petshop.Classer.Quyen;
import com.poly.petshop.Dao.TaiKhoanDao;
import com.poly.petshop.Entity.SanPhamEntity;
import com.poly.petshop.Entity.TaiKhoan;
import com.poly.petshop.Service.EmailService;
import com.poly.petshop.Service.TaiKhoanService;
import com.poly.petshop.Service.UserService;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/admin")
public class QuanLyTaiKhoanController {
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
    private TaiKhoanDao tkDao;
	@Autowired
	EmailService emailService;
	@Autowired
    private TaiKhoanService taiKhoanService;
	@Autowired UserService userService;
	@RequestMapping("/quanlytaikhoan")
	public String getAllSanPham(Model model, @RequestParam("page") Optional<Integer> page) {
		Pageable paging = PageRequest.of(page.orElse(0), 5);
		Page<TaiKhoan> tks = tkDao.findAll(paging);
		model.addAttribute("tks", tks.getContent());
		model.addAttribute("totalPages", tks.getTotalPages());
		model.addAttribute("currentPage", page.orElse(0));
//		System.out.println(tks);
		model.addAttribute("tks", tks);
		model.addAttribute("tk", new TaiKhoan());
		return "views/quanli/quanlytaikhoan";
	}

	
	@PostMapping("/quanlytaikhoan/nhuongquyen")
public String nhuongQuyen(@RequestParam("taiKhoanId") int taiKhoanId,
                          @AuthenticationPrincipal UserDetails currentUser,
                          RedirectAttributes redirectAttributes,
                          HttpSession session) {

    Optional<TaiKhoan> optionalTaiKhoan = taiKhoanService.findTaiKhoanById(taiKhoanId);

    if (optionalTaiKhoan.isPresent()) {
        TaiKhoan taiKhoan = optionalTaiKhoan.get();

        // Đổi quyền người được nhượng
        if (taiKhoan.getQuyen() == Quyen.NHAN_VIEN) {
            taiKhoan.setQuyen(Quyen.QUAN_LY);
        } else if (taiKhoan.getQuyen() == Quyen.QUAN_LY) {
            taiKhoan.setQuyen(Quyen.NHAN_VIEN);
        }

        taiKhoanService.updateTaiKhoan(taiKhoan);

        // Đổi quyền người hiện tại
        Optional<TaiKhoan> currentTaiKhoan =
                taiKhoanService.findTaiKhoanByEmail(currentUser.getUsername());

        if (currentTaiKhoan.isPresent()) {
            TaiKhoan currentAccount = currentTaiKhoan.get();

            if (currentAccount.getQuyen() == Quyen.NHAN_VIEN) {
                currentAccount.setQuyen(Quyen.QUAN_LY);
            } else if (currentAccount.getQuyen() == Quyen.QUAN_LY) {
                currentAccount.setQuyen(Quyen.NHAN_VIEN);
            }

            taiKhoanService.updateTaiKhoan(currentAccount);
        }

        redirectAttributes.addFlashAttribute("message", "Nhượng quyền thành công!");

        session.invalidate();
        return "redirect:/views/DangNhap";
    }

    redirectAttributes.addFlashAttribute("message", "Tài khoản không tồn tại.");
    return "redirect:/admin/quanlytaikhoan";
}



	@PostMapping("/quanlytaikhoan/luutaikhoan")
    public String addTaiKhoan(@RequestParam(value = "taiKhoanId", required = false) Integer taiKhoanId,
                              @RequestParam("email") String email,
                              @RequestParam("matKhau") String matKhau,
                              @RequestParam("hoTen") String hoTen,
                              @RequestParam("soDienThoai") String soDienThoai, Model model, RedirectAttributes redirectAttributes) {
		// Kiểm tra email và số điện thoại có tồn tại không
	    if (tkDao.existsByEmail(email)) {
	        redirectAttributes.addFlashAttribute("errorEmail", "Email đã được sử dụng.");
	        return "redirect:/admin/quanlytaikhoan";
	    }

	    if (tkDao.existsBySoDienThoai(soDienThoai)) {
	        redirectAttributes.addFlashAttribute("errorSDT", "Số điện thoại đã được sử dụng.");
	        return "redirect:/admin/quanlytaikhoan";
	    }
	    
		if (taiKhoanId == null || taiKhoanId == 0) {
			TaiKhoan taiKhoanMoi = new TaiKhoan();
			taiKhoanMoi.setEmail(email);
			taiKhoanMoi.setMatKhau(passwordEncoder.encode(matKhau));
			taiKhoanMoi.setHoTen(hoTen);
			taiKhoanMoi.setSoDienThoai(soDienThoai);
			taiKhoanMoi.setQuyen(Quyen.NHAN_VIEN);
			taiKhoanMoi.setTrangThai(true);
			tkDao.save(taiKhoanMoi);
			model.addAttribute("savesuccess", "Thêm nhân viên thành công");
			return "redirect:/admin/danhsach/danhsachtaikhoan";
		} 
        
        return "views/quanlytaikhoan";
    }
	
	@RequestMapping("/quanlytaikhoan/xoa/{taiKhoanId}")
	public String deleteTaiKhoan(@PathVariable("taiKhoanId") int taiKhoanId, Model model) {
		if (tkDao.existsById(taiKhoanId)) {
			tkDao.deleteById(taiKhoanId);
		} else {
			model.addAttribute("error", "Không tìm thấy tài khoản để xóa");
		}
		return "redirect:/admin/danhsach/danhsachtaikhoan";
	}
	
	@PostMapping("/quanlytaikhoan/updateStatus/{taiKhoanId}")
	public ResponseEntity<Map<String, Object>> updateAccountStatus(@PathVariable int taiKhoanId) {
	    Optional<TaiKhoan> optionalTaiKhoan = taiKhoanService.findTaiKhoanById(taiKhoanId);
	    if (optionalTaiKhoan.isPresent()) {
	        TaiKhoan taiKhoan = optionalTaiKhoan.get();
	        
	        // Toggle the status of the account
	        boolean newStatus = !taiKhoan.getTrangThai();
	        taiKhoan.setTrangThai(newStatus);
	        taiKhoanService.updateTaiKhoan(taiKhoan);

	        // Send email notification to the user
	        String subject = "Thông báo về trạng thái tài khoản";
	        String body = "Chào " + taiKhoan.getHoTen() + ",\n\n" +
	                      "Trạng thái tài khoản của bạn đã được cập nhật thành: " +
	                      (newStatus ? "Đang hoạt động" : "Ngừng hoạt động") + ".\n\n" +
	                      "Cảm ơn bạn!";
	        emailService.sendEmail(taiKhoan.getEmail(), subject, body);

	        // Return success response
	        Map<String, Object> response = new HashMap<>();
	        response.put("trangThai", newStatus);
	        return ResponseEntity.ok(response);
	    }
	    return ResponseEntity.notFound().build();
	}

}
