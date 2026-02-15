package com.poly.petshop.Controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.poly.petshop.Classer.Quyen;
import com.poly.petshop.Dao.TaiKhoanDao;
import com.poly.petshop.Entity.TaiKhoan;
import com.poly.petshop.Service.TaiKhoanService;

import jakarta.servlet.http.HttpSession;

@Controller
public class DangNhapController {
	@Autowired
	TaiKhoanDao taikhoanDao;
	
	@Autowired
	TaiKhoanService taikhoanService;

	@PostMapping("/process-signup")
	public String processSignUp(@ModelAttribute("taiKhoan") TaiKhoan taikhoan, BindingResult result, Model model) {
	    // Kiểm tra email đã tồn tại
	    if (taikhoanDao.findByEmail(taikhoan.getEmail()).isPresent()) {
	        model.addAttribute("error", "Email đã tồn tại! Vui lòng sử dụng email khác.");
	        
	        // Xóa email khỏi tài khoản để ngăn tự động điền vào form
	        taikhoan.setEmail(null);
	        model.addAttribute("taiKhoan", taikhoan); // Đẩy lại đối tượng tài khoản (không chứa email) vào model
	        return "views/DangNhap"; // Trả về form đăng ký với thông báo lỗi
	    }

	    // Kiểm tra các lỗi khác trong form
	    if (result.hasErrors()) {
	        // Giữ email lại nhưng xử lý các lỗi khác
	        model.addAttribute("taiKhoan", taikhoan);
	        return "views/DangNhap"; // Trả về form đăng ký nếu có lỗi
	    }

	    // Mã hóa mật khẩu và lưu tài khoản
	    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	    String encodedPassword = passwordEncoder.encode(taikhoan.getMatKhau());
	    taikhoan.setMatKhau(encodedPassword);
	    taikhoanDao.save(taikhoan);

	    // Thêm thông báo thành công vào giao diện
	    model.addAttribute("successMessage", "Đăng ký thành công! Vui lòng đăng nhập.");
	    return "views/DangNhap"; // Trả về form đăng nhập
	}



//Đăng ký hoặc đăng nhập
	@GetMapping("/login/success")
	public String loginSuccess(@AuthenticationPrincipal OAuth2User oAuth2User, Model model) {
		if (oAuth2User == null || oAuth2User.getAttributes() == null) {
			model.addAttribute("error", "Không lấy thông tin từ Google OAuth2");
			return "views/TrangChu";
		}
		// Lấy email từ OAuth2User
		String email = oAuth2User.getAttribute("email");
		if (email == null) {
			model.addAttribute("error", "Email không tồn tại trong thông tin trả về của Google");
			return "views/TrangChu";
		}

		// Lấy tên người dùng từ email (hoặc từ thông tin trả về của Google)
		 String fullName = oAuth2User.getAttribute("name");
		    if (fullName == null) {
		        model.addAttribute("error", "Tên đầy đủ không tồn tại trong thông tin trả về của Google");
		        return "views/TrangChu";
		    }
//		 String profilePictureUrl = oAuth2User.getAttribute("picture");
		// Truyền thông tin vào model để hiển thị
		model.addAttribute("email", email);
		model.addAttribute("name", fullName);
//		model.addAttribute("profilePicture", profilePictureUrl);
		Optional<TaiKhoan> existingUser = taikhoanDao.findByEmail(email);

		if (existingUser.isEmpty()) {
			// Nếu người dùng chưa tồn tại, tạo người dùng mới
			TaiKhoan newUser = new TaiKhoan();
			newUser.setEmail(email);
			newUser.setHoTen(fullName);
			newUser.setMatKhau("defaultPassword"); // Gán mật khẩu mặc định nếu không có giá trị mật khẩu từ Google
			// Đặt tên người dùng từ email
			newUser.setProvider("google"); // Lưu thông tin nhà cung cấp (Google)
			newUser.setQuyen(Quyen.KHACH_HANG); // Gán quyền mặc định là khách hàng
			try {
				taikhoanDao.save(newUser); // Lưu người dùng vào cơ sở dữ liệu
			} catch (Exception e) {
				model.addAttribute("error", "Lỗi khi lưu thông tin người dùng.");
				e.printStackTrace();
				return "views/TrangChu";
			} // Lưu người dùng vào cơ sở dữ liệu
		}

		return "views/TrangChu"; // Chuyển hướng đến trang chủ
	}

	// Kiểm tra trạng thái đăng nhập của người dùng
	private boolean isUserLoggedIn() {
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication != null && authentication.isAuthenticated()
				&& !(authentication.getPrincipal() instanceof String); // Chưa đăng nhập nếu principal là String
	}

	@GetMapping("/views/DangNhap/success")
	public String success(HttpSession session, Model model) {
		String emailId = "";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			emailId = ((UserDetails) principal).getUsername();
		} else {
			emailId = principal.toString();
		}
		if (emailId.isEmpty()) {
			model.addAttribute("error", "Không lấy được thông tin người dùng");
		}
		// Lưu thông tin người dùng đã đăng nhập vào session
		session.setAttribute("loggedInUser", emailId);
		// Lấy danh sách vai trò của người dùng
		var authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();

		// Kiểm tra vai trò và điều hướng đến trang tương ứng
		if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_QUAN_LY"))) {

