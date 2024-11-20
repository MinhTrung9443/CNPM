package com.cnpm.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/test")
public class Testxacthuc {

    @GetMapping("")
    public String t(Principal principal){
        return principal.getName();
    }
}
