package com.osudpotro.posmaster.salecart;

import com.osudpotro.posmaster.inventory.InventoryRepository;
import com.osudpotro.posmaster.product.Product;
import com.osudpotro.posmaster.product.ProductDetail;
import com.osudpotro.posmaster.purchase.Purchase;
import com.osudpotro.posmaster.purchase.PurchaseDetail;
import com.osudpotro.posmaster.variantunit.VariantUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SaleCartItemMapper {

    @Autowired
    private InventoryRepository inventoryRepository;  //  ADD THIS OASIK
    //Mapping Here
    //Entity → DTO
    public SaleCartItemDto toDto(SaleCartItem saleCartItem) {
        SaleCartItemDto saleCartItemDto = new SaleCartItemDto();
        saleCartItemDto.setId(saleCartItem.getId());
        saleCartItemDto.setSaleQty(saleCartItem.getSaleQty());

        if (saleCartItem.getPurchase() != null) {
            Purchase purchase = saleCartItem.getPurchase();
            saleCartItemDto.setPurchaseId(purchase.getId());
            saleCartItemDto.setPurchaseBatchNo(purchase.getPurchaseBatchNo());
            if (saleCartItem.getPurchaseDetail() != null) {
                PurchaseDetail purchaseDetail = saleCartItem.getPurchaseDetail();
                saleCartItemDto.setPurchaseDetailId(purchaseDetail.getId());
                saleCartItemDto.setProductionBatchNo(purchaseDetail.getProductionBatchNo());
                saleCartItemDto.setPurchaseBarCode(purchaseDetail.getPurchaseBarCode());
                if (purchaseDetail.getProduct() != null) {
                    Product product = purchaseDetail.getProduct();
                    saleCartItemDto.setProductId(product.getId());
                    saleCartItemDto.setProductName(product.getProductName());
                }
                if (purchaseDetail.getProductDetail() != null) {
                    ProductDetail productDetail = purchaseDetail.getProductDetail();
                    saleCartItemDto.setSellPrice(productDetail.getSellPrice());
                    saleCartItemDto.setPurchasePrice(purchaseDetail.getPurchasePrice());
                    saleCartItemDto.setMrpPrice(purchaseDetail.getMrpPrice());
                    if (productDetail.getSize() != null) {
                        VariantUnit size = productDetail.getSize();
                        saleCartItemDto.setSizeId(size.getId());
                        saleCartItemDto.setSizeName(size.getName());
                    }
                }
            }
        }

         // OASIK
        if (saleCartItem.getPurchaseDetail() != null
                && saleCartItem.getPurchaseDetail().getPurchaseBarCode() != null
                && saleCartItem.getPurchaseDetail().getProductDetail() != null
                && saleCartItem.getSaleCart() != null
                && saleCartItem.getSaleCart().getBranch() != null) {

            String purchaseBarCode = saleCartItem.getPurchaseDetail().getPurchaseBarCode();
            Long productDetailId = saleCartItem.getPurchaseDetail().getProductDetail().getId();
            Long branchId = saleCartItem.getSaleCart().getBranch().getId();

            Integer currentStock = inventoryRepository
                    .findCurrentStockByPurchaseBarCode(purchaseBarCode, productDetailId, branchId);
            saleCartItemDto.setCurrentStock(currentStock != null ? currentStock : 0);
        }
        return saleCartItemDto;
    }

    public SaleCartItemAlongDto toAlongDto(SaleCartItem saleCartItem) {
        SaleCartItemAlongDto saleCartItemAlongDto = (SaleCartItemAlongDto) toDto(saleCartItem);
        saleCartItemAlongDto.setSaleQty(saleCartItem.getSaleQty());
        SaleCart saleCart = saleCartItem.getSaleCart();
        saleCartItemAlongDto.setEmail(saleCart.getEmail());
        saleCartItemAlongDto.setMobile(saleCart.getMobile());
        return saleCartItemAlongDto;
    }
}
