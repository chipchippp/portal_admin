package com.portal.identity_service.entity;

import java.time.LocalDate;
import java.util.Set;

import com.portal.identity_service.enums.Gender;
import com.portal.identity_service.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
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

    //    unique = true để đảm bảo rằng không có hai người dùng nào có cùng tên đăng nhập,
    //    columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci" được sử dụng để chỉ định kiểu dữ liệu và
    // collation cho cột username trong cơ sở dữ liệu, giúp hỗ trợ các ký tự Unicode một cách chính xác.
    //    Nói một cách đơn giản, điều này đảm bảo rằng mỗi người dùng có một tên đăng nhập duy nhất và không thể để
    // trống, đồng thời hỗ trợ các ký tự đặc biệt và ngôn ngữ khác nhau trong tên đăng nhập.
    @Column(name = "username", unique = true, nullable = false)
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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_name"))
    Set<Role> roles;
}
