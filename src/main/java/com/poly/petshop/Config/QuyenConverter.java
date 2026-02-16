package com.poly.petshop.Config;

import com.poly.petshop.Classer.Quyen;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class QuyenConverter implements AttributeConverter<Quyen, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Quyen quyen) {
        if (quyen == null) {
            return null; // Không nên throw, tránh lỗi khi save
        }
        return quyen.getValue();
    }

    @Override
    public Quyen convertToEntityAttribute(Integer value) {
        if (value == null) {
            return null; // Tránh crash khi DB null
        }
        return Quyen.fromValue(value);
    }
}
