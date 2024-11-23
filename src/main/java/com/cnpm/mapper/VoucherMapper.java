package com.cnpm.mapper;

import com.cnpm.dto.VoucherDTO;
import com.cnpm.dto.VoucherDTO;
import com.cnpm.entity.Voucher;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VoucherMapper {

    Voucher toVoucher(VoucherDTO voucherDTO);
}
