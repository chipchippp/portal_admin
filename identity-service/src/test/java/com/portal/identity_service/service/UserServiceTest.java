package com.portal.identity_service.service;

import com.portal.identity_service.dto.request.UserCreateRequest;
import com.portal.identity_service.dto.response.UserResponse;
import com.portal.identity_service.entity.User;
import com.portal.identity_service.excetion.AppException;
import com.portal.identity_service.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource("/test.properties")
public class UserServiceTest {
    @Autowired
    private UserService userService;


    @MockBean
    private UserRepository userRepository;
    private UserCreateRequest request;
    private UserResponse userResponse;
    private LocalDate dob;
    private User user;

    @BeforeEach
    void initData(){
        dob = LocalDate.of(1990, 1, 1);
        request = UserCreateRequest.builder()
                .username("admin")
                .password("123456")
                .dateOfBirth(dob)
                .build();

        userResponse = UserResponse.builder()
                .id(1L)
                .username("admin")
                .dateOfBirth(dob)
                .build();

        user = User.builder()
                .id(1L)
                .username("admin")
                .dateOfBirth(dob)
                .build();

    }

    @Test
    void createUser_validRequest_success() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);
        // When
         var response = userService.createUser(request);

        // Then
        Assertions.assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getUsername()).isEqualTo(request.getUsername());

    }

    @Test
    void createUser_userExisted_fail() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // When
        assertThrows(AppException.class, () -> userService.createUser(request));


        // Then
        var exception = assertThrows(AppException.class, () -> userService.createUser(request));
        assertThat(exception.getErrorCode().getCode()).isEqualTo(1002);

    }
}
