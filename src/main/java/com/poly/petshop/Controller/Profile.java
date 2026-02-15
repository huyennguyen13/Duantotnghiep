package com.poly.petshop.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.poly.petshop.Entity.DoiMatKhau;
import com.poly.petshop.Entity.TaiKhoan;
import com.poly.petshop.Service.UserService;

import jakarta.validation.Valid;

@Controller
public class Profile {
	@Autowired
	UserService userService;

	@GetMapping("/Profile")
	public String showProfile(Model model, @AuthenticationPrincipal UserDetails userDetails) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			return "redirect:/views/DangNhap"; // Chuyển hướng nếu người dùng chưa đăng nhập
		}

		Object principal = authentication.getPrincipal();
		if (principal instanceof OAuth2User) {
			OAuth2User oauth2User = (OAuth2User) principal;
			String email = oauth2User.getAttribute("email"); // Lấy email từ OAuth2User (nếu có)
			TaiKhoan taiKhoan = userService.findByEmail(email);

			if (taiKhoan == null) {
				taiKhoan = new TaiKhoan();
			}

			model.addAttribute("taiKhoan", taiKhoan);
		} else if (principal instanceof UserDetails) {
			UserDetails userDetail = (UserDetails) principal;
			String email = userDetail.getUsername();
			TaiKhoan taiKhoan = userService.findByEmail(email);

			if (taiKhoan == null) {
				taiKhoan = new TaiKhoan();
			}

			model.addAttribute("taiKhoan", taiKhoan);
		}

		return "views/thongtincanhan";
	}

	@PostMapping("/Profile/update")
	public String updateProfile(@ModelAttribute("taiKhoan") @Valid TaiKhoan taiKhoan,
			RedirectAttributes redirectAttributes) {
		try {
			userService.update(taiKhoan);
			redirectAttributes.addFlashAttribute("message", "Cập nhật thành công!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Cập nhật thất bại!");
		}
		return "redirect:/Profile";

	}

	@GetMapping("/changePassword")
	public String showChangePasswordForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
		TaiKhoan taiKhoan = userService.findByEmail(userDetails.getUsername());
		model.addAttribute("taiKhoan", taiKhoan);
		model.addAttribute("doiMatKhau", new DoiMatKhau());
		return "views/DoiMatKhau";
	}

	@PostMapping("/changePassword/update")
	public String changePassword(@ModelAttribute("doiMatKhau") DoiMatKhau doiMatKhau,
			@AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes) {
		try {
			// Lấy tài khoản hiện tại từ database
			TaiKhoan taiKhoan = userService.findByEmail(userDetails.getUsername());

			// Kiểm tra mật khẩu cũ có chính xác không
			if (!userService.checkPassword(doiMatKhau.getMatKhauCu(), taiKhoan.getMatKhau())) {
				redirectAttributes.addFlashAttribute("error", "Mật khẩu cũ không chính xác.");
				return "redirect:/changePassword";
			}

			// Kiểm tra mật khẩu mới và xác nhận mật khẩu mới có khớp không
			if (!doiMatKhau.getMatKhauMoi().equals(doiMatKhau.getXacNhanMk())) {
				redirectAttributes.addFlashAttribute("error", "Mật khẩu mới không khớp với xác nhận.");
				return "redirect:/changePassword";
			}

			// Cập nhật mật khẩu mới
			userService.updatePassword(taiKhoan, doiMatKhau.getMatKhauMoi());
			redirectAttributes.addFlashAttribute("message", "Mật khẩu đã được thay đổi thành công.");
			return "redirect:/Profile";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra, vui lòng thử lại.");
			return "redirect:/changePassword";
		}
	}

}
