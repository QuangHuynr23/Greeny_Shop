package com.webnongsan.greenshop.repository;

import com.webnongsan.greenshop.repository.Entities.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
//      Lấy 20 sản phẩm mới nhất
        @Query(value = "select * from greeny_shop.products as p order by p.entered_date desc limit 20", nativeQuery = true)
        List<ProductEntity> findListProductNew();


//      Lấy 10 sản phẩm bán chạy nhất
        @Query(value = "select p.id, p.product_name, count(*) as SOLUONG from greeny_shop.products as p join greeny_shop.order_details as od " +
                "on od.product_id = p.id group by p.id, p.product_name order by SOLUONG desc limit 10", nativeQuery = true)
        List<Object[]> findListProductBestSaler10();


//      Lấy ra list product bằng list productid
        @Query("SELECT p from ProductEntity p where p.id IN :ids ")
        List<ProductEntity> findProductByIds(@Param("ids") List<Long> productIds);

//       Đếm sản phẩm theo danh mục
        @Query("SELECT c.id, c.categoryName, COUNT(p) as SoLuong FROM ProductEntity p join CategoryEntity c \n" +
                "on p.category.id = c.id group by c.id, c.categoryName ")
        List<Object[]> findListCategoryByProduct();

//        Tìm kiếm Product
        @Query("select p from ProductEntity p where :keyword is null or :keyword = '' or p.productName like %:keyword% ")
        Page<ProductEntity> findByName(@Param("keyword") String keyword, Pageable pageable);

//      List Product bằng Category id
        @Query("select p from ProductEntity p where p.category.id = :categoryId")
        List<ProductEntity> findByCategoryId(@Param("categoryId") Long categoryId);

        Page<ProductEntity> findAll(Pageable pageable);
}
