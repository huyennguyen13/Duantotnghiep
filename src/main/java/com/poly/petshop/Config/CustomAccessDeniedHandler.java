package com.poly.petshop.Config;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		// Xác định vai trò của người dùng hiện tại
        if (request.isUserInRole("ROLE_QUAN_LY") || request.isUserInRole("ROLE_NHAN_VIEN")) {
			response.sendRedirect("/employee/TrangQuanTri?error=access_denied");
		}else if(request.isUserInRole("ROLE_KHACH_HANG")){
			response.sendRedirect("/customer/TrangChu?error=access_denied");
		}

	}

}
