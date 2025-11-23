package com.ecommerce.furniture;

import com.ecommerce.furniture.models.Role;
import com.ecommerce.furniture.models.ERole;
import com.ecommerce.furniture.models.User;
import com.ecommerce.furniture.repository.UserRepository;
import com.ecommerce.furniture.security.services.UserDetailsImpl;
import com.ecommerce.furniture.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByEmailSuccess() {
        User user = new User("Mahendran", "test@example.com", "password123");
        user.setRoles(Set.of(new Role(ERole.ROLE_USER)));

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));

        UserDetailsImpl details = (UserDetailsImpl) userDetailsService.loadUserByUsername("test@example.com");

        assertEquals("test@example.com", details.getUsername());
        assertEquals("password123", details.getPassword());
        assertEquals(1, details.getAuthorities().size());
    }

    @Test
    void testLoadUserByEmailNotFound() {
        when(userRepository.findByEmail("missing@example.com"))
                .thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("missing@example.com")
        );
    }
}
