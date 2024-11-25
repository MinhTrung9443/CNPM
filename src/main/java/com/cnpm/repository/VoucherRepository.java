package com.cnpm.repository;

import com.cnpm.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    Voucher findDistinctByVoucherCode(String voucherCode);

    Optional<Voucher> findFirstByVoucherCodeAndIsUsedFalseAndStartDateBeforeAndEndDateAfter(String voucherCode, LocalDateTime startDate, LocalDateTime endDate);
}
