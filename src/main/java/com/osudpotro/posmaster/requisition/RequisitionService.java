package com.osudpotro.posmaster.requisition;

import com.osudpotro.posmaster.auth.AuthService;
import com.osudpotro.posmaster.requisitiontype.RequisitionTypeDto;
import com.osudpotro.posmaster.requisitiontype.RequisitionTypeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequisitionService {
    @Autowired
    private AuthService authService;
    @Autowired
    private RequisitionRepository requisitionRepository;
    @Autowired
    private RequisitionMapper requisitionMapper;
    public List<RequisitionDto> getAllRequisitions() {
        var authUser = authService.getCurrentUser();
        return requisitionRepository.findAll().stream()
                .map(requisitionMapper::toDto)
                .toList();
    }
    public Page<RequisitionDto> filterRequisitions(RequisitionFilter filter, Pageable pageable) {
        var authUser = authService.getCurrentUser();
        return requisitionRepository.findAll(RequisitionSpecification.filter(filter), pageable).map(requisitionMapper::toDto);
    }
    public RequisitionDto getRequisition(Long requsitionId) {
        var authUser = authService.getCurrentUser();
        var requsition = requisitionRepository.findById(requsitionId).orElseThrow(RequisitionTypeNotFoundException::new);
//        var approverDtos= requsitionApproverRepository.findByRequisitionTypeIdOrderByIdAsc(requsitionId).stream().map(requisitionApproverMapper::toDto).toList();
//        var requsitionTypeDto= customRequisitionTypeMapper.toDto(requsitionType);
//        requsitionTypeDto.setApprovers(approverDtos);

        return requisitionMapper.toDto(requsition);
    }
}
