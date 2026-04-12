package com.osudpotro.posmaster.inventory;

import com.osudpotro.posmaster.user.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {
    @Autowired
    private InventoryRepository invSummaryRepo;
    @Autowired
    private AuthService authService;
    @Autowired
    InventoryMapper invSummaryMapper;
    public List<InventoryGroupDto> getGroupInventorySummary() {
        var authUser = authService.getCurrentUser();
        return invSummaryRepo.findAllGroupInventorySummary(authUser.getBranch().getId())
                .stream()
                .toList();
    }
    public Page<InventoryGroupDto> filterGroupInventorySummary(InventoryFilter filter, Pageable pageable) {
        var authUser = authService.getCurrentUser();
        var x=InventorySummarySpecification.filter(filter,authUser);
        return invSummaryRepo.findAllGroupInventorySummary(x, pageable);
    }
    public Page<InventoryGroupDto> filterEntitiesWithOnlyPage(InventoryFilter filter, Pageable pageable) {
        var authUser = authService.getCurrentUser();
        return invSummaryRepo.findAllGroupInventorySummaryPage(pageable);
    }
    public Page<InventoryByGroupProjection> filterGroupInventorySummaryProjection(InventoryFilter filter, Pageable pageable) {
        var authUser = authService.getCurrentUser();
        return invSummaryRepo.findAllGroupInventorySummaryByProjection(authUser.getBranch().getId(),"", pageable);
    }
    public Page<InventoryByGroupProjection> filterGroupInvSummaryByBranch(InventoryFilter filter, Pageable pageable) {
        return invSummaryRepo.findAllGroupInventorySummaryByProjection(filter.getBranchId(),filter.getSearchKey(), pageable);
    }
}
