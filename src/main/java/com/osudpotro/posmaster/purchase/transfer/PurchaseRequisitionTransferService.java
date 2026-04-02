package com.osudpotro.posmaster.purchase.transfer;

import com.osudpotro.posmaster.branch.BranchRepository;
import com.osudpotro.posmaster.common.Location;
import com.osudpotro.posmaster.dispatch.Dispatch;
import com.osudpotro.posmaster.dispatch.DispatchDetail;
import com.osudpotro.posmaster.dispatch.DispatchRepository;
import com.osudpotro.posmaster.inventory.InventorySummary;
import com.osudpotro.posmaster.inventory.InventorySummaryRepository;
import com.osudpotro.posmaster.inventory.InvoiceType;
import com.osudpotro.posmaster.organization.OrganizationRepository;
import com.osudpotro.posmaster.product.ProductDetailRepository;
import com.osudpotro.posmaster.product.ProductRepository;
import com.osudpotro.posmaster.purchase.Purchase;
import com.osudpotro.posmaster.purchase.PurchaseDetail;
import com.osudpotro.posmaster.purchase.PurchaseReferenceDto;
import com.osudpotro.posmaster.purchase.PurchaseRepository;
import com.osudpotro.posmaster.purchase.requisition.*;
import com.osudpotro.posmaster.requisition.*;
import com.osudpotro.posmaster.requisitiontype.RequsitionTypeRepository;
import com.osudpotro.posmaster.tms.driver.DriverNotFoundException;
import com.osudpotro.posmaster.tms.driver.DriverRepository;
import com.osudpotro.posmaster.tms.goodsontrip.*;
import com.osudpotro.posmaster.tms.vechile.DuplicateVehicleException;
import com.osudpotro.posmaster.tms.vechile.VehicleNotFoundException;
import com.osudpotro.posmaster.tms.vechile.VehicleRepository;
import com.osudpotro.posmaster.tms.vehicletrip.TripStatus;
import com.osudpotro.posmaster.tms.vehicletrip.VehicleTrip;
import com.osudpotro.posmaster.tms.vehicletrip.VehicleTripNotFoundException;
import com.osudpotro.posmaster.tms.vehicletrip.VehicleTripRepository;
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
public class PurchaseRequisitionTransferService {
    @Autowired
    private PurchaseRequisitionRepository prRepo;
    @Autowired
    private PurchaseRequisitionTransferRepository prTransferRepo;
    @Autowired
    private PurchaseRepository purchaseRepo;
    @Autowired
    private DispatchRepository dispatchRepo;
    @Autowired
    private InventorySummaryRepository invSummaryRepo;
    @Autowired
    private PurchaseRequisitionItemTransferRepository priTransferRepo;
    @Autowired
    private RequisitionRepository requisitionRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private PurchaseRequisitionTransferMapper prTransferMapper;
    @Autowired
    private PurchaseRequisitionItemTransferMapper priTransferMapper;
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

    public List<PurchaseRequisitionTransferDto> getAllEntities() {
        return prTransferRepo.findAll()
                .stream()
                .map(prTransferMapper::toDto)
                .toList();
    }

    public Page<PurchaseRequisitionTransferDto> filterEntities(PurchaseRequisitionTransferFilter filter, Pageable pageable) {
        var authUser = authService.getCurrentUser();
        return prTransferRepo.findAll(PurchaseRequisitionTransferSpecification.filterByDelivered(filter,authUser), pageable).map(prTransferMapper::toDto);
    }

    public Page<PurchaseRequisitionTransferDto> filterEntitiesByPurchaseRequisition(PurchaseRequisitionTransferFilter filter, Long purchaseRequisitionId, Pageable pageable) {
        prRepo.findById(purchaseRequisitionId).orElseThrow(() -> new PurchaseRequisitionNotFoundException("PurchaseRequisition not found with ID: " + purchaseRequisitionId));
        return prTransferRepo.findAll(PurchaseRequisitionTransferSpecification.filterByPurchaseRequisition(filter, purchaseRequisitionId), pageable).map(prTransferMapper::toDto);
    }

