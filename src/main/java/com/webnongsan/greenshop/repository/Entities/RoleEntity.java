package com.webnongsan.greenshop.repository.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;
    @ManyToMany(mappedBy = "role", fetch = FetchType.EAGER)
    private List<UserEntity> user = new ArrayList<>();


    public RoleEntity(String name) {
        super();
        this.name = name;
    }
}
