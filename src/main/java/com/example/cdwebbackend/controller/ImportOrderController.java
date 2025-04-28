package com.example.cdwebbackend.controller;

import com.example.cdwebbackend.dto.ProductDTO;
import com.example.cdwebbackend.entity.ProductEntity;
import com.example.cdwebbackend.repository.ImportOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RestController
@RequestMapping("${api.prefix}/importOrder")
public class ImportOrderController {
    @Autowired
    ImportOrderRepository importOrderRepository;


}
