package com.osudpotro.posmaster.dispatch;

import com.osudpotro.posmaster.branch.BranchNotFoundException;
import com.osudpotro.posmaster.branch.BranchRepository;
import com.osudpotro.posmaster.inventory.Inventory;
import com.osudpotro.posmaster.inventory.InventoryByBatchNo;
import com.osudpotro.posmaster.inventory.InventoryRepository;
import com.osudpotro.posmaster.inventory.InvoiceType;
import com.osudpotro.posmaster.product.*;
import com.osudpotro.posmaster.purchase.*;
import com.osudpotro.posmaster.user.auth.AuthService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class DispatchService {
    @Autowired
    private DispatchRepository dispatchRepo;
    @Autowired
    private DispatchItemRepository dispatchItemRepo;
    @Autowired
    private InventoryRepository invRepo;
    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private DispatchMapper dispatchMapper;
    @Autowired
    private DispatchItemMapper dispatchItemMapper;
    @Autowired
    private AuthService authService;
    @Autowired
    private ProductDetailRepository productDetailRepo;
    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private PurchaseRepository purchaseRepo;
    @Autowired
    private PurchaseDetailRepository purchaseDetailRepo;

    public List<DispatchDto> getAllDispatches() {
        return dispatchRepo.findAll()
                .stream()
                .map(dispatchMapper::toDto)
                .toList();
    }

    public Page<DispatchDto> getAllDispatches(DispatchFilter filter, Pageable pageable) {
        var authUser = authService.getCurrentUser();
        return dispatchRepo.findAll(DispatchSpecification.filter(filter, authUser), pageable).map(dispatchMapper::toDto);
    }

    public Page<DispatchDto> filterDispatchByRequester(DispatchFilter filter, Pageable pageable) {
        var authUser = authService.getCurrentUser();
        return dispatchRepo.findAll(DispatchSpecification.filterByRequesterBranch(filter, authUser), pageable).map(dispatchMapper::toDto);
    }

    public Page<DispatchDto> getAllDispatchesByRequestReceivedBranch(DispatchFilter filter, Pageable pageable) {
        var authUser = authService.getCurrentUser();
        return dispatchRepo.findAll(DispatchSpecification.filterByAcceptorBranch(filter, authUser), pageable).map(dispatchMapper::toDto);
    }

    @Transactional
    public DispatchDto createDispatch(DispatchCreateRequest request) {
        var authUser = authService.getCurrentUser();
        String dispatchRef = getGenerateDispatchRef();
        if (dispatchRepo.existsByDispatchRef(dispatchRef)) {
            throw new DuplicateDispatchException();
        }
        if (Objects.equals(request.getRequesterBranchId(), authUser.getBranch().getId())) {
            throw new DispatchException("Branch is not valid");
        }
        var reqBranch = branchRepository.findById(authUser.getBranch().getId()).orElseThrow(BranchNotFoundException::new);
        var senderBranch = branchRepository.findById(request.getRequesterBranchId()).orElseThrow(BranchNotFoundException::new);
        Dispatch dispatch = new Dispatch();
        dispatch.setDispatchRef(dispatchRef);
        dispatch.setRequesterBranch(reqBranch);
        dispatch.setOrganization(reqBranch.getOrganization());
        dispatch.setAcceptorBranch(senderBranch);
        dispatch.setCreatedBy(authUser);
        dispatchRepo.save(dispatch);
        return dispatchMapper.toDto(dispatch);
    }

    @Transactional
    public DispatchDto sendByRequesterBranch(Long dispatchId, DispatchUpdateRequest request) {
        Dispatch dispatch = dispatchRepo.findById(dispatchId).orElseThrow(DispatchNotFoundException::new);
        if (dispatch.getDispatchStatus() != 1 && dispatch.getSendByRequester()!=null) {
            throw new DispatchException("You already done!");
        }
        var authUser = authService.getCurrentUser();
        String dispatchInvoice = getGenerateDispatchInvoice();
        dispatch.setDispatchInvoice(dispatchInvoice);
        dispatch.setSendByRequester(authUser);
        dispatch.setSendAtByRequester(LocalDateTime.now());
        dispatch.setSendNoteByRequester(request.getNote());
        dispatch.setDispatchStatus(2);
        return dispatchMapper.toDto(dispatch);
    }

    @Transactional
    public DispatchDto acceptByRequesterBranch(Long dispatchId, DispatchUpdateRequest request) {
        Dispatch dispatch = dispatchRepo.findById(dispatchId).orElseThrow(DispatchNotFoundException::new);
        if (dispatch.getDispatchStatus() != 4 && dispatch.getAcceptByRequester()!=null) {
            throw new DispatchException("You already done!");
        }
        List<DispatchItem> dispatchItems = dispatch.getItems();
        List<Inventory> acceptorInvList = new ArrayList<>();
        List<Inventory> requesterInvList = new ArrayList<>();
        for (var dispatchItem : dispatchItems) {
//            Deduct Quantity from Acceptor Branch
            Inventory acceptorInv = getInventory(dispatch, dispatchItem, InvoiceType.DISPATCH_SEND);
            acceptorInvList.add(acceptorInv);
//            Added Quantity from Requester Branch
            Inventory requesterInv = getInventory(dispatch, dispatchItem, InvoiceType.DISPATCH_RECEIVE);
            requesterInvList.add(requesterInv);
        }
        invRepo.saveAll(acceptorInvList);
        invRepo.saveAll(requesterInvList);
        var authUser = authService.getCurrentUser();
        dispatch.setAcceptByRequester(authUser);
        dispatch.setAcceptAtByRequester(LocalDateTime.now());
        dispatch.setAcceptNoteByRequester(request.getNote());
        dispatch.setDispatchStatus(5);
        return dispatchMapper.toDto(dispatch);
    }

    @Transactional
    public DispatchDto acceptByAcceptorBranch(Long dispatchId, DispatchUpdateRequest request) {
        Dispatch dispatch = dispatchRepo.findById(dispatchId).orElseThrow(DispatchNotFoundException::new);
        if (dispatch.getDispatchStatus() != 2 && dispatch.getAcceptByAcceptor()!=null) {
            throw new DispatchException("You already done!");
        }
        var authUser = authService.getCurrentUser();
        dispatch.setAcceptByAcceptor(authUser);
        dispatch.setAcceptAtByAcceptor(LocalDateTime.now());
        dispatch.setAcceptNoteByAcceptor(request.getNote());
        dispatch.setDispatchStatus(3);
        return dispatchMapper.toDto(dispatch);
    }

    @Transactional
    public DispatchDto sendByAcceptorBranch(Long dispatchId, DispatchUpdateRequest request) {
        Dispatch dispatch = dispatchRepo.findById(dispatchId).orElseThrow(DispatchNotFoundException::new);
        if (dispatch.getDispatchStatus() != 3 && dispatch.getSendByAcceptor()!=null) {
            throw new DispatchException("You already done!");
        }
        List<DispatchItem> dispatchItems = dispatch.getItems();
        List<DispatchItem> newDispatchItems = new ArrayList<>();
        for (var item : dispatchItems) {
            List<InventoryByBatchNo> inventoryListByBatch = invRepo.getInvListByBatch(item.getProduct().getId(), item.getProductDetail().getId());
            Integer total = 0;
            boolean isFirstRowUpdated = false;
            for (var invByBatch : inventoryListByBatch) {
                if (item.getUpdatedQty() > total) {
                    Integer nowRequiredQty = item.getUpdatedQty() - total;
                    int dispatchQty = 0;
                    if (invByBatch.getCurrentStock() >= nowRequiredQty) {
                        total = total + nowRequiredQty;
                        dispatchQty = nowRequiredQty;
                    } else {
                        total = total + invByBatch.getCurrentStock();
                        dispatchQty = invByBatch.getCurrentStock();
                    }
                    if (isFirstRowUpdated == false && Objects.equals(invByBatch.getProductId(), item.getProduct().getId()) && Objects.equals(invByBatch.getProductDetailId(), item.getProductDetail().getId())) {
                        isFirstRowUpdated = true;
                        Purchase purchase = purchaseRepo.findById(invByBatch.getPurchaseId()).orElseThrow(PurchaseException::new);
                        PurchaseDetail purchaseDetail = purchaseDetailRepo.findById(invByBatch.getPurchaseDetailId()).orElseThrow(PurchaseException::new);
                        item.setDispatchQty(dispatchQty);
                        item.setPurchase(purchase);
                        item.setPurchaseDetail(purchaseDetail);
                        newDispatchItems.add(item);
                    } else {
                        Purchase purchase = purchaseRepo.findById(invByBatch.getPurchaseId()).orElseThrow(PurchaseException::new);
                        PurchaseDetail purchaseDetail = purchaseDetailRepo.findById(invByBatch.getPurchaseDetailId()).orElseThrow(PurchaseException::new);
                        DispatchItem newDispatchItem = new DispatchItem();
                        newDispatchItem.setDispatch(dispatch);
                        newDispatchItem.setProduct(item.getProduct());
                        newDispatchItem.setProductDetail(item.getProductDetail());
                        newDispatchItem.setDispatchQty(dispatchQty);
                        newDispatchItem.setPurchase(purchase);
                        newDispatchItem.setPurchaseDetail(purchaseDetail);
                        newDispatchItems.add(newDispatchItem);
                    }
                }
            }
        }
        dispatch.getItems().clear();
        dispatch.getItems().addAll(newDispatchItems);
        var authUser = authService.getCurrentUser();
        dispatch.setSendByAcceptor(authUser);
        dispatch.setSendAtByAcceptor(LocalDateTime.now());
        dispatch.setSendNoteByAcceptor(request.getNote());
        dispatch.setDispatchStatus(4);
        dispatchRepo.save(dispatch);
        return dispatchMapper.toDto(dispatch);
    }

    public DispatchWithItemPageResponse filterWithItemPagination(Long dispatchId, Pageable pageable, DispatchItemFilter filter) {
        var authUser = authService.getCurrentUser();
        Dispatch dispatch = dispatchRepo.findById(dispatchId).orElseThrow(DispatchNotFoundException::new);
        Page<DispatchItemDto> result = dispatchItemRepo.findAll(DispatchItemSpecification.filter(filter, dispatchId), pageable).map(dispatchItemMapper::toDto);
        return dispatchMapper.toMinDto(dispatch, result);
    }

    public DispatchDto addDispatchItem(Long dispatchId, DispatchItemAddRequest request) {
        Dispatch dispatch = dispatchRepo.findById(dispatchId).orElseThrow(DispatchNotFoundException::new);
        Product product = productRepo.findById(request.getProductId()).orElseThrow(ProductNotFoundException::new);
        ProductDetail productDetail = productDetailRepo.findById(request.getProductDetailId()).orElseThrow(ProductDetailNotFoundException::new);
        DispatchItem dispatchItem=dispatchItemRepo.findByDispatchAndProductAndProductDetail(dispatch,product,productDetail).orElse(null);
        if (dispatchItem ==null) {
            dispatchItem = new DispatchItem();
            dispatchItem.setProduct(product);
            dispatchItem.setProductDetail(productDetail);
            List<DispatchItem> list = dispatch.getItems();
            list.add(dispatchItem);
            dispatch.setItems(list);
            dispatchItem.setDispatch(dispatch);
        }
        dispatchItem.setDispatchQty(request.getQty());
        dispatchItem.setRequestedQty(request.getQty());
        dispatchItem.setUpdatedQty(request.getQty());
        dispatchRepo.save(dispatch);
        return dispatchMapper.toDto(dispatch);
    }

    public DispatchDto getDispatch(Long dispatchId) {
        return null;
    }

    private String getGenerateDispatchRef() {
        Dispatch dispatch = dispatchRepo.findTopByOrderByCreatedAtDesc();
        if (dispatch == null) {
            dispatch = new Dispatch();
        }
        return dispatch.getGenerateDispatchRef();
    }

    private String getGenerateDispatchInvoice() {
        Dispatch dispatch = dispatchRepo.findTopByOrderByDispatchInvoiceDesc();
        if (dispatch == null) {
            dispatch = new Dispatch();
        }
        return dispatch.getGenerateDispatchInvoice();
    }

    private Inventory getInventory(Dispatch dispatch, DispatchItem dispatchItem, InvoiceType invoiceType) {
        Inventory invSummary = new Inventory();
        invSummary.setInvoiceId(dispatch.getId());
        invSummary.setInvoiceDetailId(dispatchItem.getId());
        if (dispatchItem.getPurchase() != null) {
            Purchase purchase = dispatchItem.getPurchase();
            invSummary.setPurchase(purchase);
            invSummary.setPurchaseBatchNo(purchase.getPurchaseBatchNo());
        }
        if (dispatchItem.getPurchaseDetail() != null) {
            PurchaseDetail purchaseDetail = dispatchItem.getPurchaseDetail();
            invSummary.setPurchaseDetail(purchaseDetail);
            invSummary.setProductionBatchNo(purchaseDetail.getProductionBatchNo());
            invSummary.setManufactureDate(purchaseDetail.getManufactureDate());
            invSummary.setExpiredDate(purchaseDetail.getExpiredDate());
            invSummary.setProduct(purchaseDetail.getProduct());
        }
        invSummary.setInvoiceType(invoiceType);
        if (invoiceType == InvoiceType.DISPATCH_SEND) {
            invSummary.setStockOut(dispatchItem.getDispatchQty());
            invSummary.setBranch(dispatch.getAcceptorBranch());
            invSummary.setInvoiceDate(dispatch.getSendAtByAcceptor());
        }
        if (invoiceType == InvoiceType.DISPATCH_RECEIVE) {
            invSummary.setStockIn(dispatchItem.getDispatchQty());
            invSummary.setBranch(dispatch.getRequesterBranch());
            invSummary.setInvoiceDate(dispatch.getAcceptAtByAcceptor());
        }
        invSummary.setProductDetail(dispatchItem.getProductDetail());
        invSummary.setOrganization(dispatchItem.getPurchase().getOrganization());
        invSummary.setWarehouse(dispatchItem.getPurchase().getWarehouse());
        invSummary.setSupplier(dispatchItem.getPurchase().getSupplier());
        return invSummary;
    }
}