    public PurchaseRequisitionTransferDto getEntity(Long purchaseRequisitionTransferId) {
        var prt = prTransferRepo.findById(purchaseRequisitionTransferId).orElseThrow(() -> new PurchaseRequisitionNotFoundException("PurchaseRequisition not found with ID: " + purchaseRequisitionTransferId));
        List<PurchaseRequisitionItemTransferDto> prilist = priTransferRepo.findEntityList(purchaseRequisitionTransferId).stream()
                .map(priTransferMapper::toDto)
                .toList();
        var prtDto = prTransferMapper.toDto(prt);
        prtDto.setItems(prilist);
        return prtDto;
    }

    public PurchaseRequisitionTransfer getMainEntity(Long purchaseRequisitionTransferId) {
        return prTransferRepo.findById(purchaseRequisitionTransferId).orElseThrow(() -> new PurchaseRequisitionNotFoundException("PurchaseRequisition not found with ID: " + purchaseRequisitionTransferId));
    }

    //    For Purchase Requisition Item
    public PurchaseRequisitionTransferWithItemPageResponse filterEntitiesWithItemPagination(Long purchaseRequisitionTransferId, PurchaseRequisitionItemTransferFilter filter, Pageable pageable) {
        PurchaseRequisitionTransfer prt = prTransferRepo.findPurchaseRequisitionById(purchaseRequisitionTransferId).orElseThrow(PurchaseRequisitionNotFoundException::new);
        var prtDto = prTransferMapper.toDto(prt);
        Page<PurchaseRequisitionItemTransfer> resultBase = priTransferRepo.findAll(PurchaseRequisitionItemTransferSpecification.filterByPurchaseRequisitionTransfer(filter, purchaseRequisitionTransferId), pageable);
        Page<PurchaseRequisitionItemTransferDto> result = resultBase.map(priTransferMapper::toDto);
        prt.setItems(resultBase.getContent());
        var pageResponse = prTransferMapper.toMinDto(prt, result);
        pageResponse.setOverallDiscount(prtDto.getOverallDiscount());
        pageResponse.setTotalPrice(prtDto.getTotalPrice());
        pageResponse.setTotalQty(prtDto.getTotalQty());
        pageResponse.setTotalGiftOrBonusQty(prtDto.getTotalGiftOrBonusQty());
        pageResponse.setTotalGiftOrBonusPrice(prtDto.getTotalGiftOrBonusPrice());
        return pageResponse;
    }

