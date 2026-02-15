package com.poly.petshop.Config;

import com.poly.petshop.Classer.Quyen;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.logging.Logger;

@Converter(autoApply = true)
public class QuyenConverter implements AttributeConverter<Quyen, Integer> {

    private static final Logger logger = Logger.getLogger(QuyenConverter.class.getName());

    @Override
    public Integer convertToDatabaseColumn(Quyen quyen) {
        if (quyen == null) {
            return Quyen.KHACH_HANG.getValue(); // Hoặc bất kỳ giá trị nào mà bạn muốn sử dụng làm mặc định
        }
        return quyen.getValue();
    }


    @Override
    public Quyen convertToEntityAttribute(Integer value) {
        try {
            return value != null ? Quyen.fromValue(value) : null;
        } catch (IllegalArgumentException e) {
            // Ghi log cảnh báo
            logger.warning("Giá trị không hợp lệ cho Quyền: " + value);
            return null; // Hoặc thay bằng Quyen.KHACH_HANG nếu muốn giá trị mặc định
        }
    }
}
