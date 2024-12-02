package com.cnpm.service.impl;

import com.cnpm.dto.VoucherDTO;
import com.cnpm.entity.Voucher;
import com.cnpm.repository.VoucherRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoucherService {

    VoucherRepository voucherRepository;

    public Voucher saveVoucher(VoucherDTO voucherDTO) {
        Voucher voucher = new Voucher();
        voucher.setVoucherValue(voucherDTO.getVoucherValue());
        voucher.setStartDate(voucherDTO.getStartDate());
        voucher.setEndDate(voucherDTO.getEndDate());
        return voucherRepository.save(voucher);
    }

    public List<Voucher> getAllVoucher() {
        return voucherRepository.findAll();
    }

    public void deleteVoucherById(Long voucherId) {
        if (!voucherRepository.existsById(voucherId)) {
            throw new IllegalArgumentException("Voucher với ID không tồn tại");
        }
        voucherRepository.deleteById(voucherId);
    }

    public Voucher getVoucherByID(Long voucherId) {

        Voucher voucher = voucherRepository.findById(voucherId).orElseThrow(() -> new RuntimeException("Voucher not found!"));
        return voucher;
    }

    public Voucher updateVoucher(Long voucherId, VoucherDTO voucherDTO) {
        if (!voucherRepository.existsById(voucherId)) {
            throw new IllegalArgumentException("Voucher với ID không tồn tại");
        }

            Voucher voucher = voucherRepository.findById(voucherId).orElseThrow(() -> new RuntimeException("Voucher not found!"));
            voucher.setVoucherValue(voucherDTO.getVoucherValue());
            voucher.setStartDate(voucherDTO.getStartDate());
            voucher.setEndDate(voucherDTO.getEndDate());

            voucherRepository.save(voucher);
            return voucher;
    }
}
