package br.com.meucaixa.api.services.impl;

import br.com.meucaixa.api.models.Role;
import br.com.meucaixa.api.repositories.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class RoleServiceImplTest {
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRoles_shouldReturnRoleListEmpty() {
        // Arrange
        when(roleRepository.findAll()).thenReturn(List.of());

        // Act
        List<Role> roles = roleService.listRoles();

        // Assert
        assertTrue(roles.isEmpty());
    }

    @Test
    void testGetRoles_shouldReturnRoleListDefault() {
        // Arrange
        Role role = new Role(1, "ADMIN", "Administrator role", null, null);
        when(roleRepository.findAll()).thenReturn(List.of(role));

        // Act
        List<Role> roles = roleService.listRoles();

        // Assert
        assertFalse(roles.isEmpty());
        assertEquals(1, roles.size());
        assertEquals(role, roles.get(0));
    }
}
