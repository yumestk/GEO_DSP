package com.geo.dsp.module.DataReceive.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/receive")
public class DataCollectionController {

    @PostMapping
    public String handleData(@RequestBody Object rawData) {
        // TODO: 处理原始数据
        System.out.println("接收到数据：" + rawData);
        return "success";
    }
}
