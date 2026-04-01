package com.osudpotro.posmaster.inventory;

import com.osudpotro.posmaster.user.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventorySummaryService {
    @Autowired
    private InventorySummaryRepository invSummaryRepo;
    @Autowired
    private AuthService authService;
    @Autowired
    InventorySummaryMapper invSummaryMapper;
    public List<InventorySummaryGroupDto> getGroupInventorySummary() {
        var authUser = authService.getCurrentUser();
        return invSummaryRepo.findAllGroupInventorySummary(authUser.getBranch().getId())
                .stream()
                .toList();
    }
    public Page<InventorySummaryGroupDto> filterGroupInventorySummary(InventorySummaryFilter filter, Pageable pageable) {
        var authUser = authService.getCurrentUser();
        var x=InventorySummarySpecification.filter(filter,authUser);
        return invSummaryRepo.findAllGroupInventorySummary(x, pageable);
    }
    public Page<InventorySummaryGroupDto> filterEntitiesWithOnlyPage(InventorySummaryFilter filter, Pageable pageable) {
        var authUser = authService.getCurrentUser();
        return invSummaryRepo.findAllGroupInventorySummaryPage(pageable);
    }
    public Page<InventorySummaryGroupProjection> filterGroupInventorySummaryProjection(InventorySummaryFilter filter, Pageable pageable) {
        var authUser = authService.getCurrentUser();
        return invSummaryRepo.findAllGroupInventorySummaryByProjection(authUser.getBranch().getId(), pageable);
    }
}
