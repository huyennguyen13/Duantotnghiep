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

    // @Autowired
    // SanPhamDao sanphamDAO;

    @GetMapping("/customer/TrangChu")
public String Trangchu(Model model) {
    return "views/TrangChu";
}


    // @GetMapping("/")
    // public String home() {
    //     return "redirect:/customer/TrangChu";
    // }

    // @GetMapping("/TrangChu")
    // public String redirectTrangChu() {
    //     return "redirect:/customer/TrangChu";
    // }
    

    // @GetMapping("/customer/TrangChu")
    // public String Trangchu(@RequestParam(required = false) String error, Model model) {
    //     if (error != null) {
    //         model.addAttribute("error","bạn không có quyền truy cập");
    //     }

    //     List<SanPhamEntity> randomProducts = sanphamDAO.findRandomLowStockProducts();

    //     List<SanPhamEntity> limitedRandomProducts = randomProducts.stream()
    //             .limit(8)
    //             .collect(Collectors.toList());

    //     model.addAttribute("lowStockProducts", limitedRandomProducts);

    //     return "views/TrangChu";
    // }

    // @GetMapping("/employee/TrangQuanTri")
    // public String Trangquantri(@RequestParam(required = false) String error, Model model) {

    //     if (error != null) {
    //         model.addAttribute("error", "Bạn không có quyền truy cập");
    //     }

    //     return "views/TrangQuanTri";
    // }
}
