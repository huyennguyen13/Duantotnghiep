package com.poly.petshop.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.server.authentication.MaximumSessionsContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;

import com.poly.petshop.Dao.SanPhamDao;
import com.poly.petshop.Entity.SanPhamEntity;

@Controller
@RequestMapping("/admin/danhsach")
public class SanPhamController {
	@Autowired
	SanPhamDao sanPhamDao;
	
	@RequestMapping("/danhsachsanpham")
	public String dssp(Model model) {
		SanPhamEntity sp = new SanPhamEntity();
		model.addAttribute("sp", sp);
		List<SanPhamEntity> sps = sanPhamDao.findAll();
		model.addAttribute("sps", sps);

		return "views/danhsachsanpham";
	}
}
