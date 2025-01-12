package kz.odik.crm.Service;

import kz.odik.crm.DTO.AccessRightsDTO;
import kz.odik.crm.DTO.AccessRightsGetAllResponseDTO;
import kz.odik.crm.Repository.AccessRightsRepository;
import kz.odik.crm.entity.AccessRights;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccessRightsService {
    @Autowired
    private AccessRightsRepository accessRightsRepository;

    public AccessRights create(AccessRightsDTO dto) {

        return accessRightsRepository.save(AccessRights.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build());

    }

    public AccessRights updateAccessRight(Long accessRightid, AccessRightsDTO dto) {
        AccessRights accessRight = accessRightsRepository.findById(accessRightid).orElseThrow(() -> new RuntimeException("Role not found not found: " + accessRightid));
        if (dto.getName() != null) {
            accessRight.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            accessRight.setDescription(dto.getDescription());
        }
        return accessRightsRepository.save(accessRight);
    }

    public List<AccessRightsGetAllResponseDTO> getAll() {
        List<AccessRights> accessRights = accessRightsRepository.findAll();
        List<AccessRightsGetAllResponseDTO> userGetAllResponseDTOS = new ArrayList<>();
        for (AccessRights user : accessRights) {
            userGetAllResponseDTOS.add(new AccessRightsGetAllResponseDTO(user.getId(), user.getName(), user.getDescription()));
        }
        return userGetAllResponseDTOS;
    }


    public Optional<AccessRights> getById(Long id) {
        return accessRightsRepository.findById(id);
    }
}
