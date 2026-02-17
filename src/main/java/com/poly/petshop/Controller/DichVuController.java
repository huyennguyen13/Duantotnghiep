package com.poly.petshop.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.poly.petshop.Dao.DichVuDao;
import com.poly.petshop.Entity.DichVu;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
public class DichVuController {

	@Autowired
	private DichVuDao dichvuDao;
	@RequestMapping("/user/QLDichVu")
	public String QLDichVu(Model model) {
		List<DichVu> listdichvu = dichvuDao.findAll();
		model.addAttribute("dichvuList", listdichvu);
		model.addAttribute("dichVu", new DichVu());
		return "views/QLDichVu";
	}

	@GetMapping("/Update")
	public String CapNhatDV( @RequestParam("id") Integer dichVuId,Model model) {
		DichVu dichvu = dichvuDao.findByDichVuId(dichVuId).orElse(new DichVu());
		if (dichvu != null) {
		model.addAttribute("dichVu", dichvu);
		}else {
			return "redirect:views/QLDichVu";
//		model.addAttribute("dichVu", new DichVu());
		}
		//danh sách
		List<DichVu> dichvuList = dichvuDao.findAll();
		model.addAttribute("dichvuList", dichvuList);
		return "views/QLDichVu";
	}
	
	@PostMapping("/Save")
	public String LuuDV(@ModelAttribute("dichVu") @Valid DichVu dichVu, 
			RedirectAttributes redirectAttributes, Model model){
		if (dichVu.getDichVuId() != 0) {
			
			DichVu exitingDichVu = dichvuDao.findById(dichVu.getDichVuId()).orElse(null);
			if (exitingDichVu != null) {
				exitingDichVu.setTenDichVu(dichVu.getTenDichVu());
				exitingDichVu.setGiaDichVu(dichVu.getGiaDichVu());
				exitingDichVu.setHinh(dichVu.getHinh());
				exitingDichVu.setMoTa(dichVu.getMoTa());
				dichvuDao.save(exitingDichVu);
				redirectAttributes.addFlashAttribute("message", "Đã cập nhật thành công!");
			} 
				else {
	            System.out.println("Không tìm thấy ID dịch vụ: " + dichVu.getDichVuId());
	        }
	    } else {
	        redirectAttributes.addFlashAttribute("message", "Không thể cập nhật. Dịch vụ không tồn tại.");
	    }
		redirectAttributes.addFlashAttribute("message", "Đã cập nhật thành công!");
		return "redirect:/employee/QLDichVu";
	}
	@GetMapping("/Remove")
	public String XoaDV(@RequestParam ("id") Integer dichVuId) {
		if (dichVuId != null) {
			dichvuDao.deleteById(dichVuId);
		}
		return "redirect:/employee/QLDichVu";
	}
	
	
}
