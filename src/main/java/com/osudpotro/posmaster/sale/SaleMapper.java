package com.osudpotro.posmaster.sale;

import com.osudpotro.posmaster.branch.Branch;
import com.osudpotro.posmaster.inventory.InventoryRepository;
import com.osudpotro.posmaster.organization.Organization;
import com.osudpotro.posmaster.product.ProductDetail;
import com.osudpotro.posmaster.purchase.Purchase;
import com.osudpotro.posmaster.purchase.PurchaseDetail;
import com.osudpotro.posmaster.salecart.SaleCartItem;
import com.osudpotro.posmaster.user.User;
import com.osudpotro.posmaster.user.UserPlainDto;
import com.osudpotro.posmaster.user.customer.Customer;
import com.osudpotro.posmaster.user.customer.address.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class SaleMapper {

    @Autowired
    private InventoryRepository inventoryRepository;

    public SaleDto toDto(Sale sale) {
        if (sale == null) return null;

        SaleDto dto = new SaleDto();
        dto.setId(sale.getId());
        dto.setSaleRef(sale.getSaleRef());

        if (sale.getPaymentMethod() != null) {
            dto.setPaymentMethod(sale.getPaymentMethod().getDescription());
        }

        // Status from SaleStatusLog
        if (sale.getSaleStatusLog() != null) {
            SaleStatusLog log = sale.getSaleStatusLog();
            dto.setSaleStatusLogId(log.getId());
            dto.setSaleStatus(log.getSaleStatus());
            dto.setSaleStatusLabel(getStatusLabel(log.getSaleStatus()));
        } else {
            dto.setSaleStatus(1);
            dto.setSaleStatusLabel("Pending");
        }

        dto.setPaymentStatus(sale.getPaymentStatus());
        dto.setPaymentStatusLabel(getPaymentLabel(sale.getPaymentStatus()));
        dto.setSaleType(sale.getSaleType());
        dto.setCreatedAt(sale.getCreatedAt());
        dto.setVatAmount(sale.getVatAmount());
        dto.setDeliveryFee(sale.getDeliveryFee());
        dto.setPrescriptionDocs(sale.getPrescriptionDocs());
        dto.setSaleChannel(sale.getSaleChannel());

        if (sale.getOrganization() != null) {
            dto.setOrganizationId(sale.getOrganization().getId());
            dto.setOrganizationName(sale.getOrganization().getName());
        }
        if (sale.getBranch() != null) {
            dto.setBranchId(sale.getBranch().getId());
            dto.setBranchName(sale.getBranch().getName());
        }

        // Customer
        if (sale.getCustomer() != null) {
            Customer customer = sale.getCustomer();
            dto.setCustomerId(customer.getId());
            dto.setCustomerEmail(customer.getEmail());
            dto.setCustomerMobile(customer.getMobile());
            dto.setCustomerName(customer.getUserName());

            if (customer.getAddresses() != null && !customer.getAddresses().isEmpty()) {
                dto.setCustomerAddress(buildAddressString(customer.getAddresses().get(0)));
            }
        }

        // Addresses
        if (sale.getDeliveryAddress() != null) {
            dto.setDeliveryAddress(buildAddressString(sale.getDeliveryAddress()));
        }
        if (sale.getBillingAddress() != null) {
            dto.setBillingAddress(buildAddressString(sale.getBillingAddress()));
        }

        // Personnel
        if (sale.getSalePointMan() != null) {
            dto.setSalePointMan(toUserPlainDto(sale.getSalePointMan()));
        }
        if (sale.getCreatedBy() != null) {
            dto.setCreatedBy(toUserPlainDto(sale.getCreatedBy()));
        }

        // Items
        List<SaleItemDto> itemDtos = new ArrayList<>();
        if (sale.getItems() != null) {
            for (SaleItem item : sale.getItems()) {
                itemDtos.add(toItemDto(item));
            }
        }
        dto.setItems(itemDtos);

        // Totals
        int totalQty = 0;
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (SaleItemDto item : itemDtos) {
            if (item.getSaleQty() != null) totalQty += item.getSaleQty();
            if (item.getSalePrice() != null && item.getSaleQty() != null) {
                totalPrice = totalPrice.add(
                        item.getSalePrice().multiply(BigDecimal.valueOf(item.getSaleQty()))
                );
            }
        }
        dto.setTotalQty(totalQty);
        dto.setTotalPrice(totalPrice);

        return dto;
    }

    // ==================== ITEM DTO ====================

    public SaleItemDto toItemDto(SaleItem item) {
        if (item == null) return null;

        SaleItemDto dto = new SaleItemDto();
        dto.setId(item.getId());
        dto.setSaleQty(item.getSaleQty());
        dto.setSalePrice(item.getSalePrice());

        if (item.getPurchase() != null) {
            Purchase purchase = item.getPurchase();
            dto.setPurchaseId(purchase.getId());
            dto.setPurchaseBatchNo(purchase.getPurchaseBatchNo());
        }

        if (item.getPurchaseDetail() != null) {
            PurchaseDetail pd = item.getPurchaseDetail();
            dto.setPurchaseDetailId(pd.getId());
            dto.setProductionBatchNo(pd.getProductionBatchNo());
            dto.setPurchaseBarCode(pd.getPurchaseBarCode());
        }

        if (item.getProduct() != null) {
            dto.setProductId(item.getProduct().getId());
            dto.setProductName(item.getProduct().getProductName());
        }

        if (item.getProductDetail() != null) {
            ProductDetail pd = item.getProductDetail();
            dto.setProductDetailId(pd.getId());
            dto.setProductDetailCode(pd.getProductDetailCode());
            dto.setProductDetailBarCode(pd.getProductDetailBarCode());
            dto.setProductDetailSku(pd.getProductDetailSku());
            dto.setSalePrice(pd.getSellPrice());
            dto.setMrpPrice(pd.getMrpPrice());
            dto.setPurchasePrice(pd.getPurchasePrice());

            if (pd.getSize() != null) {
                dto.setSizeId(pd.getSize().getId());
                dto.setSizeName(pd.getSize().getName());
            }
        }

        // Current stock
        if (item.getPurchaseDetail() != null
                && item.getPurchaseDetail().getPurchaseBarCode() != null
                && item.getProductDetail() != null
                && item.getSale() != null
                && item.getSale().getBranch() != null) {

            Integer currentStock = inventoryRepository
                    .findCurrentStockByPurchaseBarCode(
                            item.getPurchaseDetail().getPurchaseBarCode(),
                            item.getProductDetail().getId(),
                            item.getSale().getBranch().getId()
                    );
            dto.setCurrentStock(currentStock != null ? currentStock : 0);
        }

        return dto;
    }

    // ==================== SALE ITEM ENTITY ====================

    public SaleItem toSaleItemEntity(SaleCartItem cartItem, Sale sale) {
        SaleItem saleItem = new SaleItem();
        saleItem.setSale(sale);
        saleItem.setPurchase(cartItem.getPurchase());
        saleItem.setPurchaseDetail(cartItem.getPurchaseDetail());
        saleItem.setProduct(cartItem.getPurchaseDetail() != null ? cartItem.getPurchaseDetail().getProduct() : null);
        saleItem.setProductDetail(cartItem.getPurchaseDetail() != null ? cartItem.getPurchaseDetail().getProductDetail() : null);
        saleItem.setSaleQty(cartItem.getSaleQty());

        if (cartItem.getPurchaseDetail() != null
                && cartItem.getPurchaseDetail().getProductDetail() != null) {
            saleItem.setSalePrice(cartItem.getPurchaseDetail().getProductDetail().getSellPrice());
        }

        return saleItem;
    }

    // ==================== HELPERS ====================

    private UserPlainDto toUserPlainDto(User user) {
        if (user == null) return null;
        UserPlainDto dto = new UserPlainDto();
        dto.setId(user.getId());
        dto.setUserName(user.getUserName());
        dto.setMobile(user.getMobile());
        dto.setEmail(user.getEmail());
        return dto;
    }

    private String buildAddressString(Address addr) {
        if (addr == null) return "";
        StringBuilder sb = new StringBuilder();
        if (addr.getLocationName() != null && !addr.getLocationName().isEmpty()) {
            sb.append(addr.getLocationName());
        }
        if (addr.getLocationDesc() != null && !addr.getLocationDesc().isEmpty()) {
            if (!sb.isEmpty()) sb.append(", ");
            sb.append(addr.getLocationDesc());
        }
        if (addr.getArea() != null && addr.getArea().getName() != null) {
            if (!sb.isEmpty()) sb.append(", ");
            sb.append(addr.getArea().getName());
        }
        return sb.toString();
    }

    private String getStatusLabel(Integer status) {
        if (status == null) return "Pending";
        return switch (status) {
            case 1 -> "Pending";
            case 2 -> "Processing";
            case 3 -> "Accepted";
            case 4 -> "Packaging";
            case 5 -> "On the way";
            case 6 -> "Delivered";
            case 7 -> "Cancelled";
            default -> "Unknown";
        };
    }

    private String getPaymentLabel(Integer status) {
        if (status == null) return "Pending";
        return switch (status) {
            case 1 -> "Pending";
            case 2 -> "Partial";
            case 3 -> "Paid";
            default -> "Unknown";
        };
    }
}