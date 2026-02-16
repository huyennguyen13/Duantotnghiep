package com.poly.petshop.Dao;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poly.petshop.Entity.SanPhamEntity;
import com.poly.petshop.dto.ThongKeSanPhamDTO;

import jakarta.transaction.Transactional;

@Repository
public interface SanPhamDao extends JpaRepository<SanPhamEntity, Integer> {

	Page<SanPhamEntity> findByLoaiSanPhamAndLoaiThuCung(Integer loaiSanPham, Integer loaiThuCung, Pageable pageable);

	Page<SanPhamEntity> findByLoaiSanPham(Integer loaiSanPham, Pageable pageable);

	Page<SanPhamEntity> findByLoaiThuCung(Integer loaiThuCung, Pageable pageable);

	Page<SanPhamEntity> findByTenSanPhamContainingIgnoreCase(String tenSanPham, Pageable pageable);

	Page<SanPhamEntity> findByGiaBanBetween(Double giaMin, Double giaMax, Pageable pageable);

	@Query("SELECT s FROM SanPhamEntity s WHERE (:tenSanPham IS NULL OR s.tenSanPham LIKE %:tenSanPham%)"
			+ " AND (:loaiSanPham IS NULL OR s.loaiSanPham = :loaiSanPham)"
			+ " AND (:loaiThuCung IS NULL OR s.loaiThuCung = :loaiThuCung)"
			+ " AND (:giaMin IS NULL OR s.giaBan >= :giaMin)"
			+ " AND (:giaMax IS NULL OR s.giaBan <= :giaMax)")
	Page<SanPhamEntity> filterSanPhamPage(
			@Param("tenSanPham") String tenSanPham,
			@Param("loaiSanPham") Integer loaiSanPham,
			@Param("loaiThuCung") Integer loaiThuCung,
			@Param("giaMin") Double giaMin,
			@Param("giaMax") Double giaMax,
			Pageable pageable);

	Optional<SanPhamEntity> findById(Integer sanPhamId);

	List<SanPhamEntity> findAllById(Iterable<Integer> ids);

	// ✅ FIX RANDOM CHO POSTGRESQL
	@Query("SELECT sp FROM SanPhamEntity sp ORDER BY FUNCTION('RANDOM')")
	List<SanPhamEntity> findRandomLowStockProducts();

	// ================= THỐNG KÊ =================

	@Query("SELECT new com.poly.petshop.dto.ThongKeSanPhamDTO( "
			+ "COALESCE(SUM(cthd.soLuong), 0), "
			+ "COALESCE(SUM(cthd.soLuong * cthd.donGia), 0), "
			+ "COALESCE(SUM(cthd.soLuong * sp.giaBan), 0) - COALESCE(SUM(cthd.soLuong * sp.giaNhap), 0), "
			+ "(SELECT COALESCE(SUM(sp.soLuongKho), 0) FROM SanPhamEntity sp)) "
			+ "FROM CthdEntity cthd "
			+ "JOIN cthd.sanPhams sp "
			+ "JOIN cthd.hoaDons hd "
			+ "WHERE hd.trangThai = true AND hd.ngayTao BETWEEN :startDate AND :endDate "
			+ "GROUP BY hd.ngayTao "
			+ "ORDER BY hd.ngayTao")
	ThongKeSanPhamDTO thongKeSanPham(Date startDate, Date endDate);

	@Transactional
	@Query("SELECT new com.poly.petshop.dto.ThongKeSanPhamDTO( "
			+ "COALESCE(SUM(cthd.soLuong), 0), "
			+ "COALESCE(SUM(cthd.soLuong * cthd.donGia), 0), "
			+ "COALESCE(SUM(cthd.soLuong * sp.giaBan), 0) - COALESCE(SUM(cthd.soLuong * sp.giaNhap), 0), "
			+ "(SELECT COALESCE(SUM(sp.soLuongKho), 0) FROM SanPhamEntity sp)) "
			+ "FROM CthdEntity cthd "
			+ "JOIN cthd.sanPhams sp "
			+ "JOIN cthd.hoaDons hd "
			+ "WHERE hd.trangThai = true AND hd.ngayTao BETWEEN :startDate AND :endDate")
	ThongKeSanPhamDTO thongKeSanPhamTheoNgay(Date startDate, Date endDate);

	@Query(value = """
			SELECT
			    sp.SanPhamId AS sanPhamId,
			    sp.TenSanPham AS tenSanPham,
			    sp.SoLuongKho AS soLuongKho,
			    COALESCE(SUM(CASE WHEN hd.TrangThai = 1 THEN cthd.SoLuong ELSE 0 END), 0) AS soLuongDaBan,
			    COALESCE(SUM(CASE WHEN hd.TrangThai = 1 THEN cthd.SoLuong * cthd.DonGia ELSE 0 END), 0) AS doanhThu,
			    COALESCE(SUM(CASE WHEN hd.TrangThai = 1 THEN cthd.SoLuong * (sp.GiaBan - sp.GiaNhap) ELSE 0 END), 0) AS loiNhuan
			FROM
			    SanPham sp
			LEFT JOIN
			    ChiTietHoaDon cthd ON sp.SanPhamId = cthd.SanPhamId
			LEFT JOIN
			    HoaDon hd ON cthd.HoaDonId = hd.HoaDonId
			WHERE
			    hd.NgayTao BETWEEN :startDate AND :endDate
			GROUP BY
			    sp.SanPhamId, sp.TenSanPham, sp.SoLuongKho
			ORDER BY
			    sp.SanPhamId
			""", nativeQuery = true)
	List<Object[]> getSanPhamWithSales(
			@Param("startDate") Date startDate,
			@Param("endDate") Date endDate);
}
