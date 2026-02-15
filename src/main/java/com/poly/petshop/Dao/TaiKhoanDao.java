package com.poly.petshop.Dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poly.petshop.Entity.TaiKhoan;

@Repository
public interface TaiKhoanDao extends JpaRepository<TaiKhoan, Integer>{
//	@Query("SELECT u FROM TaiKhoan u WHERE u.email = ?1")
	Optional<TaiKhoan> findByEmail(String email);
	
	@Query("SELECT c FROM TaiKhoan c WHERE c.maThongBao = ?1")
	TaiKhoan findByresetpasswordtoken(String maThongBao);
	boolean existsByEmail(String email);
	TaiKhoan findByTaiKhoanId(int taiKhoanId);
	TaiKhoan findByHoTen(String hoTen);
	@Query("SELECT t FROM TaiKhoan t WHERE t.email = :email AND t.matKhau = :matKhau")
	TaiKhoan findByEmailAndPassword(@Param("email") String email, @Param("matKhau") String matKhau);

	//Phú
	boolean existsBySoDienThoai(String phone);
}
