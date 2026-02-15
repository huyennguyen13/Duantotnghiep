package com.poly.petshop.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poly.petshop.Dao.CthdDao;
import com.poly.petshop.Dao.HoaDonDao;
import com.poly.petshop.Entity.CthdEntity;
import com.poly.petshop.Entity.HoaDonEntity;


@Controller
@RequestMapping("/employee/quanli")
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
    
	@RequestMapping("/quanlihoadon")
	public String dshd(Model model) {
		HoaDonEntity hd = new HoaDonEntity();
		model.addAttribute("hd", hd);
		List<HoaDonEntity> hds = hoaDonDao.findAll();
        model.addAttribute("hds", hds);
        model.addAttribute("trangThaiMap", TRANG_THAI_MAP);
		
		return "views/quanli/quanlihoadon";
	}


	@GetMapping("/quanlihoadon/xem/{hoaDonId}")
	public String xem(@PathVariable("hoaDonId") int hoaDonId, Model model) {
		
	    List<CthdEntity> ct = cthdDao.findByHoaDonId(hoaDonId);
	    if (ct != null) {
			model.addAttribute("ct", ct);
		}
	    List<HoaDonEntity> hds = hoaDonDao.findAll();
		model.addAttribute("hds", hds);
		
		List<HoaDonEntity> hdfs = hoaDonDao.findByHoaDonId(hoaDonId);
		model.addAttribute("hdfs", hdfs);

		List<CthdEntity> cts = cthdDao.findByHoaDonId(hoaDonId);
		model.addAttribute("cts", cts);
		model.addAttribute("trangThaiMap", TRANG_THAI_MAP);
	    return "views/quanli/quanlihoadon";
	}
	
	@PostMapping("/quanlihoadon/capnhat")
	@ResponseBody
	public String capNhatHoaDon(@RequestParam("hoaDonId") Integer hoaDonId,
	                            @RequestParam("choXacNhan") Integer choXacNhan) {
	    Optional<HoaDonEntity> optionalHoaDon = hoaDonDao.findById(hoaDonId);
	    if (optionalHoaDon.isPresent()) {
	        HoaDonEntity hoaDon = optionalHoaDon.get();
	        hoaDon.setChoXacNhan(choXacNhan);
	        hoaDonDao.save(hoaDon);
	        return "Cập nhật thành công!";
	    } else {
	        return "Không tìm thấy hóa đơn";
	    }
	}
	
	

}
