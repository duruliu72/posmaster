package com.osudpotro.posmaster.requisitiontype;

import com.osudpotro.posmaster.user.auth.AuthService;
import com.osudpotro.posmaster.requisition.*;
import com.osudpotro.posmaster.user.UserNotFoundException;
import com.osudpotro.posmaster.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class RequisitionTypeService {
    private final AuthService authService;
    private final RequsitionTypeRepository requsitionTypeRepository;
    private final RequisitionApproverRepository requisitionApproverRepository;
    private final RequisitionTypeMapper requsitionTypeMapper;
    private final CustomRequisitionTypeMapper customRequisitionTypeMapper;
    private final UserRepository userRepository;
    private final RequisitionApproverMapper requisitionApproverMapper;
    public List<RequisitionTypeDto> getAllRequisitionTypes() {
        return requsitionTypeRepository.findAll().stream()
                .map(customRequisitionTypeMapper::toDto)
                .toList();
    }

    public Page<RequisitionTypeDto> filterRequisitionTypes(RequisitionTypeFilter filter, Pageable pageable) {
        return requsitionTypeRepository.findAll(RequisitionTypeSpecification.filter(filter), pageable).map(customRequisitionTypeMapper::toDto);
    }

    public RequisitionTypeDto createRequisitionType(RequisitionTypeCreateRequest request) {
        if (requsitionTypeRepository.existsByName(request.getName())) {
            throw new DuplicateRequisitionTypeException();
        }
        var user = authService.getCurrentUser();
        var requsitionType = requsitionTypeMapper.toEntity(request);
        String requisitionTypeKey = request.getName().replace(" ", "_").toUpperCase();
        requsitionType.setRequisitionTypeKey(requisitionTypeKey);
        requsitionType.setCreatedBy(user);

        requsitionTypeRepository.save(requsitionType);
        return customRequisitionTypeMapper.toDto(requsitionType);
    }

    public RequisitionTypeDto updateRequisitionType(Long requsitionTypeId, RequisitionTypeUpdateRequest request) {
        var requsitionType = requsitionTypeRepository.findById(requsitionTypeId).orElseThrow(RequisitionTypeNotFoundException::new);
        var user = authService.getCurrentUser();
        requsitionType.setName(request.getName());
        String requisitionTypeKey = request.getName().replace(" ", "_").toUpperCase();
        requsitionType.setRequisitionTypeKey(requisitionTypeKey);
        requsitionType.setUpdatedBy(user);
        List<RequisitionApprover> requisitionApprovers = new ArrayList<>();
        for (RequisitionApproverUpdateRequest r : request.getApprovers()) {
            RequisitionApprover requisitionApprover = new RequisitionApprover();
            var aUser = userRepository.findById(r.getUserId()).orElseThrow(UserNotFoundException::new);
            requisitionApprover.setUser(aUser);
            if (r.getNextUserId() != null) {
                var nextUser = userRepository.findById(r.getNextUserId()).orElse(null);
                requisitionApprover.setNextUser(nextUser);
            }
            requisitionApprover.setRequisitionType(requsitionType);
            requisitionApprover.setCreatedBy(user);
            requisitionApprovers.add(requisitionApprover);
        }
        requsitionType.setRequisitionApprovers(requisitionApprovers);
        requsitionTypeRepository.save(requsitionType);
        return customRequisitionTypeMapper.toDto(requsitionType);
    }
    @Transactional
    public RequisitionApproverDto addRequisitionTypeApprover(Long requsitionTypeId, RequisitionTypeApproverAddRequest request) {
        var requsitionType = requsitionTypeRepository.findById(requsitionTypeId).orElseThrow(RequisitionTypeNotFoundException::new);
        var authUser = authService.getCurrentUser();
        var findRequsitionTypeApprover = requisitionApproverRepository.findRequisitionApprover(requsitionTypeId, request.getUserId()).orElse(null);
        if (findRequsitionTypeApprover != null) {
            throw new RequisitionTypeApproverDuplicateException();
        }

        var user = userRepository.findById(request.getUserId()).orElseThrow(UserNotFoundException::new);
        var findApproverNextNullUser= requisitionApproverRepository.findApproverWithNullNextUser(requsitionTypeId).orElse(null);
        if(findApproverNextNullUser!=null){
//            Update Null for Previous User
            findApproverNextNullUser.setNextUser(user);
            requisitionApproverRepository.save(findApproverNextNullUser);
        }
        RequisitionApprover requisitionApprover = new RequisitionApprover();
        requisitionApprover.setRequisitionType(requsitionType);
        if(!requsitionType.getRequisitionApprovers().isEmpty()&&findApproverNextNullUser!=null){
            requisitionApprover.setPrevUser(findApproverNextNullUser.getUser());
        }
        requisitionApprover.setUser(user);
        requisitionApprover.setCreatedBy(authUser);
        requisitionApproverRepository.save(requisitionApprover);
        return requisitionApproverMapper.toDto(requisitionApprover);
    }

    public RequisitionTypeDto getRequisitionType(Long requsitionTypeId) {
        var requsitionType = requsitionTypeRepository.findById(requsitionTypeId).orElseThrow(RequisitionTypeNotFoundException::new);
        var approverDtos= requisitionApproverRepository.findByRequisitionTypeIdOrderByIdAsc(requsitionTypeId).stream().map(requisitionApproverMapper::toDto).toList();
        var requsitionTypeDto= customRequisitionTypeMapper.toDto(requsitionType);
        requsitionTypeDto.setApprovers(approverDtos);
        return requsitionTypeDto;
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
        return customRequisitionTypeMapper.toDto(requsitionType);
    }

    public RequisitionTypeDto deactivateRequisitionType(Long requsitionTypeId) {
        var requsitionType = requsitionTypeRepository.findById(requsitionTypeId).orElseThrow(() -> new RequisitionTypeNotFoundException("RequisitionType not found with ID: " + requsitionTypeId));
        var user = authService.getCurrentUser();
        requsitionType.setStatus(2);
        requsitionType.setUpdatedBy(user);
        requsitionTypeRepository.save(requsitionType);
        return customRequisitionTypeMapper.toDto(requsitionType);
    }

    public RequisitionTypeDto deleteRequisitionType(Long requsitionTypeId) {
        var requsitionType = requsitionTypeRepository.findById(requsitionTypeId).orElseThrow(() -> new RequisitionTypeNotFoundException("RequisitionType not found with ID: " + requsitionTypeId));
        var user = authService.getCurrentUser();
        requsitionType.setStatus(3);
        requsitionType.setUpdatedBy(user);
        requsitionTypeRepository.save(requsitionType);
        return customRequisitionTypeMapper.toDto(requsitionType);
    }
}