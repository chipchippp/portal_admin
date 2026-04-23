package com.portal.identity_service.enums;

public enum Role {
    ADMIN,      // Quản trị tối cao
    MANAGER,    // Quản lý kho, sản phẩm, doanh thu
    STAFF,      // Nhân viên hỗ trợ, xử lý đơn hàng
    USER        // Khách hàng mua sắm
}

//User -> many Role
// Role -> many Permission