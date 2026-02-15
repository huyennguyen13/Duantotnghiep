package com.poly.petshop.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.petshop.Dao.SanPhamDao;
import com.poly.petshop.dto.ThongKeSanPhamDTO;

@Service
public class ThongKeSanPhamService {
	@Autowired
    private SanPhamDao sanphamDAO;

    public ThongKeSanPhamDTO layThongKeSanPham(Date startDate, Date endDate) {
        return sanphamDAO.thongKeSanPham(startDate, endDate);
    }
    public ThongKeSanPhamDTO layThongKeSanPhamTheoNgay(Date startDate, Date endDate) {
        return sanphamDAO.thongKeSanPhamTheoNgay(startDate, endDate);
    }
//    public List<sanPhamEntity> laySanPhamDaBanTheoNgay(LocalDate startDate, LocalDate endDate) {
//        return sanphamDAO.findSanPhamByDateRange(startDate, endDate);
//    }
//    public List<Object[]> laySanPhamTheoNgay(LocalDate startDate, LocalDate endDate) {
//        return sanphamDAO.getSanPhamWithSalesByDateRange(startDate, endDate);
//    }
    public List<Object[]> layThongTinSanPhamVoiSoLuongDaBan(Date startDate, Date endDate) {
        return sanphamDAO.getSanPhamWithSales(startDate, endDate);
    }


}


