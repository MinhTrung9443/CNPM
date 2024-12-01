package com.cnpm.service.interfaces;

import com.cnpm.dto.VoucherDTO;
import com.cnpm.entity.Voucher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface IVoucherService {
    Double getDiscount(String voucherCode);
    Optional<Voucher> findFirst(String voucherCode);
	Voucher updateVoucher(Long voucherId, VoucherDTO voucherDTO);
	Voucher getVoucherByID(Long voucherId);
	void deleteVoucherById(Long voucherId);
	List<Voucher> getAllVoucher();
	Voucher saveVoucher(VoucherDTO voucherDTO);

    Voucher getVoucherByVoucherCode(String voucherCode);
	List<Voucher> findAllByIsUsedFalseAndStartDateBeforeAndEndDateAfter();
	Voucher findByOrder_OrderId(Long orderId);
}