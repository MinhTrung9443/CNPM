package com.cnpm.service;

import com.cnpm.entity.Voucher;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface IVoucherService {
    Double getDiscount(String voucherCode);
    Optional<Voucher> findFirst(String voucherCode);
}