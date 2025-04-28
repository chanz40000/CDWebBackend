package com.example.cdwebbackend.service.impl;

import com.example.cdwebbackend.dto.ImportOrderDTO;
import com.example.cdwebbackend.repository.ImportOrderRepository;
import com.example.cdwebbackend.service.IImportOrderService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@AllArgsConstructor
public class ImportOrderService implements IImportOrderService {
    @Autowired
    ImportOrderRepository importOrderRepository;
    @Override
    public ArrayList<ImportOrderDTO> selectAll() {
        return null;
    }

    @Override
    public ImportOrderDTO selectById(int id) {
        return null;
    }

    @Override
    public boolean insert(ImportOrderDTO imported) {
        return false;
    }

    @Override
    public boolean insertAll(ArrayList<ImportOrderDTO> list) {
        return false;
    }

    @Override
    public boolean delete(ImportOrderDTO imported) {
        return false;
    }

    @Override
    public boolean deleteAll(ArrayList<ImportOrderDTO> list) {
        return false;
    }

    @Override
    public boolean update(ImportOrderDTO imported) {
        return false;
    }
}
