package com.webnongsan.greenshop.repository.Entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class CommentEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double rating;
    private String content;
    @Temporal(TemporalType.DATE)
    @Column(name = "rate_date")
    private Date rateDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @OneToOne
    @JoinColumn(name = "order_detail_id")
    private OrderDetailEntity orderDetail;
}
