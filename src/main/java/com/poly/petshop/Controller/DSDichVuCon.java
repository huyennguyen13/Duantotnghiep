package com.poly.petshop.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.poly.petshop.Dao.DichVuDao;
import com.poly.petshop.Entity.DichVu;
@Controller
public class DSDichVuCon {
//	@Autowired
//	private DichVuDao dichvuDao;
//
//	@GetMapping("/employee/DSDichVu")
//	public String DSDichVu(Model model) {
//		List<DichVu> listdichvu = dichvuDao.findAll();
//		model.addAttribute("dichVuList", listdichvu);
//		return "views/DSDichVu";
//	}

	@Autowired
	DichVuDao dichVuDao;
	
	@GetMapping("/admin/DSDichVu")
	public String dsdv(Model model) {
		DichVu dv = new DichVu();
		model.addAttribute("dv", dv);
		List<DichVu> dvs = dichVuDao.findAll();
		model.addAttribute("dvs", dvs);

		return "views/DSDichVu";
	}
}
