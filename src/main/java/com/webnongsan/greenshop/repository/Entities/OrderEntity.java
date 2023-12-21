package com.webnongsan.greenshop.repository.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class OrderEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "order_date")
    private Date orderDate;

    private Double amount;
    private String address;
    private String phone;
    private int status;


    @ManyToMany
    @JoinTable(name = "order_details", joinColumns = @JoinColumn(name = "order_id", nullable = false)
                ,inverseJoinColumns = @JoinColumn(name = "product_id", nullable = false))
    @JsonManagedReference
    private List<ProductEntity> products = new ArrayList<>();

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
