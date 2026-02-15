package com.poly.petshop.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.poly.petshop.Dao.SanPhamDao;
import com.poly.petshop.Entity.SanPhamEntity;

import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/employee")
public class QLSanPhamController {
	@Autowired
	SanPhamDao spDao;
	@Transactional
	@RequestMapping("/quanlysanpham")
	public String getAllSanPham(Model model, @RequestParam("page") Optional<Integer> page) {
		Pageable paging = PageRequest.of(page.orElse(0), 5);
		Page<SanPhamEntity> sps = spDao.findAll(paging);
		model.addAttribute("sps", sps.getContent());
		model.addAttribute("totalPages", sps.getTotalPages());
		model.addAttribute("currentPage", page.orElse(0));

//		List<SanPhamEntity> sps = spDao.findAll();
		System.out.println(sps);
		model.addAttribute("sps", sps);
		model.addAttribute("sp", new SanPhamEntity());
		return "views/quanlysanpham";
	}
	
	@PostMapping("/quanlysanpham/luu")
	public String saveOrUpdate(@RequestParam(value = "sanPhamId", required = false) Integer sanPhamId, 
			@RequestParam ("tenSanPham") String tenSanPham,
			@RequestParam("giaBan") double giaBan,
			@RequestParam("giaNhap") double giaNhap,
			@RequestParam("khoiLuong") double khoiLuong,
			@RequestParam("donVi") int donVi,
			@RequestParam("loaiSanPham") int loaiSanPham,
			@RequestParam("loaiThuCung") int loaiThuCung,
			@RequestParam("soLuongKho") int soLuongKho,
			@RequestParam("moTa") String moTa,
			@RequestParam("hinh") String hinh,
			@RequestParam("hinhHover") String hinhHover,
			@RequestParam("xuatXu") String xuatXu,
			@RequestParam("baoBi") String baoBi,
			@RequestParam("thanhPhan") String thanhPhan, Model model){
		if (sanPhamId == null || sanPhamId == 0) {
			SanPhamEntity newSp = new SanPhamEntity();
			newSp.setTenSanPham(tenSanPham);
			newSp.setGiaBan(giaBan);
			newSp.setGiaNhap(giaNhap);
			newSp.setKhoiLuong(khoiLuong);
			newSp.setDonVi(donVi);
			newSp.setLoaiSanPham(loaiSanPham);
			newSp.setLoaiThuCung(loaiThuCung);
			newSp.setSoLuongKho(soLuongKho);
			newSp.setMoTa(moTa);
			newSp.setHinh(hinh);
			newSp.setHinhHover(hinhHover);
			newSp.setXuatXu(xuatXu);
			newSp.setBaoBi(baoBi);
			newSp.setThanhPhan(thanhPhan);
			spDao.save(newSp);
			return"redirect:/employee/danhsach/danhsachsanpham";
		} else {
			Optional<SanPhamEntity> existingSp = spDao.findById(sanPhamId);
			if (existingSp.isPresent()) {
				SanPhamEntity sp = existingSp.get();
				sp.setTenSanPham(tenSanPham);
				sp.setGiaBan(giaBan);
				sp.setGiaNhap(giaNhap);
				sp.setKhoiLuong(khoiLuong);
				sp.setDonVi(donVi);
				sp.setLoaiSanPham(loaiSanPham);
				sp.setLoaiThuCung(loaiThuCung);
				sp.setSoLuongKho(soLuongKho);
				sp.setMoTa(moTa);
				sp.setHinh(hinh);
				sp.setHinhHover(hinhHover);
				sp.setXuatXu(xuatXu);
				sp.setBaoBi(baoBi);
				sp.setThanhPhan(thanhPhan);
				spDao.save(sp);
				return"redirect:/employee/danhsach/danhsachsanpham";
			} else {
				model.addAttribute("error", "Không tìm thấy sản phẩm để sửa");
			}
		}
		return "redirect:/admin/quanlysanpham";
	}
	
	
	@RequestMapping("quanlysanpham/sua/{sanPhamId}")
	public String editSanPham(@PathVariable("sanPhamId") Integer sanPhamId, Model model) {
		Optional<SanPhamEntity> spOpt = spDao.findById(sanPhamId);
		if (spOpt.isPresent()) {
			model.addAttribute("sp", spOpt.get());
		} else {
			model.addAttribute("error", "Không tìm thấy sản phẩm");
			return "redirect:/admin/quanlysanpham";
		}
		model.addAttribute("sps", spDao.findAll());
		return "views/quanlysanpham";
	}
	
