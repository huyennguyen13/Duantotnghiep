package com.poly.petshop.Controller;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
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

    // =============================
    // Trang quên mật khẩu
    // =============================
    @GetMapping("/views/QuenMK")
    public String quenMatKhau() {
        return "views/QuenMK";
    }

    // =============================
    // Gửi email reset
    // =============================
    @PostMapping("/views/QuenMK")
    public String processForgotPass(HttpServletRequest request, Model model) {

        String email = request.getParameter("email");
        String token = RandomString.make(30);

        try {
            taikhoanservice.capNhatMaThongBao(token, email);

            String resetLink =
                    Utility.getSiteUrl(request)
                            + "/views/CapNhatMK?token=" + token;

            sendEmail(email, resetLink);

            model.addAttribute("successMessage",
                    "Chúng tôi đã gửi liên kết đặt lại mật khẩu đến email của bạn.");

        } catch (CustomerNotFoundException e) {

            model.addAttribute("error", e.getMessage());

        } catch (Exception e) {

            model.addAttribute("error", "Lỗi khi gửi email.");
        }

        return "views/QuenMK";
    }

    // =============================
    // Gửi mail
    // =============================
    public void sendEmail(String recipientEmail, String resetPassLink)
            throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper =
                new MimeMessageHelper(message, true, "utf-8");

        helper.setFrom("petshop8683@gmail.com", "PetShop");
        helper.setTo(recipientEmail);

        String subject = "Liên kết đặt lại mật khẩu";

        String content = "<p>Xin chào,</p>"
                + "<p>Bạn đã yêu cầu đặt lại mật khẩu.</p>"
                + "<p>Nhấp vào liên kết bên dưới:</p>"
                + "<p><a href=\"" + resetPassLink + "\">Đổi mật khẩu</a></p>"
                + "<br>"
                + "<p>Nếu bạn không yêu cầu, hãy bỏ qua email này.</p>";

        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }

    // =============================
    // Trang nhập mật khẩu mới
    // =============================
    @GetMapping("/views/CapNhatMK")
    public String showResetPass(String token,
                                Model model,
                                RedirectAttributes redirectAttributes) {

        if (token == null || token.isEmpty()) {
            redirectAttributes.addFlashAttribute("error",
                    "Mã không hợp lệ.");
            return "redirect:/views/DangNhap";
        }

        TaiKhoan taikhoan = taikhoanservice
        .getByResetPasswordToken(token)
        .orElse(null);


        if (taikhoan == null) {
            redirectAttributes.addFlashAttribute("error",
                    "Mã không hợp lệ.");
            return "redirect:/views/DangNhap";
        }

        // ✅ FIX LocalDateTime
        if (taikhoan.getNgayHetHan()
                .isBefore(LocalDateTime.now())) {

            redirectAttributes.addFlashAttribute("error",
                    "Mã đã hết hạn.");
            return "redirect:/views/DangNhap";
        }

        model.addAttribute("token", token);
        return "views/CapNhatMK";
    }

    // =============================
    // Xử lý đổi mật khẩu
    // =============================
    @PostMapping("/views/CapNhatMK")
    public String processResetPass(HttpServletRequest req,
                                   RedirectAttributes redirectAttributes) {

        String token = req.getParameter("token");
        String password = req.getParameter("password");

        TaiKhoan taikhoan = taikhoanservice
        .getByResetPasswordToken(token)
        .orElse(null);

        if (taikhoan == null) {
            redirectAttributes.addFlashAttribute("error",
                    "Mã không hợp lệ.");
            return "redirect:/views/DangNhap";
        }

        taikhoanservice.capNhatMatKhau(taikhoan, password);

        redirectAttributes.addFlashAttribute("successMessage",
                "Đổi mật khẩu thành công. Vui lòng đăng nhập lại.");

        return "redirect:/views/DangNhap";
    }
}
