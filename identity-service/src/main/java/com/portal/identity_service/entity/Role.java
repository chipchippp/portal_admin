package com.portal.identity_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "roles")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role {
     @Id
     String name;
     String description;

     @ManyToMany
//     @JoinTable(
//             name = "role_permissions",
//             joinColumns = @JoinColumn(name = "role_name"),
//             inverseJoinColumns = @JoinColumn(name = "permission_name")
//     )
     Set<Permission> permissions;
}
