package com.webnongsan.greenshop.repository;

import com.webnongsan.greenshop.repository.Entities.OrderDetailEntity;
import com.webnongsan.greenshop.repository.Entities.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity,Long> {
    @Query("select od from OrderDetailEntity od where od.order.id =:orderId")
    List<OrderDetailEntity> findOrderDetailByOrderId( Long orderId);

    // Statistics by product sold
    @Query("SELECT " +
            "p.productName, " +
            "sum(od.quantity), " +
            "sum(od.price * od.quantity), " +
            "avg(od.price), min(od.price), " +
            "max(od.price) " +
            "FROM OrderDetailEntity od " +
            "inner join ProductEntity p on od.product.id = p.id " +
            "group by p.productName ")
    Page<Object[]> repoWhereProduct(Pageable pageable);

    @Query("SELECT " +
            "p.productName, " +
            "sum(od.quantity), " +
            "sum(od.price * od.quantity), " +
            "avg(od.price), min(od.price), " +
            "max(od.price) " +
            "FROM OrderDetailEntity od " +
            "inner join ProductEntity p on od.product.id = p.id " +
            "group by p.productName ")
    List<Object[]> repo();

    @Query("SELECT c.categoryName, " +
            "sum(od.quantity), " +
            "sum(od.price*od.quantity)," +
            "avg(od.price),min(od.price)," +
            "max(od.price) " +
            "FROM ProductEntity p  \n" +
            "inner join CategoryEntity c on c.id = p.category.id \n" +
            "inner join OrderDetailEntity od on od.product.id = p.id " +
            "group by c.categoryName")
    List<Object[]> repoCategory();

    // Statistics by category sold
    @Query("SELECT c.categoryName, " +
            "sum(od.quantity), " +
            "sum(od.price*od.quantity)," +
            "avg(od.price),min(od.price)," +
            "max(od.price) " +
            "FROM ProductEntity p  \n" +
            "inner join CategoryEntity c on c.id = p.category.id \n" +
            "inner join OrderDetailEntity od on od.product.id = p.id " +
            "group by c.categoryName")
    Page<Object[]> repoWhereCategory(Pageable pageable);

    // Statistics of products sold by year
    @Query("SELECT year(o.orderDate)," +
            "sum(od.quantity)," +
            "sum(od.price*od.quantity)," +
            "avg(od.price),min(od.price)," +
            "max(od.price) " +
            "FROM OrderEntity o " +
            "inner join OrderDetailEntity od on o.id = od.order.id " +
            "group by year(o.orderDate)")
    Page<Object[]> repoWhereYear(Pageable pageable);

    // Statistics of products sold by month
    @Query("SELECT month(o.orderDate)," +
            "sum(od.quantity)," +
            "sum(od.price*od.quantity)," +
            "avg(od.price),min(od.price)," +
            "max(od.price) " +
            "FROM OrderEntity o " +
            "inner join OrderDetailEntity od on o.id = od.order.id " +
            "group by month(o.orderDate)")
    Page<Object[]> repoWhereMonth(Pageable pageable);

    // Statistics of products sold by quarter
    @Query("SELECT quarter(o.orderDate)," +
            "sum(od.quantity)," +
            "sum(od.price*od.quantity)," +
            "avg(od.price),min(od.price)," +
            "max(od.price) " +
            "FROM OrderEntity o " +
            "inner join OrderDetailEntity od on o.id = od.order.id " +
            "group by quarter(o.orderDate)")
    Page<Object[]> repoWhereQuarter(Pageable pageable);

    // Statistics by user
    @Query("SELECT us.name," +
            "sum(od.quantity)," +
            "sum(od.price*od.quantity)," +
            "avg(od.price),min(od.price)," +
            "max(od.price) " +
            "FROM UserEntity us " +
            "inner join OrderEntity o on us.id=o.user.id " +
            "inner join OrderDetailEntity od on o.id=od.order.id " +
            "group by us.name")
    Page<Object[]> repoWhereCustomer(Pageable pageable);

    @Query("SELECT us.name," +
            "sum(od.quantity)," +
            "sum(od.price*od.quantity)," +
            "avg(od.price),min(od.price)," +
            "max(od.price) " +
            "FROM UserEntity us " +
            "inner join OrderEntity o on us.id=o.user.id " +
            "inner join OrderDetailEntity od on o.id=od.order.id " +
            "group by us.name")
    List<Object[]> repoCustomer();

    @Query("SELECT p.productName, " +
            "sum(od.quantity), " +
            "sum(od.price * od.quantity), " +
            "avg(od.price), min(od.price), " +
            "max(od.price) " +
            "FROM OrderDetailEntity od " +
            "inner join ProductEntity p on od.product.id = p.id " +
            "where :keyword is null or :keyword = '' or p.productName like %:keyword% "+
            "group by p.productName ")
    Page<Object[]> statisticsByProductOfKey(@Param("keyword") String keyword, Pageable pageable);
}
