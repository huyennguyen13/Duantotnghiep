package com.poly.petshop.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class HoaDonRequest {
	private String address;
    private String totalPayment;
}