    @Transactional
    public PurchaseRequisitionTransferDto assignToVehicle(Long purchaseRequisitionTransferId, AssignToVehicleRequest request) {
        var prt = prTransferRepo.findById(purchaseRequisitionTransferId).orElseThrow(PurchaseRequisitionNotFoundException::new);
        var pr = prt.getPurchaseRequisition();
        if (pr.getRequisition().getRequisitionStatus() != 3) {
            throw new PurchaseRequisitionException("Purchase Requisition is not approved yet!");
        }
        if (pr.getIsFinal()) {
            throw new PurchaseRequisitionException("Purchase Requisition is not possible to processed further");
        }
        if (pr.getTotalItems() == 0) {
            throw new PurchaseRequisitionEmptyException();
        }
        var goodsOnTripFind = goodsOnTripRepository.findByPurchaseRequisitionTransfer(prt).orElse(null);
        if (goodsOnTripFind != null) {
            throw new DuplicateVehicleException("Vehicle Trip already Assign");
        }
        VehicleTrip vehicleTrip = null;
        var authUser = authService.getCurrentUser();
        if (request.getTripRef() != null) {
            vehicleTrip = vehicleTripRepository.findByTripRef(request.getTripRef()).orElseThrow(VehicleTripNotFoundException::new);
            if (vehicleTrip.getTripStatus() == TripStatus.PENDING) {
                vehicleTrip.setTripStatus(TripStatus.SCHEDULED);
                vehicleTripRepository.save(vehicleTrip);
            }
        }
        if (request.getTripRef() == null && request.getVehicleId() != null && request.getDriverId() != null) {
            String tripRef = generateTripRef();
            if (vehicleTripRepository.existsByTripRef(tripRef)) {
                throw new DuplicateVehicleException("Vehicle Trip already exists");
            }
            var vehicle = vehicleRepository.findById(request.getVehicleId()).orElseThrow(VehicleNotFoundException::new);
            var driver = driverRepository.findById(request.getDriverId()).orElseThrow(DriverNotFoundException::new);
            vehicleTrip = new VehicleTrip();
            vehicleTrip.setTripRef(generateTripRef());
            vehicleTrip.setVehicle(vehicle);
            vehicleTrip.setDriver(driver);
            vehicleTrip.setTripStatus(TripStatus.SCHEDULED);
            vehicleTrip.setCreatedBy(authUser);
            vehicleTripRepository.save(vehicleTrip);
        }
        GoodsOnTrip goodsOnTrip = new GoodsOnTrip();
        goodsOnTrip.setGoodsRef(generateGoodsRef());
        goodsOnTrip.setVehicleTrip(vehicleTrip);
        goodsOnTrip.setGoodsType(GoodsType.INVOICE);
        goodsOnTrip.setGoodsStatus(GoodsStatus.ASSIGN_TO_VEHICLE);
        goodsOnTrip.setSourceAddress(request.getSourceAddress());
        goodsOnTrip.setDestAddress(request.getDestAddress());
        var rootBranch = pr.getRootBranch();
        var reqBranch = pr.getReqBranch();
        Location sourceLoc = new Location();
        sourceLoc.setLatitude(rootBranch.getLatitude());
        sourceLoc.setLongitude(rootBranch.getLongitude());
        goodsOnTrip.setSource(sourceLoc);
        Location destination = new Location();
        destination.setLatitude(reqBranch.getLatitude());
        destination.setLongitude(reqBranch.getLongitude());
        goodsOnTrip.setDestination(destination);
        goodsOnTrip.setAssignBy(authUser);
        goodsOnTrip.setCreatedBy(authUser);
        goodsOnTrip.setPurchaseRequisitionTransfer(prt);
        goodsOnTripRepository.save(goodsOnTrip);
        prTransferRepo.save(prt);
        return prTransferMapper.toDto(prt);
    }

    @Transactional
    public PurchaseRequisitionTransferDto receivePurchaseRequisitionTransfer(Long purchaseRequisitionTransferId) {
        var prt = prTransferRepo.findById(purchaseRequisitionTransferId).orElseThrow(PurchaseRequisitionNotFoundException::new);
        var pr = prt.getPurchaseRequisition();
        if (pr.getRequisition().getRequisitionStatus() != 3) {
            throw new PurchaseRequisitionException("Purchase Requisition is not approved yet!");
        }
        if (pr.getIsFinal()) {
            throw new PurchaseRequisitionException("Purchase Requisition is not possible to processed further");
        }
        if (pr.getTotalItems() == 0) {
            throw new PurchaseRequisitionEmptyException();
        }
        GoodsOnTrip gooodsOnTrip = prt.getGoodsOnTrip();
        if (!gooodsOnTrip.getGoodsStatus().equals(GoodsStatus.DELIVERED)) {
            throw new GoodsOnTripDeliveryException("Goods On Trip is not Delivered yet!");
        }
        if (gooodsOnTrip.getIsReceived() != null && gooodsOnTrip.getIsReceived()) {
            throw new GoodsOnTripDeliveryException("Goods On Trip is Already Received!");
        }
        var authUser = authService.getCurrentUser();
        gooodsOnTrip.setIsReceived(true);
        gooodsOnTrip.setReceivedBy(authUser);
        gooodsOnTrip.setReceivedAt(LocalDateTime.now());

        prt.setTransferStatus(2);
        prTransferRepo.save(prt);
        //update Purchase requsition tabel
        pr.setOrderRefs(pr.getOrderRefs());
        pr.setOrderRefs("");
        pr.setPurchaseInvoices(pr.getPurchaseInvoices());
        pr.setPurchaseInvoices("");
        pr.setPurchaseInvoiceDocs(pr.getPurchaseInvoiceDocs());
        pr.setPurchaseInvoiceDocs("");
        pr.setOverallDiscount(pr.getOverallDiscount());
        pr.setOverallDiscount(null);
        goodsOnTripRepository.save(gooodsOnTrip);
        prRepo.save(pr);
        return prTransferMapper.toDto(prt);
    }

