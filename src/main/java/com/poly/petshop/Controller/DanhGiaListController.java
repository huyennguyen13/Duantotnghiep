package com.poly.petshop.Controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.poly.petshop.Dao.DanhGiaDao;
import com.poly.petshop.Dao.SanPhamDao;
import com.poly.petshop.Entity.DanhGia;
import com.poly.petshop.Entity.SanPhamEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;
import org.springframework.web.bind.annotation.PostMapping;
@Controller
@RequestMapping("/danhgia")
public class DanhGiaListController {

    @Autowired
    private DanhGiaDao danhGiaDao;

    @Autowired
    private SanPhamDao sanPhamDao;
    @Autowired
    private Cloudinary cloudinary;

    @GetMapping("/listAll")
    public String listAllDanhGia(Model model) {
        // Lấy toàn bộ danh sách đánh giá
        model.addAttribute("danhGiaList", danhGiaDao.findAll());
        return "views/ListDanhGia";
    }
    @PostMapping("/listAll/delete/{danhGiaId}")
    public String deleteDanhGia(@PathVariable("danhGiaId") int danhGiaId, Model model) {
        Optional<DanhGia> danhGiaOpt = danhGiaDao.findById(danhGiaId);

        if (danhGiaOpt.isPresent()) {
            DanhGia danhGia = danhGiaOpt.get();

            // Lấy URL của ảnh từ cơ sở dữ liệu
            String imageUrl = danhGia.getHinh();

            // Kiểm tra xem ảnh có URL hợp lệ không
            if (imageUrl != null && !imageUrl.isEmpty()) {
                try {
                    // Trích xuất phần publicId từ URL ảnh Cloudinary
                    String publicId = imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.lastIndexOf("."));
                    // Gọi Cloudinary để xóa ảnh
                    cloudinary.uploader().destroy("danhgia/" + publicId, ObjectUtils.emptyMap());
                    System.out.println("Xóa ảnh thành công từ Cloudinary: " + publicId);
                } catch (Exception e) {
                    model.addAttribute("error", "Không thể xóa ảnh: " + e.getMessage());
                    return "views/ErrorPage";
                }
            }

            // Xóa đánh giá trong DB
            danhGiaDao.deleteById(danhGiaId);
        } else {
            model.addAttribute("error", "Đánh giá không tồn tại.");
        }

        return "redirect:/danhgia/listAll";
    }

}
