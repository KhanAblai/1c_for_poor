package kz.odik.crm.Service;

import jakarta.transaction.Transactional;
import kz.odik.crm.DTO.CreateStoreResponseDTO;
import kz.odik.crm.DTO.GetAllStoresDTO;
import kz.odik.crm.DTO.StoreDTO;
import kz.odik.crm.Repository.StoreRepository;
import kz.odik.crm.Repository.UsersStoresRepository;
import kz.odik.crm.entity.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StoreService {
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private UsersStoresRepository usersStoresRepository;

    public List<GetAllStoresDTO> getAllStores() {
        List<Store> stores = storeRepository.findAll();
        List<GetAllStoresDTO> storesDTOS = new ArrayList<>();
        for (Store store : stores) {
            storesDTOS.add(new GetAllStoresDTO(store.getId(), store.getName(), store.getPlace()));
        }
        return storesDTOS;
    }

    public List<GetAllStoresDTO> getAllStoresToUser(Long userId) {
        List<Store> stores = storeRepository.findAllStoresByUserId(userId);
        List<GetAllStoresDTO> storesDTOS = new ArrayList<>();
        for (Store store : stores) {
            storesDTOS.add(new GetAllStoresDTO(store.getId(), store.getName(), store.getPlace()));
        }
        return storesDTOS;
    }

    public CreateStoreResponseDTO create(StoreDTO storeDTO) {
        Store store = new Store();
        store.setName(storeDTO.getName());
        store.setPlace(storeDTO.getPlace());
        storeRepository.save(store);
        CreateStoreResponseDTO createStoreResponseDTO = new CreateStoreResponseDTO();
        createStoreResponseDTO.setName(store.getName());
        createStoreResponseDTO.setPlace(store.getPlace());
        return createStoreResponseDTO;
    }

    @Transactional
    public CreateStoreResponseDTO updateStore(Long storeId, StoreDTO dto) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new RuntimeException("Store not found:" + storeId));
        System.out.println("Assssss");
        if (dto.getName() != null) {
            store.setName(dto.getName());
        }
        if (dto.getPlace() != null) {
            store.setPlace(dto.getPlace());
        }
        storeRepository.save(store);
        CreateStoreResponseDTO createStoreResponseDTO = new CreateStoreResponseDTO();
        createStoreResponseDTO.setName(store.getName());
        createStoreResponseDTO.setPlace(store.getPlace());
        return createStoreResponseDTO;
    }
}
