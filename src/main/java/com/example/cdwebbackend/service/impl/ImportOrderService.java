package com.example.cdwebbackend.service.impl;

import com.example.cdwebbackend.converter.ImportOrderConverter;
import com.example.cdwebbackend.dto.ImportOrderDTO;
import com.example.cdwebbackend.dto.ImportOrderProductDTO;
import com.example.cdwebbackend.entity.*;
import com.example.cdwebbackend.exceptions.DataNotFoundException;
import com.example.cdwebbackend.repository.*;
import com.example.cdwebbackend.service.IImportOrderService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
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
    @Autowired
    UserRepository userRepository;
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
        return importOrderConverter.toDTO(importOrderRepository.findOneById(id).get());
    }

//    @Override
//    public ImportOrderDTO selectById(int id) {
//        //return importOrderRepository.findOneById(id);
//        return null;
//    }

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
//        ImportOrderEntity importOrderEntity = importOrderConverter.toEntity(imported);
//        //lay ra danh sach san pham nhap
//        List<ImportOrderProductEntity> products = importOrderProductRepository.findByImportOrderId(imported.getId()).stream().toList();
//        //thay doi lai so luong
//        for (ImportOrderProductEntity importOrderProductEntity: products) {
//            //lay ra sp trong db
//            ProductSizeColorEntity productSizeColorEntity = importOrderProductEntity.getProduct();
//            //sua so luong
//            productSizeColorEntity.setStock(productSizeColorEntity.getStock() - importOrderProductEntity.getQuantity());
//            productSizeColorRepository.save(productSizeColorEntity);
//        }
//        System.out.println("size importOrderProduct: "+ importOrderEntity.getImportOrderProducts().size());
//        ImportOrderEntity importOrderEntityInDatabase = importOrderRepository.findOneById(imported.getId()).get();
//        if(importOrderEntityInDatabase.getImportOrderProducts().size()>0){
//            System.out.println("id importOrder: "+ imported.getId());
//            importOrderProductRepository.deleteByImportOrderId(imported.getId());
//        }
//        //xoa importorderproduct
//        for (ImportOrderProductDTO importOrderProductDTO: imported.getProducts()){
//            importOrderProductRepository.deleteById(importOrderProductDTO.getImportOrderId());
//        }
//        importOrderRepository.delete(importOrderEntity);
//        return true;

        try {
            if (imported == null || imported.getId() == null) {
                throw new IllegalArgumentException("ImportOrderDTO hoặc ID không được null");
            }

            System.out.println("Xóa đơn nhập hàng với id: " + imported.getId());
            ImportOrderEntity importOrderEntity = importOrderRepository.findOneById(imported.getId())
                    .orElseThrow(() -> new DataNotFoundException("Đơn nhập hàng với id " + imported.getId() + " không tồn tại"));

            // Cập nhật stock
            List<ImportOrderProductEntity> products = importOrderEntity.getImportOrderProducts();
            for (ImportOrderProductEntity product : products) {
                ProductSizeColorEntity productSizeColor = product.getProduct();
                if (productSizeColor != null) {
                    productSizeColor.setStock(productSizeColor.getStock() - product.getQuantity());
                    productSizeColorRepository.save(productSizeColor);
                }
            }

            // Xóa tất cả import_order_product
            if (!products.isEmpty()) {
                System.out.println("Xóa import_order_product với import_order_id: " + imported.getId());
                importOrderProductRepository.deleteByImportOrderId(imported.getId());
            }

            // Xóa import_order
            importOrderRepository.delete(importOrderEntity);
            System.out.println("Xóa đơn nhập hàng thành công: " + imported.getId());
            return true;
        } catch (IllegalArgumentException e) {
            System.err.println("Lỗi tham số: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Lỗi khi xóa đơn hàng: " + e.getMessage());
            throw new RuntimeException("Không thể xóa đơn hàng: " + e.getMessage());
        }
    }

    public boolean deleteById(long id_import) {
//        System.out.println("xoa don hang voi id: "+ id_import);
//        //lay ra danh sach san pham nhap
//
//        List<ImportOrderProductEntity> products = importOrderProductRepository.findByImportOrderId(id_import).stream().toList();
//        //duyet qua tung sp
//        for (ImportOrderProductEntity importOrderProductEntity: products){
//            //lay ra sp trong db
//            ProductSizeColorEntity productSizeColorEntity = importOrderProductEntity.getProduct();
//            //sua so luong
//
//            productSizeColorEntity.setStock(productSizeColorEntity.getStock()-importOrderProductEntity.getQuantity());
//            productSizeColorRepository.save(productSizeColorEntity);
//        }
//        //xoa don hang trong bang importOrderProduct
//        ImportOrderEntity importOrder = importOrderRepository.findOneById(id_import).get();
//        if(importOrder.getImportOrderProducts().size()>0){
//            importOrderProductRepository.deleteByImportOrderId(id_import);
//        }
//
//        //xoa don hang trong bang importOrder
//        importOrderRepository.delete(this.selectById2(id_import));
//        return true;
        try {
//            if (id_import == null) {
//                throw new IllegalArgumentException("ID đơn nhập hàng không được null");
//            }

            System.out.println("Xóa đơn hàng với id: " + id_import);
            ImportOrderEntity importOrder = importOrderRepository.findOneById(id_import)
                    .orElseThrow(() -> new DataNotFoundException("Đơn nhập hàng với id " + id_import + " không tồn tại"));

            // Cập nhật stock
            List<ImportOrderProductEntity> products = importOrderRepository.findOneById(id_import).get().getImportOrderProducts();
            for (ImportOrderProductEntity product : products) {
                ProductSizeColorEntity productSizeColor = product.getProduct();
                if (productSizeColor != null) {
                    productSizeColor.setStock(productSizeColor.getStock() - product.getQuantity());
                    productSizeColorRepository.save(productSizeColor);
                }
            }

            // Xóa import_order_product
            if (!products.isEmpty()) {
                System.out.println("Xóa import_order_product với import_order_id: " + id_import);
                importOrderProductRepository.deleteByImportOrderId(id_import);
            }

            // Xóa import_order
            importOrderRepository.delete(importOrder);
            System.out.println("Xóa đơn hàng thành công: " + id_import);
            return true;
        } catch (IllegalArgumentException e) {
            System.err.println("Lỗi tham số: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Lỗi khi xóa đơn hàng: " + e.getMessage());
            throw new RuntimeException("Không thể xóa đơn hàng: " + e.getMessage());
        }
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

//    @Override
//    public boolean update(ImportOrderDTO imported) {
//        ImportOrderEntity importOrderEntity = importOrderConverter.toEntity(imported);
//        if(this.selectById2(importOrderEntity.getId())==null){
//            System.out.println("Khong ton tai don hang");
//        }else{
//            //importOrderRepository.save(importOrderEntity);
//            this.delete(imported);
//            this.insert(imported);
//        }
//
//        return true;
//    }
@Transactional
@Override
public boolean update(ImportOrderDTO imported) {
    try {
        if (imported == null || imported.getId() == null) {
            throw new IllegalArgumentException("ImportOrderDTO hoặc ID không được null");
        }

        ImportOrderEntity existingOrder = importOrderRepository.findOneById(imported.getId())
                .orElseThrow(() -> new DataNotFoundException("Đơn nhập hàng với id " + imported.getId() + " không tồn tại"));

        // Cập nhật stock cho các sản phẩm hiện tại (trừ đi)
        List<ImportOrderProductEntity> currentProducts = importOrderRepository.findOneById(imported.getId()).get().getImportOrderProducts();
        for (ImportOrderProductEntity product : currentProducts) {
            ProductSizeColorEntity productSizeColor = product.getProduct();
            if (productSizeColor != null) {
                productSizeColor.setStock(productSizeColor.getStock() - product.getQuantity());
                productSizeColorRepository.save(productSizeColor);
            }
        }

        // Xóa các sản phẩm hiện tại
        if (!currentProducts.isEmpty()) {
            importOrderProductRepository.deleteByImportOrderId(imported.getId());
        }

        // Cập nhật thông tin đơn nhập hàng
        existingOrder.setUser(userRepository.findOneByUsername(imported.getUsername()).get());
        existingOrder.setImportPrice(imported.getImportPrice());
        existingOrder.setModifiedDate(new Date());

        // Thêm các sản phẩm mới
        List<ImportOrderProductDTO> newProducts = imported.getProducts();
        for (ImportOrderProductDTO productDTO : newProducts) {
            ProductSizeColorEntity productSizeColor = productSizeColorRepository.findOneById(productDTO.getProductId());
            if (productSizeColor != null) {
                // Cập nhật stock
                productSizeColor.setStock(productSizeColor.getStock() + productDTO.getQuantity());
                productSizeColorRepository.save(productSizeColor);

                // Cập nhật giá nhập
                ProductEntity product = productSizeColor.getProduct();
                if (product.getImport_price() != productDTO.getPrice()) {
                    product.setImport_price(productDTO.getPrice());
                    productRepository.save(product);
                }

                // Tạo mới ImportOrderProductEntity
                ImportOrderProductEntity productEntity = new ImportOrderProductEntity();
                productEntity.setImportOrder(existingOrder);
                productEntity.setProduct(productSizeColor);
                productEntity.setQuantity(productDTO.getQuantity());
                productEntity.setPrice(productDTO.getPrice());
                importOrderProductRepository.save(productEntity);
            }
        }

        importOrderRepository.save(existingOrder);
        return true;
    } catch (IllegalArgumentException e) {
        System.err.println("Lỗi tham số: " + e.getMessage());
        throw e;
    } catch (Exception e) {
        System.err.println("Lỗi khi cập nhật đơn hàng: " + e.getMessage());
        throw new RuntimeException("Không thể cập nhật đơn hàng: " + e.getMessage());
    }
}


}
