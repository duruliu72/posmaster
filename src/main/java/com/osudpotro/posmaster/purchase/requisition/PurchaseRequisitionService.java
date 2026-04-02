package com.osudpotro.posmaster.purchase.requisition;

import com.osudpotro.posmaster.branch.Branch;
import com.osudpotro.posmaster.purchase.checked.CheckedPurchaseRequisition;
import com.osudpotro.posmaster.purchase.checked.CheckedPurchaseRequisitionItem;
import com.osudpotro.posmaster.purchase.checked.CheckedPurchaseRequisitionItemRepository;
import com.osudpotro.posmaster.purchase.checked.CheckedPurchaseRequisitionRepository;
import com.osudpotro.posmaster.purchase.transfer.*;
import com.osudpotro.posmaster.tms.driver.DriverRepository;
import com.osudpotro.posmaster.tms.goodsontrip.GoodsOnTrip;
import com.osudpotro.posmaster.tms.goodsontrip.GoodsOnTripRepository;
import com.osudpotro.posmaster.tms.vechile.VehicleRepository;
import com.osudpotro.posmaster.tms.vehicletrip.VehicleTrip;
import com.osudpotro.posmaster.tms.vehicletrip.VehicleTripRepository;
import com.osudpotro.posmaster.user.auth.AuthService;
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

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PurchaseRequisitionService {
    @Autowired
    private PurchaseRequisitionRepository prRepo;
    @Autowired
    private PurchaseRequisitionItemRepository priRepostory;
    @Autowired
    private CheckedPurchaseRequisitionRepository cprRepo;
    @Autowired
    private CheckedPurchaseRequisitionItemRepository cprItemRepo;
    @Autowired
    private PurchaseRequisitionTransferRepository prTransferRepo;
    @Autowired
    private PurchaseRequisitionItemTransferRepository prItemTransferRepo;
    @Autowired
    private RequisitionRepository requisitionRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private PurchaseRequisitionMapper purchaseRequisitionMapper;
    @Autowired
    private PurchaseRequisitionItemMapper priMapper;
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
    private RequisitionApproverRepository requisitionApproverRepository;
    @Autowired
    private RequisitionOnPathRepository ropRepository;
    @Autowired
    private VehicleTripRepository vehicleTripRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private GoodsOnTripRepository goodsOnTripRepository;

    public List<PurchaseRequisitionDto> getAllPurchaseRequisitions() {
        return prRepo.findAll()
                .stream()
                .map(purchaseRequisitionMapper::toDto)
                .toList();
    }

    public Page<PurchaseRequisitionDto> filterPrEntities(PurchaseRequisitionFilter filter, Pageable pageable) {
        var authUser = authService.getCurrentUser();
        return prRepo.findAll(PurchaseRequisitionSpecification.filter(filter, authUser), pageable).map(purchaseRequisitionMapper::toDto);
    }

    @Transactional
    public PurchaseRequisitionDto createPurchaseRequisition(PurchaseRequisitionCreateRequest request) {
        var authUser = authService.getCurrentUser();
        String requisitionRef = generateRequisitionRef();
        if (prRepo.existsByRequsitionRef(requisitionRef)) {
            throw new DuplicatePurchaseRequisitionException();
        }
        Branch branch = branchRepository.findById(authUser.getBranch().getId()).orElseThrow(BranchNotFoundException::new);
        var organization = organizationRepository.findById(branch.getOrganization().getId()).orElse(null);
        if (organization == null) {
            throw new OrganizationNotFoundException();
        }
        PurchaseRequisition pr = new PurchaseRequisition();
        pr.setRequsitionRef(requisitionRef);
        pr.setPurchaseType(PurchaseType.fromCode(request.getPurchaseType()));
        pr.setOrganization(organization);
        pr.setBranch(branch);
        pr.setCreatedBy(authUser);
        pr = prRepo.save(pr);
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
        prRepo.save(pr);
        return purchaseRequisitionMapper.toDto(pr);
    }

    @Transactional
    public PurchaseRequisitionDto updatePurchaseRequisition(Long purchaseRequisitionId, PurchaseRequisitionUpdateRequest request) {
        PurchaseRequisition pr = prRepo.findById(purchaseRequisitionId).orElseThrow(PurchaseRequisitionNotFoundException::new);
        if (pr.getTotalItems() == 0) {
            throw new PurchaseRequisitionEmptyException();
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
        int requisitionStatus = 0;
        Set<Integer> checkRequisitionStatus = Set.of(1, 5);
        if (pr.getRequisition() != null) {
            requisitionStatus = pr.getRequisition().getRequisitionStatus();
            if (!checkRequisitionStatus.contains(requisitionStatus)) {
                throw new RequisitionUpdateException("Already Processed! ");
            }
            requisition = requisitionRepository.findById(pr.getRequisition().getId()).orElse(null);
        } else {
            requisitionStatus = 1;
        }
        if (requisition != null) {
            requisition.setPurchaseRequisition(pr);
            requisition.setRequsitionRef(pr.getRequsitionRef());
            requisition.setRequisitionType(requisitionType);
            if (requisition.getReviewCount() == 1) {
                requisition.setReviewCount(1);
            }
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
        if (checkRequisitionStatus.contains(requisitionStatus) && request.getRequisitionStatus() != null && request.getRequisitionStatus() == 2) {
            if (!ropRepository.existRequisitionOnPathByUser(authUser.getId(), pr.getRequisition().getId())) {
                var findApproverPrevNullUser = requisitionApproverRepository.findApproverWithNullPrevUser(requisitionType.getId()).orElseThrow(RequsitionOnPathNotFoundException::new);
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
        prRepo.save(pr);
        return purchaseRequisitionMapper.toDto(pr);
    }

    @Transactional
    public PurchaseRequisitionDto updatePurchaseRequisitionInvoiceRef(Long purchaseRequisitionId, PurchaseRequisitionInvoiceRefRequest request) {
        var pr = prRepo.findById(purchaseRequisitionId).orElseThrow(PurchaseRequisitionNotFoundException::new);
        if (pr.getRequisition().getRequisitionStatus() != 3) {
            throw new PurchaseRequisitionException("Purchase Requisition is not approved yet!");
        }
        if (pr.getIsFinal()) {
            throw new PurchaseRequisitionException("Purchase Requisition is not possible to processed further");
        }
        if (pr.getTotalItems() == 0) {
            throw new PurchaseRequisitionEmptyException();
        }
        boolean hasInvoice = false;
        if (request.getPurchaseInvoices() != null) {
            String[] reqInvoices = request.getPurchaseInvoices().split(",");
            for (String invoice : reqInvoices) {
                if (!hasInvoice) {
                    var findPr = prRepo.findPurchaseRequisitionByInvoice(purchaseRequisitionId, invoice).orElse(null);
                    if (findPr != null && findPr.getPurchaseInvoices() != null) {
                        String invoices = findPr.getPurchaseInvoices();
                        if (!invoices.isEmpty()) {
                            invoices = invoices + "," + findPr.getPurchaseInvoices();
                        } else {
                            invoices = findPr.getPurchaseInvoices();
                        }
                        String[] prevInvoices = invoices.split(",");
                        if (Arrays.asList(prevInvoices).contains(invoice)) {
                            hasInvoice = true;
                        }
                    }
                }
            }
            if (hasInvoice) {
                throw new DuplicatePurchaseRequisitionException("Duplicate Purchase Invoice Found");
            }
        }
//        Check here IsAdmin Checked or not in CheckedPurchaseRequisition
        var cpr = cprRepo.findCheckPurchaseRequisitionByPrIDAndCheckedStatus(purchaseRequisitionId, 1).orElse(null);
        if (cpr != null) {
            boolean hasTempInvoice = false;
            if (request.getPurchaseInvoices() != null) {
                String[] reqInvoices = request.getPurchaseInvoices().split(",");
                for (String invoice : reqInvoices) {
                    if (!hasTempInvoice) {
                        var findFinalPr = cprRepo.findCheckedPurchaseRequisitionByInvoiceExceptprId(purchaseRequisitionId, invoice).orElse(null);
                        if (findFinalPr != null) {
                            hasTempInvoice = true;
                        }
                    }
                }
            }
            if (hasTempInvoice) {
                throw new DuplicatePurchaseRequisitionException("Duplicate Purchase Invoice Found that you have already send! ");
            }
        }
        var authUser = authService.getCurrentUser();
        pr.setUpdatedBy(authUser);
        if (request.getIsFinal() != null && request.getIsFinal()) {
            pr.setIsFinal(true);
        }
        if (cpr == null) {
            cpr = new CheckedPurchaseRequisition();
            cpr.setCheckByBranchMan(authUser);
            cpr.setCheckByBranchAt(LocalDateTime.now());
        }
        pr.setPurchaseInvoices(request.getPurchaseInvoices());
        pr.setPurchaseInvoiceDocs(request.getPurchaseInvoiceDocs());
        pr.setOrderRefs(request.getOrderRefs());
        pr.setOverallDiscount(request.getOverallDiscount());
        cpr.setRequsitionRef(pr.getRequsitionRef());
        cpr.setOverallDiscount(request.getOverallDiscount());
        cpr.setPurchaseInvoices(request.getPurchaseInvoices());
        cpr.setPurchaseInvoiceDocs(request.getPurchaseInvoiceDocs());
        cpr.setOrderRefs(request.getOrderRefs());
        cpr.setPurchaseRequisition(pr);
        cprRepo.save(cpr);
        prRepo.save(pr);
        return purchaseRequisitionMapper.toDto(pr);
    }

    public PurchaseRequisitionDto getPurchaseRequisition(Long purchaseRequisitionId) {
        var pr = prRepo.findById(purchaseRequisitionId).orElseThrow(() -> new PurchaseRequisitionNotFoundException("PurchaseRequisition not found with ID: " + purchaseRequisitionId));
        List<PurchaseRequisitionItemDto> prilist = priRepostory.findPurchaseRequisitionItemsList(purchaseRequisitionId).stream()
                .map(priMapper::toDto)
                .toList();
        var prDto = purchaseRequisitionMapper.toDto(pr);
        prDto.setItems(prilist);
        return prDto;
    }

    public PurchaseRequisitionReportDto findPurchaseRequisitionItemReportList(Long purchaseRequisitionId) {
        var pr = prRepo.findById(purchaseRequisitionId).orElseThrow(() -> new PurchaseRequisitionNotFoundException("PurchaseRequisition not found with ID: " + purchaseRequisitionId));
        List<PurchaseRequisitionItemReportDTO> prilist = priRepostory.findPurchaseRequisitionItemReportList(purchaseRequisitionId);
        var prDto = purchaseRequisitionMapper.toReportDto(pr);
        prDto.setItems(prilist);
        return prDto;
    }

    public PurchaseRequisition getPurchaseRequisitionEntity(Long purchaseRequisitionId) {
        return prRepo.findById(purchaseRequisitionId).orElseThrow(() -> new PurchaseRequisitionNotFoundException("PurchaseRequisition not found with ID: " + purchaseRequisitionId));
    }

    public PurchaseRequisitionDto activePurchaseRequisition(Long purchaseRequisitionId) {
        var pr = prRepo.findById(purchaseRequisitionId).orElseThrow(() -> new PurchaseRequisitionNotFoundException("PurchaseRequisition not found with ID: " + purchaseRequisitionId));
        var authUser = authService.getCurrentUser();
        pr.setStatus(1);
        pr.setUpdatedBy(authUser);
        prRepo.save(pr);
        return purchaseRequisitionMapper.toDto(pr);
    }

    public PurchaseRequisitionDto deactivatePurchaseRequisition(Long purchaseRequisitionId) {
        var pr = prRepo.findById(purchaseRequisitionId).orElseThrow(() -> new PurchaseRequisitionNotFoundException("PurchaseRequisition not found with ID: " + purchaseRequisitionId));
        var authUser = authService.getCurrentUser();
        pr.setStatus(2);
        pr.setUpdatedBy(authUser);
        prRepo.save(pr);
        return purchaseRequisitionMapper.toDto(pr);
    }

    public PurchaseRequisitionDto deletePurchaseRequisition(Long purchaseRequisitionId) {
        var pr = prRepo.findById(purchaseRequisitionId).orElseThrow(() -> new PurchaseRequisitionNotFoundException("PurchaseRequisition not found with ID: " + purchaseRequisitionId));
        var authUser = authService.getCurrentUser();
        pr.setStatus(3);
        pr.setUpdatedBy(authUser);
        prRepo.save(pr);
        return purchaseRequisitionMapper.toDto(pr);
    }

    public int deleteBulkPurchaseRequisition(List<Long> purchaseRequisitionIds) {
        return prRepo.deleteBulkPurchaseRequisition(purchaseRequisitionIds, 3L);
    }

    //    For Purchase Requisition Item
    public PurchaseRequisitionWithItemPageResponse filterWithItemPagination(Long purchaseRequisitionId, Pageable pageable, PurchaseRequisitionItemFilter filter) {
        PurchaseRequisition pr = prRepo.findPurchaseRequisitionById(purchaseRequisitionId).orElseThrow(PurchaseRequisitionNotFoundException::new);
        Page<PurchaseRequisitionItemDto> result = priRepostory.findPurchaseRequisitionItems(purchaseRequisitionId, filter.getName(), pageable).map(priMapper::toDto);
        return purchaseRequisitionMapper.toMinDto(pr, result);
    }

    //    For Purchase Requisition Item
    public PurchaseRequisitionWithItemPageResponse filterAddablePurchaseRequisitionItems(Long purchaseRequisitionId, Pageable pageable, PurchaseRequisitionItemFilter filter) {
        PurchaseRequisition pr = prRepo.findPurchaseRequisitionById(purchaseRequisitionId).orElseThrow(PurchaseRequisitionNotFoundException::new);
        Page<PurchaseRequisitionItem> resultBase = priRepostory.filterAddablePurchaseRequisitionItems(purchaseRequisitionId, filter.getName(), pageable);
        Page<PurchaseRequisitionItemDto> result = resultBase.map(priMapper::toDto);
        pr.setItems(resultBase.getContent());
        return purchaseRequisitionMapper.toMinDto(pr, result);
    }

    @Transactional
    public PurchaseRequisitionItemDto addPurchaseRequisitionItem(Long purchaseRequisitionId, PurchaseRequisitionItemAddRequest request) {
        PurchaseRequisition pr = prRepo.findById(purchaseRequisitionId).orElse(null);
        if (pr == null) {
            throw new PurchaseRequisitionNotFoundException();
        }
        if (pr.getIsFinal()) {
            throw new PurchaseRequisitionException("Purchase Requisition is not possible to processed further");
        }
        var product = productRepository.findById(request.getProductId()).orElse(null);
        if (product == null) {
            throw new ProductNotFoundException();
        }
        var productDetail = productDetailRepository.findById(request.getProductDetailId()).orElse(null);
        if (productDetail == null) {
            throw new ProductDetailNotFoundException();
        }
        var findPurchaseRequisitionItem = priRepostory.findPurchaseRequisitionItem(purchaseRequisitionId, request.getProductId(), request.getProductDetailId()).orElse(null);
        if (findPurchaseRequisitionItem != null) {
            throw new PurchaseRequisitionItemDuplicateException();
        }
        PurchaseRequisitionItem prItem = new PurchaseRequisitionItem();
        prItem.setPurchaseRequisition(pr);
        prItem.setProduct(product);
        prItem.setProductDetail(productDetail);
        prItem.setPurchaseProductUnit(product.getPurchaseProductUnit());
        prItem.setPurchaseQty(request.getPurchaseQty());
        prItem.setActualQty(request.getPurchaseQty());
        prItem.setGiftOrBonusQty(request.getGiftOrBonusQty());
        prItem.setPurchasePrice(request.getPurchasePrice());
        if (request.getPurchasePrice() != null && request.getMrpPrice() != null) {
            prItem.setDiscount(request.getMrpPrice().subtract(request.getPurchasePrice()));
        }
        prItem.setMrpPrice(request.getMrpPrice());
        if (prItem.getProductDetail() != null && request.getPurchasePrice() == null && request.getMrpPrice() == null) {
            prItem.setPurchasePrice(prItem.getProductDetail().getPurchasePrice());
            prItem.setMrpPrice(prItem.getProductDetail().getMrpPrice());
            if (prItem.getProductDetail().getPurchasePrice() != null && prItem.getProductDetail().getMrpPrice() != null) {
                prItem.setDiscount(prItem.getProductDetail().getMrpPrice()
                        .subtract(prItem.getProductDetail().getPurchasePrice()));
            }
        }
        priRepostory.save(prItem);
        return priMapper.toDto(prItem);
    }

    public PurchaseRequisitionItemDto updatePurchaseRequisitionItem(Long purchaseRequisitionId, Long purchaseRequisitionItemId, PurchaseRequisitionItemUpdateRequest request) {
        PurchaseRequisition pr = prRepo.findById(purchaseRequisitionId).orElse(null);
        if (pr == null) {
            throw new PurchaseRequisitionNotFoundException();
        }
        PurchaseRequisitionItem prItem = priRepostory.findById(purchaseRequisitionItemId).orElse(null);
        if (prItem == null) {
            throw new PurchaseRequisitionItemNotFoundException();
        }
        var product = productRepository.findById(prItem.getProduct().getId()).orElse(null);
        if (product == null) {
            throw new ProductNotFoundException();
        }
        var authUser = authService.getCurrentUser();
        if (request.getPurchaseQty() != null) {
            prItem.setPurchaseQty(request.getPurchaseQty());
            prItem.setActualQty(request.getPurchaseQty());
        }
        if (prItem.getProductDetail() != null) {
            prItem.setPurchasePrice(prItem.getProductDetail().getPurchasePrice());
            prItem.setMrpPrice(prItem.getProductDetail().getMrpPrice());
            if (prItem.getProductDetail().getPurchasePrice() != null && prItem.getProductDetail().getMrpPrice() != null) {
                prItem.setDiscount(prItem.getProductDetail().getMrpPrice()
                        .subtract(prItem.getProductDetail().getPurchasePrice()));
            }
        }
        prItem.setPurchaseProductUnit(product.getPurchaseProductUnit());
        priRepostory.save(prItem);
        return priMapper.toDto(prItem);
    }

    public PurchaseRequisitionItemDto CheckInvoiceAndUpdatePurchaseRequisitionItem(Long purchaseRequisitionId, Long purchaseRequisitionItemId, PurchaseRequisitionItemUpdateRequest request) {
        PurchaseRequisition pr = prRepo.findById(purchaseRequisitionId).orElse(null);
        if (pr == null) {
            throw new PurchaseRequisitionNotFoundException();
        }
        PurchaseRequisitionItem prItem = priRepostory.findById(purchaseRequisitionItemId).orElse(null);
        if (prItem == null) {
            throw new PurchaseRequisitionItemNotFoundException();
        }
        if (pr.getRequisition().getRequisitionStatus() != 3) {
            throw new RequisitionItemNotApprovedException();
        }
        if (pr.getRequisition().getRequisitionStatus() == 3) {
            if (request.getActualQty() != null) {
                prItem.setActualQty(request.getActualQty());
            }
            if (request.getGiftOrBonusQty() != null) {
                prItem.setGiftOrBonusQty(request.getGiftOrBonusQty());
            }
            if (request.getAddableStatus() != null) {
                prItem.setAddableStatus(request.getAddableStatus());
            }
        }
        if (request.getPurchasePrice() != null && request.getMrpPrice() != null) {
            var productDetail = prItem.getProductDetail();
            if (!Objects.equals(request.getPurchasePrice(), prItem.getPurchasePrice())) {
                productDetail.setPurchasePrice(request.getPurchasePrice());
            }
            if (!Objects.equals(request.getMrpPrice(), prItem.getMrpPrice())) {
                productDetail.setPurchasePrice(request.getPurchasePrice());
            }
            productDetailRepository.save(productDetail);
        }
        if (request.getPurchasePrice() != null && request.getMrpPrice() != null) {
            prItem.setDiscount(request.getMrpPrice().subtract(request.getPurchasePrice()));
        }

        prItem.setPurchasePrice(request.getPurchasePrice());
        prItem.setMrpPrice(request.getMrpPrice());
        priRepostory.save(prItem);
        return priMapper.toDto(prItem);
    }

    public PurchaseRequisitionItemDto removePurchaseRequisitionItem(Long purchaseRequisitionId, Long purchaseRequisitionItemId) {
        PurchaseRequisition purchaseRequisition = prRepo.findById(purchaseRequisitionId).orElse(null);
        if (purchaseRequisition == null) {
            throw new PurchaseRequisitionNotFoundException();
        }
        PurchaseRequisitionItem purchaseRequisitionItem = priRepostory.findById(purchaseRequisitionItemId).orElse(null);
        if (purchaseRequisitionItem == null) {
            throw new PurchaseRequisitionItemNotFoundException();
        }
        purchaseRequisition.getItems().remove(purchaseRequisitionItem);
        purchaseRequisitionItem.setPurchaseRequisition(null);
        prRepo.save(purchaseRequisition);
        return priMapper.toDto(purchaseRequisitionItem);
    }

    public int removeBulkPurchaseRequisitionItem(Long purchaseRequisitionId, List<Long> purchaseRequisitionItemIds) {
        return priRepostory.removeBulkPurchaseRequisitionItem(purchaseRequisitionId, purchaseRequisitionItemIds);
    }

    @Transactional
    public int updateBulkForAddableItem(Long purchaseRequisitionId, List<Long> purchaseRequisitionItemIds, Integer addableStatus) {
        PurchaseRequisition pr = prRepo.findById(purchaseRequisitionId).orElse(null);
        if (pr == null) {
            throw new PurchaseRequisitionNotFoundException();
        }
        if (pr.getIsFinal()) {
            throw new PurchaseRequisitionException("Purchase Requisition is not possible to processed further");
        }
        if (pr.getPurchaseInvoices() == null && pr.getPurchaseInvoiceDocs() == null) {
            throw new PurchaseRequisitionException("Purchase Invoices and Docs input first");
        }
        var priList = pr.getItems();
        for (Long purchaseRequisitionItemId : purchaseRequisitionItemIds) {
            var findPri = priList.stream().filter((item) -> {
                return Objects.equals(item.getId(), purchaseRequisitionItemId) && (item.getAddableStatus() != null && item.getAddableStatus() == 2);
            }).findFirst();
            if (findPri.isPresent()) {
                throw new PurchaseRequisitionItemException("One of them item you have already send");
            }
        }
        var cpr = cprRepo.findCheckPurchaseRequisitionByPrIDAndCheckedStatus(purchaseRequisitionId, 1).orElse(null);
        if (cpr == null) {
            cpr = new CheckedPurchaseRequisition();
            cpr.setRequsitionRef(pr.getRequsitionRef());
            cpr.setOverallDiscount(pr.getOverallDiscount());
            cpr.setPurchaseInvoices(pr.getPurchaseInvoices());
            cpr.setPurchaseInvoiceDocs(pr.getPurchaseInvoiceDocs());
            cpr.setOrderRefs(pr.getOrderRefs());
            cpr.setPurchaseRequisition(pr);
            cprRepo.save(cpr);
        }
        List<CheckedPurchaseRequisitionItem> items = new ArrayList<>();
        for (Long purchaseRequisitionItemId : purchaseRequisitionItemIds) {
            var cprItem = cprItemRepo.findByCheckedPurchaseRequisitionIdAndPurchaseRequisitionItemId(cpr.getId(), purchaseRequisitionItemId).orElse(null);
            if (cprItem == null) {
                cprItem = new CheckedPurchaseRequisitionItem();
                cprItem.setCheckedPurchaseRequisition(cpr);
                PurchaseRequisitionItem prItem = priRepostory.findById(purchaseRequisitionItemId).orElse(null);
                if (prItem == null) {
                    throw new PurchaseRequisitionItemNotFoundException();
                }
                cprItem.setPurchaseRequisition(pr);
                cprItem.setPurchaseRequisitionItem(prItem);
                cprItem.setProduct(prItem.getProduct());
                cprItem.setProductDetail(prItem.getProductDetail());
                cprItem.setMrpPrice(prItem.getMrpPrice());
                cprItem.setPurchasePrice(prItem.getPurchasePrice());
                cprItem.setPurchaseQty(prItem.getPurchaseQty());
                cprItem.setDiscount(prItem.getDiscount());
                cprItem.setGiftOrBonusQty(prItem.getGiftOrBonusQty());
            }
            items.add(cprItem);
        }
        cprItemRepo.saveAll(items);
        return priRepostory.updateBulkForAddableItem(purchaseRequisitionId, purchaseRequisitionItemIds, addableStatus);
    }

    private String generateRequisitionRef() {
        PurchaseRequisition pr = prRepo.findTopByOrderByCreatedAtDesc();
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

    public String generateTripRef() {
        VehicleTrip vehicleTrip = vehicleTripRepository.findTopByOrderByCreatedAtDesc();
        if (vehicleTrip == null) {
            vehicleTrip = new VehicleTrip();
        }
        return vehicleTrip.getGeneratedTripRef();
    }

    public String generateGoodsRef() {
        GoodsOnTrip goodsOnTrip = goodsOnTripRepository.findTopByOrderByCreatedAtDesc();
        if (goodsOnTrip == null) {
            goodsOnTrip = new GoodsOnTrip();
        }
        return goodsOnTrip.getGeneratedGoodsRef();
    }
}
