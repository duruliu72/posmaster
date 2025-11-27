package com.osudpotro.posmaster.requisitiontype;

import com.osudpotro.posmaster.auth.AuthService;
import com.osudpotro.posmaster.requisition.Approver;
import com.osudpotro.posmaster.requisition.ApproverUpdateRequest;
import com.osudpotro.posmaster.user.UserNotFoundException;
import com.osudpotro.posmaster.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class RequisitionTypeService {
    private final AuthService authService;
    private final RequsitionTypeRepository requsitionTypeRepository;
    private final RequisitionTypeMapper requsitionTypeMapper;
    private final CustomRequisitionMapper customRequisitionMapper;
    private final UserRepository userRepository;
    public List<RequisitionTypeDto> gerAllRequisitionTypes() {
        return requsitionTypeRepository.findAll().stream()
                .map(customRequisitionMapper::toDto)
                .toList();
    }

    public RequisitionTypeDto createRequisitionType(RequisitionTypeCreateRequest request) {
        if (requsitionTypeRepository.existsByName(request.getName())) {
            throw new DuplicateRequisitionTypeException();
        }
        var user = authService.getCurrentUser();
        var requsitionType = requsitionTypeMapper.toEntity(request);
        requsitionType.setCreatedBy(user);

        requsitionTypeRepository.save(requsitionType);
        return customRequisitionMapper.toDto(requsitionType);
    }

    public RequisitionTypeDto updateRequisitionType(Long requsitionTypeId, RequisitionTypeUpdateRequest request) {
        var requsitionType = requsitionTypeRepository.findById(requsitionTypeId).orElseThrow(RequisitionTypeNotFoundException::new);
        var user = authService.getCurrentUser();
        requsitionType.setName(request.getName());
        Set<Approver> approvers = new HashSet<>();
        for (ApproverUpdateRequest r : request.getApprovers()) {
            Approver approver = new Approver();
            var aUser = userRepository.findById(r.getUserId()).orElseThrow(UserNotFoundException::new);
            approver.setUser(aUser);
            if(r.getNextUserId()!=null){
                var nextUser = userRepository.findById(r.getNextUserId()).orElse(null);
                approver.setNextUser(nextUser);
            }
            approver.setRequisitionType(requsitionType);
            approver.setCreatedBy(user);
            approvers.add(approver);
        }
        requsitionType.setApprovers(approvers);
        requsitionType.setUpdatedBy(user);
        requsitionTypeRepository.save(requsitionType);
        return customRequisitionMapper.toDto(requsitionType);
    }

    public RequisitionTypeDto getRequisitionType(Long requsitionTypeId) {
        var requsitionType = requsitionTypeRepository.findById(requsitionTypeId).orElseThrow(RequisitionTypeNotFoundException::new);
        return customRequisitionMapper.toDto(requsitionType);
    }

    public RequisitionType getRequisitionTypeEntity(Long requsitionTypeId) {
        return requsitionTypeRepository.findById(requsitionTypeId).orElseThrow(RequisitionTypeNotFoundException::new);
    }

    public RequisitionTypeDto activeRequisitionType(Long requsitionTypeId) {
        var requsitionType = requsitionTypeRepository.findById(requsitionTypeId).orElseThrow(() -> new RequisitionTypeNotFoundException("RequisitionType not found with ID: " + requsitionTypeId));
        var user = authService.getCurrentUser();
        requsitionType.setStatus(1);
        requsitionType.setUpdatedBy(user);
        requsitionTypeRepository.save(requsitionType);
        return customRequisitionMapper.toDto(requsitionType);
    }

    public RequisitionTypeDto deactivateRequisitionType(Long requsitionTypeId) {
        var requsitionType = requsitionTypeRepository.findById(requsitionTypeId).orElseThrow(() -> new RequisitionTypeNotFoundException("RequisitionType not found with ID: " + requsitionTypeId));
        var user = authService.getCurrentUser();
        requsitionType.setStatus(2);
        requsitionType.setUpdatedBy(user);
        requsitionTypeRepository.save(requsitionType);
        return customRequisitionMapper.toDto(requsitionType);
    }

    public RequisitionTypeDto deleteRequisitionType(Long requsitionTypeId) {
        var requsitionType = requsitionTypeRepository.findById(requsitionTypeId).orElseThrow(() -> new RequisitionTypeNotFoundException("RequisitionType not found with ID: " + requsitionTypeId));
        var user = authService.getCurrentUser();
        requsitionType.setStatus(3);
        requsitionType.setUpdatedBy(user);
        requsitionTypeRepository.save(requsitionType);
        return customRequisitionMapper.toDto(requsitionType);
    }
}