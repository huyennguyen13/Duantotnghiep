package com.poly.petshop.Controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import jxl.write.Number;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.MediaType;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poly.petshop.Service.ThongKeSanPhamService;
import com.poly.petshop.dto.ThongKeSanPhamDTO;


@Controller
@RequestMapping("/admin")
public class ThongKeSanPhamController {
	@Autowired
    private ThongKeSanPhamService thongKeSanPhamService;
	
	@GetMapping("/thongkesanpham")
    public String thongKeSanPhamTheoNgay(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                         Model model) throws JsonProcessingException {
        // Xử lý ngày mặc định nếu không được cung cấp
        if (startDate == null && endDate == null) {
            endDate = LocalDate.now();
            startDate = LocalDate.now().minusDays(7);
        } else if (startDate == null) {
            startDate = endDate.minusDays(7);
        } else if (endDate == null) {
            endDate = startDate.plusDays(7);
        }

        // Chuyển đổi LocalDate thành java.sql.Date
        java.sql.Date startSqlDate = java.sql.Date.valueOf(startDate);
        java.sql.Date endSqlDate = java.sql.Date.valueOf(endDate);

        // Gọi service lấy dữ liệu
        ThongKeSanPhamDTO thongKe = thongKeSanPhamService.layThongKeSanPhamTheoNgay(startSqlDate, endSqlDate);
        List<Object[]> sanPhams = thongKeSanPhamService.layThongTinSanPhamVoiSoLuongDaBan(startSqlDate, endSqlDate);

        // Tạo dữ liệu cho biểu đồ
        List<Map<String, Object>> chartData = new ArrayList<>();
        for (Object[] row : sanPhams) {
            Map<String, Object> dataPoint = new HashMap<>();
            dataPoint.put("tenSanPham", row[1]); // Tên sản phẩm
            dataPoint.put("soLuongDaBan", row[3]); // Số lượng đã bán
            chartData.add(dataPoint);
        }

        // Chuyển chartData thành JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String chartDataJson = objectMapper.writeValueAsString(chartData);

        model.addAttribute("chartDataJson", chartDataJson);
        System.out.println(chartDataJson);

        // Gửi dữ liệu đến giao diện
        model.addAttribute("startDate", startSqlDate);
        model.addAttribute("endDate", endSqlDate);
        model.addAttribute("thongKe", thongKe);
        model.addAttribute("sps", sanPhams);
        System.out.println(sanPhams);

        return "views/quanli/thongkesanpham";
    }
    
    @GetMapping("/exportExcel")
    public ResponseEntity<byte[]> exportToExcel(@RequestParam(required = false) Date startDate,
                                                @RequestParam(required = false) Date endDate,
                                                Model model) throws IOException, RowsExceededException, WriteException {
        // Xử lý ngày mặc định nếu không được cung cấp
        if (startDate == null && endDate == null) {
            endDate = Date.valueOf(LocalDate.now());
            startDate = Date.valueOf(LocalDate.now().minusDays(7));
        } else if (startDate == null) {
            startDate = Date.valueOf(endDate.toLocalDate().minusDays(7));
        } else if (endDate == null) {
            endDate = Date.valueOf(startDate.toLocalDate().plusDays(7));
        }

        // Lấy tất cả thông tin sản phẩm từ service (dữ liệu không phân trang)
        List<Object[]> sanPhams = thongKeSanPhamService.layThongTinSanPhamVoiSoLuongDaBan(startDate, endDate);

        // Tạo workbook và worksheet mới
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        WritableWorkbook workbook = Workbook.createWorkbook(baos);
        WritableSheet sheet = workbook.createSheet("ThongKeSanPham", 0);

        // Tiêu đề của các cột trong bảng Excel
        String[] headers = {"Tên Sản Phẩm", "Số Lượng Trong Kho", "Số Lượng Đã Bán", "Doanh Thu", "Lợi Nhuận"};
        
        // Đưa tiêu đề vào dòng đầu tiên của bảng
        for (int i = 0; i < headers.length; i++) {
            sheet.addCell(new Label(i, 0, headers[i]));
        }

        // Thêm dữ liệu sản phẩm vào Excel
        int rowIndex = 1;
        for (Object[] row : sanPhams) {
            sheet.addCell(new Label(0, rowIndex, (String) row[1]));  // Tên sản phẩm
            sheet.addCell(new Number(1, rowIndex, (int) row[2]));  // Mã sản phẩm
            sheet.addCell(new Number(2, rowIndex, (int) row[3]));
            sheet.addCell(new Number(3, rowIndex, (double) row[4]));
            sheet.addCell(new Number(4, rowIndex, (double) row[5]));
            rowIndex++;
        }

        // Ghi workbook vào ByteArrayOutputStream và đóng workbook
        workbook.write();
        workbook.close();

        // Trả về file Excel dưới dạng byte array
        byte[] excelData = baos.toByteArray();

        // Đặt header để tải file Excel
        HttpHeaders headersResponse = new HttpHeaders();
        headersResponse.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ThongKeSanPham.xlsx");
        headersResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok()
                             .headers(headersResponse)
                             .body(excelData);
    }


}
