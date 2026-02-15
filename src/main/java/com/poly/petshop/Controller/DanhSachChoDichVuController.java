
package com.poly.petshop.Controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poly.petshop.Dao.datDichVuDao;
import com.poly.petshop.Entity.DatDichVuEntity;
import com.poly.petshop.Entity.TaiKhoan;



@Controller
public class DanhSachChoDichVuController {

	@Autowired
	private datDichVuDao datDichVuDao;

	@RequestMapping("/employee/DSDVCho")
	public String showDanhSachDichVu(Model model) {
		// Lấy danh sách các dịch vụ từ database (bao gồm thông tin liên quan)
		List<DatDichVuEntity> listDichVu = datDichVuDao.findAllWithRelations();
		model.addAttribute("listDichVu", listDichVu); // Thêm danh sách vào model
		return "views/quanli/danhsachdichvucho"; // Trả về view để hiển thị danh sách
	}
	@GetMapping("/employee/DSDVCho/doi/{datDichVuId}")
	public String doi(@PathVariable("datDichVuId") int datDichVuId, Model model) {
	    Optional<DatDichVuEntity> hdon = datDichVuDao.findById(datDichVuId);
	    if (hdon.isPresent()) {
	        DatDichVuEntity hd = hdon.get();
	        
	        // Đổi trạng thái
	        boolean newStatus = !hd.isTrangThai();
	        hd.setTrangThai(newStatus);

	        // Nếu đổi sang trạng thái "Đã nhận", thiết lập ngày thực hiện là hôm nay
	        if (newStatus) {
	            hd.setNgayThucHien(new Date());
	        }

	        // Lưu lại thông tin thay đổi
	        datDichVuDao.save(hd);
	    }

	    // Trả về lại danh sách dịch vụ sau khi thay đổi
	    return "redirect:/employee/DSDVCho";
	}

	
}







