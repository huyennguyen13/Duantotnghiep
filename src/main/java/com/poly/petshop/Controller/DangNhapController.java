package com.poly.petshop.Controller;
import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DangNhapController {

    @GetMapping("/views/DangNhap")
    public String loginPage() {
        return "views/DangNhap";
    }

    @GetMapping("/views/DangNhap/success")
    public String loginSuccess(Principal principal) {

        if (principal == null) {
            return "redirect:/views/DangNhap?error=true";
        }

        // Spring Security sẽ tự lấy role
        if (principal != null) {
            return "redirect:/TrangChu";
        }

        return "redirect:/views/DangNhap?error=true";
    }

    @GetMapping("/views/logoff/success")
    public String logoutSuccess() {
        return "redirect:/views/DangNhap?logout=true";
    }
}
