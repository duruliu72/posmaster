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
import com.osudpotro.posmaster.offerhub.membership.Membership;
import com.osudpotro.posmaster.offerhub.membership.MembershipRepository;
import com.osudpotro.posmaster.purchase.Purchase;
import com.osudpotro.posmaster.purchase.PurchaseDetail;
import com.osudpotro.posmaster.purchase.PurchaseDetailRepository;
import com.osudpotro.posmaster.purchase.PurchaseRepository;
import com.osudpotro.posmaster.securityadmistration.role.Role;
import com.osudpotro.posmaster.securityadmistration.role.RoleRepository;
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
import com.osudpotro.posmaster.user.customer.wallet.Wallet;
import com.osudpotro.posmaster.user.customer.wallet.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class SaleService {
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private PurchaseDetailRepository purchaseDetailRepo;
    @Autowired
    private PurchaseRepository purchaseRepo;

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

    @Transactional
    public SaleDto createPosSale(PosSaleCreateRequest request) {
        User authUser = authService.getCurrentUser();
        Branch authBranch = authUser.getBranch();
        String saleRef = generateSaleRef();
        // 1. Create Sale
        Sale sale = new Sale();
        sale.setSaleRef(saleRef);
//        Customer related info
        Customer customer = null;
        if (request.getCustomerId() != null) {
            customer = customerRepository.findById(request.getCustomerId()).orElse(null);
            if (customer == null) {
                throw new EntityNotFoundException("Customer not found");
            }
            sale.setCustomerUser(customer.getUser());
            sale.setCustomer(customer);
//          Get Membership offer
            Membership membership = customer.getMembership();
            sale.setMembershipDiscount(membership.getDiscount());
            sale.setMaxDiscount(membership.getMaxDiscount());
            sale.setMembershipDiscountType(membership.getDiscountType());
        }
        if (request.getOverallDiscount() != null) {
            sale.setOverallDiscount(request.getOverallDiscount());
            if (request.getOverallDiscountType() != null) {
                sale.setOverallDiscountType(request.getOverallDiscountType());
            } else {
                sale.setOverallDiscountType(AmountType.FIXED_AMOUNT);
            }

        }
        if (request.getVatType() != null) {
            sale.setVat(request.getVat());
            sale.setVatType(request.getVatType());
        }
        sale.setAdjustmentAmount(request.getAdjustmentAmount());
        sale.setPrescriptionDocs(request.getPrescriptionDocs());
        sale.setSpecialInstruction(request.getSpecialInstruction());
        sale.setPaymentOption(PaymentOption.fromCode("cash"));
        sale.setSaleChannel(1);
//      Sale Item info
        if (request.getItems() != null) {
            List<SaleItem> saleItemList = new ArrayList<>();
            for (SaleItemRequest itemRequest : request.getItems()) {
                SaleItem saleItem = new SaleItem();
                saleItem.setSale(sale);
                Purchase purchase = purchaseRepo.findById(itemRequest.getPurchaseId()).orElse(null);
                if (purchase == null) {
                    throw new EntityNotFoundException("Purchase not found with ID: " + itemRequest.getPurchaseId());
                }
                saleItem.setPurchase(purchase);
                PurchaseDetail purchaseDetail = purchaseDetailRepo.findById(itemRequest.getPurchaseDetailId()).orElse(null);
                if (purchaseDetail == null) {
                    throw new EntityNotFoundException("Purchase Detail not found with ID: " + itemRequest.getPurchaseDetailId());
                }
                if (purchaseDetail.getProduct() == null) {
                    throw new EntityNotFoundException("Product not found");
                }
                if (purchaseDetail.getProductDetail() == null) {
                    throw new EntityNotFoundException("Product Detail not found");
                }
                saleItem.setPurchaseDetail(purchaseDetail);
                saleItem.setProduct(purchaseDetail.getProduct());
                saleItem.setProductDetail(purchaseDetail.getProductDetail());
                saleItem.setSaleQty(itemRequest.getSaleQty());
                saleItem.setSalePrice(itemRequest.getSalePrice());
                saleItem.setDiscount(itemRequest.getDiscount());
                if (itemRequest.getDiscountType() != null) {
                    saleItem.setDiscountType(itemRequest.getDiscountType());
                } else {
                    saleItem.setDiscountType(AmountType.FIXED_AMOUNT);
                }
                saleItemList.add(saleItem);
            }
            sale.setItems(saleItemList);
        }
        BigDecimal grandTotal = sale.getGrandTotalPrice();
        BigDecimal bonusAmount = grandTotal.divide(BigDecimal.valueOf(100));
        if (customer != null && bonusAmount.compareTo(BigDecimal.ZERO) > 0) {
            Wallet creditWallet = new Wallet();
            creditWallet.setCustomer(customer);
            creditWallet.setUser(customer.getUser());
            creditWallet.setUserType(UserType.CUSTOMER);
            creditWallet.setCreditAmount(bonusAmount);
            creditWallet.setWalletType(3);
            creditWallet.setSale(sale);
            creditWallet.setSaleRef(saleRef);
            walletRepository.save(creditWallet);
        }
        //       sale status 6=for delivered
        sale.setSaleStatus(6);
//        3=for success for payment
        sale.setPaymentStatus(3);
//      Purchase Bonus add to wallet
        sale.setCreatedBy(authUser);
        sale.setBranch(authUser.getBranch());
        saleRepo.save(sale);
        if (sale.getSalePayments() == null) {
            sale.setSalePayments(new ArrayList<>());
        }
        //Sale Payment info
        if (request.getSalePayments() != null) {
            List<SalePayment> salePayments = new ArrayList<>();
            for (SalePaymentRequest paymentRequest : request.getSalePayments()) {
                if (paymentRequest.getPaymentAmount() != null && paymentRequest.getPaymentAmount().compareTo(BigDecimal.ZERO) > 0) {
                    SalePayment salePayment = new SalePayment();
                    salePayment.setSale(sale);
                    salePayment.setSaleRef(saleRef);
                    if (Objects.equals(paymentRequest.getPaymentMethod(), "wallet") && customer != null) {
                        Wallet debitWallet = new Wallet();
                        debitWallet.setCustomer(customer);
                        debitWallet.setUser(customer.getUser());
                        debitWallet.setUserType(UserType.CUSTOMER);
                        debitWallet.setDebitAmount(paymentRequest.getPaymentAmount());
                        debitWallet.setWalletType(2);
                        debitWallet.setSale(sale);
                        debitWallet.setSaleRef(saleRef);
                        walletRepository.save(debitWallet);
                    }
                    salePayment.setPaymentMethod(PaymentMethod.fromCode(paymentRequest.getPaymentMethod()));
                    salePayment.setCashIn(paymentRequest.getPaymentAmount());
                    salePayment.setIsSysGenTrx(paymentRequest.getIsSysGenTrx());
                    if (!paymentRequest.getIsSysGenTrx()) {
                        salePayment.setTrxId(paymentRequest.getTrxId());
                    } else {
                        String x = generatePaymentTrxId();
                        salePayment.setTrxId(generatePaymentTrxId());
                    }
                    salePayment.setTransactionType(1);
                    salePaymentRepo.save(salePayment);
//                    salePayments.add(salePayment);
                    sale.getSalePayments().add(salePayment);
                }
            }
            if (request.getCashReturnAmount() != null && request.getCashReturnAmount().compareTo(BigDecimal.ZERO) > 0) {
                SalePayment salePayment = new SalePayment();
                salePayment.setSale(sale);
                salePayment.setSaleRef(saleRef);
                salePayment.setCashOut(request.getCashReturnAmount());
                salePayment.setTrxId(generatePaymentTrxId());
                salePayment.setTransactionType(1);
                salePaymentRepo.save(salePayment);
//                salePayments.add(salePayment);
                sale.getSalePayments().add(salePayment);
            }
//            sale.setSalePayments(salePayments);
        }
//        Stock Out from inventory
        List<Inventory> inventoryList = new ArrayList<>();
        for (SaleItem saleItem : sale.getItems()) {
            Inventory stockOut = new Inventory();
            stockOut.setInvoiceId(sale.getId());
            stockOut.setInvoiceDetailId(saleItem.getId());
            stockOut.setInvoiceType(InvoiceType.SALE);
            stockOut.setPurchase(saleItem.getPurchase());
            stockOut.setPurchaseDetail(saleItem.getPurchaseDetail());
            stockOut.setStockOut(saleItem.getSaleQty());
            stockOut.setBranch(authBranch);
            stockOut.setInvoiceDate(LocalDateTime.now());
            stockOut.setProduct(saleItem.getPurchaseDetail().getProduct());
            stockOut.setProductDetail(saleItem.getPurchaseDetail().getProductDetail());
            stockOut.setPurchaseBatchNo(saleItem.getPurchase().getPurchaseBatchNo());
            stockOut.setProductionBatchNo(saleItem.getPurchaseDetail().getProductionBatchNo());
            stockOut.setPurchaseBarCode(saleItem.getPurchaseDetail().getPurchaseBarCode());
            stockOut.setOrganization(authBranch.getOrganization());
            stockOut.setWarehouse(sale.getWarehouse());
            inventoryList.add(stockOut);
        }
        inventoryRepo.saveAll(inventoryList);
        return saleMapper.toDto(sale);
    }
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
        sale.setVat(request.getVatAmount());
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
                payment.setCashIn(request.getCreditAmount());
                payment.setCashOut(request.getDebitAmount());
                payment.setTransactionType(1);
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
        Sale sale = saleRepo.findTopByOrderByIdDesc();
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

    private String generatePaymentTrxId() {
        SalePayment salePayment = salePaymentRepo.findTopByOrderByIdDesc();
        String prefix = "OPWT";
        long nextSeq = 1;
        if (salePayment != null && salePayment.getTrxId() != null) {
            String lastTrxId = salePayment.getTrxId();
            String lastPart = lastTrxId.length() > 5 ? lastTrxId.substring(lastTrxId.length() - 6) : lastTrxId;
            if (!lastPart.isEmpty()) {
                try {
                    nextSeq = Long.parseLong(lastPart) + 1;
                } catch (Exception e) {
                    log.error("e: ", e);
                }
            }
        }
        return String.format("%s-%06d", prefix, nextSeq);
    }
}