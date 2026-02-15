package com.poly.petshop.Dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.petshop.Entity.ThuCungEntity;

public interface thuCungDao extends JpaRepository<ThuCungEntity, Integer> {
	
}
