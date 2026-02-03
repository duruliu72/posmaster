package com.osudpotro.posmaster.purchase.requisition;

import com.osudpotro.posmaster.auth.AuthService;
import com.osudpotro.posmaster.branch.BranchNotFoundException;
import com.osudpotro.posmaster.branch.BranchRepository;
import com.osudpotro.posmaster.organization.OrganizationNotFoundException;
import com.osudpotro.posmaster.organization.OrganizationRepository;
import com.osudpotro.posmaster.product.ProductDetailNotFoundException;
import com.osudpotro.posmaster.product.ProductDetailRepository;
import com.osudpotro.posmaster.product.ProductNotFoundException;
import com.osudpotro.posmaster.product.ProductRepository;
import com.osudpotro.posmaster.purchase.PurchaseType;
import com.osudpotro.posmaster.requisition.*;
import com.osudpotro.posmaster.requisitiontype.RequisitionTypeNotFoundException;
import com.osudpotro.posmaster.requisitiontype.RequsitionTypeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class PurchaseRequisitionService {
    @Autowired
    private PurchaseRequisitionRepository purchaseRequisitionRepository;
    @Autowired
    private PurchaseRequisitionItemRepository purchaseRequisitionItemRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private PurchaseRequisitionMapper purchaseRequisitionMapper;
    @Autowired
    private PurchaseRequisitionItemMapper purchaseRequisitionItemMapper;
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductDetailRepository productDetailRepository;
    @Autowired
    private RequsitionTypeRepository requsitionTypeRepository;
    @Autowired
    private RequisitionRepository requisitionRepository;
    @Autowired
    private RequisitionApproverRepository requisitionApproverRepository;
    @Autowired
    private RequisitionOnPathRepository ropRepository;

    public List<PurchaseRequisitionDto> getAllPurchaseRequisitions() {
        return purchaseRequisitionRepository.findAll()
                .stream()
                .map(purchaseRequisitionMapper::toDto)
                .toList();
    }

    public Page<PurchaseRequisitionDto> getPurchaseRequisitions(PurchaseRequisitionFilter filter, Pageable pageable) {
        return purchaseRequisitionRepository.findAll(PurchaseRequisitionSpecification.filter(filter), pageable).map(purchaseRequisitionMapper::toDto);
    }

    public PurchaseRequisitionDto createPurchaseRequisition(PurchaseRequisitionCreateRequest request) {
        if (purchaseRequisitionRepository.existsByRequsitionRef(request.getRequsitionRef())) {
            throw new DuplicatePurchaseRequisitionException();
        }
        var organization = organizationRepository.findById(request.getOrganizationId()).orElse(null);
        if (organization == null) {
            throw new OrganizationNotFoundException();
        }
        var branch = branchRepository.findById(request.getBranchId()).orElse(null);
        if (branch == null) {
            throw new BranchNotFoundException();
        }
        var authUser = authService.getCurrentUser();
        PurchaseRequisition pr = new PurchaseRequisition();
        String requisitionRef = generateRequisitionRef();
        pr.setRequsitionRef(requisitionRef);
        pr.setPurchaseType(PurchaseType.fromCode(request.getPurchaseType()));

        pr.setOrganization(organization);
        pr.setBranch(branch);
        pr.setCreatedBy(authUser);
        pr = purchaseRequisitionRepository.save(pr);
        //Common Requsition Start Here
        if (pr.getRequisition() == null) {
            Requisition requisition = new Requisition();
            requisition.setPurchaseRequisition(pr);
            requisition.setRequsitionRef(pr.getRequsitionRef());
            String requsitionName = "";
            if (pr.getPurchaseType().getCode().equals("cp")) {
                requsitionName = "COMPANY_PURCHASE_REQUISITION";
            }
            if (pr.getPurchaseType().getCode().equals("lp")) {
                requsitionName = "LOCAL_PURCHASE_REQUISITION";
            }
            if (pr.getPurchaseType().getCode().equals("po")) {
                requsitionName = "PURCHASE_ORDER_REQUISITION";
            }
            if (pr.getPurchaseType().getCode().equals("procurement")) {
                requsitionName = "PROCUREMENT_REQUISITION";
            }
            var requisitionType = requsitionTypeRepository.findByRequisitionTypeKey(requsitionName).orElse(null);
            requisition.setRequisitionType(requisitionType);
            requisition.setReviewCount(1);
            requisition.setCreatedBy(authUser);
            requisition.setRequisitionStatus(1);
            if (request.getNote() != null) {
                requisition.setNote(request.getNote());
            }
            requisitionRepository.save(requisition);
            pr.setRequisition(requisition);
        }
        purchaseRequisitionRepository.save(pr);
        return purchaseRequisitionMapper.toDto(pr);
    }

    @Transactional
    public PurchaseRequisitionDto updatePurchaseRequisition(Long purchaseRequisitionId, PurchaseRequisitionUpdateRequest request) {
        var pr = purchaseRequisitionRepository.findById(purchaseRequisitionId).orElseThrow(PurchaseRequisitionNotFoundException::new);
        if (pr.getTotalItems() == 0) {
            throw new PurchaseRequisitionEmptyException();
        }
        if (request.getOrganizationId() != null) {
            var organization = organizationRepository.findById(request.getOrganizationId()).orElse(null);
            if (organization == null) {
                throw new OrganizationNotFoundException();
            }
            pr.setOrganization(organization);
        }
        if (request.getBranchId() != null) {
            var branch = branchRepository.findById(request.getBranchId()).orElse(null);
            if (branch == null) {
                throw new BranchNotFoundException();
            }
            pr.setBranch(branch);
        }
        if (request.getPurchaseType() != null) {
            pr.setPurchaseType(PurchaseType.fromCode(request.getPurchaseType()));
        }

        var authUser = authService.getCurrentUser();
        pr.setUpdatedBy(authUser);
        //Common Requsition Start Here
        String requsitionName = "";
        if (pr.getPurchaseType().getCode().equals("cp")) {
            requsitionName = "COMPANY_PURCHASE_REQUISITION";
        }
        if (pr.getPurchaseType().getCode().equals("lp")) {
            requsitionName = "LOCAL_PURCHASE_REQUISITION";
        }
        if (pr.getPurchaseType().getCode().equals("po")) {
            requsitionName = "PURCHASE_ORDER_REQUISITION";
        }
        if (pr.getPurchaseType().getCode().equals("procurement")) {
            requsitionName = "PROCUREMENT_REQUISITION";
        }
        var requisitionType = requsitionTypeRepository.findByRequisitionTypeKey(requsitionName).orElseThrow(RequisitionTypeNotFoundException::new);
        Requisition requisition = null;
        int requisitionStatus = 1;
        if (pr.getRequisition() != null) {
            requisitionStatus = pr.getRequisition().getRequisitionStatus();
            if (requisitionStatus != 1) {
                throw new RequisitionUpdateException();
            }
            requisition = requisitionRepository.findById(pr.getRequisition().getId()).orElse(null);
        }

        if (requisition != null) {
            requisition.setPurchaseRequisition(pr);
            requisition.setRequsitionRef(pr.getRequsitionRef());
            requisition.setRequisitionType(requisitionType);
            requisition.setReviewCount(1);
            if (request.getRequisitionStatus() != null) {
                requisition.setRequisitionStatus(request.getRequisitionStatus());
            }
            if (request.getNote() != null) {
                requisition.setNote(request.getNote());
            }
            requisition.setCreatedBy(authUser);
            requisitionRepository.save(requisition);
        } else {
            requisition = new Requisition();
            requisition.setPurchaseRequisition(pr);
            requisition.setRequsitionRef(pr.getRequsitionRef());
            requisition.setRequisitionType(requisitionType);
            requisition.setReviewCount(1);
            requisition.setCreatedBy(authUser);
            if (request.getRequisitionStatus() != null) {
                requisition.setRequisitionStatus(request.getRequisitionStatus());
            }
            if (request.getNote() != null) {
                requisition.setNote(request.getNote());
            }
            requisitionRepository.save(requisition);
        }
        pr.setRequisition(requisition);
        // requisition approver paths
        if (requisitionStatus == 1 && request.getRequisitionStatus() != null && request.getRequisitionStatus() == 2) {
            if (!ropRepository.existRequisitionOnPathByUser(authUser.getId(), pr.getRequisition().getId())) {
                var findApproverPrevNullUser = requisitionApproverRepository.findApproverWithNullPrevUser(requisitionType.getId()).orElseThrow(RequsitionOnPathNotFoundException::new);
                if (findApproverPrevNullUser != null) {
                    RequisitionOnPath rop = new RequisitionOnPath();
                    rop.setRequisition(pr.getRequisition());
                    rop.setReviewCount(pr.getRequisition().getReviewCount());
                    rop.setPrevUser(findApproverPrevNullUser.getPrevUser());
                    rop.setUser(findApproverPrevNullUser.getUser());
                    rop.setNextUser(findApproverPrevNullUser.getNextUser());
                    rop.setApprovedStatus(1);
                    rop.setCreatedBy(authUser);
                    ropRepository.save(rop);
                    requisition.getRequisitionOnPaths().add(rop);
                }
            }
        }
        purchaseRequisitionRepository.save(pr);
        return purchaseRequisitionMapper.toDto(pr);
    }

    @Transactional
    public PurchaseRequisitionDto updatePurchaseRequisitionInvoiceRef(Long purchaseRequisitionId, PurchaseRequisitionInvoiceRefRequest request) {
        var pr = purchaseRequisitionRepository.findById(purchaseRequisitionId).orElseThrow(PurchaseRequisitionNotFoundException::new);
        if (pr.getTotalItems() == 0) {
            throw new PurchaseRequisitionEmptyException();
        }
        var authUser = authService.getCurrentUser();
        pr.setUpdatedBy(authUser);
        if (request.getIsFinal() != null && request.getIsFinal()) {
            pr.setIsFinal(true);
        }
        pr.setPurchaseInvoices(request.getPurchaseInvoices());
        pr.setPurchaseInvoiceDocs(request.getPurchaseInvoiceDocs());
        pr.setOrderRefs(request.getOrderRefs());
        purchaseRequisitionRepository.save(pr);
        return purchaseRequisitionMapper.toDto(pr);
    }

    public PurchaseRequisitionDto getPurchaseRequisition(Long purchaseRequisitionId) {
        var pr = purchaseRequisitionRepository.findById(purchaseRequisitionId).orElseThrow(() -> new PurchaseRequisitionNotFoundException("PurchaseRequisition not found with ID: " + purchaseRequisitionId));
        List<PurchaseRequisitionItemDto> prilist = purchaseRequisitionItemRepository.findPurchaseRequisitionItemsList(purchaseRequisitionId).stream()
                .map(purchaseRequisitionItemMapper::toDto)
                .toList();
        var prDto=purchaseRequisitionMapper.toDto(pr);
        prDto.setItems(prilist);
        return prDto;
    }
    public PurchaseRequisitionReportDto findPurchaseRequisitionItemReportList(Long purchaseRequisitionId) {
        var pr = purchaseRequisitionRepository.findById(purchaseRequisitionId).orElseThrow(() -> new PurchaseRequisitionNotFoundException("PurchaseRequisition not found with ID: " + purchaseRequisitionId));
        List<PurchaseRequisitionItemReportDTO> prilist = purchaseRequisitionItemRepository.findPurchaseRequisitionItemReportList(purchaseRequisitionId);
        var prDto=purchaseRequisitionMapper.toReportDto(pr);
        prDto.setItems(prilist);
        return prDto;
    }
    public PurchaseRequisition getPurchaseRequisitionEntity(Long purchaseRequisitionId) {
        return purchaseRequisitionRepository.findById(purchaseRequisitionId).orElseThrow(() -> new PurchaseRequisitionNotFoundException("PurchaseRequisition not found with ID: " + purchaseRequisitionId));
    }

    public PurchaseRequisitionDto activePurchaseRequisition(Long purchaseRequisitionId) {
        var pr = purchaseRequisitionRepository.findById(purchaseRequisitionId).orElseThrow(() -> new PurchaseRequisitionNotFoundException("PurchaseRequisition not found with ID: " + purchaseRequisitionId));
        var authUser = authService.getCurrentUser();
        pr.setStatus(1);
        pr.setUpdatedBy(authUser);
        purchaseRequisitionRepository.save(pr);
        return purchaseRequisitionMapper.toDto(pr);
    }

    public PurchaseRequisitionDto deactivatePurchaseRequisition(Long purchaseRequisitionId) {
        var pr = purchaseRequisitionRepository.findById(purchaseRequisitionId).orElseThrow(() -> new PurchaseRequisitionNotFoundException("PurchaseRequisition not found with ID: " + purchaseRequisitionId));
        var authUser = authService.getCurrentUser();
        pr.setStatus(2);
        pr.setUpdatedBy(authUser);
        purchaseRequisitionRepository.save(pr);
        return purchaseRequisitionMapper.toDto(pr);
    }

    public PurchaseRequisitionDto deletePurchaseRequisition(Long purchaseRequisitionId) {
        var pr = purchaseRequisitionRepository.findById(purchaseRequisitionId).orElseThrow(() -> new PurchaseRequisitionNotFoundException("PurchaseRequisition not found with ID: " + purchaseRequisitionId));
        var authUser = authService.getCurrentUser();
        pr.setStatus(3);
        pr.setUpdatedBy(authUser);
        purchaseRequisitionRepository.save(pr);
        return purchaseRequisitionMapper.toDto(pr);
    }

    public int deleteBulkPurchaseRequisition(List<Long> purchaseRequisitionIds) {
        return purchaseRequisitionRepository.deleteBulkPurchaseRequisition(purchaseRequisitionIds, 3L);
    }

    //    For Purchase Requisition Item
    public PurchaseRequisitionWithItemPageResponse getPurchaseRequisitionWithItemPagination(Long purchaseRequisitionId, Pageable pageable, PurchaseRequisitionItemFilter filter) {
        PurchaseRequisition pr = purchaseRequisitionRepository.findPurchaseRequisitionById(purchaseRequisitionId).orElseThrow(PurchaseRequisitionNotFoundException::new);
        Page<PurchaseRequisitionItemDto> result = purchaseRequisitionItemRepository.findPurchaseRequisitionItems(purchaseRequisitionId, filter.getName(), pageable).map(purchaseRequisitionItemMapper::toDto);
        return purchaseRequisitionMapper.toMinDto(pr, result);
    }

    public PurchaseRequisitionItemDto addPurchaseRequisitionItem(Long purchaseRequisitionId, PurchaseRequisitionItemAddRequest request) {
        PurchaseRequisition pr = purchaseRequisitionRepository.findById(purchaseRequisitionId).orElse(null);
        if (pr == null) {
            throw new PurchaseRequisitionNotFoundException();
        }
        var product = productRepository.findById(request.getProductId()).orElse(null);
        if (product == null) {
            throw new ProductNotFoundException();
        }
        var productDetail = productDetailRepository.findById(request.getProductDetailId()).orElse(null);
        if (productDetail == null) {
            throw new ProductDetailNotFoundException();
        }
        var findPurchaseRequisitionItem = purchaseRequisitionItemRepository.findPurchaseRequisitionItem(purchaseRequisitionId, request.getProductId(), request.getProductDetailId()).orElse(null);
        if (findPurchaseRequisitionItem != null) {
            throw new PurchaseRequisitionItemDuplicateException();
        }
        PurchaseRequisitionItem prItem = new PurchaseRequisitionItem();
        prItem.setPurchaseRequisition(pr);
        prItem.setProduct(product);
        prItem.setProductDetail(productDetail);
        prItem.setPurchaseProductUnit(product.getPurchaseProductUnit());
        if (request.getGiftQty() != null) {
            prItem.setGiftQty(request.getGiftQty());
        }
        prItem.setPurchaseQty(request.getPurchaseQty());
        prItem.setActualQty(request.getPurchaseQty());
        if (request.getPurchasePrice() != null) {
            prItem.setPurchasePrice(request.getPurchasePrice());
        }
        if (request.getPurchasePrice() != null) {
            if (!Objects.equals(request.getPurchasePrice(), productDetail.getPurchasePrice())) {
                productDetail.setPurchasePrice(request.getPurchasePrice());
                productDetailRepository.save(productDetail);
            }
            prItem.setPurchasePrice(request.getPurchasePrice());
        }
        purchaseRequisitionItemRepository.save(prItem);
        return purchaseRequisitionItemMapper.toDto(prItem);
    }

    @Transactional
    public PurchaseRequisitionItemDto updatePurchaseRequisitionItem(Long purchaseRequisitionId, Long purchaseRequisitionItemId, PurchaseRequisitionItemUpdateRequest request) {
        PurchaseRequisition pr = purchaseRequisitionRepository.findById(purchaseRequisitionId).orElse(null);
        if (pr == null) {
            throw new PurchaseRequisitionNotFoundException();
        }
        PurchaseRequisitionItem prItem = purchaseRequisitionItemRepository.findById(purchaseRequisitionItemId).orElse(null);
        if (prItem == null) {
            throw new PurchaseRequisitionItemNotFoundException();
        }
        prItem.setPurchaseProductUnit(prItem.getProduct().getPurchaseProductUnit());
        if (pr.getRequisition().getRequisitionStatus() == 3) {
            if (request.getActualQty() != null) {
                prItem.setActualQty(request.getActualQty());
            }
            if (request.getGiftQty() != null) {
                prItem.setGiftQty(request.getGiftQty());
            }
            if (request.getAddableStatus() != null) {
                prItem.setAddableStatus(request.getAddableStatus());
            }
        }
        if (request.getPurchaseQty() != null) {
            prItem.setPurchaseQty(request.getPurchaseQty());
        }
        if (request.getPurchasePrice() != null) {
            if (!Objects.equals(request.getPurchasePrice(), prItem.getProductDetail().getPurchasePrice())) {
                var productDetail = prItem.getProductDetail();
                productDetail.setPurchasePrice(request.getPurchasePrice());
                productDetailRepository.save(productDetail);
            }
            prItem.setPurchasePrice(request.getPurchasePrice());
        }
        purchaseRequisitionItemRepository.save(prItem);
        return purchaseRequisitionItemMapper.toDto(prItem);
    }

    public PurchaseRequisitionItemDto removePurchaseRequisitionItem(Long purchaseRequisitionId, Long purchaseRequisitionItemId) {
        PurchaseRequisition purchaseRequisition = purchaseRequisitionRepository.findById(purchaseRequisitionId).orElse(null);
        if (purchaseRequisition == null) {
            throw new PurchaseRequisitionNotFoundException();
        }
        PurchaseRequisitionItem purchaseRequisitionItem = purchaseRequisitionItemRepository.findById(purchaseRequisitionItemId).orElse(null);
        if (purchaseRequisitionItem == null) {
            throw new PurchaseRequisitionItemNotFoundException();
        }
        purchaseRequisition.getItems().remove(purchaseRequisitionItem);
        purchaseRequisitionItem.setPurchaseRequisition(null);
        purchaseRequisitionRepository.save(purchaseRequisition);
        return purchaseRequisitionItemMapper.toDto(purchaseRequisitionItem);
    }

    public int removeBulkPurchaseRequisitionItem(Long purchaseRequisitionId, List<Long> purchaseRequisitionItemIds) {
        return purchaseRequisitionItemRepository.removeBulkPurchaseRequisitionItem(purchaseRequisitionId, purchaseRequisitionItemIds);
    }

    public int updateBulkForAddableItem(Long purchaseRequisitionId, List<Long> purchaseRequisitionItemIds, Integer addableStatus) {

        return purchaseRequisitionItemRepository.updateBulkForAddableItem(purchaseRequisitionId, purchaseRequisitionItemIds, addableStatus);
    }

    private String generateRequisitionRef() {
        PurchaseRequisition pr = purchaseRequisitionRepository.findTopByOrderByCreatedAtDesc();
        String prefix = "OSPRE";
        // Extract sequence number from last code
        long nextSeq = 1;
        if (pr != null && pr.getRequsitionRef() != null) {
            String lastItemRef = pr.getRequsitionRef();
            String lastPart = lastItemRef.length() > 5 ? lastItemRef.substring(5) : lastItemRef;
            if (!lastPart.isEmpty()) {
                try {
                    nextSeq = Long.parseLong(lastPart) + 1;
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
        //Format code
        return String.format("%s%06d", prefix, nextSeq);
    }
}
