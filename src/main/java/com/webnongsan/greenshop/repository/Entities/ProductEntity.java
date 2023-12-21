package com.webnongsan.greenshop.repository.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class ProductEntity implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name")
    private String productName;

    private int quantity;

    private double price;

    private int discount;

    @Column(name = "product_image")
    private String productImage;

    private String description;

    @Temporal(TemporalType.DATE)
    @Column(name = "entered_date")
    private Date enteredDate;

    private Boolean status;

    public Boolean favorite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @ManyToMany(mappedBy = "products",  fetch = FetchType.LAZY)
    private List<OrderEntity> orders =new ArrayList<>();

    @OneToMany(mappedBy ="product")
    private Collection<CartItemEntity> cartItemEntities;
}
