package com.poly.petshop.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.poly.petshop.Dao.CthdDao;
import com.poly.petshop.Dao.HoaDonDao;
import com.poly.petshop.Entity.CthdEntity;
import com.poly.petshop.Entity.HoaDonEntity;

@Controller
@RequestMapping("/employee") // 🔥 đổi về employee
public class QLHoaDonController {

    @Autowired
    HoaDonDao hoaDonDao;

    @Autowired
    CthdDao cthdDao;

    // Danh sách trạng thái
    private static final Map<Integer, String> TRANG_THAI_MAP = Map.of(
            1, "Chờ xác nhận",
            2, "Đã xác nhận",
            3, "Đang giao",
            4, "Đã giao",
            5, "Đã nhận",
            6, "Đã hủy"
    );

    // ===== DANH SÁCH HÓA ĐƠN =====
    @GetMapping("/quanlyhoadon")
    public String dshd(Model model) {

        model.addAttribute("hd", new HoaDonEntity());
        model.addAttribute("hds", hoaDonDao.findAll());
        model.addAttribute("trangThaiMap", TRANG_THAI_MAP);

        return "views/quanli/quanlihoadon";
    }

    // ===== XEM CHI TIẾT =====
    @GetMapping("/quanlyhoadon/xem/{hoaDonId}")
    public String xem(@PathVariable("hoaDonId") int hoaDonId, Model model) {

        List<CthdEntity> cts = cthdDao.findByHoaDonId(hoaDonId);
        model.addAttribute("cts", cts);

        model.addAttribute("hds", hoaDonDao.findAll());
        model.addAttribute("trangThaiMap", TRANG_THAI_MAP);

        return "views/quanli/quanlihoadon";
    }

    // ===== CẬP NHẬT TRẠNG THÁI (AJAX) =====
    @PostMapping("/quanlyhoadon/capnhat")
    @ResponseBody
    public String capNhatHoaDon(@RequestParam("hoaDonId") Integer hoaDonId,
                                @RequestParam("choXacNhan") Integer choXacNhan) {

        Optional<HoaDonEntity> optional = hoaDonDao.findById(hoaDonId);

        if (optional.isPresent()) {
            HoaDonEntity hoaDon = optional.get();
            hoaDon.setChoXacNhan(choXacNhan);
            hoaDonDao.save(hoaDon);
            return "success";
        }

        return "error";
    }
}
