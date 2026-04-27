package com.osudpotro.posmaster.offerhub.membership;

import com.osudpotro.posmaster.user.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class MembershipService {
    @Autowired
    private AuthService authService;
    @Autowired
    private MembershipRepository membershipRepo;
    @Autowired
    private MembershipMapper membershipMapper;

    public List<MembershipDto> getAllEntities() {
        return membershipRepo.findAll()
                .stream()
                .map(membershipMapper::toDto)
                .toList();
    }

    public Page<MembershipDto> getAllEntities(MembershipFilter filter, Pageable pageable) {
        return membershipRepo.findAll(MembershipSpecification.filter(filter), pageable).map(membershipMapper::toDto);
    }

    public MembershipDto getEntity(Long entityId) {
        var entity = membershipRepo.findById(entityId).orElseThrow(() -> new MembershipNotFoundException("Membership not found with ID: " + entityId));
        return membershipMapper.toDto(entity);
    }

    public MembershipDto createEntity(MembershipCreateRequest request) {
        if (membershipRepo.existsByName(request.getName())) {
            throw new DuplicateMembershipException();
        }
        var user = authService.getCurrentUser();
        Membership membership = new Membership();
        membership.setName(request.getName());
        membership.setDiscount(request.getDiscount());
        membership.setMaxDiscount(request.getMaxDiscount());
        membership.setIsPercentage(request.getIsPercentage());
        membership.setCreatedBy(user);
        membershipRepo.save(membership);
        return membershipMapper.toDto(membership);
    }

    public MembershipDto updateEntity(Long entityId, MembershipUpdateRequest request) {
        var membership = membershipRepo.findById(entityId).orElseThrow(MembershipNotFoundException::new);
        if(!Objects.equals(membership.getName(), request.getName())){
            if (membershipRepo.existsByName(request.getName())) {
                throw new DuplicateMembershipException();
            }
        }
        var user = authService.getCurrentUser();
        membership.setName(request.getName());
        membership.setDiscount(request.getDiscount());
        membership.setMaxDiscount(request.getMaxDiscount());
        membership.setIsPercentage(request.getIsPercentage());
        membership.setUpdatedBy(user);
        membershipRepo.save(membership);
        return membershipMapper.toDto(membership);
    }

    public MembershipDto deleteEntity(Long entityId) {
        var membership = membershipRepo.findById(entityId).orElseThrow(() -> new MembershipNotFoundException("Membership not found with ID: " + entityId));
        var user = authService.getCurrentUser();
        membership.setStatus(3);
        membership.setUpdatedBy(user);
        membershipRepo.save(membership);
        return membershipMapper.toDto(membership);
    }

    public int deleteBulkEntity(List<Long> entityIds) {
        return membershipRepo.deleteBulkEntity(entityIds, 3L);
    }

    public MembershipDto activateEntity(Long entityId) {
        var membership = membershipRepo.findById(entityId).orElseThrow(() -> new MembershipNotFoundException("Membership not found with ID: " + entityId));
        var user = authService.getCurrentUser();
        membership.setStatus(1);
        membership.setUpdatedBy(user);
        membershipRepo.save(membership);
        return membershipMapper.toDto(membership);
    }

    public MembershipDto deactivateEntity(Long entityId) {
        var membership = membershipRepo.findById(entityId).orElseThrow(() -> new MembershipNotFoundException("Membership not found with ID: " + entityId));
        var user = authService.getCurrentUser();
        membership.setStatus(2);
        membership.setUpdatedBy(user);
        membershipRepo.save(membership);
        return membershipMapper.toDto(membership);
    }
}