    @Transactional
    public PurchaseRequisitionTransferDto addToInventory(Long purchaseRequisitionTransferId, AddToInventoryRequest request) {
        var prt = prTransferRepo.findById(purchaseRequisitionTransferId).orElseThrow(PurchaseRequisitionNotFoundException::new);
        var purchaseFind = purchaseRepo.findByPurchaseRequisitionTransfer(prt).orElse(null);
        if (prt.getTransferStatus() == 3 || purchaseFind != null) {
            throw new PurchaseRequisitionException("Already Added to Inventory!");
        }
        prt.setTransferStatus(3);
        var authUser = authService.getCurrentUser();
//        Update Purchase Requisition Item Transfer
        List<PurchaseRequisitionItemTransfer> priTransferList = new ArrayList<>();
        for (var item : request.getItems()) {
            var prtItem = priTransferRepo.findById(item.getPurchaseRequisitionItemTransferId()).orElseThrow(PurchaseRequisitionItemTransferNotFoundException::new);
            prtItem = getPurchaseRequisitionItemTransfer(prtItem, item);
            priTransferList.add(prtItem);
        }
        prt.getItems().clear();
        prt.getItems().addAll(priTransferList);
//        Insert Purchase and Purchase details table for Main Branch
        Purchase purchase = new Purchase();
        PurchaseReferenceDto purchaseReference=generatePurchaseRef();
        purchase.setPurchaseRef(purchaseReference.getPurchaseRef());
        purchase.setRequsitionRef(prt.getPurchaseRequisition().getRequsitionRef());
        purchase.setPurchaseRequisitionTransfer(prt);
        purchase.setPurchaseRequisition(prt.getPurchaseRequisition());
        purchase.setPurchaseType(prt.getPurchaseRequisition().getPurchaseType());
        purchase.setOrganization(prt.getPurchaseRequisition().getOrganization());
        purchase.setBranch(prt.getPurchaseRequisition().getRootBranch());
        purchase.setPurchaseBatchNo(purchaseReference.getPurchaseBatchNo());
        purchase.setOverallDiscount(prt.getOverallDiscount());
        purchase.setPurchaseInvoices(prt.getPurchaseInvoices());
        purchase.setPurchaseInvoiceDocs(prt.getPurchaseInvoiceDocs());
        purchase.setOrderRefs(prt.getPurchaseRequisition().getOrderRefs());
        purchase.setAddedBy(authUser);
//        Purchase Details
        List<PurchaseDetail> purchaseDetailList = new ArrayList<>();
        for (var priTransfer : priTransferList) {
            PurchaseDetail pd = getPurchaseDetails(priTransfer, authUser, purchase);
            purchaseDetailList.add(pd);
        }
        purchase.setItems(purchaseDetailList);
        purchase = purchaseRepo.save(purchase);
//        Inventory Summary add here
        List<InventorySummary> inventorySummaryList = new ArrayList<>();
        for (var purchaseDetail : purchaseDetailList) {
            InventorySummary invSummary = getInventorySummary(purchaseDetail, purchase);
            invSummary.setPurchase(purchase);
            invSummary.setPurchaseDetail(purchaseDetail);
            invSummary.setInvoiceType(InvoiceType.PURCHASE);
            Integer qty = purchaseDetail.getPurchaseQty();
            if (purchaseDetail.getGiftOrBonusQty() != null) {
                qty = qty + purchaseDetail.getGiftOrBonusQty();
            }
            invSummary.setStockIn(qty);
            inventorySummaryList.add(invSummary);
        }
        invSummaryRepo.saveAll(inventorySummaryList);
//        Dispatch to Destination Branch
//        Here have Stock out from purchase table
        List<InventorySummary> inventorySummaryListForDispatchSend = new ArrayList<>();
        for (var purchaseDetail : purchaseDetailList) {
            InventorySummary invSummary = getInventorySummary(purchaseDetail, purchase);
            invSummary.setPurchase(purchase);
            invSummary.setPurchaseDetail(purchaseDetail);
            invSummary.setInvoiceType(InvoiceType.DISPATCH_SEND);
            Integer qty = purchaseDetail.getPurchaseQty();
            if (purchaseDetail.getGiftOrBonusQty() != null) {
                qty = qty + purchaseDetail.getGiftOrBonusQty();
            }
            invSummary.setStockOut(qty);
            inventorySummaryListForDispatchSend.add(invSummary);
        }
        invSummaryRepo.saveAll(inventorySummaryListForDispatchSend);
        Dispatch dispatch = new Dispatch();
        dispatch.setPurchase(purchase);
        dispatch.setDispatchInvoice(generateDispatchInvoice());
        dispatch.setOrganization(purchase.getOrganization());
        dispatch.setRootBranch(purchase.getBranch());//Main Branch
        dispatch.setSenderBranch(purchase.getBranch());
        dispatch.setWarehouse(null);
        dispatch.setSupplier(purchase.getSupplier());
        dispatch.setDispatchBy(authUser);
        //        Dispatch Details
        List<DispatchDetail> dispatchDetailsList = new ArrayList<>();
        for (var purchaseDetail : purchaseDetailList) {
            DispatchDetail dispatchDetail = new DispatchDetail();
            dispatchDetail.setDispatch(dispatch);
            dispatchDetail.setPurchase(purchase);
            dispatchDetail.setPurchaseDetail(purchaseDetail);
            dispatchDetail.setProduct(purchaseDetail.getProduct());
            dispatchDetail.setProductDetail(purchaseDetail.getProductDetail());
            Integer dispatchQty = purchaseDetail.getPurchaseQty();
            if (purchaseDetail.getGiftOrBonusQty() != null) {
                dispatchQty = dispatchQty + purchaseDetail.getGiftOrBonusQty();
            }
            dispatchDetail.setDispatchQty(dispatchQty);
            dispatchDetailsList.add(dispatchDetail);
        }
        dispatch.setItems(dispatchDetailsList);
        dispatch = dispatchRepo.save(dispatch);
        List<InventorySummary> inventorySummaryListForDispatchReceive = new ArrayList<>();
        for (var dispatchDetail : dispatchDetailsList) {
            var purchaseDetails = dispatchDetail.getPurchaseDetail();
            InventorySummary invSummaryForDispatchReceive = new InventorySummary();
            invSummaryForDispatchReceive.setPurchase(purchase);
            invSummaryForDispatchReceive.setPurchaseDetail(purchaseDetails);
            invSummaryForDispatchReceive.setInvoiceType(InvoiceType.DISPATCH_RECEIVE);
            invSummaryForDispatchReceive.setInvoiceId(dispatch.getId());
            invSummaryForDispatchReceive.setInvoiceDetailId(dispatchDetail.getId());
            invSummaryForDispatchReceive.setBranch(dispatch.getReceiverBranch());
            invSummaryForDispatchReceive.setInvoiceDate(dispatch.getDispatchAt());
            invSummaryForDispatchReceive.setPurchaseRef(dispatch.getDispatchInvoice());
            invSummaryForDispatchReceive.setPurchaseBatchNo(purchase.getPurchaseBatchNo());

            invSummaryForDispatchReceive.setProductionBatchNo(purchaseDetails.getProductionBatchNo());
            invSummaryForDispatchReceive.setManufactureDate(purchaseDetails.getManufactureDate());
            invSummaryForDispatchReceive.setExpiredDate(purchaseDetails.getExpiredDate());
            invSummaryForDispatchReceive.setProduct(purchaseDetails.getProduct());
            invSummaryForDispatchReceive.setProductDetail(purchaseDetails.getProductDetail());
            invSummaryForDispatchReceive.setOrganization(dispatch.getOrganization());
            invSummaryForDispatchReceive.setWarehouse(dispatch.getWarehouse());
            invSummaryForDispatchReceive.setSupplier(dispatch.getSupplier());
            invSummaryForDispatchReceive.setStockIn(dispatchDetail.getDispatchQty());
            inventorySummaryListForDispatchReceive.add(invSummaryForDispatchReceive);
        }
        invSummaryRepo.saveAll(inventorySummaryListForDispatchReceive);
        prTransferRepo.save(prt);
        return prTransferMapper.toDto(prt);
    }

