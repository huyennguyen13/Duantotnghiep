package com.poly.petshop.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poly.petshop.Entity.CthdEntity;

import java.util.List;

@Repository
public interface CthdDao extends JpaRepository<CthdEntity, Integer>{
	@Query("SELECT cthd FROM CthdEntity cthd WHERE cthd.hoaDons.hoaDonId = :hoaDonId")
	List<CthdEntity> findByHoaDonId(@Param("hoaDonId") int hoaDonId);
	
	List<CthdEntity> findByHoaDons_HoaDonId(int hoaDonId);
}
