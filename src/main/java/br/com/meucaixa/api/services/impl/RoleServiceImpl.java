package br.com.meucaixa.api.services.impl;

import br.com.meucaixa.api.models.Role;
import br.com.meucaixa.api.repositories.RoleRepository;
import br.com.meucaixa.api.services.RoleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Page<Role> listRolesPageable(Pageable pageable) {
        return roleRepository.findAllPageable(pageable);
    }
}