    private InventorySummary getInventorySummary(PurchaseDetail purchaseDetail, Purchase purchase) {
        InventorySummary invSummary = new InventorySummary();
        invSummary.setInvoiceId(purchase.getId());
        invSummary.setInvoiceDetailId(purchaseDetail.getId());
        invSummary.setBranch(purchase.getBranch());
        invSummary.setInvoiceDate(purchase.getPurchaseAt());
        invSummary.setPurchaseRef(purchase.getPurchaseRef());
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

    private PurchaseDetail getPurchaseDetails(PurchaseRequisitionItemTransfer priTransfer, User authUser, Purchase purchase) {
        PurchaseDetail pd = new PurchaseDetail();
        pd.setPurchaseRequisition(priTransfer.getPurchaseRequisition());
        pd.setPurchaseRequisitionItem(priTransfer.getPurchaseRequisitionItem());
        pd.setProduct(priTransfer.getProduct());
        pd.setProductDetail(priTransfer.getProductDetail());
        pd.setPurchasePrice(priTransfer.getPurchasePrice());
        pd.setMrpPrice(priTransfer.getMrpPrice());
        pd.setPurchaseDiscount(priTransfer.getDiscountPrice());
        pd.setPurchaseQty(priTransfer.getPurchaseQty());
        pd.setGiftOrBonusQty(priTransfer.getGiftOrBonusQty());
        pd.setAtomQty(priTransfer.getProductDetail().getAtomQty());
        pd.setProductionBatchNo(priTransfer.getProductionBatchNo());
        pd.setManufactureDate(priTransfer.getManufactureDate());
        pd.setExpiredDate(priTransfer.getExpiredDate());
        pd.setAddedBy(authUser);
        pd.setPurchase(purchase);
        return pd;
    }

    public PurchaseRequisitionTransferDto updateFromMedicineCorner(Long purchaseRequisitionTransferId, UpdateFromMedicineCornerRequest request) {
        var prt = prTransferRepo.findById(purchaseRequisitionTransferId).orElseThrow(PurchaseRequisitionNotFoundException::new);
        return prTransferMapper.toDto(prt);
    }

    public PurchaseRequisitionTransferDto updateItemFromMedicineCorner(Long purchaseRequisitionTransferId, Long purchaseRequisitionItemTransferId, UpdateFromMedicineCornerRequest request) {
        var prt = prTransferRepo.findById(purchaseRequisitionTransferId).orElseThrow(PurchaseRequisitionNotFoundException::new);
        var purchaseFind = purchaseRepo.findByPurchaseRequisitionTransfer(prt).orElse(null);
        if (prt.getTransferStatus() == 3 || purchaseFind != null) {
            throw new PurchaseRequisitionException("As you Already Added to Inventory. Not to possible update");
        }
        var prtItem = priTransferRepo.findById(purchaseRequisitionItemTransferId).orElseThrow(PurchaseRequisitionItemTransferNotFoundException::new);
        prtItem = getPurchaseRequisitionItemTransfer(prtItem, request);
        priTransferRepo.save(prtItem);
        return prTransferMapper.toDto(prt);
    }

    public PurchaseRequisitionItemTransfer getPurchaseRequisitionItemTransfer(PurchaseRequisitionItemTransfer prtItem, UpdateFromMedicineCornerRequest request) {
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
    public String generateTripRef() {
        VehicleTrip vehicleTrip = vehicleTripRepository.findTopByOrderByCreatedAtDesc();
        if (vehicleTrip == null) {
            vehicleTrip = new VehicleTrip();

        }
        return vehicleTrip.getGeneratedTripRef();
    }
    public String generateDispatchInvoice() {
        Dispatch dispatch = dispatchRepo.findTopByOrderByDispatchAtDesc();
        if (dispatch == null) {
            dispatch = new Dispatch();

        }
        return dispatch.getGenerateDispatchInvoice();
    }
    public String generateGoodsRef() {
        GoodsOnTrip goodsOnTrip = goodsOnTripRepository.findTopByOrderByCreatedAtDesc();
        if (goodsOnTrip == null) {
            goodsOnTrip = new GoodsOnTrip();
        }
        return goodsOnTrip.getGeneratedGoodsRef();
    }
}
