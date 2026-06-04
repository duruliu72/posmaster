package com.osudpotro.posmaster.dispatch;
import com.osudpotro.posmaster.common.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DispatchItemService {
    @Autowired
    private DispatchItemRepository dispatchItemRepo;
    @Autowired
    private DispatchItemMapper dispatchItemMapper;
    public DispatchItemDto deleteEntity(Long dispatchItemId) {
        DispatchItem dispatchItem = dispatchItemRepo.findById(dispatchItemId).orElseThrow(() -> new EntityNotFoundException("Dispatch Item not found with ID: " + dispatchItemId));
        dispatchItemRepo.deleteById(dispatchItemId);
        return dispatchItemMapper.toDto(dispatchItem);
    }
}
