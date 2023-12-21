package com.webnongsan.greenshop.model.dto;

import com.webnongsan.greenshop.repository.Entities.RoleEntity;
import lombok.*;

import java.util.Collection;
import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String avatar;
    private Date registerDate;
    private Boolean status;
    private Collection<RoleEntity> roleEntities;
}
