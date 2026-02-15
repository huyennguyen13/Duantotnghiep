package com.poly.petshop.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.petshop.Entity.KhuyenMaiEntity;


@Repository
public interface KhuyenMaiDao extends JpaRepository<KhuyenMaiEntity, Integer>{

}
