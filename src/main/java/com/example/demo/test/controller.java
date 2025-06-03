package com.example.demo.test;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("a")
public class controller {


    @GetMapping("b")
    public String test() {
        return "aaabbba";
    }
}


