package com.cnpm.service.impl;

import com.cnpm.dto.VoucherDTO;
import com.cnpm.entity.Voucher;
import com.cnpm.repository.VoucherRepository;
import com.cnpm.service.interfaces.IVoucherService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoucherService implements IVoucherService{

    VoucherRepository voucherRepository;

    @Override
	public Voucher saveVoucher(VoucherDTO voucherDTO) {
        Voucher voucher = new Voucher();
        voucher.setVoucherValue(voucherDTO.getVoucherValue());
        voucher.setStartDate(voucherDTO.getStartDate());
        voucher.setEndDate(voucherDTO.getEndDate());
        voucher.setVoucherCode(voucherDTO.getVoucherCode());
        return voucherRepository.save(voucher);
    }

    @Override
    public Voucher getVoucherByVoucherCode(String voucherCode) {
        return voucherRepository.findFirstByVoucherCodeAndIsUsedFalseAndStartDateBeforeAndEndDateAfter(voucherCode, LocalDateTime.now(), LocalDateTime.now()).orElseThrow(() -> new RuntimeException("Voucher "+voucherCode+" is not available"));
    }

    @Override
	public List<Voucher> getAllVoucher() {
        return voucherRepository.findAll();
    }

    @Override
	public void deleteVoucherById(Long voucherId) {
        if (!voucherRepository.existsById(voucherId)) {
            throw new IllegalArgumentException("Voucher với ID không tồn tại");
        }
        voucherRepository.deleteById(voucherId);
    }

    @Override
	public Voucher getVoucherByID(Long voucherId) {

        Voucher voucher = voucherRepository.findById(voucherId).orElseThrow(() -> new RuntimeException("Voucher not found!"));
        return voucher;
    }

    @Override
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

    @Override
	public Double getDiscount(String voucherCode) {
        Voucher voucher = voucherRepository.findFirstByVoucherCodeAndIsUsedFalseAndStartDateBeforeAndEndDateAfter(voucherCode, LocalDateTime.now(), LocalDateTime.now()).orElseThrow(() -> new RuntimeException("Voucher "+voucherCode+" is not available"));
        return voucher.getVoucherValue();
    }


    @Override
	public Optional<Voucher> findFirst(String voucherCode) {
        return voucherRepository.findFirstByVoucherCodeAndIsUsedFalseAndStartDateBeforeAndEndDateAfter(voucherCode, LocalDateTime.now(), LocalDateTime.now());
    }

	@Override
	public List<Voucher> findAllByIsUsedFalseAndStartDateBeforeAndEndDateAfter() {
		LocalDateTime startDate = LocalDateTime.now();
		LocalDateTime endDate = LocalDateTime.now();
		
		return voucherRepository.findAllByIsUsedFalseAndStartDateBeforeAndEndDateAfter(startDate, endDate);
	}

	@Override
	public Voucher findByOrder_OrderId(Long orderId) {
		return voucherRepository.findByOrder_OrderId(orderId);
	}
    
    
}
