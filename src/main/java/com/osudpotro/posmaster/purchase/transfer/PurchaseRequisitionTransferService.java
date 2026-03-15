package com.osudpotro.posmaster.purchase.transfer;

import com.osudpotro.posmaster.branch.Branch;
import com.osudpotro.posmaster.branch.BranchNotFoundException;
import com.osudpotro.posmaster.branch.BranchRepository;
import com.osudpotro.posmaster.common.Location;
import com.osudpotro.posmaster.organization.OrganizationRepository;
import com.osudpotro.posmaster.product.ProductDetailRepository;
import com.osudpotro.posmaster.product.ProductRepository;
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
import com.osudpotro.posmaster.user.auth.AuthService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PurchaseRequisitionTransferService {
    @Autowired
    private PurchaseRequisitionRepository prRepo;
    @Autowired
    private PurchaseRequisitionTransferRepository prTransferRepo;
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
        return prTransferRepo.findAll(PurchaseRequisitionTransferSpecification.filterByDelivered(filter), pageable).map(prTransferMapper::toDto);
    }
    public Page<PurchaseRequisitionTransferDto> filterEntitiesByPurchaseRequisition(PurchaseRequisitionTransferFilter filter,Long purchaseRequisitionId, Pageable pageable) {
        prRepo.findById(purchaseRequisitionId).orElseThrow(() -> new PurchaseRequisitionNotFoundException("PurchaseRequisition not found with ID: " + purchaseRequisitionId));
        return prTransferRepo.findAll(PurchaseRequisitionTransferSpecification.filterByPurchaseRequisition(filter,purchaseRequisitionId), pageable).map(prTransferMapper::toDto);
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
        Page<PurchaseRequisitionItemTransfer> resultBase = priTransferRepo.findAll(PurchaseRequisitionItemTransferSpecification.filterByPurchaseRequisitionTransfer(filter,purchaseRequisitionTransferId), pageable);
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
        var pr=prt.getPurchaseRequisition();
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
        Branch sourceBranch = branchRepository.findById(1L).orElseThrow(BranchNotFoundException::new);
        Branch destBranch = prt.getBranch();
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
        goodsOnTrip.setGoodsReference(prt.getPurchaseInvoices());
        goodsOnTrip.setGoodsReferenceDocs(prt.getPurchaseInvoiceDocs());
        goodsOnTrip.setGoodsStatus(GoodsStatus.ASSIGN_TO_VEHICLE);
        goodsOnTrip.setSourceAddress(request.getSourceAddress());
        goodsOnTrip.setDestAddress(request.getDestAddress());
        goodsOnTrip.setSourceBranch(sourceBranch);
        Location source = new Location();
        source.setLatitude(sourceBranch.getLatitude());
        source.setLongitude(sourceBranch.getLongitude());
        goodsOnTrip.setSource(source);
        goodsOnTrip.setDestBranch(destBranch);
        Location destination = new Location();
        destination.setLatitude(destBranch.getLatitude());
        destination.setLongitude(destBranch.getLongitude());
        goodsOnTrip.setDestination(destination);
//        goodsOnTrip.setLoadedAt(LocalDateTime.now());
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
        var pr=prt.getPurchaseRequisition();
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
        if (gooodsOnTrip.getIsReceived()!=null&&gooodsOnTrip.getIsReceived()) {
            throw new GoodsOnTripDeliveryException("Goods On Trip is Already Received!");
        }
        var authUser = authService.getCurrentUser();
        gooodsOnTrip.setIsReceived(true);
        gooodsOnTrip.setReceivedBy(authUser);
        prt.setTransferStatus(2);
        prTransferRepo.save(prt);
        //        update Purchase requsition tabel
        pr.setOrderRefs(pr.getOrderRefs());
        pr.setOrderRefs("");
        pr.setPurchaseInvoices(pr.getPurchaseInvoices());
        pr.setPurchaseInvoices("");
        pr.setPurchaseInvoiceDocs(pr.getPurchaseInvoiceDocs());
        pr.setPurchaseInvoiceDocs("");
        pr.setOverallDiscount(pr.getOverallDiscount());
        pr.setOverallDiscount(null);
        prRepo.save(pr);
        return prTransferMapper.toDto(prt);
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
