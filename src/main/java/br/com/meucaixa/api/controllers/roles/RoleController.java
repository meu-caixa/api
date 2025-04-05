package br.com.meucaixa.api.controllers.roles;

import br.com.meucaixa.api.exceptions.ValidationException;
import br.com.meucaixa.api.models.Role;
import br.com.meucaixa.api.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public HttpEntity<PagedModel<RoleResponse>> listRolesPageable(Pageable pageable, PagedResourcesAssembler assembler) {
        Page<Role> roles = roleService.listRolesPageable(pageable);
        PagedModel<RoleResponse> model = assembler.toModel(
                roles.map(role -> new RoleResponse(role.getId(), role.getName(), role.getDescription())));
        return ResponseEntity.ok(model);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException(ValidationException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
