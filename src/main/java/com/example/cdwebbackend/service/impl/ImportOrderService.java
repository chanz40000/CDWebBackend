package com.example.cdwebbackend.service.impl;

import com.example.cdwebbackend.converter.ImportOrderConverter;
import com.example.cdwebbackend.dto.ImportOrderDTO;
import com.example.cdwebbackend.dto.ImportOrderProductDTO;
import com.example.cdwebbackend.entity.ImportOrderEntity;
import com.example.cdwebbackend.entity.ImportOrderProductEntity;
import com.example.cdwebbackend.entity.ProductEntity;
import com.example.cdwebbackend.entity.ProductSizeColorEntity;
import com.example.cdwebbackend.repository.ImportOrderProductRepository;
import com.example.cdwebbackend.repository.ImportOrderRepository;
import com.example.cdwebbackend.repository.ProductRepository;
import com.example.cdwebbackend.repository.ProductSizeColorRepository;
import com.example.cdwebbackend.service.IImportOrderService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ImportOrderService implements IImportOrderService {
    @Autowired
    ImportOrderRepository importOrderRepository;

    @Autowired
    ImportOrderConverter importOrderConverter;
    @Autowired
    ProductSizeColorRepository productSizeColorRepository;
    @Autowired
    ImportOrderProductRepository importOrderProductRepository;
    @Autowired
    ProductRepository productRepository;
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
        //lay ra danh sach san pham nhap va them so luong
        List<ImportOrderProductDTO> products = imported.getProducts();
        //duyet qua tung sp
        for (ImportOrderProductDTO productImport: products){
            //lay ra sp trong db
            ProductSizeColorEntity productSizeColorEntity = productSizeColorRepository.findOneById(productImport.getProductId());
            //sua so luong

            productSizeColorEntity.setStock(productSizeColorEntity.getStock()+productImport.getQuantity());
            productSizeColorRepository.save(productSizeColorEntity);

            //sua gia nhap
            //lay ra san pham goc
            ProductEntity productEntity = productSizeColorEntity.getProduct();
            if (productEntity.getImport_price()!=productImport.getPrice()){
                productEntity.setImport_price(productImport.getPrice());
                productRepository.save(productEntity);
            }

        }
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
        //lay ra danh sach san pham nhap

        List<ImportOrderProductEntity> products = importOrderProductRepository.findByImportOrderId(imported.getId()).stream().toList();
        //duyet qua tung sp
//        for (ImportOrderProductEntity importOrderProductEntity: products){
//            //lay ra sp trong db
//            ProductSizeColorEntity productEntity = productSizeColorRepository.findOneById(importOrderProductEntity.getId());
//            //sua so luong, tru so luong da nhap ra
//            productEntity.setStock(productEntity.getStock()-importOrderProductEntity.getQuantity());
//            productSizeColorRepository.save(productEntity);
//        }

        for (ImportOrderProductEntity importOrderProductEntity: products){
            //lay ra sp trong db
            ProductSizeColorEntity productSizeColorEntity = importOrderProductEntity.getProduct();
            //sua so luong

            productSizeColorEntity.setStock(productSizeColorEntity.getStock()-importOrderProductEntity.getQuantity());
            productSizeColorRepository.save(productSizeColorEntity);
        }
        importOrderRepository.delete(importOrderEntity);
        return true;
    }

    public boolean deleteById(long id_import) {
        System.out.println("xoa don hang voi id: "+ id_import);
        //lay ra danh sach san pham nhap

        List<ImportOrderProductEntity> products = importOrderProductRepository.findByImportOrderId(id_import).stream().toList();
        //duyet qua tung sp
        for (ImportOrderProductEntity importOrderProductEntity: products){
            //lay ra sp trong db
            ProductSizeColorEntity productSizeColorEntity = importOrderProductEntity.getProduct();
            //sua so luong

            productSizeColorEntity.setStock(productSizeColorEntity.getStock()-importOrderProductEntity.getQuantity());
            productSizeColorRepository.save(productSizeColorEntity);
        }
        //xoa don hang trong bang importOrderProduct
        for (ImportOrderProductEntity importOrderProductEntity: importOrderRepository.findOneById(id_import).get().getImportOrderProducts()){
            importOrderProductRepository.delete(importOrderProductEntity);
        }
        //xoa don hang trong bang importOrder
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
            //importOrderRepository.save(importOrderEntity);
            this.delete(imported);
            this.insert(imported);
        }

        return true;
    }

}
