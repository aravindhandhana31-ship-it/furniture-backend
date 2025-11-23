package com.ecommerce.furniture;

import com.ecommerce.furniture.controllers.AuthController;
import com.ecommerce.furniture.models.*;
import com.ecommerce.furniture.payload.request.LoginRequest;
import com.ecommerce.furniture.payload.request.SignupRequest;
import com.ecommerce.furniture.repository.RoleRepository;
import com.ecommerce.furniture.repository.UserRepository;
import com.ecommerce.furniture.security.jwt.JwtUtils;
import com.ecommerce.furniture.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------------------------------------------------------
    // SIGN-IN
    // ---------------------------------------------------------
    @Test
    void testAuthenticateUserSuccess() {
        LoginRequest login = new LoginRequest();
        login.setEmail("test@example.com");
        login.setPassword("123456");

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);

        UserDetailsImpl details = new UserDetailsImpl(
                1L,
                "Mahendran",
                "test@example.com",
                "password",
                List.of(() -> "ROLE_USER")
        );

        when(authentication.getPrincipal()).thenReturn(details);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("fake-jwt-token");

        ResponseEntity<?> response = authController.authenticateUser(login);

        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    // ---------------------------------------------------------
    // SIGN-UP
    // ---------------------------------------------------------

    @Test
    void testSignupFails_NameExists() {
        SignupRequest req = new SignupRequest();
        req.setName("Mahendran");
        req.setEmail("new@example.com");
        req.setPassword("123");
        req.setConfirmPassword("123");

        when(userRepository.existsByName("Mahendran")).thenReturn(true);

        ResponseEntity<?> response = authController.registerUser(req);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testSignupFails_EmailExists() {
        SignupRequest req = new SignupRequest();
        req.setName("NewUser");
        req.setEmail("test@example.com");
        req.setPassword("123");
        req.setConfirmPassword("123");

        when(userRepository.existsByName("NewUser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        ResponseEntity<?> response = authController.registerUser(req);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testSignupFails_PasswordMismatch() {
        SignupRequest req = new SignupRequest();
        req.setName("Test");
        req.setEmail("test@example.com");
        req.setPassword("123");
        req.setConfirmPassword("124");

        ResponseEntity<?> response = authController.registerUser(req);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testSignupSuccess_DefaultUserRole() {
        SignupRequest req = new SignupRequest();
        req.setName("Test");
        req.setEmail("test@example.com");
        req.setPassword("123");
        req.setConfirmPassword("123");

        when(userRepository.existsByName("Test")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);

        Role userRole = new Role(ERole.ROLE_USER);

        when(roleRepository.findByName(ERole.ROLE_USER))
                .thenReturn(Optional.of(userRole));

        when(encoder.encode("123")).thenReturn("encoded-password");

        ResponseEntity<?> response = authController.registerUser(req);

        assertEquals(200, response.getStatusCodeValue());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testSignupSuccess_AdminRole() {
        SignupRequest req = new SignupRequest();
        req.setName("Admin");
        req.setEmail("admin@example.com");
        req.setPassword("123");
        req.setConfirmPassword("123");
        req.setRole(Set.of("admin"));

        when(userRepository.existsByName("Admin")).thenReturn(false);
        when(userRepository.existsByEmail("admin@example.com")).thenReturn(false);

        Role adminRole = new Role(ERole.ROLE_ADMIN);

        when(roleRepository.findByName(ERole.ROLE_ADMIN))
                .thenReturn(Optional.of(adminRole));

        when(encoder.encode("123")).thenReturn("encoded-password");

        ResponseEntity<?> response = authController.registerUser(req);

        assertEquals(200, response.getStatusCodeValue());
        verify(userRepository, times(1)).save(any(User.class));
    }
}
