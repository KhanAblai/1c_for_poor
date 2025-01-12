package kz.odik.crm.controller;

import kz.odik.crm.DTO.*;
import kz.odik.crm.Repository.RolesRepository;
import kz.odik.crm.Service.AccessRightsService;
import kz.odik.crm.Service.RoleService;
import kz.odik.crm.entity.AccessRights;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/roles")
public class RoleController {
    private RolesRepository rolesRepository;
    private RoleService roleService;
    private AccessRightsService accessRightsService;

    @PreAuthorize("hasAuthority('UPDATE_USER') or hasAuthority('CREATE_USER')")
    @GetMapping("/all")
    public List<GetAllRolesDTO> getAllRoles() {
        return roleService.getAllRoles();
    }

    @PostMapping("/ar")
    public AccessRights createAccessRight(@RequestBody AccessRightsDTO dto) {
        return accessRightsService.create(dto);
    }

    @PostMapping("/set/{id}")
    public UpdateRoleResponseDTO setAccessRightToRole(@PathVariable("id") Long roleId, @RequestBody UpdateRoleDTO dto) {
        return roleService.updateRole(roleId, dto);
    }

    @GetMapping("/all/ar")
    public List<AccessRightsGetAllResponseDTO> getAllAccessRights() {
        return accessRightsService.getAll();
    }
}
