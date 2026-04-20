package com.portal.identity_service.configuration;

import com.portal.identity_service.entity.User;
import com.portal.identity_service.enums.Role;
import com.portal.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.HashSet;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Configuration
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner init(UserRepository userRepository) {
        return args -> {
            // Khởi tạo dữ liệu mặc định hoặc thực hiện các tác vụ cần thiết khi ứng dụng khởi động
            var roles = new HashSet<String>();
            roles.add(Role.ADMIN.name());
            if(userRepository.findByUsername("admin").isEmpty()){
                // Tạo tài khoản admin mặc định nếu chưa tồn tại
                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("123456")) // Mật khẩu đã được mã hóa (ví dụ: "admin123")
                        .fullName("Admin")
                        .email("admin@gmail.com")
                        .phoneNumber("0123456789")
                        .roles(roles)
                        .build();
                userRepository.save(user);
                log.warn("Admin user created with username: admin and password: 123456");

            }
        };
    }
}
