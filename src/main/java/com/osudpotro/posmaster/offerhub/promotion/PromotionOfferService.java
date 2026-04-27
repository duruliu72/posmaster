package com.osudpotro.posmaster.offerhub.promotion;

import com.osudpotro.posmaster.common.DuplicateEntityException;
import com.osudpotro.posmaster.common.EntityNotFoundException;
import com.osudpotro.posmaster.offerhub.membership.DuplicateMembershipException;
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
public class PromotionOfferService {
    @Autowired
    private AuthService authService;
    @Autowired
    private PromotionOfferRepository promoOfferRepo;
    @Autowired
    private PromotionOfferMapper promoOfferMapper;
    private String timeStartPrefix=" 24:00:00";
    private String timeEndPrefix=" 23:59:59";
    public List<PromotionOfferDto> getAllEntities() {
        return promoOfferRepo.findAll()
                .stream()
                .map(promoOfferMapper::toDto)
                .toList();
    }

    public Page<PromotionOfferDto> getAllEntities(PromotionOfferFilter filter, Pageable pageable) {
        return promoOfferRepo.findAll(PromotionOfferSpecification.filter(filter), pageable).map(promoOfferMapper::toDto);
    }

    public PromotionOfferDto getEntity(Long entityId) {
        var entity = promoOfferRepo.findById(entityId).orElseThrow(() -> new EntityNotFoundException("Promotion Offer not found with ID: " + entityId));
        return promoOfferMapper.toDto(entity);
    }

    public PromotionOfferDto createEntity(PromotionOfferCreateRequest request) {
        if (promoOfferRepo.existsByName(request.getName())) {
            throw new DuplicateMembershipException();
        }
        var user = authService.getCurrentUser();
        PromotionOffer promotionOffer = new PromotionOffer();
        promotionOffer.setName(request.getName());
        promotionOffer.setDesc(request.getDesc());
        promotionOffer.setAlias(request.getAlias());
        promotionOffer.setPromotionValue(request.getPromotionValue());
        promotionOffer.setMinOrderValue(request.getMinOrderValue());
        promotionOffer.setMaxCount(request.getMaxCount());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (request.getPromoStartDate() != null) {
            String promoStartDateInput = "";
            if (request.getPromoStartDate().length() > 10) {
                promoStartDateInput = request.getPromoStartDate().substring(0, 10) + timeStartPrefix;
            } else {
                promoStartDateInput = request.getPromoStartDate() + timeStartPrefix;
            }
            LocalDateTime promoStartDate = LocalDateTime.parse(promoStartDateInput, formatter);
            promotionOffer.setPromoStartDate(promoStartDate);
        } else {
            promotionOffer.setPromoEndDate(null);
        }
        if (request.getPromoEndDate() != null) {
            String promoEndDateInput = "";
            if (request.getPromoEndDate().length() > 10) {
                promoEndDateInput = request.getPromoEndDate().substring(0, 10) + timeEndPrefix;
            } else {
                promoEndDateInput = request.getPromoEndDate() + timeEndPrefix;
            }
            LocalDateTime promoEndDate = LocalDateTime.parse(promoEndDateInput, formatter);
            promotionOffer.setPromoEndDate(promoEndDate);
        } else {
            promotionOffer.setPromoEndDate(null);
        }
        promotionOffer.setCreatedBy(user);
        promoOfferRepo.save(promotionOffer);
        return promoOfferMapper.toDto(promotionOffer);
    }

    public PromotionOfferDto updateEntity(Long entityId, PromotionOfferUpdateRequest request) {
        var promotionOffer = promoOfferRepo.findById(entityId).orElseThrow(EntityNotFoundException::new);
        if (!Objects.equals(promotionOffer.getName(), request.getName())) {
            if (promoOfferRepo.existsByName(request.getName())) {
                throw new DuplicateEntityException();
            }
        }
        var user = authService.getCurrentUser();
        promotionOffer.setName(request.getName());
        promotionOffer.setDesc(request.getDesc());
        promotionOffer.setAlias(request.getAlias());
        promotionOffer.setPromotionValue(request.getPromotionValue());
        promotionOffer.setMinOrderValue(request.getMinOrderValue());
        promotionOffer.setMaxCount(request.getMaxCount());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (request.getPromoStartDate() != null) {
            String promoStartDateInput = "";
            if (request.getPromoStartDate().length() > 10) {
                promoStartDateInput = request.getPromoStartDate().substring(0, 10) + timeStartPrefix;
            } else {
                promoStartDateInput = request.getPromoStartDate() + timeStartPrefix;
            }
            LocalDateTime promoStartDate = LocalDateTime.parse(promoStartDateInput, formatter);
            promotionOffer.setPromoStartDate(promoStartDate);
        } else {
            promotionOffer.setPromoEndDate(null);
        }
        if (request.getPromoEndDate() != null) {
            String promoEndDateInput = "";
            if (request.getPromoEndDate().length() > 10) {
                promoEndDateInput = request.getPromoEndDate().substring(0, 10) + timeEndPrefix;
            } else {
                promoEndDateInput = request.getPromoEndDate() + timeEndPrefix;
            }
            LocalDateTime promoEndDate = LocalDateTime.parse(promoEndDateInput, formatter);
            promotionOffer.setPromoEndDate(promoEndDate);
        } else {
            promotionOffer.setPromoEndDate(null);
        }
        promotionOffer.setUpdatedBy(user);
        promoOfferRepo.save(promotionOffer);
        return promoOfferMapper.toDto(promotionOffer);
    }

    public PromotionOfferDto deleteEntity(Long entityId) {
        var promotionOffer = promoOfferRepo.findById(entityId).orElseThrow(() -> new EntityNotFoundException("Promotion not found with ID: " + entityId));
        var user = authService.getCurrentUser();
        promotionOffer.setStatus(3);
        promotionOffer.setUpdatedBy(user);
        promoOfferRepo.save(promotionOffer);
        return promoOfferMapper.toDto(promotionOffer);
    }

    public int deleteBulkEntity(List<Long> entityIds) {
        return promoOfferRepo.deleteBulkEntity(entityIds, 3L);
    }

    public PromotionOfferDto activateEntity(Long entityId) {
        var promotionOffer = promoOfferRepo.findById(entityId).orElseThrow(() -> new EntityNotFoundException("Promotion not found with ID: " + entityId));
        var user = authService.getCurrentUser();
        promotionOffer.setStatus(1);
        promotionOffer.setUpdatedBy(user);
        promoOfferRepo.save(promotionOffer);
        return promoOfferMapper.toDto(promotionOffer);
    }

    public PromotionOfferDto deactivateEntity(Long entityId) {
        var promotionOffer = promoOfferRepo.findById(entityId).orElseThrow(() -> new EntityNotFoundException("Promotion not found with ID: " + entityId));
        var user = authService.getCurrentUser();
        promotionOffer.setStatus(2);
        promotionOffer.setUpdatedBy(user);
        promoOfferRepo.save(promotionOffer);
        return promoOfferMapper.toDto(promotionOffer);
    }
}
