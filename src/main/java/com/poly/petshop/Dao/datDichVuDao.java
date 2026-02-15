package com.poly.petshop.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.poly.petshop.Entity.DatDichVuEntity;


@Repository
public interface datDichVuDao extends JpaRepository<DatDichVuEntity, Integer> {

    /**
     * Lấy danh sách dịch vụ kèm thông tin tài khoản, thú cưng, và dịch vụ liên quan.
     * @return Danh sách các đối tượng DatDichVuEntity.
     */
    @Query("SELECT ddv FROM DatDichVuEntity ddv " +
           "JOIN FETCH ddv.taiKhoan tk " +
           "JOIN FETCH ddv.thuCung tc " +
           "JOIN FETCH ddv.dichVu dv")
    List<DatDichVuEntity> findAllWithRelations();
}
//package com.poly.petshop.Dao;
//
//import java.util.List;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import com.poly.petshop.Entity.DatDichVuEntity;
//
//
//
//@Repository
//public interface datDichVuDao extends JpaRepository<DatDichVuEntity, Integer> {
//
//    @Query("SELECT ddv FROM DatDichVuEntity ddv " +
//           "JOIN FETCH ddv.taiKhoan tk " +
//           "JOIN FETCH ddv.thuCung tc " +
//           "JOIN FETCH ddv.dichVu dv")
//    List<DatDichVuEntity> findAllWithRelations();
//
//    @Query("SELECT ddv FROM DatDichVuEntity ddv " +
//           "JOIN FETCH ddv.taiKhoan tk " +
//           "JOIN FETCH ddv.thuCung tc " +
//           "JOIN FETCH ddv.dichVu dv " +
//           "WHERE (:taiKhoanId IS NULL OR tk.id = :taiKhoanId) " +
//           "AND (:thuCungId IS NULL OR tc.id = :thuCungId) " +
//           "AND (:dichVuId IS NULL OR dv.id = :dichVuId)")
//    List<DatDichVuEntity> findByFilters(Integer taiKhoanId, Integer thuCungId, Integer dichVuId);
//}

