package com.example.cdwebbackend.controller;

import com.example.cdwebbackend.dto.ImportOrderDTO;
import com.example.cdwebbackend.dto.ProductDTO;
import com.example.cdwebbackend.dto.UserDTO;
import com.example.cdwebbackend.entity.ProductEntity;
import com.example.cdwebbackend.entity.UserEntity;
import com.example.cdwebbackend.repository.ImportOrderRepository;
import com.example.cdwebbackend.service.impl.ImportOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RestController
@RequestMapping("${api.prefix}/importOrder")
public class ImportOrderController {
    @Autowired
    ImportOrderRepository importOrderRepository;

    @Autowired
    ImportOrderService importOrderService;



}
