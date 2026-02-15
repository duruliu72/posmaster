package com.osudpotro.posmaster.requisition;

import com.osudpotro.posmaster.user.auth.AuthService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class RequisitionOnPathService {
    @Autowired
    private AuthService authService;
    @Autowired
    private RequisitionOnPathRepository requisitionOnPathRepository;
    @Autowired
    private RequisitionOnPathMapper requisitionOnPathMapper;
    @Autowired
    private RequisitionApproverRepository requisitionApproverRepository;
    @Autowired
    private RequisitionRepository requisitionRepository;

    public List<RequisitionOnPathDto> getAllRequisitionOnPaths() {
        return requisitionOnPathRepository.findAll().stream()
                .map(requisitionOnPathMapper::toDto)
                .toList();
    }

    public List<RequisitionOnPathDto> getAllRequisitionsOnPathByUser() {
        var authUser = authService.getCurrentUser();
        return requisitionOnPathRepository.findAllByUser(authUser).stream()
                .map(requisitionOnPathMapper::toDto)
                .toList();
    }

    public Page<RequisitionOnPathDto> filterRequisitionOnPaths(RequisitionOnPathFilter filter, Pageable pageable) {
        return requisitionOnPathRepository.findAll(RequisitionOnPathSpecification.filter(filter), pageable).map(requisitionOnPathMapper::toDto);
    }

    public Page<RequisitionOnPathDto> filterRequisitionsOnPathByUser(RequisitionOnPathFilter filter, Pageable pageable) {
        var authUser = authService.getCurrentUser();
//       return requisitionOnPathRepository.findRequisitionOnPathByUserWithPage(authUser.getId(),pageable).map(requisitionOnPathMapper::toDto);
        return requisitionOnPathRepository.findAll(RequisitionOnPathSpecification.filterByUser(filter, authUser.getId()), pageable).map(requisitionOnPathMapper::toDto);
    }

    public RequisitionOnPathDto getRequisitionOnPath(Long requisitionOnPathId) {
        var authUser = authService.getCurrentUser();
        var requsitionOnPath = requisitionOnPathRepository.findByIdAndUser(requisitionOnPathId, authUser).orElseThrow(RequsitionOnPathNotFoundException::new);
        return requisitionOnPathMapper.toDto(requsitionOnPath);
    }

    @Transactional
    public RequisitionOnPathDto updateRequisitionOnPath(Long requisitionOnPathId, RequisitionOnPathServiceUpdateRequest request) {
        var authUser = authService.getCurrentUser();
        var requsitionOnPath = requisitionOnPathRepository.findByIdAndUser(requisitionOnPathId, authUser).orElseThrow(RequsitionOnPathNotFoundException::new);
        var requisitionType = requsitionOnPath.getRequisition().getRequisitionType();
        var requisition = requsitionOnPath.getRequisition();
        Set<Integer> checkApprovedStatus = Set.of(2, 3, 4);
        if (checkApprovedStatus.contains(requsitionOnPath.getApprovedStatus())) {
            throw new RequisitionOnPathAlreadyApprovedException();
        }
        if (requsitionOnPath.getApprovedStatus() == 1) {
            int totalApprovers=requisitionType.getTotalApprover();
            int totalPaths=requisition.getTotalPaths();
            if (request.getApprovedStatus() == 2 && totalApprovers == totalPaths) {
//                3=Approved
                requisition.setRequisitionStatus(3);
                requisitionRepository.save(requisition);
            }
            if (request.getApprovedStatus() == 3) {
//                4=Rejected
                requisition.setRequisitionStatus(4);
                requisitionRepository.save(requisition);
            }
            if (request.getApprovedStatus() == 4) {
//                5=Review
                int reviewCount=requisition.getReviewCount()+1;
                requisition.setRequisitionStatus(5);
                requisition.setReviewCount(reviewCount);
                requisitionRepository.save(requisition);
                var findApproverPrevUser = requisitionApproverRepository.findApproverWithNullPrevUser(requisitionType.getId()).orElseThrow(RequsitionOnPathNotFoundException::new);
                RequisitionOnPath rop = new RequisitionOnPath();
                rop.setRequisition(requisition);
                rop.setReviewCount(reviewCount);
                rop.setPrevUser(findApproverPrevUser.getPrevUser());
                rop.setUser(findApproverPrevUser.getUser());
                rop.setNextUser(findApproverPrevUser.getNextUser());
                rop.setApprovedStatus(1);
                rop.setCreatedBy(authUser);
                requisitionOnPathRepository.save(rop);
            }
            requsitionOnPath.setComment(request.getComment());
            requsitionOnPath.setApprovedStatus(request.getApprovedStatus());
//        Forward Requisition next approver
            var nextUser = requsitionOnPath.getNextUser();
            if (nextUser != null) {
                var findApproverNextUser = requisitionApproverRepository.findRequisitionApprover(requisitionType.getId(), nextUser.getId()).orElseThrow(RequsitionOnPathNotFoundException::new);
                RequisitionOnPath rop = new RequisitionOnPath();
                rop.setRequisition(requisition);
                rop.setReviewCount(requsitionOnPath.getRequisition().getReviewCount());
                rop.setPrevUser(findApproverNextUser.getPrevUser());
                rop.setUser(findApproverNextUser.getUser());
                rop.setNextUser(findApproverNextUser.getNextUser());
                rop.setApprovedStatus(1);
                rop.setCreatedBy(authUser);
                requisitionOnPathRepository.save(rop);
            }
        }
        requisitionOnPathRepository.save(requsitionOnPath);
        return requisitionOnPathMapper.toDto(requsitionOnPath);

    }
}
