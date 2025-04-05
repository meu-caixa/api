package br.com.meucaixa.api.services;

import br.com.meucaixa.api.models.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleService {
    Page<Role> listRolesPageable(Pageable pageable);
}