			return "redirect:/employee/TrangQuanTri"; // Quản lý được chuyển đến trang quản trị

		} else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_NHAN_VIEN"))) {

			return "redirect:/employee/TrangQuanTri"; // Nhân viên được chuyển đến trang nhân viên

		} else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_KHACH_HANG"))) {

			return "redirect:/customer/TrangChu"; // Khách hàng được chuyển đến trang chủ của khách hàng

		} else {
			model.addAttribute("error", "Người dùng không có vai trò hợp lệ!");
			return "views/TrangChu";
		}
	}

	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	// Kiểm tra mật khẩu trong cơ sở dữ liệu
	private boolean checkPasswordInDatabase(String email, String matKhau) {
		// Kiểm tra đầu vào
		if (email == null || email.trim().isEmpty() || matKhau == null || matKhau.trim().isEmpty()) {
			return false; // Dữ liệu không hợp lệ
		}

		try {
			// Tìm tài khoản theo email và kiểm tra mật khẩu
			return taikhoanDao.findByEmail(email)
					.map(taikhoan -> passwordEncoder.matches(matKhau, taikhoan.getMatKhau())).orElse(false); // Nếu																				// false
		} catch (Exception e) {
			// Log lỗi (có thể thay System.err bằng logger thực tế)

			return false;
		}
	}

	// Kiểm tra email trong cơ sở dữ liệu
	private boolean checkEmailInDatabase(String email) {
		// Kiểm tra đầu vào
		if (email == null || email.trim().isEmpty()) {
			return false; // Dữ liệu không hợp lệ
		}

		try {
			return taikhoanDao.existsByEmail(email);
		} catch (Exception e) {
			// Log lỗi
			return false;
		}
	}

	@GetMapping("/views/DangNhap")
	public String showLoginForm(@RequestParam(value = "error", required = false) String error, Model model,
			HttpSession session, @ModelAttribute("taiKhoan") TaiKhoan taikhoan) {
		// Kiểm tra nếu tài khoản đã tồn tại trong session
		if (session.getAttribute("taiKhoan") != null) {
			return "redirect:/views/TrangChu";
		}

		// Thêm lỗi nếu có
		if (error != null) {
			model.addAttribute("error", "Email hoặc mật khẩu không chính xác!");
		}
		model.addAttribute("taikhoan", new TaiKhoan()); // Khởi tạo đối tượng cho form
		return "views/DangNhap";
	}

	@PostMapping("/views/DangNhap/submit")
	public String loginError(@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "Password", required = false) String matKhau, Model model, HttpSession session) {

		model.addAttribute("taikhoan", new TaiKhoan()); // Tạo đối tượng tài khoản mới cho form
		// Xác thực thông tin đăng nhập
//	    String validationError = validate(email, matKhau, model);
//	    if (validationError != null) {
//	        return validationError; // Trả về trang đăng nhập nếu có lỗi
//	    }

//	    TaiKhoan taikhoan = taikhoanDao.findByEmail(email).orElse(null);
//	    session.setAttribute("taiKhoan", taikhoan);

	    // Xác thực email và mật khẩu
	    TaiKhoan taiKhoan = taikhoanDao.findByEmail(email).orElse(null);

	    if (taiKhoan == null || !passwordEncoder.matches(matKhau, taiKhoan.getMatKhau())) {
	        model.addAttribute("error", "Email hoặc mật khẩu không chính xác!");
	        return "views/DangNhap";
	    }

	    if (taiKhoan.getTrangThai() == null || !taiKhoan.getTrangThai()) {
	        model.addAttribute("error", "Tài khoản đã bị ngừng hoạt động.");
	        return "views/DangNhap";
	    }

	    // Lưu tài khoản vào session nếu hợp lệ
	    session.setAttribute("taiKhoan", taiKhoan);
	    return "redirect:/views/TrangChu";
	}

	private String validate(String email, String matKhau, Model model) {
		System.out.println("Email Error: " + model.getAttribute("emailError"));
		System.out.println("Password Error: " + model.getAttribute("passwordError"));

		// Xử lý khoảng trắng
		email = email != null ? email.trim() : null;
		matKhau = matKhau != null ? matKhau.trim() : null;

		// Kiểm tra email rỗng
		if (email == null || email.isEmpty()) {
			model.addAttribute("emailError", "Email không được để trống.");
			return "views/DangNhap";
		}

		if (matKhau == null || matKhau.isEmpty()) {
			model.addAttribute("passwordError", "Mật khẩu không được để trống.");
			model.addAttribute("email", email); // Giữ email khi mật khẩu sai
			return "views/DangNhap";
		}

		// Kiểm tra tài khoản trong cơ sở dữ liệu
		Optional<TaiKhoan> optionalTaiKhoan = taikhoanDao.findByEmail(email);
		if (optionalTaiKhoan.isEmpty()) {
			model.addAttribute("emailError", "Email không tồn tại trong hệ thống!");
			model.addAttribute("email", email);
			return "views/DangNhap"; // Trả về view nếu email không tồn tại
		}

		// Nếu email tồn tại nhưng mật khẩu không đúng
		TaiKhoan taikhoan = optionalTaiKhoan.get();
		if (!passwordEncoder.matches(matKhau, taikhoan.getMatKhau())) {
			model.addAttribute("passwordError", "Mật khẩu không đúng!");
			model.addAttribute("email", email);
			return "views/DangNhap"; // Trả về view nếu mật khẩu sai
		}

		return null; // Không có lỗi, tiếp tục xử lý đăng nhập
	}

	@GetMapping("/views/logoff/success")
	public String logoff(HttpSession session, Model model) {
		session.invalidate(); // Xoá toàn bộ dữ liệu trong session
		model.addAttribute("successMessage", "Bạn đã đăng xuất thành công!");
		return "redirect:/views/DangNhap";
	}

}
