package com.osudpotro.posmaster.sale;

import com.osudpotro.posmaster.branch.Branch;
import com.osudpotro.posmaster.deliverycharge.DeliveryCharge;
import com.osudpotro.posmaster.deliverymethod.DeliveryMethod;
import com.osudpotro.posmaster.inventory.InventoryRepository;
import com.osudpotro.posmaster.offerhub.membership.Membership;
import com.osudpotro.posmaster.offerhub.promotion.PromotionOffer;
import com.osudpotro.posmaster.organization.Organization;
import com.osudpotro.posmaster.product.ProductDetail;
import com.osudpotro.posmaster.purchase.Purchase;
import com.osudpotro.posmaster.purchase.PurchaseDetail;
import com.osudpotro.posmaster.salecart.SaleCartItem;
import com.osudpotro.posmaster.user.User;
import com.osudpotro.posmaster.user.UserPlainDto;
import com.osudpotro.posmaster.user.customer.Customer;
import com.osudpotro.posmaster.user.customer.CustomerMapper;
import com.osudpotro.posmaster.user.customer.address.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        if (sale.getPaymentOption() != null) {
            dto.setPaymentOption(sale.getPaymentOption().getDescription());
        }
        dto.setUserType(sale.getUserType());
        if (sale.getCustomer() != null) {
            Customer customer = sale.getCustomer();
            dto.setCustomerId(customer.getId());
            dto.setCustomerEmail(customer.getEmail());
            dto.setCustomerMobile(sale.getCustomer().getMobile());
            dto.setCustomerName(sale.getCustomer().getUserName());
        }
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
        dto.setOverallDiscount(sale.getOverallDiscount());
        dto.setOverallDiscountType(sale.getOverallDiscountType());
        dto.setVat(sale.getVat());
        dto.setVatType(sale.getVatType());

        if (sale.getBillingAddress() != null) {
            Address billingAddress = sale.getBillingAddress();
            dto.setBillingAddressId(billingAddress.getId());
            dto.setBillingAddress(billingAddress.getLocationDesc());
        }
        if (sale.getDeliveryAddress() != null) {
            Address deliveryAddress = sale.getDeliveryAddress();
            dto.setDeliveryAddressId(deliveryAddress.getId());
            dto.setDeliveryAddress(deliveryAddress.getLocationDesc());
        }
////        Offer info
//        if (sale.getOffer() != null) {
//            Offer offer = sale.getOffer();
//            dto.setOfferId(offer.getId());
//            dto.setOfferValue(sale.getOfferValue());
//        }
//        promotion Offer info
        if (sale.getPromotionOffer() != null) {
            PromotionOffer promotionOffer = sale.getPromotionOffer();
            dto.setPromotionOfferId(promotionOffer.getId());
            dto.setPromotionValue(sale.getPromotionValue());
            dto.setPromoStartDate(sale.getPromoStartDate());
            dto.setPromoEndDate(sale.getPromoEndDate());
        }
//        Membership info;
        if (sale.getMembership() != null) {
            Membership membership = sale.getMembership();
            dto.setMembershipId(membership.getId());
            dto.setMembershipDiscount(membership.getDiscount());
            dto.setMembershipDiscountType(membership.getDiscountType());
        }
//        Delivery Method info
        if (sale.getDeliveryMethod() != null) {
            DeliveryMethod deliveryMethod = sale.getDeliveryMethod();
            dto.setDeliveryMethodId(deliveryMethod.getId());
            dto.setDefaultDeliveryFee(sale.getDefaultDeliveryFee());
        }
//        Delivery Charge info
        if (sale.getDeliveryCharge() != null) {
            DeliveryCharge deliveryCharge = sale.getDeliveryCharge();
            dto.setDeliveryChargeId(deliveryCharge.getId());
            dto.setDeliveryFee(sale.getDeliveryFee());
            dto.setMinSaleAmountForDeliveryFree(sale.getMinSaleAmountForDeliveryFree());
        }
        dto.setPrescriptionDocs(sale.getPrescriptionDocs());
        dto.setAdjustmentAmount(sale.getAdjustmentAmount());
        dto.setSaleChannel(sale.getSaleChannel());
//        Sale Status Log info
        if (sale.getSaleStatusLogs() != null) {
            dto.setSaleStatusLogs(sale.getSaleStatusLogs());
        }
        if (sale.getSaleStatusLogs() != null && !sale.getSaleStatusLogs().isEmpty()) {
            SaleStatusLog latestLog = sale.getSaleStatusLogs().get(sale.getSaleStatusLogs().size() - 1);
            dto.setSaleStatus(latestLog.getSaleStatus());
            dto.setSaleStatusLabel(getSaleStatusText(latestLog.getSaleStatus()));
        } else {
            dto.setSaleStatus(sale.getSaleStatus());
            dto.setSaleStatusLabel(getSaleStatusText(sale.getSaleStatus()));
        }
        List<SalePaymentDto> salePayments = new ArrayList<>();
        if (sale.getSalePayments() != null) {
            for (SalePayment salePayment : sale.getSalePayments()) {
                SalePaymentDto salePaymentDto = new SalePaymentDto();
                salePaymentDto.setId(salePayment.getId());
                if(salePayment.getPaymentMethod()!=null){
                    salePaymentDto.setPaymentMethod(salePayment.getPaymentMethod().getCode());
                }
                salePaymentDto.setTrxId(salePayment.getTrxId());
                salePaymentDto.setIsSysGenTrx(salePayment.getIsSysGenTrx());
                salePaymentDto.setCashIn(salePayment.getCashIn());
                salePaymentDto.setCashOut(salePayment.getCashOut());
                salePaymentDto.setTransactionType(salePayment.getTransactionType());
                salePayments.add(salePaymentDto);
            }
            dto.setSalePayments(salePayments);
        }
        dto.setPaymentStatus(sale.getPaymentStatus());
        dto.setSaleType(sale.getSaleType());
        dto.setSpecialInstruction(sale.getSpecialInstruction());
        dto.setCreatedAt(sale.getCreatedAt());
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
        dto.setGrandTotalPrice(sale.getGrandTotalPrice());
        if(sale.getCashReceiveAmount()!=null){
            dto.setCashReceiveAmount(sale.getCashReceiveAmount());
        }
        if(sale.getCashReturnAmount()!=null){
            dto.setCashReturnAmount(sale.getCashReturnAmount());
        }
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

    private String getSaleStatusText(Integer status) {
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