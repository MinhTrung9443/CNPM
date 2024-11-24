package com.cnpm.controller.owner;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class OwnerController {

    @GetMapping({"", "/index", "/index.html"})
    public String home(){
        return "owner/index";
    }

    @GetMapping({"/voucherManage", "/voucherManage.html"})
    public String voucherManagePage(){
        return "/owner/voucherManage";
    }

}
