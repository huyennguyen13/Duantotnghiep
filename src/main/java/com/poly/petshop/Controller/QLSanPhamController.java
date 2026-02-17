package com.poly.petshop.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.poly.petshop.Dao.SanPhamDao;
import com.poly.petshop.Entity.SanPhamEntity;

import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/admin")
public class QLSanPhamController {

    @Autowired
    private SanPhamDao spDao;

    // ================= DANH SÁCH + PHÂN TRANG =================
    @GetMapping("/quanlysanpham")
    public String getAllSanPham(Model model,
                                @RequestParam("page") Optional<Integer> page) {

        int currentPage = page.orElse(0);
        Pageable pageable = PageRequest.of(currentPage, 5);

        Page<SanPhamEntity> pageResult = spDao.findAll(pageable);

        model.addAttribute("sps", pageResult.getContent());
        model.addAttribute("totalPages", pageResult.getTotalPages());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("sp", new SanPhamEntity());

        return "views/quanli/QuanLiSanPham";
    }

    // ================= THÊM HOẶC CẬP NHẬT =================
    @PostMapping("/quanlysanpham/luu")
    public String saveOrUpdate(@ModelAttribute SanPhamEntity sp,
                               RedirectAttributes redirectAttributes) {

        spDao.save(sp);
        redirectAttributes.addFlashAttribute("message", "Lưu sản phẩm thành công!");

        return "redirect:/quanli/quanlysanpham";
    }

    // ================= SỬA =================
    @GetMapping("/quanlysanpham/sua/{sanPhamId}")
    public String editSanPham(@PathVariable Integer sanPhamId,
                              Model model) {

        Optional<SanPhamEntity> spOpt = spDao.findById(sanPhamId);

        if (spOpt.isPresent()) {
            model.addAttribute("sp", spOpt.get());
        } else {
            model.addAttribute("error", "Không tìm thấy sản phẩm!");
        }

        model.addAttribute("sps", spDao.findAll());
        return "views/quanlysanpham";
    }

    // ================= XÓA =================
    @GetMapping("/quanlysanpham/xoa/{sanPhamId}")
    @Transactional
    public String deleteSanPham(@PathVariable Integer sanPhamId,
                                RedirectAttributes redirectAttributes) {

        try {
            Optional<SanPhamEntity> spOpt = spDao.findById(sanPhamId);

            if (spOpt.isPresent()) {
                SanPhamEntity sp = spOpt.get();

                // Nếu sản phẩm có ràng buộc -> đặt số lượng về 0
                sp.setSoLuongKho(0);
                spDao.save(sp);

                redirectAttributes.addFlashAttribute("message",
                        "Sản phẩm đã được xử lý (đặt số lượng = 0).");
            } else {
                redirectAttributes.addFlashAttribute("error",
                        "Không tìm thấy sản phẩm.");
            }

        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error",
                    "Không thể xóa sản phẩm do ràng buộc dữ liệu.");
        }

        return "redirect:/quanli/quanlysanpham";
    }
}
