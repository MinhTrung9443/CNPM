package com.cnpm.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE) // Moi cai field khong xac dinh thi mac dinh la PRIVATE
public class VoucherDTO {

    Long voucherId;
    String voucherCode;
    Double voucherValue;
    LocalDateTime startDate;
    LocalDateTime endDate;

}
