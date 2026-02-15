package com.poly.petshop.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poly.petshop.Dao.TaiKhoanDao;
import com.poly.petshop.Entity.TaiKhoan;

@Controller
@RequestMapping("/admin/danhsach")
public class TaiKhoanController {
	@Autowired
	TaiKhoanDao taiKhoanDao;
	
	@RequestMapping("/danhsachtaikhoan")
	public String dstk(Model model) {
		TaiKhoan tk = new TaiKhoan();
		model.addAttribute("tk", tk);
		List<TaiKhoan> tks = taiKhoanDao.findAll();
		model.addAttribute("tks", tks);

		return "views/DanhSachTaiKhoan";
	}
}

