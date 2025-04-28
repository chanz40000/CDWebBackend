package com.example.cdwebbackend.service;

import com.example.cdwebbackend.dto.ImportOrderDTO;
import com.example.cdwebbackend.dto.ProductDTO;
import com.example.cdwebbackend.entity.ProductEntity;
import com.example.cdwebbackend.exceptions.DataNotFoundException;
import org.springframework.context.annotation.Import;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public interface IImportOrderService {

        public ArrayList<ImportOrderDTO> selectAll() ;

        public ImportOrderDTO selectById(int id);

    ImportOrderDTO selectById(long id);

    public boolean insert(ImportOrderDTO imported) ;

        public boolean insertAll(ArrayList<ImportOrderDTO> list) ;


        public boolean delete(ImportOrderDTO imported) ;

        public boolean deleteAll(ArrayList<ImportOrderDTO> list) ;

        public boolean update(ImportOrderDTO imported) ;
    }
