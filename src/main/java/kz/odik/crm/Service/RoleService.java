package kz.odik.crm.Service;

import kz.odik.crm.DTO.RolesDTO;
import kz.odik.crm.Repository.AccessRightsRepository;
import kz.odik.crm.Repository.RolesRepository;
import kz.odik.crm.entity.AccessRights;
import kz.odik.crm.entity.Roles;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    private RolesRepository rolesRepository;
    private AccessRightsRepository accessRightsRepository;

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

    public Roles addAccessRightTorole(Long roleid, Long accessRightId, RolesDTO dto) {
        Roles role = rolesRepository.findById(roleid).orElseThrow(() -> new RuntimeException("Role not found not found: " + roleid));
        AccessRights accessRights = accessRightsRepository.findById(accessRightId).orElseThrow(() -> new RuntimeException("AAccess Right not found:" + accessRightId));
        role.getAccessRights().add(accessRights);
        return rolesRepository.save(role);
    }

    public Roles removeAccessRightTorole(Long roleid, Long accessRightId, RolesDTO dto) {
        Roles role = rolesRepository.findById(roleid).orElseThrow(() -> new RuntimeException("Role not found not found: " + roleid));
        AccessRights accessRights = accessRightsRepository.findById(accessRightId).orElseThrow(() -> new RuntimeException("AAccess Right not found:" + accessRightId));
        role.getAccessRights().remove(accessRights);
        return rolesRepository.save(role);
    }

    public List<Roles> getAllRoles() {
        return rolesRepository.findAll();
    }

    public Optional<Roles> getRoleById(Long id) {
        return rolesRepository.findById(id);
    }

    public void deleteRoleById(Long id) {
        rolesRepository.deleteById(id);
    }
}
