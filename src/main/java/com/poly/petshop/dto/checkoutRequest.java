package com.poly.petshop.dto;


import java.util.List;

public class checkoutRequest {
    private String email;
    private String matKhau;
    private int quyen;  // 1: Quản lý, 2: Nhân viên, 3: Khách hàng
    private String diaChi;
    private int khuyenMaiId;
    private int gioHangId;
    private byte phuongThucThanhToan; // 1: Tiền mặt, 2: VN Pay
    private float tongTien;
    private List<CartItems> cartItems;

    // Getter và Setter
}

