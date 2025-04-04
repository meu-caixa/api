package br.com.meucaixa.api.repositories;

import br.com.meucaixa.api.models.Role;

import java.util.List;

public interface RoleRepository {
    List<Role> findAll();
}
