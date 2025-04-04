package br.com.meucaixa.api.repositories;

import br.com.meucaixa.api.models.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleRepository {
    Page<Role> findAllPageable(Pageable pageable);
}
