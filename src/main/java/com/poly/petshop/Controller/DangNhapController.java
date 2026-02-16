package com.poly.petshop.Controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.poly.petshop.Entity.TaiKhoan;

@Controller
public class DangNhapController {

    // ===================== LOAD PAGE =====================
    @GetMapping("/views/DangNhap")
    public String loginPage(Model model) {

        model.addAttribute("taiKhoan", new TaiKhoan());

        return "views/DangNhap";
    }

    // ===================== LOGIN SUCCESS =====================
    @GetMapping("/login-success")
    public String loginSuccess(Principal principal) {

        if (principal == null) {
            return "redirect:/views/DangNhap?error=true";
        }

        return "redirect:/TrangChu";
    }

    // ===================== LOGOUT SUCCESS =====================
    @GetMapping("/logout-success")
    public String logoutSuccess() {
        return "redirect:/views/DangNhap?logout=true";
    }
}
