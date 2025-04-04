package br.com.meucaixa.api.services.impl;

import br.com.meucaixa.api.models.Role;
import br.com.meucaixa.api.repositories.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class RoleServiceImplTest {
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private final PageRequest pageable = PageRequest.of(0, 10);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRoles_shouldReturnRoleListEmpty() {
        // Arrange
        when(roleRepository.findAllPageable(pageable)).thenReturn(new PageImpl<>(List.of(), pageable, 0));

        // Act
        var page = roleService.listRolesPageable(pageable);

        // Assert
        assertTrue(page.isEmpty());
    }

    @Test
    void testGetRoles_shouldReturnRoleListDefault() {
        // Arrange
        Role role = new Role(1, "ADMIN", "Administrator role", null, null);
        when(roleRepository.findAllPageable(pageable)).thenReturn(new PageImpl<>(List.of(role), pageable, 1));

        // Act
        var page = roleService.listRolesPageable(pageable);

        // Assert
        assertFalse(page.isEmpty());
        assertEquals(1, page.getContent().size());
        assertEquals(role, page.getContent().get(0));
    }
}
