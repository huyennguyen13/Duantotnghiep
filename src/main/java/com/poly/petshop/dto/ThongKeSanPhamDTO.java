package com.poly.petshop.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ThongKeSanPhamDTO {
    private Long tongSanPhamDaBan;
    private Double tongDoanhThu;
    private Double tongLoiNhuan;
    private Long tongSanPhamTrongKho;
    

}
