package kz.odik.crm.Service;

import kz.odik.crm.DTO.GetAllRolesDTO;
import kz.odik.crm.DTO.RolesDTO;
import kz.odik.crm.DTO.UpdateRoleDTO;
import kz.odik.crm.DTO.UpdateRoleResponseDTO;
import kz.odik.crm.Repository.AccessRightsRepository;
import kz.odik.crm.Repository.RoleAccessRightsRepository;
import kz.odik.crm.Repository.RolesRepository;
import kz.odik.crm.entity.AccessRights;
import kz.odik.crm.entity.RoleAccessRights;
import kz.odik.crm.entity.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleService {
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private AccessRightsRepository accessRightsRepository;
    @Autowired
    private RoleAccessRightsRepository roleAccessRightsRepository;

    public Roles create(RolesDTO dto) {
        Roles role = Roles.builder().name(dto.getName()).build();
        role = rolesRepository.save(role);
        for (long id : dto.getAccess_rights_ids()) {
            AccessRights accessRights = accessRightsRepository.findById(id).orElseThrow(() -> new RuntimeException("AAccess Right not found:" + id));
            role.getAccessRights().add(accessRights);
        }
        return rolesRepository.save(role);
    }

    public Roles updateRoles(Long roleid, RolesDTO dto) {
        Roles role = rolesRepository.findById(roleid).orElseThrow(() -> new RuntimeException("Role not found not found: " + roleid));
        if (dto.getName() != null) {
            role.setName(dto.getName());
        }
        return rolesRepository.save(role);
    }

    public UpdateRoleResponseDTO updateRole(Long roleId, UpdateRoleDTO dto) {
        System.out.println("sdsdsdsd");
        Roles role = rolesRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found not found: " + roleId));
        if (dto.getName() != null) {
            role.setName(dto.getName());
        }
        roleAccessRightsRepository.deleteByRole(role);
        if (dto.getAccessRights() != null) {
            System.out.println("Initialization");
            List<AccessRights> accessRights = accessRightsRepository.findByNames(dto.getAccessRights());
            System.out.println("setAccessrig");
            List<RoleAccessRights> roleAccessRights = accessRights.stream().map(it -> RoleAccessRights.builder().role(role).accessRight(it).build()).collect(Collectors.toList());
            roleAccessRightsRepository.saveAll(roleAccessRights);
            System.out.println("Set AR");
        }
        System.out.println("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ");
        UpdateRoleResponseDTO updateRoleResponseDTO = new UpdateRoleResponseDTO();
        updateRoleResponseDTO.setId(role.getId());
        updateRoleResponseDTO.setName(role.getName());
        return updateRoleResponseDTO;
    }

    public Roles removeAccessRightTorole(Long roleid, Long accessRightId, RolesDTO dto) {
        Roles role = rolesRepository.findById(roleid).orElseThrow(() -> new RuntimeException("Role not found not found: " + roleid));
        AccessRights accessRights = accessRightsRepository.findById(accessRightId).orElseThrow(() -> new RuntimeException("AAccess Right not found:" + accessRightId));
        role.getAccessRights().remove(accessRights);
        return rolesRepository.save(role);
    }


    public List<GetAllRolesDTO> getAllRoles() {
        List<Roles> roles = rolesRepository.findAll();
        List<GetAllRolesDTO> roleDTOs = new ArrayList<>();
        for (Roles role : roles) {
            GetAllRolesDTO roleDTO = new GetAllRolesDTO(role.getId(), role.getName());
            roleDTOs.add(roleDTO);
        }
        return roleDTOs;
    }


    public Optional<Roles> getRoleById(Long id) {
        return rolesRepository.findById(id);
    }

    public void deleteRoleById(Long id) {
        rolesRepository.deleteById(id);
    }
}
