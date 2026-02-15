package com.poly.petshop.Controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.poly.petshop.Config.VNPayService;
import com.poly.petshop.Dao.CthdDao;
import com.poly.petshop.Dao.HoaDonDao;
import com.poly.petshop.Dao.SanPhamDao;
import com.poly.petshop.Dao.TaiKhoanDao;
import com.poly.petshop.Entity.CthdEntity;
import com.poly.petshop.Entity.HoaDonEntity;
import com.poly.petshop.Entity.SanPhamEntity;
import com.poly.petshop.Entity.TaiKhoan;
import com.poly.petshop.Service.HoaDonService;
import com.poly.petshop.Service.UserService;
import com.poly.petshop.dto.CartItems;
import com.poly.petshop.dto.CthdDTO;
import com.poly.petshop.dto.HoaDonRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class DatHangController {

	@Autowired
	CthdDao cthdDao;

	@Autowired
	SanPhamDao sanphamDao;

	@Autowired
	HoaDonDao hoadonDao;

	@Autowired
	TaiKhoanDao taikhoanDao;
	
	@Autowired
	UserService userService;
	
	@Autowired
    private HoaDonService hoaDonService;
	
	@Autowired
	private VNPayService vnPayService;
	
	@RequestMapping("/customer/datHang")
	public String showDatHang(Model model,@AuthenticationPrincipal UserDetails userDetails, HttpSession session) {
	    List<CartItems> cartItems = (List<CartItems>) session.getAttribute("cartItems");
	    if (cartItems == null) {
	        cartItems = new ArrayList<>();
	    }
	    model.addAttribute("cartItems", cartItems);
	    //model.addAttribute("hoaDonId", hd.getHoaDonId());  // Thêm ID của hóa đơn mới vào model

	    return "views/datHang";  // Tên template giao diện
	}


	@PostMapping("/customer/save-cart")
	public String createHoaDon(
	        @ModelAttribute HoaDonRequest request,
	        @RequestParam("paymentMethod") String paymentMethod,
	        @AuthenticationPrincipal UserDetails userDetails,
	        HttpSession session,
	        Model model) {

	    String email = userDetails.getUsername();
	    TaiKhoan taiKhoan = userService.findByEmail(email);
	    if (taiKhoan == null) {
	        throw new RuntimeException("Không tìm thấy tài khoản.");
	    }

	    List<CartItems> cartItems = (List<CartItems>) session.getAttribute("cartItems");
	    if (cartItems == null || cartItems.isEmpty()) {
	        throw new RuntimeException("Giỏ hàng rỗng, không thể tạo hóa đơn.");
	    }

	    // Tạo hóa đơn mới cho mỗi đơn hàng
	    HoaDonEntity hoaDonEntity = new HoaDonEntity();
	    hoaDonEntity.setTaiKhoans(taiKhoan);
	    hoaDonEntity.setNgayTao(new Date());
	    hoaDonEntity.setDiaChi(request.getAddress());
	    hoaDonEntity.setPhuongThucThanhToan(1);
	    hoaDonEntity.setTongTien(Double.parseDouble(request.getTotalPayment()));
	    hoaDonEntity.setTrangThai(false);  // Trạng thái chưa thanh toán
	    hoaDonEntity.setChoXacNhan(1);
	    hoadonDao.save(hoaDonEntity); // Lưu hóa đơn mới

	    // Lặp qua giỏ hàng và lưu chi tiết vào hóa đơn mới
	    for (CartItems item : cartItems) {
	        SanPhamEntity sanPham = sanphamDao.findById(item.getSanPhamId())
	                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại với ID: " + item.getSanPhamId()));

	        // Kiểm tra số lượng sản phẩm trong kho
	        if (sanPham.getSoLuongKho() < item.getSoLuong()) {
	            throw new RuntimeException("Số lượng sản phẩm trong kho không đủ.");
	        }

	        // Trừ số lượng sản phẩm trong kho
	        sanPham.setSoLuongKho(sanPham.getSoLuongKho() - item.getSoLuong());

	        // Lưu chi tiết hóa đơn
	        CthdEntity cthd = new CthdEntity();
	        cthd.setHoaDons(hoaDonEntity);
	        cthd.setSanPhams(sanPham);
	        cthd.setSoLuong(item.getSoLuong());
	        cthd.setDonGia(item.getDonGia());
	        cthdDao.save(cthd);

	        // Cập nhật lại sản phẩm trong cơ sở dữ liệu
	        sanphamDao.save(sanPham);
	    }

	    // Xóa giỏ hàng trong session
	    session.removeAttribute("cartItems");

	    // Kiểm tra phương thức thanh toán
	    if ("1".equals(paymentMethod)) { // Thanh toán trực tuyến
	    	
	    	Double tongTien = Double.parseDouble(request.getTotalPayment());
	    	int intValue = (int) Math.round(tongTien);
	    	String mahd = String.valueOf(hoaDonEntity.getHoaDonId());
	    	
	    	String orderInfo = String.valueOf(hoaDonEntity.getHoaDonId());
	        String paymentUrl = vnPayService.createOrder(intValue, orderInfo, "http://localhost:8080");
	        
	    	model.addAttribute("tongtien", intValue);
	    	model.addAttribute("mahd", mahd);
	    	
	    	 return "redirect:" + paymentUrl;
	    } else if ("2".equals(paymentMethod)) { // Thanh toán khi nhận hàng
	        return "redirect:/customer/sanpham"; // Chuyển hướng trang sản phẩm (hiện tại)
	    } else {
	        throw new IllegalArgumentException("Phương thức thanh toán không hợp lệ.");
	    }
	}

	@PostMapping("/submitOrder")
	public String submidOrder(@RequestParam("amount") int orderTotal, @RequestParam("orderInfo") String orderInfo,
			HttpServletRequest request) {
		String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
		String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo, baseUrl);
		return "redirect:" + vnpayUrl;
	}

	@GetMapping("/vnpay-payment")
	public String vnpayPayment(HttpServletRequest request, HttpSession session, Model model) {
	    String vnpResponseCode = request.getParameter("vnp_ResponseCode");
	    String orderId = request.getParameter("vnp_OrderInfo");

	    if ("00".equals(vnpResponseCode)) { // Thanh toán thành công
	        HoaDonEntity hoaDonEntity = hoadonDao.findById(Integer.parseInt(orderId))
	                .orElseThrow(() -> new RuntimeException("Hóa đơn không tồn tại"));
	        
	        hoaDonEntity.setTrangThai(true);  // Đánh dấu hóa đơn là đã thanh toán
	        hoaDonEntity.setNgayTao(new Date());  // Cập nhật ngày thanh toán
	        hoaDonEntity.setPhuongThucThanhToan(2); // Cập nhật pttt online
	        hoadonDao.save(hoaDonEntity); // Lưu hóa đơn đã thanh toán

	        // Cập nhật giỏ hàng vào cơ sở dữ liệu
	        List<CartItems> cartItems = (List<CartItems>) session.getAttribute("cartItems");
	        if (cartItems != null) {
	            for (CartItems item : cartItems) {
	                SanPhamEntity sanPham = sanphamDao.findById(item.getSanPhamId())
	                        .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

	                // Lưu chi tiết hóa đơn
	                CthdEntity cthd = new CthdEntity();
	                cthd.setHoaDons(hoaDonEntity);
	                cthd.setSanPhams(sanPham);
	                cthd.setSoLuong(item.getSoLuong());
	                cthd.setDonGia(item.getDonGia());
	                cthdDao.save(cthd);

	                // Cập nhật số lượng tồn kho
	                sanPham.setSoLuongKho(sanPham.getSoLuongKho() - item.getSoLuong());
	                sanphamDao.save(sanPham);
	            }

	            // Xóa giỏ hàng trong session
	            session.removeAttribute("cartItems");
	        }
	        String paymentTime = request.getParameter("vnp_PayDate");
			String transactionId = request.getParameter("vnp_TransactionNo");
	        model.addAttribute("orderId", orderId);
	        model.addAttribute("totalPrice", hoaDonEntity.getTongTien());
	        model.addAttribute("paymentTime", paymentTime);
			model.addAttribute("transactionId", transactionId);
	        return "views/ordersuccess"; // Trang thanh toán thành công
	    } else {
	        return "views/orderfail"; // Trang thanh toán thất bại
	    }
	}

}
