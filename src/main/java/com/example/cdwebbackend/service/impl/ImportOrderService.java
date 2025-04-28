package com.example.cdwebbackend.service.impl;

import com.example.cdwebbackend.converter.ImportOrderConverter;
import com.example.cdwebbackend.dto.ImportOrderDTO;
import com.example.cdwebbackend.entity.ImportOrderEntity;
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

    @Autowired
    ImportOrderConverter importOrderConverter;
    @Override
    public ArrayList<ImportOrderDTO> selectAll(){
        ArrayList<ImportOrderDTO> importOrderDTOS = new ArrayList<>();

        for (ImportOrderEntity importOrderEntity: importOrderRepository.findAll()){
            ImportOrderDTO importOrderDTO = importOrderConverter.toDTO(importOrderEntity);
            importOrderDTOS.add(importOrderDTO);
        }

        return importOrderDTOS;
    }

    @Override
    public ImportOrderDTO selectById(int id) {
        //return importOrderRepository.findOneById(id);
        return null;
    }

    @Override
    public ImportOrderDTO selectById(long id) {
        return importOrderConverter.toDTO(importOrderRepository.findOneById(id).get());
    }
    public ImportOrderEntity selectById2(long id) {
        return importOrderRepository.findOneById(id).get();
    }

    @Override
    public boolean insert(ImportOrderDTO imported) {
        System.out.println("vao insert don hang");
        ImportOrderEntity importOrderEntity = importOrderConverter.toEntity(imported);
        importOrderRepository.save(importOrderEntity);
        return true;
    }

    @Override
    public boolean insertAll(ArrayList<ImportOrderDTO> list) {
        ArrayList<ImportOrderEntity> importOrderEntities = new ArrayList<>();

        for (ImportOrderDTO importOrderDTO: list){
            ImportOrderEntity importOrderEntity = importOrderConverter.toEntity(importOrderDTO);
            importOrderEntities.add(importOrderEntity);
        }
        importOrderRepository.saveAll(importOrderEntities);
        return true;
    }

    @Override
    public boolean delete(ImportOrderDTO imported) {
        ImportOrderEntity importOrderEntity = importOrderConverter.toEntity(imported);
        importOrderRepository.delete(importOrderEntity);
        return true;
    }

    public boolean deleteById(long id_import) {
        System.out.println("xoa don hang voi id: "+ id_import);
        //ImportOrderEntity importOrderEntity = importOrderConverter.toEntity(imported);
        //ImportOrderEntity importOrderEntity =
        importOrderRepository.delete(this.selectById2(id_import));
        return true;
    }

    @Override
    public boolean deleteAll(ArrayList<ImportOrderDTO> list) {
        ArrayList<ImportOrderEntity> importOrderEntities = new ArrayList<>();

        for (ImportOrderDTO importOrderDTO: list){
            ImportOrderEntity importOrderEntity = importOrderConverter.toEntity(importOrderDTO);
            importOrderEntities.add(importOrderEntity);
        }
        importOrderRepository.deleteAll(importOrderEntities);
        return true;
    }

    @Override
    public boolean update(ImportOrderDTO imported) {
        ImportOrderEntity importOrderEntity = importOrderConverter.toEntity(imported);
        if(this.selectById2(importOrderEntity.getId())==null){
            System.out.println("Khong ton tai don hang");
        }else{
            importOrderRepository.save(importOrderEntity);
        }

        return true;
    }

}
