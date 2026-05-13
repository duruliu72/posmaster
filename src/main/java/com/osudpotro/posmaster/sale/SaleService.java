//package com.osudpotro.posmaster.sale;
//
//import com.osudpotro.posmaster.branch.Branch;
//import com.osudpotro.posmaster.branch.BranchNotFoundException;
//import com.osudpotro.posmaster.branch.BranchRepository;
//import com.osudpotro.posmaster.common.EntityNotFoundException;
//import com.osudpotro.posmaster.deliverymethod.DeliveryMethod;
//import com.osudpotro.posmaster.deliverymethod.DeliveryMethodRepository;
//import com.osudpotro.posmaster.inventory.Inventory;
//import com.osudpotro.posmaster.inventory.InventoryRepository;
//import com.osudpotro.posmaster.inventory.InvoiceType;
//import com.osudpotro.posmaster.salecart.*;
//import com.osudpotro.posmaster.user.auth.AuthService;
//import com.osudpotro.posmaster.user.customer.CustomerRepository;
//import jakarta.transaction.Transactional;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.stereotype.Service;
//import com.osudpotro.posmaster.security.UnauthorizedException;
//import com.osudpotro.posmaster.role.Role;
//
//import java.text.SimpleDateFormat;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//@Slf4j
//@Service
//public class SaleService {
//
//    @Autowired
//    private SaleRepository saleRepo;
//
//    @Autowired
//    private SaleItemRepository saleItemRepo;
//
//    @Autowired
//    private SalePaymentRepository salePaymentRepo;
//
//    @Autowired
//    private SaleCartRepository saleCartRepo;
//
//    @Autowired
//    private SaleCartItemRepository saleCartItemRepo;
//
//    @Autowired
//    private InventoryRepository inventoryRepo;
//
//    @Autowired
//    private BranchRepository branchRepo;
//
//    @Autowired
//    private AuthService authService;
//
//    @Autowired
//    private SaleMapper saleMapper;
//
//    @Autowired
//    private DeliveryMethodRepository deliveryMethodRepo;
//
//
//    @Autowired
//    private CustomerRepository customerRepository;
//
//    /**
//     * CHECKOUT — Move SaleCart items to Sale + SaleItem and update Inventory
//     */
//    @Transactional
//    public SaleDto checkoutSaleCart(Long saleCartId, SaleCheckoutRequest request) {
//        var authUser = authService.getCurrentUser();
//        Branch branch = branchRepo.findById(authUser.getBranch().getId())
//                .orElseThrow(BranchNotFoundException::new);
//
//        // 1. Find SaleCart
//        SaleCart saleCart = saleCartRepo.findByIdAndBranch(saleCartId, branch)
//                .orElseThrow(() -> new EntityNotFoundException("Sale Cart Not Found"));
//
//        // 2. Validate cart not empty
//        if (saleCart.getItems() == null || saleCart.getItems().isEmpty()) {
//            throw new EntityNotFoundException("Cart is empty");
//        }
//
//
//        // 3. Update SaleCart with customer info from frontend
//        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
//            saleCart.setEmail(request.getEmail());
//        }
//        if (request.getMobile() != null && !request.getMobile().isEmpty()) {
//            saleCart.setMobile(request.getMobile());
//        }
//        saleCartRepo.save(saleCart);
//
//        // 4. Create Sale
//        Sale sale = new Sale();
//        sale.setSaleRef(generateSaleRef());
//
//        // Inside checkoutSaleCart method, after creating Sale:
//// Link customer by email
//        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
//            var customerOpt = customerRepository.findByEmail(request.getEmail());
//            if (customerOpt.isPresent() && customerOpt.get().getUser() != null) {
//                sale.setCustomer(customerOpt.get().getUser());
//            }
//        }
//
//// Link customer by mobile if not found by email
//        if (sale.getCustomer() == null && request.getMobile() != null && !request.getMobile().isEmpty()) {
//            var customerOpt = customerRepository.findByMobile(request.getMobile());
//            if (customerOpt.isPresent() && customerOpt.get().getUser() != null) {
//                sale.setCustomer(customerOpt.get().getUser());
//            }
//        }
//
//        //  FIXED: Payment method
//        try {
//            if (request.getPaymentMethod() != null && !request.getPaymentMethod().isEmpty()) {
//                sale.setPaymentMethod(PaymentMethod.fromCode(request.getPaymentMethod()));
//            } else {
//                sale.setPaymentMethod(PaymentMethod.COD); // default
//            }
//        } catch (IllegalArgumentException e) {
//            log.warn("Invalid payment method: {}, defaulting to COD", request.getPaymentMethod());
//            sale.setPaymentMethod(PaymentMethod.COD);
//        }
//
//        sale.setOrganization(branch.getOrganization());
//        sale.setBranch(branch);
//
//        // Status
//        sale.setSaleStatus(request.getSaleStatus() != null ? request.getSaleStatus() : 1);
//        sale.setPaymentStatus(request.getPaymentStatus() != null ? request.getPaymentStatus() : 1);
//        sale.setSaleType(request.getSaleType() != null ? request.getSaleType() : 1);
//        sale.setSaleChannel(request.getSaleChannel() != null ? request.getSaleChannel() : 1);
//
//        // Address from frontend form
//        sale.setBillingAddress(request.getAddress());
//        sale.setDeliveryAddress(
//                (request.getAddress() != null ? request.getAddress() : "") +
//                        (request.getHouseOrFlatNo() != null ? ", " + request.getHouseOrFlatNo() : "") +
//                        (request.getSpecialInstruction() != null ? " (" + request.getSpecialInstruction() + ")" : "")
//        );
//
//        // Delivery & Offers
//        if (request.getDeliveryMethodId() != null) {
//            DeliveryMethod deliveryMethod = deliveryMethodRepo.findById(request.getDeliveryMethodId()).orElse(null);
//            sale.setDeliveryMethod(deliveryMethod);
//        }
//        sale.setDeliveryFee(request.getDeliveryFee());
//        sale.setVatAmount(request.getVatAmount());
//        sale.setPrescriptionDocs(request.getPrescriptionDocs());
//
//        // Sale personnel
//        sale.setSalePointMan(authUser);
//        sale.setCreatedBy(authUser);
//
//        // 5. Move CartItems → SaleItems + Update Inventory
//        List<SaleItem> saleItems = new ArrayList<>();
//        List<Inventory> inventoryList = new ArrayList<>();
//
//        for (SaleCartItem cartItem : saleCart.getItems()) {
//            SaleItem saleItem = saleMapper.toSaleItemEntity(cartItem, sale);
//            saleItems.add(saleItem);
//
//            // Inventory Stock Out
//            Inventory stockOut = new Inventory();
//            stockOut.setInvoiceId(sale.getId());
//            stockOut.setInvoiceType(InvoiceType.SALE);
//            stockOut.setPurchase(cartItem.getPurchase());
//            stockOut.setPurchaseDetail(cartItem.getPurchaseDetail());
//            stockOut.setStockOut(cartItem.getSaleQty());
//            stockOut.setBranch(branch);
//            stockOut.setInvoiceDate(LocalDateTime.now());
//            stockOut.setProduct(cartItem.getPurchaseDetail().getProduct());
//            stockOut.setProductDetail(cartItem.getPurchaseDetail().getProductDetail());
//            stockOut.setPurchaseBatchNo(cartItem.getPurchase().getPurchaseBatchNo());
//            stockOut.setProductionBatchNo(cartItem.getPurchaseDetail().getProductionBatchNo());
//            stockOut.setPurchaseBarCode(cartItem.getPurchaseDetail().getPurchaseBarCode());
//            stockOut.setOrganization(branch.getOrganization());
//            stockOut.setWarehouse(sale.getWarehouse());
//
//            inventoryList.add(stockOut);
//        }
//
//        // 6. Save Sale first
//        sale = saleRepo.save(sale);
//
//        // 7. Save SaleItems with sale reference
//        for (SaleItem item : saleItems) {
//            item.setSale(sale);
//        }
//        saleItemRepo.saveAll(saleItems);
//
//        // 8. Set invoiceDetailId and save Inventory
//        for (int i = 0; i < inventoryList.size(); i++) {
//            inventoryList.get(i).setInvoiceId(sale.getId());
//            inventoryList.get(i).setInvoiceDetailId(saleItems.get(i).getId());
//        }
//        inventoryRepo.saveAll(inventoryList);
//
//        // 9. Save Payment
//        if (request.getPaymentMethod() != null && !request.getPaymentMethod().isEmpty()) {
//            try {
//                SalePayment payment = new SalePayment();
//                payment.setSale(sale);
//                payment.setSaleRef(sale.getSaleRef());
//                payment.setPaymentMethod(PaymentMethod.fromCode(request.getPaymentMethod()));
//                payment.setTrxId(request.getTrxId());
//                payment.setCreditAmount(request.getCreditAmount());
//                payment.setDebitAmount(request.getDebitAmount());
//                payment.setTransactionType("1");
//                salePaymentRepo.save(payment);
//            } catch (IllegalArgumentException e) {
//                log.warn("Invalid payment method for payment record: {}", request.getPaymentMethod());
//            }
//        }
//
//        // 10. Clear SaleCart
//        saleCart.setStatus(3);
//        saleCartRepo.save(saleCart);
//
//        // 11. Return Sale with items
//        sale.setItems(saleItems);
//        return saleMapper.toDto(sale);
//    }
//
//    private String generateSaleRef() {
//        Sale sale = saleRepo.findTopByOrderByCreatedAtDesc();
//        String prefix = "SAL";
//        String datePart = new SimpleDateFormat("yyyyMMdd").format(new Date());
//        long nextSeq = 1;
//        if (sale != null && sale.getSaleRef() != null) {
//            String lastRef = sale.getSaleRef();
//            String lastPart = lastRef.length() > 5 ? lastRef.substring(lastRef.length() - 6) : lastRef;
//            if (!lastPart.isEmpty()) {
//                try {
//                    nextSeq = Long.parseLong(lastPart) + 1;
//                } catch (Exception e) {
//                    log.error("e: ", e);
//                }
//            }
//        }
//        return String.format("%s-%s-%06d", prefix, datePart, nextSeq);
//    }
//
//    public Page<SaleDto> filterSales(SaleFilter filter, Pageable pageable) {
//        var authUser = authService.getCurrentUser();
//        return saleRepo.findAll(SaleSpecification.filter(filter, authUser), pageable)
//                .map(saleMapper::toDto);
//    }
//
//    public SaleDto getSale(Long saleId) {
//        var authUser = authService.getCurrentUser();
//        Branch branch = authUser.getBranch();
//        Sale sale = saleRepo.findByIdAndBranch(saleId, branch)
//                .orElseThrow(() -> new EntityNotFoundException("Sale not found"));
//        return saleMapper.toDto(sale);
//    }
//
//    public List<SaleDto> getAllSales() {
//        var authUser = authService.getCurrentUser();
//        return saleRepo.findAll(SaleSpecification.filter(new SaleFilter(), authUser),
//                        PageRequest.of(0, 10, Sort.by("createdAt").descending()))
//                .map(saleMapper::toDto)
//                .toList();
//    }
//}
package com.osudpotro.posmaster.sale;

