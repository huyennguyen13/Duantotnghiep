package com.poly.petshop.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.poly.petshop.Dao.HoaDonDao;
import com.poly.petshop.Entity.HoaDonEntity;

@Controller
@RequestMapping("/employee/danhsach")
public class HoaDonController {

    @Autowired
    private HoaDonDao hoaDonDao;

    private static final Map<Integer, String> TRANG_THAI_MAP = Map.of(
            1, "Chờ xác nhận",
            2, "Đã xác nhận",
            3, "Đang giao",
            4, "Đã giao",
            5, "Đã nhận",
            6, "Đã hủy"
    );

    // ===== DANH SÁCH =====
    @GetMapping("/danhsachhoadon")
    public String danhSachHoaDon(Model model) {

        List<HoaDonEntity> hds = hoaDonDao.findAll();

        model.addAttribute("hds", hds);
        model.addAttribute("trangThaiMap", TRANG_THAI_MAP);

        return "views/danhsachhoadon";
    }

    // ===== CẬP NHẬT TRẠNG THÁI =====
    @PostMapping("/danhsachhoadon/capnhat")
    @ResponseBody
    public String capNhatHoaDon(@RequestParam Integer hoaDonId,
                                @RequestParam Integer choXacNhan) {

        Optional<HoaDonEntity> optional = hoaDonDao.findById(hoaDonId);

        if (optional.isPresent()) {
            HoaDonEntity hd = optional.get();
            hd.setChoXacNhan(choXacNhan);
            hoaDonDao.save(hd);
            return "OK";
        }

        return "NOT_FOUND";
    }

    // ===== FILTER =====
    @GetMapping("/danhsachhoadon/filter")
    public String filterHoaDon(
            @RequestParam(value = "status", required = false) Integer status,
            Model model) {

        List<HoaDonEntity> hds;

        if (status != null && status > 0) {
            hds = hoaDonDao.findByChoXacNhan(status);
        } else {
            hds = hoaDonDao.findAll();
        }

        model.addAttribute("hds", hds);
        model.addAttribute("trangThaiMap", TRANG_THAI_MAP);

        return "views/danhsachhoadon";
    }
}
