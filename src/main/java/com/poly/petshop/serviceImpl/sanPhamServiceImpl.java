package com.poly.petshop.serviceImpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.poly.petshop.Dao.SanPhamDao;
import com.poly.petshop.Entity.SanPhamEntity;
import com.poly.petshop.Service.SanPhamService;

import java.util.List;

@Service
public class sanPhamServiceImpl implements SanPhamService {

	@Autowired
	private SanPhamDao sanPhamDao;

	@Override
	public List<SanPhamEntity> getAllSanPham() {
		return sanPhamDao.findAll();
	}

	@Override
	public SanPhamEntity getSanPhamById(Integer id) {
		return sanPhamDao.findById(id)
				.orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
	}

	@Override
	public Page<SanPhamEntity> getAllSanPhamPaginated(int page, int size, String sortOrder) {
		Sort sort = "ASC".equalsIgnoreCase(sortOrder)
				? Sort.by(Sort.Order.asc("giaBan"))
				: Sort.by(Sort.Order.desc("giaBan")); // Sort by giaBan
		Pageable pageable = PageRequest.of(page, size, sort);
		return sanPhamDao.findAll(pageable);
	}

	@Override
	public Page<SanPhamEntity> filterSanPham(String tenSanPham, Integer loaiSanPham, Integer loaiThuCung,
                                       Double giaMin, Double giaMax, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);

		if (loaiSanPham != null && loaiThuCung != null) {
			return sanPhamDao.findByLoaiSanPhamAndLoaiThuCung(loaiSanPham, loaiThuCung, pageable);
		} else if (tenSanPham != null) {
			return sanPhamDao.findByTenSanPhamContainingIgnoreCase(tenSanPham, pageable);
		} else if (giaMin != null && giaMax != null) {
			return sanPhamDao.findByGiaBanBetween(giaMin, giaMax, pageable);
		} else if (loaiSanPham != null) {
			return sanPhamDao.findByLoaiSanPham(loaiSanPham, pageable);
		} else if (loaiThuCung != null) {
			return sanPhamDao.findByLoaiThuCung(loaiThuCung, pageable);
		} else {
			return sanPhamDao.findAll(pageable);
		}
	}

	@Override
	public Page<SanPhamEntity> filterSanPhamWithSort(
			String tenSanPham, Integer loaiSanPham, Integer loaiThuCung,
			Double giaMin, Double giaMax, int page, int size, String sortOrder) {

		Sort sort = "ASC".equalsIgnoreCase(sortOrder)
				? Sort.by(Sort.Order.asc("giaBan"))
				: Sort.by(Sort.Order.desc("giaBan")); // Sort by giaBan
		Pageable pageable = PageRequest.of(page, size, sort);

		if (loaiThuCung != null) {
			if (loaiThuCung == 1) {
				return sanPhamDao.filterSanPhamPage(tenSanPham, loaiSanPham, null, giaMin, giaMax, pageable);
			} else {
				return sanPhamDao.filterSanPhamPage(tenSanPham, loaiSanPham, loaiThuCung, giaMin, giaMax, pageable);
			}
		} else {
			return sanPhamDao.filterSanPhamPage(tenSanPham, loaiSanPham, null, giaMin, giaMax, pageable);
		}
	}


}
