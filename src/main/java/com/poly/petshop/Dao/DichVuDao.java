package com.poly.petshop.Dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.petshop.Entity.DichVu;

public interface DichVuDao extends JpaRepository<DichVu, Integer>{
	Optional<DichVu> findByDichVuId(int dichVuId);
	
}
