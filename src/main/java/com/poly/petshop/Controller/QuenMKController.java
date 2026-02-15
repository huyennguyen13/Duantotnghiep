package com.poly.petshop.Controller;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.poly.petshop.Classer.CustomerNotFoundException;
import com.poly.petshop.Classer.Utility;
import com.poly.petshop.Entity.TaiKhoan;
import com.poly.petshop.Service.TaiKhoanService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import net.bytebuddy.utility.RandomString;
@Controller
public class QuenMKController {
	@Autowired
	TaiKhoanService taikhoanservice;
	@Autowired
	JavaMailSender mailSender;

	@GetMapping("/views/QuenMK")
	public String quenMatKhau() {
		return "views/QuenMK";
	}

	@PostMapping("/views/QuenMK")
	public String processForgotPass(HttpServletRequest request, Model model) {
		String email = request.getParameter("email");
		String token = RandomString.make(30);

		System.out.println("Email: " + email);
		System.out.println("Token: " + token);

		try {
			taikhoanservice.CapNhatMaThongBao(token, email);
			String resetpasslink = Utility.getSiteUrl(request) + "/views/CapNhatMk?token=" + token;
//			System.out.println(resetpasslink);
			sendEmail(email, resetpasslink);
			model.addAttribute("successMessage",
					"Chúng tôi đã liên kết đặt lại mật khẩu đến email của bạn." + "Hãy kiểm tra");
		} catch (CustomerNotFoundException e) {
			// CustomerNotFoundException: ném ra khi không tìm thấy email trong hệ thống
			model.addAttribute("error", e.getMessage());
		} catch (UnsupportedEncodingException | MessagingException e) {
			// UnsupportedEncodingException: Xảy ra khi mã hóa ký tự không được hỗ trợ
			// MessagingException: Liên quan đến các lỗi xảy ra trong quá trình xử lý email
			model.addAttribute("error", "Lỗi khi gửi email");
		}
		return "views/QuenMK";
	}

	public void sendEmail(String recipientEmail, String resetPassLink)
	        throws MessagingException, UnsupportedEncodingException {
	    MimeMessage message = mailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

	    // Sử dụng email hợp lệ làm "From"
	    helper.setFrom("petshop8683@gmail.com", "PetShop");

	    helper.setTo(recipientEmail);

	    String subject = "Đây là liên kết đặt lại mật khẩu của bạn";
	    String content = "<p>Xin chào,</p>"
	                   + "<p>Bạn đã yêu cầu đặt lại mật khẩu của mình.</p>"
	                   + "<p>Nhấp vào liên kết bên dưới để thay đổi mật khẩu của bạn:</p>"
	                   + "<p><a href=\"" + resetPassLink + "\">Change my password</a></p>"
	                   + "<br>"
	                   + "<p>Nếu bạn không yêu cầu thay đổi, vui lòng bỏ qua email này.</p>";

	    helper.setSubject(subject);
	    helper.setText(content, true); // Kích hoạt HTML

	    try {
	        mailSender.send(message);
//	        System.out.println("Email sent successfully to: " + recipientEmail);
	    } catch (Exception e) {
//	        System.err.println("Error sending email to: " + recipientEmail);
	        e.printStackTrace();
	    }
	}


	@GetMapping("/views/CapNhatMk")
	public String showResetpass(@Param(value = "token") String token, Model model, RedirectAttributes redirectAttributes) {
		if (token == null || token.isEmpty()) {
			redirectAttributes.addFlashAttribute("successMessage", "mã thông báo không hợp lệ");
			return "redirect:/views/DangNhap";
		}

		TaiKhoan taikhoan = taikhoanservice.getByResetPasswordToken(token);
		
		boolean taikhoan2 = taikhoanservice.existsByEmail(token);
		if (!taikhoan2) {
			model.addAttribute("message", "Email không tồn tại");
		}

		if (taikhoan == null) {
			redirectAttributes.addFlashAttribute("successMessage", "Mã của bạn khong hợp lệ");
//			System.out.println(taikhoan);
			return "redirect:/views/DangNhap";
		}

		Date expirydate = taikhoan.getNgayHetHan();
		if (expirydate.before(new Date())) {
			redirectAttributes.addFlashAttribute("successMessage","Mã đã hết hạn. Vui lòng yêu cầu lại mật khẩu ");
			return "redirect:/views/DangNhap";
		}
		model.addAttribute("token",token);
		return "views/CapNhatMK";
	}
	@PostMapping("/views/CapNhatMK")
	public String processResetPass(HttpServletRequest req, Model model, RedirectAttributes redirectAttributes) {
	    String token = req.getParameter("token");
	    String password = req.getParameter("password");
	    
	    TaiKhoan taikhoan = taikhoanservice.getByResetPasswordToken(token);
	    if (taikhoan == null) {
	        model.addAttribute("error", "Mã không hợp lệ");
	        return "redirect:/views/CapNhatMK";
	    } else {
	        taikhoanservice.CapNhatMatKhau(taikhoan, password);
	        redirectAttributes.addFlashAttribute("successMessage", "Bạn đã thay đổi mật khẩu thành công. Vui lòng đăng nhập lại.");
	        return "redirect:/views/DangNhap"; // Chuyển hướng đến trang đăng nhập
	    }
	}


}
