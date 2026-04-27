package com.osudpotro.posmaster.offerhub.offer;

import com.osudpotro.posmaster.common.DuplicateEntityException;
import com.osudpotro.posmaster.common.EntityNotFoundException;
import com.osudpotro.posmaster.offerhub.membership.*;
import com.osudpotro.posmaster.user.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
public class OfferService {
    @Autowired
    private AuthService authService;
    @Autowired
    private OfferRepository offerRepo;
    @Autowired
    private OfferMapper offerMapper;
    private String timeStartPrefix=" 24:00:00";
    private String timeEndPrefix=" 23:59:59";
    public List<OfferDto> getAllEntities() {
        return offerRepo.findAll()
                .stream()
                .map(offerMapper::toDto)
                .toList();
    }

    public Page<OfferDto> getAllEntities(OfferFilter filter, Pageable pageable) {
        return offerRepo.findAll(OfferSpecification.filter(filter), pageable).map(offerMapper::toDto);
    }

    public OfferDto getEntity(Long entityId) {
        var entity = offerRepo.findById(entityId).orElseThrow(() -> new EntityNotFoundException("Offer not found with ID: " + entityId));
        return offerMapper.toDto(entity);
    }

    public OfferDto createEntity(OfferCreateRequest request) {
        if (offerRepo.existsByName(request.getName())) {
            throw new DuplicateMembershipException();
        }
        var user = authService.getCurrentUser();
        Offer offer = new Offer();
        offer.setName(request.getName());
        offer.setOfferValue(request.getOfferValue());
        offer.setMaxOfferValue(request.getMaxOfferValue());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (request.getOfferStartDate() != null) {
            String offerStartDateInput = "";
            if (request.getOfferStartDate().length() > 10) {
                offerStartDateInput = request.getOfferStartDate().substring(0, 10) + timeStartPrefix;
            } else {
                offerStartDateInput = request.getOfferStartDate() + timeStartPrefix;
            }
            LocalDateTime offerStartDate = LocalDateTime.parse(offerStartDateInput, formatter);
            offer.setOfferStartDate(offerStartDate);
        } else {
            offer.setOfferStartDate(null);
        }
        if (request.getOfferEndDate() != null) {
            String offerEndDateInput = "";
            if (request.getOfferEndDate().length() > 10) {
                offerEndDateInput = request.getOfferEndDate().substring(0, 10) + timeEndPrefix;
            } else {
                offerEndDateInput = request.getOfferEndDate() + timeEndPrefix;
            }
            LocalDateTime offerEndDate = LocalDateTime.parse(offerEndDateInput, formatter);
            offer.setOfferEndDate(offerEndDate);
        } else {
            offer.setOfferEndDate(null);
        }
        offer.setCreatedBy(user);
        offerRepo.save(offer);
        return offerMapper.toDto(offer);
    }

    public OfferDto updateEntity(Long entityId, OfferUpdateRequest request) {
        var offer = offerRepo.findById(entityId).orElseThrow(EntityNotFoundException::new);
        if (!Objects.equals(offer.getName(), request.getName())) {
            if (offerRepo.existsByName(request.getName())) {
                throw new DuplicateEntityException();
            }
        }
        var user = authService.getCurrentUser();
        offer.setName(request.getName());
        offer.setOfferValue(request.getOfferValue());
        offer.setMaxOfferValue(request.getMaxOfferValue());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (request.getOfferStartDate() != null) {
            String offerStartDateInput = "";
            if (request.getOfferStartDate().length() > 10) {
                offerStartDateInput = request.getOfferStartDate().substring(0, 10) + timeStartPrefix;
            } else {
                offerStartDateInput = request.getOfferStartDate() + timeStartPrefix;
            }
            LocalDateTime offerStartDate = LocalDateTime.parse(offerStartDateInput, formatter);
            offer.setOfferStartDate(offerStartDate);
        } else {
            offer.setOfferStartDate(null);
        }
        if (request.getOfferEndDate() != null) {
            String offerEndDateInput = "";
            if (request.getOfferEndDate().length() > 10) {
                offerEndDateInput = request.getOfferEndDate().substring(0, 10) + timeEndPrefix;
            } else {
                offerEndDateInput = request.getOfferEndDate() + timeEndPrefix;
            }
            LocalDateTime offerEndDate = LocalDateTime.parse(offerEndDateInput, formatter);
            offer.setOfferEndDate(offerEndDate);
        } else {
            offer.setOfferEndDate(null);
        }
        offer.setUpdatedBy(user);
        offerRepo.save(offer);
        return offerMapper.toDto(offer);
    }

    public OfferDto deleteEntity(Long entityId) {
        var offer = offerRepo.findById(entityId).orElseThrow(() -> new EntityNotFoundException("Offer not found with ID: " + entityId));
        var user = authService.getCurrentUser();
        offer.setStatus(3);
        offer.setUpdatedBy(user);
        offerRepo.save(offer);
        return offerMapper.toDto(offer);
    }

    public int deleteBulkEntity(List<Long> entityIds) {
        return offerRepo.deleteBulkEntity(entityIds, 3L);
    }

    public OfferDto activateEntity(Long entityId) {
        var offer = offerRepo.findById(entityId).orElseThrow(() -> new EntityNotFoundException("Offer not found with ID: " + entityId));
        var user = authService.getCurrentUser();
        offer.setStatus(1);
        offer.setUpdatedBy(user);
        offerRepo.save(offer);
        return offerMapper.toDto(offer);
    }

    public OfferDto deactivateEntity(Long entityId) {
        var offer = offerRepo.findById(entityId).orElseThrow(() -> new EntityNotFoundException("Offer not found with ID: " + entityId));
        var user = authService.getCurrentUser();
        offer.setStatus(2);
        offer.setUpdatedBy(user);
        offerRepo.save(offer);
        return offerMapper.toDto(offer);
    }
}
