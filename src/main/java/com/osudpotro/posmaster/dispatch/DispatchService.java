package com.osudpotro.posmaster.dispatch;

import com.osudpotro.posmaster.branch.BranchNotFoundException;
import com.osudpotro.posmaster.branch.BranchRepository;
import com.osudpotro.posmaster.user.auth.AuthService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DispatchService {
    @Autowired
    private DispatchRepository dispatchRepo;
    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private DispatchMapper dispatchMapper;
    @Autowired
    private AuthService authService;

    public List<DispatchDto> getAllDispatches() {
        return dispatchRepo.findAll()
                .stream()
                .map(dispatchMapper::toDto)
                .toList();
    }

    public Page<DispatchDto> getAllDispatches(DispatchFilter filter, Pageable pageable) {
        var authUser = authService.getCurrentUser();
        return dispatchRepo.findAll(DispatchSpecification.filter(filter, authUser), pageable).map(dispatchMapper::toDto);
    }

    @Transactional
    public DispatchDto createDispatch(DispatchCreateRequest request) {
        var authUser = authService.getCurrentUser();
        String dispatchRef = getGenerateDispatchRef();
        if (dispatchRepo.existsByDispatchRef(dispatchRef)) {
            throw new DuplicateDispatchException();
        }
        var reqBranch = branchRepository.findById(authUser.getBranch().getId()).orElseThrow(BranchNotFoundException::new);
        var senderBranch = branchRepository.findById(request.getRequestReceivedBranchId()).orElseThrow(BranchNotFoundException::new);
        Dispatch dispatch = new Dispatch();
        dispatch.setDispatchRef(dispatchRef);
        dispatch.setReqestedBranch(reqBranch);
        dispatch.setRequestReceivedBranch(senderBranch);
        dispatch.setDispatchBy(authUser);
        dispatchRepo.save(dispatch);
        return dispatchMapper.toDto(dispatch);
    }

    @Transactional
    public DispatchDto updateDispatch(Long dispatchId, DispatchUpdateRequest request) {
        return null;
    }
    private String getGenerateDispatchRef() {
        Dispatch dispatch = dispatchRepo.findTopByOrderByCreatedAtDesc();
        if(dispatch==null){
            dispatch=new Dispatch();
        }
        return dispatch.getGenerateDispatchRef();
    }
    private String getGenerateDispatchInvoice() {
        Dispatch dispatch = dispatchRepo.findTopByOrderByDispatchInvoiceDesc();
        if(dispatch==null){
            dispatch=new Dispatch();
        }
        return dispatch.getGenerateDispatchInvoice();
    }
}
