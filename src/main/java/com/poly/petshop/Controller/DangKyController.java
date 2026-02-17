package com.poly.petshop.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.poly.petshop.Classer.Quyen;
import com.poly.petshop.Dao.TaiKhoanDao;
import com.poly.petshop.Entity.TaiKhoan;

@Controller
public class DangKyController {

    @Autowired
    private TaiKhoanDao taiKhoanDao;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/process-signup")
    public String register(TaiKhoan taiKhoan,
                           RedirectAttributes redirectAttributes) {

        // Encode password
        taiKhoan.setMatKhau(passwordEncoder.encode(taiKhoan.getMatKhau()));

        // Gán quyền mặc định
        taiKhoan.setQuyen(Quyen.KHACH_HANG);
        // Lưu DB
        taiKhoanDao.save(taiKhoan);

        redirectAttributes.addFlashAttribute("successMessage", "Đăng ký thành công!");
        return "redirect:/views/DangNhap";
    }
}
