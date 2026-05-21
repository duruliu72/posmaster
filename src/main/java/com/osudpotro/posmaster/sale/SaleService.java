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
//import com.osudpotro.posmaster.offerhub.membership.MembershipRepository;
//import com.osudpotro.posmaster.role.Role;
//import com.osudpotro.posmaster.role.RoleRepository;
//import com.osudpotro.posmaster.salecart.*;
//import com.osudpotro.posmaster.security.UnauthorizedException;
//import com.osudpotro.posmaster.user.User;
//import com.osudpotro.posmaster.user.UserRepository;
//import com.osudpotro.posmaster.user.UserType;
//import com.osudpotro.posmaster.user.auth.AuthService;
//import com.osudpotro.posmaster.user.customer.Customer;
//import com.osudpotro.posmaster.user.customer.CustomerRepository;
//import com.osudpotro.posmaster.user.customer.address.Address;
//import com.osudpotro.posmaster.user.customer.address.AddressRepository;
//import jakarta.transaction.Transactional;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.text.SimpleDateFormat;
//import java.time.LocalDateTime;
//import java.util.*;
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
//    @Autowired
//    private CustomerRepository customerRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private RoleRepository roleRepository;
//
//    @Autowired
//    private MembershipRepository membershipRepo;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//    @Autowired
//    private AddressRepository addressRepo;
//    // ==================== CHECKOUT ====================
//
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
//        // ==================== CUSTOMER LINKING / AUTO-REGISTER ====================
//
//        // Try find existing customer by email
//        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
//            Customer customerOpt = customerRepository.findByEmail(request.getEmail()).orElse(null);
//            if (customerOpt != null) {
//                sale.setCustomer(customerOpt);
//                sale.setCustomerUser(customerOpt.getUser());
//            }
//        }
//
//        // Try find existing customer by mobile if not found by email
//        if (sale.getCustomer() == null && request.getMobile() != null && !request.getMobile().isEmpty()) {
//            var customerOpt = customerRepository.findByMobile(request.getMobile()).orElse(null);
//            if (customerOpt != null) {
//                sale.setCustomer(customerOpt);
//                sale.setCustomerUser(customerOpt.getUser());
//            }
//        }
//
//        // ✅ AUTO-REGISTER: If customer still not found, create new Customer + User
//        if (sale.getCustomer() == null
//                && request.getCustomerName() != null && !request.getCustomerName().isEmpty()
//                && request.getEmail() != null && !request.getEmail().isEmpty()
//                && request.getMobile() != null && !request.getMobile().isEmpty()) {
//
//            // Double-check doesn't exist
//            Customer existingCustomer = customerRepository.findByEmail(request.getEmail()).orElse(null);
//            if (existingCustomer == null) {
//                existingCustomer = customerRepository.findByMobile(request.getMobile()).orElse(null);
//            }
//
//            if (existingCustomer == null) {
//                // Create new User
//                User newUser = new User();
//                newUser.setUserType(UserType.CUSTOMER);
//                newUser.setCreatedBy(authUser);
//                newUser.setBranch(branch);
//
//                // Assign ROLE_CUSTOMER
//                Role customerRole = roleRepository.findByRoleKey("ROLE_CUSTOMER")
//                        .orElseGet(() -> {
//                            Role role = new Role();
//                            role.setName("Customer");
//                            role.setRoleKey("ROLE_CUSTOMER");
//                            role.setCreatedBy(authUser);
//                            return roleRepository.save(role);
//                        });
//                Set<Role> roles = new HashSet<>();
//                roles.add(customerRole);
//                newUser.setRoles(roles);
//
//                newUser = userRepository.save(newUser);
//
//                // Create new Customer
//                Customer newCustomer = new Customer();
//                newCustomer.setUserName(request.getCustomerName());
//                newCustomer.setEmail(request.getEmail());
//                newCustomer.setMobile(request.getMobile());
//                newCustomer.setPassword(passwordEncoder.encode(request.getMobile()));
//                newCustomer.setUser(newUser);
//                newCustomer.setCreatedBy(authUser);
//
//// Assign default membership
//                var membership = membershipRepo.findByCode("new").orElse(null);
//                if (membership != null) {
//                    newCustomer.setMembership(membership);
//                }
//
//                newCustomer = customerRepository.save(newCustomer);
//
//// ✅ FIX: Set customer back on User so getUserName() works
//                newUser.setCustomer(newCustomer);
//                userRepository.save(newUser);
//
//                sale.setCustomer(newCustomer);
//                sale.setCustomerUser(newUser);
//                log.info("New customer auto-registered: {} | {}", request.getCustomerName(), request.getEmail());
//            }
//        }
//
//        // ==================== PAYMENT METHOD ====================
//
//        try {
//            if (request.getPaymentMethod() != null && !request.getPaymentMethod().isEmpty()) {
//                sale.setPaymentMethod(PaymentMethod.fromCode(request.getPaymentMethod()));
//            } else {
//                sale.setPaymentMethod(PaymentMethod.COD);
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
//        sale.setSaleStatus(1);  // Pending
//        sale.setPaymentStatus(1); // Pending
//        sale.setSaleType(request.getSaleType() != null ? request.getSaleType() : 1);
//        sale.setSaleChannel(request.getSaleChannel() != null ? request.getSaleChannel() : 1);
//        // Address from frontend form
//        if(request.getBillingAddressId()!=null){
//            Address billingAddress = addressRepo.findById(request.getBillingAddressId())
//                    .orElseThrow(() -> new EntityNotFoundException("Address Not Found"));
//            sale.setBillingAddress(billingAddress);
//        }
//        if(request.getDeliveryAddressId()!=null){
//            Address deliveryAddress = addressRepo.findById(request.getDeliveryAddressId())
//                    .orElseThrow(() -> new EntityNotFoundException("Address Not Found"));
//            sale.setDeliveryAddress(deliveryAddress);
//        }
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
//        // ==================== MOVE CART ITEMS TO SALE ====================
//
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
//        // Save Sale
//        sale = saleRepo.save(sale);
//
//        // Save SaleItems
//        for (SaleItem item : saleItems) {
//            item.setSale(sale);
//        }
//        saleItemRepo.saveAll(saleItems);
//
//        // Save Inventory
//        for (int i = 0; i < inventoryList.size(); i++) {
//            inventoryList.get(i).setInvoiceId(sale.getId());
//            inventoryList.get(i).setInvoiceDetailId(saleItems.get(i).getId());
//        }
//        inventoryRepo.saveAll(inventoryList);
//
//        // Save Payment
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
//        // Clear SaleCart
//        saleCart.setStatus(3);
//        saleCartRepo.save(saleCart);
//
//        // Return Sale with items
//        sale.setItems(saleItems);
//        return saleMapper.toDto(sale);
//    }
//
//    // ==================== STATUS WORKFLOW ====================
//
//    @Transactional
//    public SaleDto updateSaleStatus(Long saleId, SaleStatusUpdateRequest request) {
//        var authUser = authService.getCurrentUser();
//        var sale = saleRepo.findById(saleId)
//                .orElseThrow(() -> new EntityNotFoundException("Sale not found with ID: " + saleId));
//
//        int currentStatus = sale.getSaleStatus();
//        int newStatus = request.getSaleStatus();
//
//        // ✅ Payment must be PAID before delivery
//        if (newStatus == 6 && sale.getPaymentStatus() != 3) {
//            throw new UnauthorizedException("Payment must be PAID before delivery!");
//        }
//
//        // Validate status transition based on user role
//        validateStatusTransition(authUser, currentStatus, newStatus);
//
//        sale.setSaleStatus(newStatus);
//
//        // Set department-specific user
//        switch (newStatus) {
//            case 2:
//                sale.setCustomerCareMan(authUser);
//                sale.setCustomerCareAt(LocalDateTime.now());
//                break;
//            case 3:
//            case 4:
//                sale.setSaleMan(authUser);
//                break;
//            case 5:
//                sale.setDeliveryMan(authUser);
//                sale.setDeliveryAt(LocalDateTime.now());
//                break;
//            case 6:
//                sale.setDeliveryAt(LocalDateTime.now());
//                break;
//            case 7:
//                break;
//        }
//
//        sale.setUpdatedBy(authUser);
//        saleRepo.save(sale);
//        return saleMapper.toDto(sale);
//    }
//
//    private void validateStatusTransition(User user, int currentStatus, int newStatus) {
//        String userRole = user.getRoles().stream()
//                .map(Role::getRoleKey)
//                .findFirst()
//                .orElse("");
//
//        boolean isAdmin = userRole.contains("SUPER_ADMIN") || userRole.contains("ADMIN");
//        boolean isCustomerCare = userRole.contains("CUSTOMER_CARE");
//        boolean isPharmacy = userRole.contains("PHARMACY");
//        boolean isRider = userRole.contains("RIDER") || userRole.contains("FLEET");
//
//        if (isAdmin) return;
//
//        if (currentStatus == 1 && newStatus == 2) {
//            if (!isCustomerCare) throw new UnauthorizedException("Only Customer Care can process orders");
//            return;
//        }
//        if (currentStatus == 2 && newStatus == 3) {
//            if (!isPharmacy) throw new UnauthorizedException("Only Pharmacy can accept orders");
//            return;
//        }
//        if (currentStatus == 3 && newStatus == 4) {
//            if (!isPharmacy) throw new UnauthorizedException("Only Pharmacy can package orders");
//            return;
//        }
//        if (currentStatus == 4 && newStatus == 5) {
//            if (!isRider) throw new UnauthorizedException("Only Rider/Fleet can dispatch orders");
//            return;
//        }
//        if (currentStatus == 5 && newStatus == 6) {
//            if (!isRider) throw new UnauthorizedException("Only Rider can mark as delivered");
//            return;
//        }
//        if (newStatus == 7 && currentStatus != 6) {
//            return;
//        }
//
//        throw new UnauthorizedException("Invalid status transition from " + currentStatus + " to " + newStatus);
//    }
//
//    @Transactional
//    public SaleDto updatePaymentStatus(Long saleId, SalePaymentStatusUpdateRequest request) {
//        var authUser = authService.getCurrentUser();
//        var sale = saleRepo.findById(saleId)
//                .orElseThrow(() -> new EntityNotFoundException("Sale not found with ID: " + saleId));
//
//        sale.setPaymentStatus(request.getPaymentStatus());
//        sale.setUpdatedBy(authUser);
//        saleRepo.save(sale);
//        return saleMapper.toDto(sale);
//    }
//
//    // ==================== QUERIES ====================
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
//
//    // ==================== HELPERS ====================
//
//    private String generateSaleRef() {
//        Sale sale = saleRepo.findTopByOrderByCreatedAtDesc();
//        String prefix = "OSDP";
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
import com.osudpotro.posmaster.offerhub.membership.MembershipRepository;
import com.osudpotro.posmaster.role.Role;
import com.osudpotro.posmaster.role.RoleRepository;
import com.osudpotro.posmaster.salecart.*;
import com.osudpotro.posmaster.security.UnauthorizedException;
import com.osudpotro.posmaster.user.User;
import com.osudpotro.posmaster.user.UserRepository;
import com.osudpotro.posmaster.user.UserType;
import com.osudpotro.posmaster.user.auth.AuthService;
import com.osudpotro.posmaster.user.customer.Customer;
import com.osudpotro.posmaster.user.customer.CustomerRepository;
import com.osudpotro.posmaster.user.customer.address.Address;
import com.osudpotro.posmaster.user.customer.address.AddressRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MembershipRepository membershipRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AddressRepository addressRepo;

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

        // 3. Update SaleCart with customer info
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

        // ==================== CUSTOMER LINKING / AUTO-REGISTER ====================

        // Try find existing customer by email
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            Customer existingCustomer = customerRepository.findByEmail(request.getEmail()).orElse(null);
            if (existingCustomer != null) {
                sale.setCustomer(existingCustomer);
            }
        }

        // Try find existing customer by mobile if not found by email
        if (sale.getCustomer() == null && request.getMobile() != null && !request.getMobile().isEmpty()) {
            Customer existingCustomer = customerRepository.findByMobile(request.getMobile()).orElse(null);
            if (existingCustomer != null) {
                sale.setCustomer(existingCustomer);
            }
        }

        // AUTO-REGISTER: Create new Customer + User if not found
        if (sale.getCustomer() == null
                && request.getCustomerName() != null && !request.getCustomerName().isEmpty()
                && request.getEmail() != null && !request.getEmail().isEmpty()
                && request.getMobile() != null && !request.getMobile().isEmpty()) {

            // Double-check doesn't exist
            Customer existingCustomer = customerRepository.findByEmail(request.getEmail()).orElse(null);
            if (existingCustomer == null) {
                existingCustomer = customerRepository.findByMobile(request.getMobile()).orElse(null);
            }

            if (existingCustomer == null) {
                // Create new User
                User newUser = new User();
                newUser.setUserType(UserType.CUSTOMER);
                newUser.setCreatedBy(authUser);
                newUser.setBranch(branch);

                Role customerRole = roleRepository.findByRoleKey("ROLE_CUSTOMER")
                        .orElseGet(() -> {
                            Role role = new Role();
                            role.setName("Customer");
                            role.setRoleKey("ROLE_CUSTOMER");
                            role.setCreatedBy(authUser);
                            return roleRepository.saveAndFlush(role);
                        });
                Set<Role> roles = new HashSet<>();
                roles.add(customerRole);
                newUser.setRoles(roles);

                newUser = userRepository.saveAndFlush(newUser);

                // Create new Customer
                Customer newCustomer = new Customer();
                newCustomer.setUserName(request.getCustomerName());
                newCustomer.setEmail(request.getEmail());
                newCustomer.setMobile(request.getMobile());
                newCustomer.setPassword(passwordEncoder.encode(request.getMobile()));
                newCustomer.setUser(newUser);
                newCustomer.setCreatedBy(authUser);

                var membership = membershipRepo.findByCode("new").orElse(null);
                if (membership != null) {
                    newCustomer.setMembership(membership);
                }

                newCustomer = customerRepository.saveAndFlush(newCustomer);

                newUser.setCustomer(newCustomer);
                userRepository.save(newUser);

                sale.setCustomer(newCustomer);
                log.info("New customer auto-registered: {} | {}", request.getCustomerName(), request.getEmail());
            } else {
                sale.setCustomer(existingCustomer);
            }
        }

        // ==================== PAYMENT METHOD ====================

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
        sale.setSaleStatus(1);
        sale.setPaymentStatus(1);
        sale.setSaleType(request.getSaleType() != null ? request.getSaleType() : 1);
        sale.setSaleChannel(request.getSaleChannel() != null ? request.getSaleChannel() : 1);

        // Address
        if (request.getBillingAddressId() != null) {
            Address billingAddress = addressRepo.findById(request.getBillingAddressId())
                    .orElseThrow(() -> new EntityNotFoundException("Address Not Found"));
            sale.setBillingAddress(billingAddress);
        }
        if (request.getDeliveryAddressId() != null) {
            Address deliveryAddress = addressRepo.findById(request.getDeliveryAddressId())
                    .orElseThrow(() -> new EntityNotFoundException("Address Not Found"));
            sale.setDeliveryAddress(deliveryAddress);
        }

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

        // ==================== MOVE CART ITEMS TO SALE ====================

        List<SaleItem> saleItems = new ArrayList<>();
        List<Inventory> inventoryList = new ArrayList<>();

        for (SaleCartItem cartItem : saleCart.getItems()) {
            SaleItem saleItem = saleMapper.toSaleItemEntity(cartItem, sale);
            saleItems.add(saleItem);

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

        // Save Sale
        sale = saleRepo.save(sale);

        // Save SaleItems
        for (SaleItem item : saleItems) {
            item.setSale(sale);
        }
        saleItemRepo.saveAll(saleItems);

        // Save Inventory
        for (int i = 0; i < inventoryList.size(); i++) {
            inventoryList.get(i).setInvoiceId(sale.getId());
            inventoryList.get(i).setInvoiceDetailId(saleItems.get(i).getId());
        }
        inventoryRepo.saveAll(inventoryList);

        // Save Payment
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

        // Clear SaleCart
        saleCart.setStatus(3);
        saleCartRepo.save(saleCart);

        // Return
        sale.setItems(saleItems);
        return saleMapper.toDto(sale);
    }

    // ==================== STATUS WORKFLOW ====================

    @Transactional
    public SaleDto updateSaleStatus(Long saleId, SaleStatusUpdateRequest request) {
        var authUser = authService.getCurrentUser();
        var sale = saleRepo.findById(saleId)
                .orElseThrow(() -> new EntityNotFoundException("Sale not found with ID: " + saleId));

        int currentStatus = sale.getSaleStatus();
        int newStatus = request.getSaleStatus();

        if (newStatus == 6 && sale.getPaymentStatus() != 3) {
            throw new UnauthorizedException("Payment must be PAID before delivery!");
        }

        validateStatusTransition(authUser, currentStatus, newStatus);

        sale.setSaleStatus(newStatus);

        // ✅ Create new SaleStatusLog and add to list
        SaleStatusLog statusLog = new SaleStatusLog();
        statusLog.setSaleStatus(newStatus);
        statusLog.setSale(sale);
        statusLog.setCreatedBy(authUser);
        sale.getSaleStatusLogs().add(statusLog);

        switch (newStatus) {
            case 2:
                sale.setCustomerCareMan(authUser);
                sale.setCustomerCareAt(LocalDateTime.now());
                break;
            case 3:
            case 4:
                sale.setSaleMan(authUser);
                break;
            case 5:
                sale.setDeliveryMan(authUser);
                sale.setDeliveryAt(LocalDateTime.now());
                break;
            case 6:
                sale.setDeliveryAt(LocalDateTime.now());
                break;
        }

        sale.setUpdatedBy(authUser);
        saleRepo.saveAndFlush(sale);
        return saleMapper.toDto(sale);
    }

    private void validateStatusTransition(User user, int currentStatus, int newStatus) {
        String userRole = user.getRoles().stream()
                .map(Role::getRoleKey)
                .findFirst()
                .orElse("");

        boolean isAdmin = userRole.contains("SUPER_ADMIN") || userRole.contains("ADMIN");
        boolean isCustomerCare = userRole.contains("CUSTOMER_CARE");
        boolean isPharmacy = userRole.contains("PHARMACY");
        boolean isRider = userRole.contains("RIDER") || userRole.contains("FLEET");

        if (isAdmin) return;

        if (currentStatus == 1 && newStatus == 2) {
            if (!isCustomerCare) throw new UnauthorizedException("Only Customer Care can process orders");
            return;
        }
        if (currentStatus == 2 && newStatus == 3) {
            if (!isPharmacy) throw new UnauthorizedException("Only Pharmacy can accept orders");
            return;
        }
        if (currentStatus == 3 && newStatus == 4) {
            if (!isPharmacy) throw new UnauthorizedException("Only Pharmacy can package orders");
            return;
        }
        if (currentStatus == 4 && newStatus == 5) {
            if (!isRider) throw new UnauthorizedException("Only Rider/Fleet can dispatch orders");
            return;
        }
        if (currentStatus == 5 && newStatus == 6) {
            if (!isRider) throw new UnauthorizedException("Only Rider can mark as delivered");
            return;
        }
        if (newStatus == 7 && currentStatus != 6) {
            return;
        }

        throw new UnauthorizedException("Invalid status transition from " + currentStatus + " to " + newStatus);
    }

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
        Sale sale = saleRepo.findById(saleId)
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
        String prefix = "OSDP";
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