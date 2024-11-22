package com.cnpm.service.impl;

import com.cnpm.entity.Voucher;
import com.cnpm.repository.VoucherRepository;
import com.cnpm.service.IVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class VoucherService implements IVoucherService {
    @Autowired
    VoucherRepository voucherRepository;
    @Override
    public Double getDiscount(String voucherCode) {
        Voucher voucher = voucherRepository.findFirstByVoucherCodeAndIsUsedFalseAndStartDateBeforeAndEndDateAfter(voucherCode, LocalDateTime.now(), LocalDateTime.now()).orElseThrow(() -> new RuntimeException("Voucher "+voucherCode+" is not available"));
        return voucher.getVoucherValue();
    }

    @Override
    public Optional<Voucher> findFirst(String voucherCode) {
        return voucherRepository.findFirstByVoucherCodeAndIsUsedFalseAndStartDateBeforeAndEndDateAfter(voucherCode, LocalDateTime.now(), LocalDateTime.now());
    }
}
