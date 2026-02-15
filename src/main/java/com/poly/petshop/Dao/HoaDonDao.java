package com.poly.petshop.Dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poly.petshop.Entity.HoaDonEntity;
import com.poly.petshop.Entity.TaiKhoan;


@Repository
public interface HoaDonDao extends JpaRepository<HoaDonEntity, Integer>{
	@Query("SELECT hd FROM HoaDonEntity hd WHERE hd.hoaDonId = :hoaDonId")
	List<HoaDonEntity> findByHoaDonId(@Param("hoaDonId") int hoaDonId);
	
	@Query("select hd from HoaDonEntity hd where hd.taiKhoans = :taiKhoanId")
	HoaDonEntity findByTaiKhoanID(@Param("taiKhoanId") int taiKhoanId);
	
	@Query(value = "SELECT TOP 1 h.hoaDonId FROM hoadon h WHERE h.taiKhoanId = :taiKhoanId ORDER BY h.hoaDonId DESC", nativeQuery = true)
	Optional<Integer> findLatestHoaDonIdByTaiKhoanId(@Param("taiKhoanId") Integer taiKhoanId);
	
	@Query("SELECT hd FROM HoaDonEntity hd WHERE hd.choXacNhan = 1")
	List<HoaDonEntity> findAlltrangThai();
	
	@Query("SELECT h FROM HoaDonEntity h WHERE h.taiKhoans.taiKhoanId = :taiKhoanId")
    List<HoaDonEntity> findHoaDonsByTaiKhoanId(@Param("taiKhoanId") int taiKhoanId);
	
	public List<HoaDonEntity> findByChoXacNhan(Integer choXacNhan);
}
