package com.poly.petshop.Controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.poly.petshop.Dao.DichVuDao;
import com.poly.petshop.Dao.TaiKhoanDao;
import com.poly.petshop.Dao.thuCungDao;
import com.poly.petshop.Dao.datDichVuDao;
import com.poly.petshop.Entity.DatDichVuEntity;
import com.poly.petshop.Entity.DichVu;
import com.poly.petshop.Entity.TaiKhoan;
import com.poly.petshop.Entity.ThuCungEntity;
import com.poly.petshop.Entity.dichVuEntity;
import com.poly.petshop.Service.UserService;

@Controller
public class DanhSachDatDichVuController {

    @Autowired
    private DichVuDao dichVuDao; 
    
    @Autowired
    private TaiKhoanDao taiKhoanDao;
    
    @Autowired
    private thuCungDao thuCungDao;
    
    @Autowired
    private datDichVuDao datDichVuDao;
    
    @Autowired
	UserService userService;
    
    // Hiển thị form đặt dịch vụ
    @RequestMapping("/customer/DatDichVu")
    public String showForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
    	String email = userDetails.getUsername();
	    TaiKhoan taiKhoan = userService.findByEmail(email);
	    if (taiKhoan == null) {
	        throw new RuntimeException("Không tìm thấy tài khoản.");
	    }
        List<DichVu> dichVuList = dichVuDao.findAll();
        
        LocalDate today = LocalDate.now();

        // Truyền dữ liệu vào model để hiển thị trên giao diện
        model.addAttribute("dichVuList", dichVuList);
        model.addAttribute("hoTen", taiKhoan.getHoTen());
        model.addAttribute("soDienThoai", taiKhoan.getSoDienThoai());
        model.addAttribute("today", today);

        return "redirect:/customer/DatDichVu";
    }

    // Xử lý yêu cầu thêm dịch vụ
    @RequestMapping(value = "/customer/ThemdichVu", method = RequestMethod.POST)
    public String submitForm(
        @RequestParam("dichVuId") int dichVuId,
        @RequestParam("petName") String tenThuCung,
        @RequestParam("loaiThuCung") int loaiThuCung,
        @RequestParam("giongThuCung") String gioiTinhStr,
        @RequestParam("tuoi") int thangTuoi,
        @RequestParam("canNang") float canNang,
        @RequestParam(value = "ghiChu", defaultValue = "") String ghiChu, // Cung cấp giá trị mặc định rỗng
        @AuthenticationPrincipal UserDetails userDetails,
        Model model) {
    	
    	String email = userDetails.getUsername();
	    TaiKhoan taiKhoan = userService.findByEmail(email);
	    if (taiKhoan == null) {
	        throw new RuntimeException("Không tìm thấy tài khoản.");
	    }
        
        // Tạo mới đối tượng ThuCungEntity
        ThuCungEntity thuCung = new ThuCungEntity();
        thuCung.setTaiKhoan(taiKhoan);
        thuCung.setTenThuCung(tenThuCung);
        thuCung.setLoaiThuCung(loaiThuCung);
        thuCung.setGioiTinh(gioiTinhStr.equals("male"));
        thuCung.setThangTuoi(thangTuoi);
        thuCung.setCanNang(canNang);
        thuCung.setGhiChu(ghiChu); // Nếu ghiChu không có giá trị, nó sẽ là một chuỗi rỗng
        thuCungDao.save(thuCung);

        // Tạo mới đối tượng DatDichVuEntity
        DichVu dichVu = dichVuDao.findById(dichVuId).orElse(null);
        DatDichVuEntity datDichVu = new DatDichVuEntity();
        datDichVu.setTaiKhoan(taiKhoan);
        datDichVu.setThuCung(thuCung);
        datDichVu.setDichVu(dichVu);
        datDichVu.setNgayDat(java.sql.Date.valueOf(LocalDate.now()));
        datDichVu.setTrangThai(false); // Đặt mặc định chưa thực hiện
        datDichVuDao.save(datDichVu);

        // Truyền thông báo thành công
        model.addAttribute("message", "Yêu cầu dịch vụ đã được gửi thành công!");
        return "redirect:/customer/DatDichVu"; 
    }
    
}
