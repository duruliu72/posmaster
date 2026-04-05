package com.osudpotro.posmaster.purchase.checked;

import com.osudpotro.posmaster.inventory.InventorySummary;
import com.osudpotro.posmaster.inventory.InventorySummaryRepository;
import com.osudpotro.posmaster.inventory.InvoiceType;
import com.osudpotro.posmaster.purchase.Purchase;
import com.osudpotro.posmaster.purchase.PurchaseDetail;
import com.osudpotro.posmaster.purchase.PurchaseReferenceDto;
import com.osudpotro.posmaster.purchase.PurchaseRepository;
import com.osudpotro.posmaster.purchase.requisition.PurchaseRequisitionException;
import com.osudpotro.posmaster.purchase.requisition.PurchaseRequisitionNotFoundException;
import com.osudpotro.posmaster.purchase.requisition.PurchaseRequisitionRepository;
import com.osudpotro.posmaster.user.User;
import com.osudpotro.posmaster.user.auth.AuthService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class CheckedPurchaseRequisitionService {
    @Autowired
    private PurchaseRequisitionRepository prRepo;
    @Autowired
    private PurchaseRepository purchaseRepo;
    @Autowired
    private CheckedPurchaseRequisitionRepository cprRepo;
    @Autowired
    private CheckedPurchaseRequisitionItemRepository cprItemRepo;
    @Autowired
    private InventorySummaryRepository invSummaryRepo;
    @Autowired
    private AuthService authService;
    @Autowired
    private CheckedPurchaseRequisitionMapper cprMapper;
    @Autowired
    private CheckedPurchaseRequisitionItemMapper cprItemMapper;

    public List<CheckedPurchaseRequisitionDto> getAllEntities() {
        return cprRepo.findAll()
                .stream()
                .map(cprMapper::toDto)
                .toList();
    }

    public Page<CheckedPurchaseRequisitionDto> filterEntities(CheckedPurchaseRequisitionFilter filter, Pageable pageable) {
        var authUser = authService.getCurrentUser();
        return cprRepo.findAll(CheckedPurchaseRequisitionSpecification.filter(filter, authUser), pageable).map(cprMapper::toDto);
    }

    public Page<CheckedPurchaseRequisitionDto> filterEntitiesByBranch(CheckedPurchaseRequisitionFilter filter, Pageable pageable) {
        var authUser = authService.getCurrentUser();
        return cprRepo.findAll(CheckedPurchaseRequisitionSpecification.filterByBranch(filter, authUser), pageable).map(cprMapper::toDto);
    }

    //For CheckedPurchaseRequisition With Item FilterAndPage
    public CheckedPurchaseRequisitionWithItemPageResponse getEntityWithFilterItems(Long checkedPurchaseRequisitionId, Pageable pageable, CheckedPurchaseRequisitionFilter filter) {
        CheckedPurchaseRequisition cpr = cprRepo.findById(checkedPurchaseRequisitionId).orElseThrow(CheckedPurchaseRequisitionNotFoundException::new);
        Page<CheckedPurchaseRequisitionItemDto> result = cprItemRepo.findAllWithFilterItems(checkedPurchaseRequisitionId, filter.getName(), pageable).map(cprItemMapper::toDto);
        return cprMapper.toMinDto(cpr, result);
    }
    @Transactional
    public CheckedPurchaseRequisitionDto updateByAdmin(Long checkedPurchaseRequisitionId, CheckedPurchaseRequisitionRequest request) {
        CheckedPurchaseRequisition cpr = cprRepo.findById(checkedPurchaseRequisitionId).orElseThrow(CheckedPurchaseRequisitionNotFoundException::new);
        var pr = cpr.getPurchaseRequisition();
        var authUser = authService.getCurrentUser();
        if (cpr.getCheckedStatus() == 2) {
            throw new PurchaseRequisitionException("Not processed further");
        }
        if (cpr.getCheckedStatus() == 1 && request.getCheckedStatus() == 2) {
            cpr.setCheckedStatus(2);
            //update Purchase requisition table
            pr.setOrderRefs(pr.getOrderRefs());
            pr.setOrderRefs("");
            pr.setPurchaseInvoices(pr.getPurchaseInvoices());
            pr.setPurchaseInvoices("");
            pr.setPurchaseInvoiceDocs(pr.getPurchaseInvoiceDocs());
            pr.setPurchaseInvoiceDocs("");
            pr.setOverallDiscount(pr.getOverallDiscount());
            pr.setOverallDiscount(null);
            cpr.setCheckByAdmin(authUser);
            cpr.setCheckByAdminAt(LocalDateTime.now());
        }
        prRepo.save(pr);
        cprRepo.save(cpr);
        return cprMapper.toDto(cpr);
    }

    public CheckedPurchaseRequisitionDto updateItemFromBranch(Long cprId, Long cprItemId, UpdateFromBranchRequest request) {
        var cpr = cprRepo.findById(cprId).orElseThrow(CheckedPurchaseRequisitionNotFoundException::new);
        var purchaseFind = purchaseRepo.findByCheckedPurchaseRequisition(cpr).orElse(null);
        if (cpr.getCheckedStatus() == 3 || purchaseFind != null) {
            throw new PurchaseRequisitionException("As you Already Added to InventorySummary. Not to possible update");
        }
        var prtItem = cprItemRepo.findById(cprItemId).orElseThrow(CheckedPurchaseRequisitionItemNotFoundException::new);
        prtItem = getCheckedPurchaseRequisitionItem(prtItem, request);
        cprItemRepo.save(prtItem);
        return cprMapper.toDto(cpr);
    }

    @Transactional
    public CheckedPurchaseRequisitionDto addToInventory(Long cprId, AddToInventoryRequest request) {
        var crp = cprRepo.findById(cprId).orElseThrow(PurchaseRequisitionNotFoundException::new);
        var purchaseFind = purchaseRepo.findByCheckedPurchaseRequisition(crp).orElse(null);
        if (crp.getCheckedStatus() == 3 || purchaseFind != null) {
            throw new PurchaseRequisitionException("Already Added to InventorySummary!");
        }
//        crp.setCheckedStatus(3);
        var authUser = authService.getCurrentUser();
        //  Update CheckedPurchaseRequisitionItem
        List<CheckedPurchaseRequisitionItem> cprItemList = new ArrayList<>();
        for (var item : request.getItems()) {
            if(item.getCheckedPurchaseRequisitionItemId()==null){
                throw new PurchaseRequisitionException("Item id will not Empty!");
            }
            var prtItem = cprItemRepo.findById(item.getCheckedPurchaseRequisitionItemId()).orElseThrow(CheckedPurchaseRequisitionItemNotFoundException::new);
            prtItem = getCheckedPurchaseRequisitionItem(prtItem, item);
            cprItemList.add(prtItem);
        }
        crp.getItems().clear();
        crp.getItems().addAll(cprItemList);
        //   Insert Purchase and Purchase details table for Requsition Branch
        Purchase purchase = new Purchase();
        PurchaseReferenceDto purchaseReference=generatePurchaseRef();
        purchase.setPurchaseRef(purchaseReference.getPurchaseRef());
        purchase.setRequsitionRef(crp.getPurchaseRequisition().getRequsitionRef());
        purchase.setCheckedPurchaseRequisition(crp);
        purchase.setPurchaseRequisition(crp.getPurchaseRequisition());
        purchase.setPurchaseType(crp.getPurchaseRequisition().getPurchaseType());
        purchase.setOrganization(crp.getPurchaseRequisition().getOrganization());
        purchase.setBranch(crp.getPurchaseRequisition().getBranch());
        purchase.setPurchaseBatchNo(purchaseReference.getPurchaseBatchNo());
        purchase.setOverallDiscount(crp.getOverallDiscount());
        purchase.setPurchaseInvoices(crp.getPurchaseInvoices());
        purchase.setPurchaseInvoiceDocs(crp.getPurchaseInvoiceDocs());
        purchase.setOrderRefs(crp.getPurchaseRequisition().getOrderRefs());
        purchase.setAddedBy(authUser);
        //Purchase Details
        List<PurchaseDetail> purchaseDetailList = new ArrayList<>();
        for (var crpItem : cprItemList) {
            PurchaseDetail pd = getPurchaseDetails(crpItem, authUser, purchase);
            purchaseDetailList.add(pd);
        }
        purchase.setItems(purchaseDetailList);
        purchase = purchaseRepo.save(purchase);
//        InventorySummary Summary add here
        List<InventorySummary> inventorySummaryList = new ArrayList<>();
        for (var purchaseDetail : purchaseDetailList) {
            InventorySummary invSummary = getInventorySummary(purchaseDetail, purchase);
            inventorySummaryList.add(invSummary);
        }
        invSummaryRepo.saveAll(inventorySummaryList);
        cprRepo.save(crp);
        return cprMapper.toDto(crp);
    }
    private InventorySummary getInventorySummary(PurchaseDetail purchaseDetail, Purchase purchase) {
        InventorySummary invSummary = new InventorySummary();
        invSummary.setInvoiceId(purchase.getId());
        invSummary.setInvoiceDetailId(purchaseDetail.getId());
        invSummary.setPurchase(purchase);
        invSummary.setPurchaseDetail(purchaseDetail);
        invSummary.setInvoiceType(InvoiceType.PURCHASE);
        Integer qty = purchaseDetail.getPurchaseQty();
        if (purchaseDetail.getGiftOrBonusQty() != null) {
            qty = qty + purchaseDetail.getGiftOrBonusQty();
        }
        invSummary.setStockIn(qty);
        invSummary.setBranch(purchase.getBranch());
        invSummary.setInvoiceDate(purchase.getPurchaseAt());
        invSummary.setPurchaseBatchNo(purchase.getPurchaseBatchNo());
        invSummary.setProductionBatchNo(purchaseDetail.getProductionBatchNo());
        invSummary.setManufactureDate(purchaseDetail.getManufactureDate());
        invSummary.setExpiredDate(purchaseDetail.getExpiredDate());
        invSummary.setProduct(purchaseDetail.getProduct());
        invSummary.setProductDetail(purchaseDetail.getProductDetail());
        invSummary.setOrganization(purchase.getOrganization());
        invSummary.setWarehouse(purchase.getWarehouse());
        invSummary.setSupplier(purchase.getSupplier());
        return invSummary;
    }
    private PurchaseDetail getPurchaseDetails(CheckedPurchaseRequisitionItem prtItem, User authUser, Purchase purchase) {
        PurchaseDetail pd = new PurchaseDetail();
        pd.setPurchaseRequisition(prtItem.getPurchaseRequisition());
        pd.setPurchaseRequisitionItem(prtItem.getPurchaseRequisitionItem());
        pd.setProduct(prtItem.getProduct());
        pd.setProductDetail(prtItem.getProductDetail());
        pd.setPurchasePrice(prtItem.getPurchasePrice());
        pd.setMrpPrice(prtItem.getMrpPrice());
        pd.setPurchaseDiscount(prtItem.getDiscountPrice());
        pd.setPurchaseQty(prtItem.getPurchaseQty());
        pd.setGiftOrBonusQty(prtItem.getGiftOrBonusQty());
        pd.setAtomQty(prtItem.getProductDetail().getAtomQty());
        pd.setProductionBatchNo(prtItem.getProductionBatchNo());
        pd.setManufactureDate(prtItem.getManufactureDate());
        pd.setExpiredDate(prtItem.getExpiredDate());
        pd.setAddedBy(authUser);
        pd.setPurchase(purchase);
        return  pd;
    }
    public CheckedPurchaseRequisitionItem getCheckedPurchaseRequisitionItem(CheckedPurchaseRequisitionItem prtItem, UpdateFromBranchRequest request) {
        prtItem.setProductionBatchNo(request.getProductionBatchNo());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (request.getManufactureDate() != null) {
            String manufactureDateInput = "";
            if (request.getManufactureDate().length() > 10) {
                manufactureDateInput = request.getManufactureDate().substring(0, 10) + " 00:00:00";
            } else {
                manufactureDateInput = request.getManufactureDate() + " 00:00:00";
            }
            LocalDateTime manufactureDate = LocalDateTime.parse(manufactureDateInput, formatter);
            prtItem.setManufactureDate(manufactureDate);
        } else {
            prtItem.setManufactureDate(null);
        }
        if (request.getExpiredDate() != null) {
            String expiredDateInput = "";
            if (request.getExpiredDate().length() > 10) {
                expiredDateInput = request.getExpiredDate().substring(0, 10) + " 00:00:00";
            } else {
                expiredDateInput = request.getExpiredDate() + " 00:00:00";
            }
            LocalDateTime expiredDate = LocalDateTime.parse(expiredDateInput, formatter);
            prtItem.setExpiredDate(expiredDate);
        } else {
            prtItem.setExpiredDate(null);
        }
        return prtItem;
    }
    public PurchaseReferenceDto generatePurchaseRef() {
        Purchase purchase = purchaseRepo.findTopByOrderByPurchaseAtDesc();
        if (purchase == null) {
            purchase = new Purchase();

        }
        PurchaseReferenceDto purchaseReference=new PurchaseReferenceDto();
        purchaseReference.setPurchaseRef(purchase.getGeneratePurchaseRef());
        purchaseReference.setPurchaseBatchNo(purchase.getGeneratePurchaseBatchNo());
        return purchaseReference;
    }
}
