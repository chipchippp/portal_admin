package com.portal.identity_service.entity;

import com.portal.identity_service.enums.Gender;
import com.portal.identity_service.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     long id;
     String username;
     String password;
     String fullName;
     String email;
     String phoneNumber;
     @Enumerated(EnumType.STRING)
     Gender gender;
     @Enumerated(EnumType.STRING)
     Status status;
     LocalDate dateOfBirth;
     Set<String> roles;
}