import com.osudpotro.posmaster.branch.Branch;
import com.osudpotro.posmaster.branch.BranchNotFoundException;
import com.osudpotro.posmaster.branch.BranchRepository;
import com.osudpotro.posmaster.common.EntityNotFoundException;
import com.osudpotro.posmaster.deliverymethod.DeliveryMethod;
import com.osudpotro.posmaster.deliverymethod.DeliveryMethodRepository;
import com.osudpotro.posmaster.inventory.Inventory;
import com.osudpotro.posmaster.inventory.InventoryRepository;
import com.osudpotro.posmaster.inventory.InvoiceType;
import com.osudpotro.posmaster.role.Role;
import com.osudpotro.posmaster.salecart.*;
import com.osudpotro.posmaster.security.UnauthorizedException;
import com.osudpotro.posmaster.user.User;
import com.osudpotro.posmaster.user.auth.AuthService;
import com.osudpotro.posmaster.user.customer.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepo;

    @Autowired
    private SaleItemRepository saleItemRepo;

    @Autowired
    private SalePaymentRepository salePaymentRepo;

    @Autowired
    private SaleCartRepository saleCartRepo;

    @Autowired
    private SaleCartItemRepository saleCartItemRepo;

    @Autowired
    private InventoryRepository inventoryRepo;

    @Autowired
    private BranchRepository branchRepo;

    @Autowired
    private AuthService authService;

    @Autowired
    private SaleMapper saleMapper;

    @Autowired
    private DeliveryMethodRepository deliveryMethodRepo;

    @Autowired
    private CustomerRepository customerRepository;


    // ==================== CHECKOUT ====================

    @Transactional
    public SaleDto checkoutSaleCart(Long saleCartId, SaleCheckoutRequest request) {
        var authUser = authService.getCurrentUser();
        Branch branch = branchRepo.findById(authUser.getBranch().getId())
                .orElseThrow(BranchNotFoundException::new);

        // 1. Find SaleCart
        SaleCart saleCart = saleCartRepo.findByIdAndBranch(saleCartId, branch)
                .orElseThrow(() -> new EntityNotFoundException("Sale Cart Not Found"));

        // 2. Validate cart not empty
        if (saleCart.getItems() == null || saleCart.getItems().isEmpty()) {
            throw new EntityNotFoundException("Cart is empty");
        }

        // 3. Update SaleCart with customer info from frontend
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            saleCart.setEmail(request.getEmail());
        }
        if (request.getMobile() != null && !request.getMobile().isEmpty()) {
            saleCart.setMobile(request.getMobile());
        }
        saleCartRepo.save(saleCart);

        // 4. Create Sale
        Sale sale = new Sale();
        sale.setSaleRef(generateSaleRef());

        // Link customer by email
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            var customerOpt = customerRepository.findByEmail(request.getEmail());
            if (customerOpt.isPresent() && customerOpt.get().getUser() != null) {
                sale.setCustomer(customerOpt.get().getUser());
            }
        }

        // Link customer by mobile if not found by email
        if (sale.getCustomer() == null && request.getMobile() != null && !request.getMobile().isEmpty()) {
            var customerOpt = customerRepository.findByMobile(request.getMobile());
            if (customerOpt.isPresent() && customerOpt.get().getUser() != null) {
                sale.setCustomer(customerOpt.get().getUser());
            }
        }

        // Payment method
        try {
            if (request.getPaymentMethod() != null && !request.getPaymentMethod().isEmpty()) {
                sale.setPaymentMethod(PaymentMethod.fromCode(request.getPaymentMethod()));
            } else {
                sale.setPaymentMethod(PaymentMethod.COD);
            }
        } catch (IllegalArgumentException e) {
            log.warn("Invalid payment method: {}, defaulting to COD", request.getPaymentMethod());
            sale.setPaymentMethod(PaymentMethod.COD);
        }

        sale.setOrganization(branch.getOrganization());
        sale.setBranch(branch);

        // Status
        sale.setSaleStatus(1);  // Pending
        sale.setPaymentStatus(1); // Pending
        sale.setSaleType(request.getSaleType() != null ? request.getSaleType() : 1);
        sale.setSaleChannel(request.getSaleChannel() != null ? request.getSaleChannel() : 1);

        // Address from frontend form
        sale.setBillingAddress(request.getAddress());
        sale.setDeliveryAddress(
                (request.getAddress() != null ? request.getAddress() : "") +
                        (request.getHouseOrFlatNo() != null ? ", " + request.getHouseOrFlatNo() : "") +
                        (request.getSpecialInstruction() != null ? " (" + request.getSpecialInstruction() + ")" : "")
        );

        // Delivery & Offers
        if (request.getDeliveryMethodId() != null) {
            DeliveryMethod deliveryMethod = deliveryMethodRepo.findById(request.getDeliveryMethodId()).orElse(null);
            sale.setDeliveryMethod(deliveryMethod);
        }
        sale.setDeliveryFee(request.getDeliveryFee());
        sale.setVatAmount(request.getVatAmount());
        sale.setPrescriptionDocs(request.getPrescriptionDocs());

        // Sale personnel
        sale.setSalePointMan(authUser);
        sale.setCreatedBy(authUser);

        // 5. Move CartItems → SaleItems + Update Inventory
        List<SaleItem> saleItems = new ArrayList<>();
        List<Inventory> inventoryList = new ArrayList<>();

        for (SaleCartItem cartItem : saleCart.getItems()) {
            SaleItem saleItem = saleMapper.toSaleItemEntity(cartItem, sale);
            saleItems.add(saleItem);

            // Inventory Stock Out
            Inventory stockOut = new Inventory();
            stockOut.setInvoiceId(sale.getId());
            stockOut.setInvoiceType(InvoiceType.SALE);
            stockOut.setPurchase(cartItem.getPurchase());
            stockOut.setPurchaseDetail(cartItem.getPurchaseDetail());
            stockOut.setStockOut(cartItem.getSaleQty());
            stockOut.setBranch(branch);
            stockOut.setInvoiceDate(LocalDateTime.now());
            stockOut.setProduct(cartItem.getPurchaseDetail().getProduct());
            stockOut.setProductDetail(cartItem.getPurchaseDetail().getProductDetail());
            stockOut.setPurchaseBatchNo(cartItem.getPurchase().getPurchaseBatchNo());
            stockOut.setProductionBatchNo(cartItem.getPurchaseDetail().getProductionBatchNo());
            stockOut.setPurchaseBarCode(cartItem.getPurchaseDetail().getPurchaseBarCode());
            stockOut.setOrganization(branch.getOrganization());
            stockOut.setWarehouse(sale.getWarehouse());

            inventoryList.add(stockOut);
        }

        // 6. Save Sale first
        sale = saleRepo.save(sale);

        // 7. Save SaleItems with sale reference
        for (SaleItem item : saleItems) {
            item.setSale(sale);
        }
        saleItemRepo.saveAll(saleItems);

        // 8. Set invoiceDetailId and save Inventory
        for (int i = 0; i < inventoryList.size(); i++) {
            inventoryList.get(i).setInvoiceId(sale.getId());
            inventoryList.get(i).setInvoiceDetailId(saleItems.get(i).getId());
        }
        inventoryRepo.saveAll(inventoryList);

        // 9. Save Payment
        if (request.getPaymentMethod() != null && !request.getPaymentMethod().isEmpty()) {
            try {
                SalePayment payment = new SalePayment();
                payment.setSale(sale);
                payment.setSaleRef(sale.getSaleRef());
                payment.setPaymentMethod(PaymentMethod.fromCode(request.getPaymentMethod()));
                payment.setTrxId(request.getTrxId());
                payment.setCreditAmount(request.getCreditAmount());
                payment.setDebitAmount(request.getDebitAmount());
                payment.setTransactionType("1");
                salePaymentRepo.save(payment);
            } catch (IllegalArgumentException e) {
                log.warn("Invalid payment method for payment record: {}", request.getPaymentMethod());
            }
        }

        // 10. Clear SaleCart
        saleCart.setStatus(3);
        saleCartRepo.save(saleCart);

        // 11. Return Sale with items
        sale.setItems(saleItems);
        return saleMapper.toDto(sale);
    }

    // ==================== STATUS WORKFLOW ====================

    /**
     * Update Sale Status with department validation
     */
    @Transactional
    public SaleDto updateSaleStatus(Long saleId, SaleStatusUpdateRequest request) {
        var authUser = authService.getCurrentUser();
        var sale = saleRepo.findById(saleId)
                .orElseThrow(() -> new EntityNotFoundException("Sale not found with ID: " + saleId));

        int currentStatus = sale.getSaleStatus();
        int newStatus = request.getSaleStatus();


        // ✅ CHECK PAYMENT BEFORE DELIVERY
        if (newStatus == 6 && sale.getPaymentStatus() != 3) {
            throw new UnauthorizedException("Payment must be PAID before delivery!");
        }

        // Validate status transition based on user role
        validateStatusTransition(authUser, currentStatus, newStatus);

        sale.setSaleStatus(newStatus);

        // Set department-specific user based on new status
        switch (newStatus) {
            case 2: // Processing → Customer Care
                sale.setCustomerCareMan(authUser);
                sale.setCustomerCareAt(LocalDateTime.now());
                break;
            case 3: // Accepted by Pharmacy
            case 4: // Packaging by Pharmacy
                sale.setSaleMan(authUser);
                break;
            case 5: // Dispatch / On the way
                sale.setDeliveryMan(authUser);
                sale.setDeliveryAt(LocalDateTime.now());
                break;
            case 6: // Delivered
                sale.setDeliveryAt(LocalDateTime.now());
                break;
            case 7: // Cancelled
                break;
        }

        sale.setUpdatedBy(authUser);
        saleRepo.save(sale);
        return saleMapper.toDto(sale);
    }

    /**
     * Validate if user has permission for this status transition
     */
    private void validateStatusTransition(User user, int currentStatus, int newStatus) {
        String userRole = user.getRoles().stream()
                .map(Role::getRoleKey)
                .findFirst()
                .orElse("");

        boolean isAdmin = userRole.contains("SUPER_ADMIN") || userRole.contains("ADMIN");
        boolean isCustomerCare = userRole.contains("CUSTOMER_CARE");
        boolean isPharmacy = userRole.contains("PHARMACY");
        boolean isRider = userRole.contains("RIDER") || userRole.contains("FLEET");

        // Admin can do anything
        if (isAdmin) return;

        // Customer Care: 1 → 2 (Pending → Processing)
        if (currentStatus == 1 && newStatus == 2) {
            if (!isCustomerCare)
                throw new UnauthorizedException("Only Customer Care can process orders");
            return;
        }

        // Pharmacy: 2 → 3 (Processing → Accepted by Pharmacy)
        if (currentStatus == 2 && newStatus == 3) {
            if (!isPharmacy)
                throw new UnauthorizedException("Only Pharmacy can accept orders");
            return;
        }

        // Pharmacy: 3 → 4 (Accepted → Packaging)
        if (currentStatus == 3 && newStatus == 4) {
            if (!isPharmacy)
                throw new UnauthorizedException("Only Pharmacy can package orders");
            return;
        }

        // Rider/Fleet: 4 → 5 (Packaging → Dispatch/On the way)
        if (currentStatus == 4 && newStatus == 5) {
            if (!isRider)
                throw new UnauthorizedException("Only Rider/Fleet can dispatch orders");
            return;
        }

        // Rider: 5 → 6 (On the way → Delivered)
        if (currentStatus == 5 && newStatus == 6) {
            if (!isRider)
                throw new UnauthorizedException("Only Rider can mark as delivered");
            return;
        }

        // Anyone can cancel (except already delivered)
        if (newStatus == 7 && currentStatus != 6) {
            return;
        }

        throw new UnauthorizedException(
                "Invalid status transition from " + currentStatus + " to " + newStatus);
    }

    /**
     * Update Payment Status
     */
    @Transactional
    public SaleDto updatePaymentStatus(Long saleId, SalePaymentStatusUpdateRequest request) {
        var authUser = authService.getCurrentUser();
        var sale = saleRepo.findById(saleId)
                .orElseThrow(() -> new EntityNotFoundException("Sale not found with ID: " + saleId));

        sale.setPaymentStatus(request.getPaymentStatus());
        sale.setUpdatedBy(authUser);
        saleRepo.save(sale);
        return saleMapper.toDto(sale);
    }

    // ==================== QUERIES ====================

    public Page<SaleDto> filterSales(SaleFilter filter, Pageable pageable) {
        var authUser = authService.getCurrentUser();
        return saleRepo.findAll(SaleSpecification.filter(filter, authUser), pageable)
                .map(saleMapper::toDto);
    }

    public SaleDto getSale(Long saleId) {
        var authUser = authService.getCurrentUser();
        Branch branch = authUser.getBranch();
        Sale sale = saleRepo.findByIdAndBranch(saleId, branch)
                .orElseThrow(() -> new EntityNotFoundException("Sale not found"));
        return saleMapper.toDto(sale);
    }

    public List<SaleDto> getAllSales() {
        var authUser = authService.getCurrentUser();
        return saleRepo.findAll(SaleSpecification.filter(new SaleFilter(), authUser),
                        PageRequest.of(0, 10, Sort.by("createdAt").descending()))
                .map(saleMapper::toDto)
                .toList();
    }

    // ==================== HELPERS ====================

    private String generateSaleRef() {
        Sale sale = saleRepo.findTopByOrderByCreatedAtDesc();
        String prefix = "SAL";
        String datePart = new SimpleDateFormat("yyyyMMdd").format(new Date());
        long nextSeq = 1;
        if (sale != null && sale.getSaleRef() != null) {
            String lastRef = sale.getSaleRef();
            String lastPart = lastRef.length() > 5 ? lastRef.substring(lastRef.length() - 6) : lastRef;
            if (!lastPart.isEmpty()) {
                try {
                    nextSeq = Long.parseLong(lastPart) + 1;
                } catch (Exception e) {
                    log.error("e: ", e);
                }
            }
        }
        return String.format("%s-%s-%06d", prefix, datePart, nextSeq);
    }
}