package com.osudpotro.posmaster.user.customer.wallet;

import com.osudpotro.posmaster.common.EntityNotFoundException;
import com.osudpotro.posmaster.user.User;
import com.osudpotro.posmaster.user.UserType;
import com.osudpotro.posmaster.user.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WalletService {
    @Autowired
    private AuthService authService;
    @Autowired
    private WalletRepository walletRepo;
    @Autowired
    private WalletMapper walletMapper;

    public List<WalletDto> getAllEntities() {
        User user = authService.getCurrentUser();
        if (user.getUserType().equals(UserType.CUSTOMER)) {
            throw new EntityNotFoundException("You are  not Allowed");
        }
        return walletRepo.findAllByCustomer(user.getCustomer())
                .stream()
                .map(walletMapper::toDto)
                .toList();
    }

    public Page<WalletDto> getAllEntities(WalletFilter filter, Pageable pageable) {
        User user = authService.getCurrentUser();
        if (user.getUserType().equals(UserType.CUSTOMER)) {
            throw new EntityNotFoundException("You are  not Allowed");
        }
        return walletRepo.findAllByCustomer(user.getCustomer(), WalletSpecification.filter(filter), pageable).map(walletMapper::toDto);
    }

    public WalletDto getEntity(Long entityId) {
        var entity = walletRepo.findById(entityId).orElseThrow(() -> new EntityNotFoundException("Wallet not found with ID: " + entityId));
        return walletMapper.toDto(entity);
    }

    public WalletDto createEntity(WalletCreateRequest request) {
        return null;
    }

    public WalletDto updateEntity(Long entityId, WalletUpdateRequest request) {
        return null;
    }
}
