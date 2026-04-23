package com.portal.identity_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "permissions")
public class Permission {

     @Id
     private String name;

     private String description;

     @ManyToMany(mappedBy = "permissions")
     @JsonIgnore
     private Set<Role> roles;
}
