package com.cnpm.mapper;



import org.mapstruct.Mapper;

import com.cnpm.dto.VoucherDTO;
import com.cnpm.entity.Voucher;

@Mapper(componentModel = "spring")
public interface VoucherMapper {

    Voucher toVoucher(VoucherDTO voucherDTO);
}
