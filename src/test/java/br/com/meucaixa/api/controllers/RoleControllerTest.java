package br.com.meucaixa.api.controllers;

import br.com.meucaixa.api.BaseIntegrationTest;
import br.com.meucaixa.api.models.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoleControllerTest extends BaseIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testGetRoles_shouldReturnRoleList() {
        // Arrange
        Role admin = new Role(1, "ADMIN", "Admin role", LocalDateTime.now(), LocalDateTime.now());
        PopulateRoles(List.of(admin));

        String url = "http://localhost:" + port + "/api/v1/roles";

        // Act
        var response = restTemplate.getForEntity(url, Object.class);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());

        var body = (LinkedHashMap<String, Object>) response.getBody();
        assertPage(body, 20, 1, 1, 0);
        assertRole(body, 1, 1, "ADMIN", "Admin role");
    }


    @Test
    void testGetRoles_shouldReturnRoleListPageable() {
        // Arrange
        Role admin = new Role(1, "ADMIN", "Admin role", LocalDateTime.now(), LocalDateTime.now());
        Role user = new Role(2, "USER", "User role", LocalDateTime.now(), LocalDateTime.now());
        Role guest = new Role(3, "GUEST", "Guest role", LocalDateTime.now(), LocalDateTime.now());
        PopulateRoles(List.of(admin, user, guest));

        String url = "http://localhost:" + port + "/api/v1/roles?page=1&size=1&sort=name,asc";

        // Act
        var response = restTemplate.getForEntity(url, Object.class);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());

        var body = (LinkedHashMap<String, Object>) response.getBody();
        assertPage(body, 1, 3, 3, 1);
        assertRole(body, 1, 3, "GUEST", "Guest role");
    }


    @ParameterizedTest
    @CsvSource({
            "'sort=invalid_field,asc', 'Invalid sort in query: invalid_field: ASC'",
            "'sort=name,invalid_order', 'Invalid sort in query: name: ASC,invalid_order: ASC'",
    })
    void testGetRoles_shouldReturnException(String pageable, String expectedMessage) {
        // Arrange
        String url = "http://localhost:" + port + "/api/v1/roles?" + pageable;

        // Act
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // Assert
        assertTrue(response.getStatusCode().is4xxClientError());
        assertNotNull(response.getBody());
        assertEquals(expectedMessage, response.getBody());
    }

    private void assertPage(LinkedHashMap<String, Object> body, int size, int total, int totalPages, int pageNumber) {
        var page = (LinkedHashMap<String, Object>) body.get("page");
        assertNotNull(page);
        assertEquals(size, page.get("size"));
        assertEquals(total, page.get("totalElements"));
        assertEquals(totalPages, page.get("totalPages"));
        assertEquals(pageNumber, page.get("number"));
    }

    private void assertRole(LinkedHashMap<String, Object> body, int expectedSize, int id, String name, String description) {
        var embedded = (LinkedHashMap<String, Object>) body.get("_embedded");
        assertNotNull(embedded);

        var roles = (List<LinkedHashMap<String, Object>>) embedded.get("roleResponseList");
        assertNotNull(roles);

        assertEquals(expectedSize, roles.size());

        var role = roles.get(0);
        assertEquals(id, role.get("id"));
        assertEquals(name, role.get("name"));
        assertEquals(description, role.get("description"));
        assertNull(role.get("createdAt"));
        assertNull(role.get("updatedAt"));
    }

    private void PopulateRoles(List<Role> user) {
        for (Role role : user) {
            String sql = "INSERT INTO roles (id, name, description, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, role.getId(), role.getName(), role.getDescription(), role.getCreatedAt(), role.getUpdatedAt());
        }
    }
}
