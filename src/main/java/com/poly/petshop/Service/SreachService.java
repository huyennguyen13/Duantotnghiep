package com.poly.petshop.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.poly.petshop.Dao.SanPhamDao;
import com.poly.petshop.Dao.SreachDAO;
import com.poly.petshop.Entity.SanPhamEntity;

@Service
public class SreachService {
	@Autowired
	private SreachDAO sreachDAO;
	
    private final SanPhamDao sanphamDAO;

    public SreachService(SanPhamDao sanphamDAO) {
        this.sanphamDAO = sanphamDAO;
    }
    public List<SanPhamEntity> searchSanPhamWithoutPaging(String query) {
        // Thực hiện tìm kiếm sản phẩm mà không phân trang
        return sreachDAO.findByTenSanPhamContainingIgnoreCase(query); // Sử dụng phương thức tìm kiếm trong repository
    }
}

