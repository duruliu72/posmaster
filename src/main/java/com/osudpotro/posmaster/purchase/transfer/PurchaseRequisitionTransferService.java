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
import com.osudpotro.posmaster.tms.goodsontrip.GoodsOnTrip;
import com.osudpotro.posmaster.tms.goodsontrip.GoodsOnTripRepository;
import com.osudpotro.posmaster.tms.goodsontrip.GoodsStatus;
import com.osudpotro.posmaster.tms.goodsontrip.GoodsType;
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
    private  PurchaseRequisitionTransferMapper prTransferMapper;
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
        return prTransferRepo.findAll(PurchaseRequisitionTransferSpecification.filter(filter), pageable).map(prTransferMapper::toDto);
    }
    public PurchaseRequisitionTransferWithItemPageResponse filterEntitiesWithItemPage(Long purchaseRequisitionTransferId,PurchaseRequisitionItemTransferFilter filter, Pageable pageable) {
        PurchaseRequisitionTransfer prt = prTransferRepo.findPurchaseRequisitionById(purchaseRequisitionTransferId).orElseThrow(PurchaseRequisitionNotFoundException::new);
        Page<PurchaseRequisitionItemTransfer> resultBase = priTransferRepo.findAll(PurchaseRequisitionItemTransferSpecification.filter(filter), pageable);
        Page<PurchaseRequisitionItemTransferDto> result = resultBase.map(priTransferMapper::toDto);
        prt.setItems(resultBase.getContent());
        return prTransferMapper.toMinDto(prt, result);
    }
    public Page<PurchaseRequisitionTransferDto> filterEntitiesByPurchaseRequisition(PurchaseRequisitionTransferFilter filter, Pageable pageable) {
        prRepo.findById(filter.getPurchaseRequisitionId()).orElseThrow(() -> new PurchaseRequisitionNotFoundException("PurchaseRequisition not found with ID: " + filter.getPurchaseRequisitionId()));
        return prTransferRepo.findAll(PurchaseRequisitionTransferSpecification.filter(filter), pageable).map(prTransferMapper::toDto);
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
    public PurchaseRequisitionTransferWithItemPageResponse getPurchaseRequisitionTransferWithItemPagination(Long purchaseRequisitionTransferId, Pageable pageable, PurchaseRequisitionItemTransferFilter filter) {
        PurchaseRequisitionTransfer pr = prTransferRepo.findPurchaseRequisitionById(purchaseRequisitionTransferId).orElseThrow(PurchaseRequisitionNotFoundException::new);
        Page<PurchaseRequisitionItemTransferDto> result = priTransferRepo.filterPurchaseRequisitionTransferItems(purchaseRequisitionTransferId, filter.getName(), pageable).map(priTransferMapper::toDto);
        return prTransferMapper.toMinDto(pr, result);
    }

    @Transactional
    public PurchaseRequisitionTransferDto assignToVehicle(Long purchaseRequisitionTransferId, AssignToVehicleRequest request) {
        var prt = prTransferRepo.findById(purchaseRequisitionTransferId).orElseThrow(PurchaseRequisitionNotFoundException::new);
//        if (r.getDeliveryStatus() != 1) {
//            throw new DuplicateVehicleException("Vehicle Trip already Assign");
//        }
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
        goodsOnTrip.setLoadedAt(LocalDateTime.now());
        goodsOnTrip.setAssignBy(authUser);
        goodsOnTrip.setCreatedBy(authUser);
        goodsOnTrip.setPurchaseRequisitionTransfer(prt);
        goodsOnTripRepository.save(goodsOnTrip);
        prTransferRepo.save(prt);
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