	@PostMapping("/quanlysanpham/capnhat")
	public String updateSanpham(@RequestParam("sanPhamId") Integer sanPhamId,
			@RequestParam ("tenSanPham") String tenSanPham,
			@RequestParam("giaBan") double giaBan,
			@RequestParam("giaNhap") double giaNhap,
			@RequestParam("khoiLuong") double khoiLuong,
			@RequestParam("donVi") int donVi,
			@RequestParam("loaiSanPham") int loaiSanPham,
			@RequestParam("loaiThuCung") int loaiThuCung,
			@RequestParam("soLuongKho") int soLuongKho,
			@RequestParam("moTa") String moTa,
			@RequestParam("hinh") String hinh,
			@RequestParam("hinhHover") String hinhHover,
			@RequestParam("xuatXu") String xuatXu,
			@RequestParam("baoBi") String baoBi,
			@RequestParam("thanhPhan") String thanhPhan, Model model) {
		Optional<SanPhamEntity> spOpt =spDao.findById(sanPhamId);
		if (spOpt.isPresent()) {
			SanPhamEntity sp = spOpt.get();
			sp.setTenSanPham(tenSanPham);
			sp.setGiaBan(giaBan);
			sp.setGiaNhap(giaNhap);
			sp.setKhoiLuong(khoiLuong);
			sp.setDonVi(donVi);
			sp.setLoaiSanPham(loaiSanPham);
			sp.setLoaiThuCung(loaiThuCung);
			sp.setSoLuongKho(soLuongKho);
			sp.setMoTa(moTa);
			sp.setHinh(hinh);
			sp.setHinhHover(hinhHover);
			sp.setXuatXu(xuatXu);
			sp.setBaoBi(baoBi);
			sp.setThanhPhan(thanhPhan);
			spDao.save(sp);
		} else {
			model.addAttribute("error", "Không tìm thấy sản phẩm cần cập nhật");
		}
		return "redirect:/admin/quanlysanpham";
	}
	
//	@RequestMapping("/quanlysanpham/xoa/{sanPhamId}")
//	public String deleteSanPham(@PathVariable("sanPhamId") int sanPhamId, Model model) {
//		if (spDao.existsById(sanPhamId)) {
//			spDao.deleteById(sanPhamId);
//		} else {
//			model.addAttribute("error", "Không tìm thây sản phẩm để xóa");
//		}
//		return "redirect:/employee/danhsach/danhsachsanpham";
//	}
	
	
	@RequestMapping("/quanlysanpham/xoa/{sanPhamId}")
	@Transactional
	public String deleteSanPham(@PathVariable("sanPhamId") int sanPhamId, RedirectAttributes redirectAttributes) {
	    try {
	        if (spDao.existsById(sanPhamId)) {
	            Optional<SanPhamEntity> spOpt = spDao.findById(sanPhamId);
	            if (spOpt.isPresent()) {
	                SanPhamEntity sp = spOpt.get();
	                // Nếu sản phẩm đang được tham chiếu, đặt số lượng về 0 thay vì xóa
	                sp.setSoLuongKho(0);
	                spDao.save(sp);
	                redirectAttributes.addFlashAttribute("message", "Sản phẩm đang được tham chiếu, số lượng đã được đặt về 0.");
	            }
	        } else {
	            redirectAttributes.addFlashAttribute("error", "Không tìm thấy sản phẩm để xử lý.");
	        }
	    } catch (DataIntegrityViolationException e) {
	        redirectAttributes.addFlashAttribute("error", "Lỗi không xác định khi xử lý sản phẩm.");
	    }
	    return "redirect:/employee/danhsach/danhsachsanpham";
	}

}
