package com.cnpm.controller.owner;


import com.cnpm.dto.VoucherDTO;
import com.cnpm.entity.Voucher;
import com.cnpm.service.interfaces.IVoucherService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vouchers")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoucherController {

    IVoucherService voucherService;

    @PostMapping("/add")
    public ResponseEntity<Voucher> creationVoucher(@RequestBody VoucherDTO request){
    	Random random = new Random();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < 5; i++) {
            int digit = random.nextInt(10);
            result.append(digit);
        }
    	
    	request.setVoucherCode(result.toString());
        Voucher saveVoucher = voucherService.saveVoucher(request);
        return new ResponseEntity<>(saveVoucher, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Voucher>> getAllVoucher(){
        List<Voucher> vouchers = voucherService.getAllVoucher();
        return new ResponseEntity<>(vouchers, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteVoucher(@PathVariable Long id) {
        try {
            voucherService.deleteVoucherById(id);
            return ResponseEntity.ok("Xóa thành công");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("Không tìm thấy mã giảm giá với ID: " + id);
        } catch (Exception e) {
            // Log lỗi để biết chi tiết
            e.printStackTrace();
            return ResponseEntity.status(500).body("Đã xảy ra lỗi trong khi xóa mã giảm giá.");
        }
    }

    @GetMapping("/{voucherId}")
    public Voucher getVoucherById(@PathVariable Long voucherId){
        return voucherService.getVoucherByID(voucherId);
    }

    @PutMapping("/update/{voucherId}")
    public ResponseEntity<Voucher> updateVoucher(@PathVariable Long voucherId, @RequestBody VoucherDTO voucherDTO){
        Voucher voucherUpdate = voucherService.updateVoucher(voucherId, voucherDTO);
        return new ResponseEntity<>(voucherUpdate, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<?> getVoucherByVoucherCode(@RequestParam String voucherCode){
        try{
            Voucher voucher = voucherService.getVoucherByVoucherCode(voucherCode);
            return new ResponseEntity<>(voucher, HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
