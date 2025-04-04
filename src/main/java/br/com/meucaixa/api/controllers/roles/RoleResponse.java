package br.com.meucaixa.api.controllers.roles;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoleResponse {
    private int id;
    private String name;
    private String description;
}
