package com.poly.petshop.Classer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum Quyen {
	QUAN_LY(1),
    NHAN_VIEN(2),
    KHACH_HANG(3);

	private final int value;

	private static final Map<Integer, Quyen> VALUE_MAP;

	static {
	    Map<Integer, Quyen> map = new HashMap<>();
	    for (Quyen quyen : Quyen.values()) {
	        map.put(quyen.getValue(), quyen);
	    }
	    VALUE_MAP = Collections.unmodifiableMap(map);
	}


    Quyen(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    // Phương thức từ giá trị int (tinyint từ CSDL) sang enum
    public static Quyen fromValue(int value) {
        Quyen quyen = VALUE_MAP.get(value);
        if (quyen == null) {
            throw new IllegalArgumentException("Giá trị không hợp lệ cho Quyền: " + value);
        }
        return quyen;
    }

    // Phương thức từ String sang enum (so sánh không phân biệt chữ hoa và chữ thường)
    public static Quyen fromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Giá trị không thể là null");
        }
        for (Quyen quyen : Quyen.values()) {
            if (quyen.name().equalsIgnoreCase(value)) {
                return quyen;
            }
        }
        throw new IllegalArgumentException("Giá trị không hợp lệ cho Quyền: " + value);
    }

}


