package com.example.gulimall.ware.vo;

import lombok.Data;

import java.util.List;

@Data
public class DoneVo {
    private Long id;
    List<Item> items;
}
