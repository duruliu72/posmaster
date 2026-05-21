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
import com.osudpotro.posmaster.user.customer.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class SaleMapper {

    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private CustomerMapper customerMapper;

    public SaleDto toDto(Sale sale) {
        if (sale == null) return null;

        SaleDto dto = new SaleDto();
        dto.setId(sale.getId());
        dto.setSaleRef(sale.getSaleRef());
        // ✅ Use description instead of enum name
        if (sale.getPaymentMethod() != null) {
            dto.setPaymentMethod(sale.getPaymentMethod().getDescription());
        }
        dto.setVat(sale.getVat());
        dto.setVatType(sale.getVatType());
        dto.setAdjustmentAmount(sale.getAdjustmentAmount());
        dto.setGrandTotalPrice(sale.getGrandTotalPrice());
        dto.setCashReceiveAmount(BigDecimal.valueOf(10));
        dto.setCashReturnAmount(BigDecimal.valueOf(10));
//        dto.setBillingAddress(sale.getBillingAddress());
//        dto.setDeliveryAddress(sale.getDeliveryAddress());
        dto.setDeliveryFee(sale.getDeliveryFee());
        dto.setPrescriptionDocs(sale.getPrescriptionDocs());
        dto.setSaleChannel(sale.getSaleChannel());
        if (sale.getSaleStatusLog() != null) {
            SaleStatusLog saleStatusLog = sale.getSaleStatusLog();
            dto.setSaleStatusLogId(saleStatusLog.getId());
            dto.setSaleStatus(saleStatusLog.getSaleStatus());
        }

        dto.setPaymentStatus(sale.getPaymentStatus());
        dto.setSaleType(sale.getSaleType());
        dto.setCreatedAt(sale.getCreatedAt());

        if (sale.getOrganization() != null) {
            Organization org = sale.getOrganization();
            dto.setOrganizationId(org.getId());
            dto.setOrganizationName(org.getName());
        }
        if (sale.getBranch() != null) {
            Branch branch = sale.getBranch();
            dto.setBranchId(branch.getId());
            dto.setBranchName(branch.getName());
        }

        if (sale.getCustomer() != null) {
            Customer customer = sale.getCustomer();
            dto.setCustomerId(customer.getId());
            dto.setCustomerEmail(customer.getEmail());
            dto.setCustomerMobile(sale.getCustomer().getMobile());
            dto.setCustomerName(sale.getCustomer().getUserName());
        }

        if (sale.getSalePointMan() != null) {
            dto.setSalePointMan(toUserPlainDto(sale.getSalePointMan()));
        }

        if (sale.getCreatedBy() != null) {
            dto.setCreatedBy(toUserPlainDto(sale.getCreatedBy()));
        }

        // Map items
        List<SaleItemDto> itemDtos = new ArrayList<>();
        for (SaleItem item : sale.getItems()) {
            itemDtos.add(toItemDto(item));
        }
        dto.setItems(itemDtos);
        dto.setTotalQty(sale.getTotalQty());
        dto.setSubTotalPrice(sale.getSubTotalPrice());
        return dto;
    }

    public SaleItemDto toItemDto(SaleItem item) {
        if (item == null) return null;

        SaleItemDto dto = new SaleItemDto();
        dto.setId(item.getId());
        dto.setSaleQty(item.getSaleQty());
        dto.setSalePrice(item.getSalePrice());
        dto.setTotalPrice(item.getTotalPrice());
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

        // Get current stock by barcode
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

    private UserPlainDto toUserPlainDto(User user) {
        if (user == null) return null;
        UserPlainDto dto = new UserPlainDto();
        dto.setId(user.getId());
        dto.setUserName(user.getUserName());
        dto.setMobile(user.getMobile());
        dto.setEmail(user.getEmail());
        return dto;
    }
}