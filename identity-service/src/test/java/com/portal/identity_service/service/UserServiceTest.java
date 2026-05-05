package com.portal.identity_service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import com.portal.identity_service.dto.request.UserCreateRequest;
import com.portal.identity_service.dto.response.UserResponse;
import com.portal.identity_service.entity.User;
import com.portal.identity_service.excetion.AppException;
import com.portal.identity_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

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
    void initData() {
        dob = LocalDate.of(1990, 1, 1);
        request = UserCreateRequest.builder()
                .username("admin")
                .password("123456")
                .dateOfBirth(dob)
                .build();

        userResponse =
                UserResponse.builder().id(1L).username("admin").dateOfBirth(dob).build();

        user = User.builder().id(1L).username("admin").dateOfBirth(dob).build();
    }

    @Test
    void createUser_validRequest_success() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);
        // When
        var response = userService.createUser(request);

        // Then
        assertThat(response.getId()).isEqualTo(userResponse.getId());
        assertThat(response.getUsername()).isEqualTo(request.getUsername());
    }

    @Test
    void createUser_userExisted_fail() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // When
        var exception = assertThrows(AppException.class, () -> userService.createUser(request));

        // Then
        assertThat(exception.getErrorCode().getCode()).isEqualTo(1002);
    }

    @Test
    @WithMockUser(
            username = "admin",
            roles = {"ADMIN"})
    void getMyProfile_validUser_success() {
        // Given
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        // When
        var response = userService.getMyProfile();

        // Then
        assertThat(response.getId()).isEqualTo(userResponse.getId());
        assertThat(response.getUsername()).isEqualTo(response.getUsername());
    }

    @Test
    @WithMockUser(
            username = "admin",
            roles = {"ADMIN"})
    void getMyProfile_userNotFound_error() {
        // Given
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.ofNullable(null));

        // When
        var exception = assertThrows(AppException.class, () -> userService.getMyProfile());

        // Then
        assertThat(exception.getErrorCode().getCode()).isEqualTo(1006);
    }
}
