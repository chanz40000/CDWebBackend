package com.example.cdwebbackend.repository;

import com.example.cdwebbackend.entity.ProductEntity;
import com.example.cdwebbackend.entity.ProductSizeColorEntity;
import com.example.cdwebbackend.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
//    public List<ProductEntity> findAll();
    public ProductEntity findOneById(long id);
    Page<ProductEntity> findAllByIsActive(Boolean isActive, Pageable pageable);


    Optional<ProductEntity> findByIdAndIsActiveTrue(Long id);

    List<ProductEntity> findAllByNameProductContainingIgnoreCase(String nameProduct);
  List<ProductEntity> findAllByNameProductContainingIgnoreCaseAndIsActiveTrue(String nameProduct);

    //lay ra danh sach san pham co ton kho bang khong duoc mua 3 thang nay
    @Query("SELECT DISTINCT psc FROM ProductSizeColorEntity psc " +
            "JOIN psc.product p " +
            "JOIN psc.productColor pc " +
            "JOIN psc.size s " +
            "JOIN OrderDetailEntity od ON od.productSizeColor = psc " +
            "WHERE psc.stock = 0 " +
            "AND od.createdDate >= :threeMonthsAgo")
    List<ProductSizeColorEntity> findProductSizeColorsWithZeroStockPurchasedInLastThreeMonths(@Param("threeMonthsAgo") LocalDateTime threeMonthsAgo);


}
