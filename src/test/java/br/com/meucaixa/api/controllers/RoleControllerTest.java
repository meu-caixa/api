package br.com.meucaixa.api.controllers;

import br.com.meucaixa.api.BaseIntegrationTest;
import br.com.meucaixa.api.models.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

class RoleControllerTest extends BaseIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testGetRoles_shouldReturnRoleListDefault() {
        // Arrange
        String url = "http://localhost:" + port + "/api/v1/roles";

        // Act
        ResponseEntity<Role[]> response = restTemplate.getForEntity(url, Role[].class);

        // Assert
        assert response.getStatusCode().is2xxSuccessful();
        assert response.getBody() != null;
        assert response.getBody().length == 1;
        assert response.getBody()[0].getId() == 1;
        assert response.getBody()[0].getName().equals("ADMIN");
    }
}
