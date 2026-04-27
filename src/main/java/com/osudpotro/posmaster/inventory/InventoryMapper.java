package com.osudpotro.posmaster.inventory;

import com.osudpotro.posmaster.branch.Branch;
import com.osudpotro.posmaster.manufacturer.Manufacturer;
import com.osudpotro.posmaster.organization.Organization;
import com.osudpotro.posmaster.product.Product;
import com.osudpotro.posmaster.product.ProductDetail;
import com.osudpotro.posmaster.producttype.ProductType;
import com.osudpotro.posmaster.purchase.Purchase;
import com.osudpotro.posmaster.purchase.PurchaseDetail;
import com.osudpotro.posmaster.supplier.Supplier;
import com.osudpotro.posmaster.variantunit.VariantUnit;
import com.osudpotro.posmaster.warehouse.Warehouse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class InventoryMapper {
    public InventoryDto toDto(Inventory inventory) {
        if (inventory == null) {
            return null;
        }
        InventoryDto invDto = new InventoryDto();
        invDto.setId(inventory.getId());
        invDto.setInvoiceId(inventory.getInvoiceId());
        invDto.setInvoiceDetailId(inventory.getInvoiceDetailId());
        invDto.setInvoiceType(inventory.getInvoiceType());
        invDto.setPurchaseBatchNo(inventory.getPurchaseBatchNo());
        invDto.setProductionBatchNo(inventory.getProductionBatchNo());
        invDto.setManufactureDate(inventory.getManufactureDate());
        invDto.setExpiredDate(inventory.getExpiredDate());
        if (inventory.getProduct() != null) {
            Product product = inventory.getProduct();
            invDto.setProductId(product.getId());
            invDto.setProductName(product.getProductName());
            invDto.setProductCode(product.getProductCode());
            invDto.setProductBarCode(product.getProductBarCode());
            if (product.getProductType() != null) {
                ProductType productType = product.getProductType();
                invDto.setProductTypeId(productType.getId());
                invDto.setProductTypeName(productType.getName());
            }
            if (product.getManufacturer() != null) {
                Manufacturer manufacturer = product.getManufacturer();
                invDto.setManufacturerId(manufacturer.getId());
                invDto.setManufacturerName(manufacturer.getName());
            }
        }
        if (inventory.getProductDetail() != null) {
            ProductDetail productDetail = inventory.getProductDetail();
            invDto.setProductDetailId(productDetail.getId());
            invDto.setProductDetailCode(productDetail.getProductDetailCode());
            invDto.setProductDetailBarCode(productDetail.getProductDetailBarCode());
            invDto.setProductDetailSku(productDetail.getProductDetailSku());
            invDto.setSellPrice(productDetail.getSellPrice());
            invDto.setLastMrpPrice(productDetail.getMrpPrice());
            invDto.setLastPurchasePrice(productDetail.getPurchasePrice());
            invDto.setLastSellPrice(productDetail.getSellPrice());
            invDto.setLastUpdatedAt(productDetail.getUpdatedAt());
            if (productDetail.getSize() != null) {
                VariantUnit size = productDetail.getSize();
                invDto.setSizeId(size.getId());
                invDto.setSizeName(size.getName());
            }
        }
        if (inventory.getPurchase() != null) {
            Purchase purchase = inventory.getPurchase();
            invDto.setPurchaseRef(purchase.getPurchaseRef());
        }
        if (inventory.getPurchaseDetail() != null) {
            PurchaseDetail purchaseDetail = inventory.getPurchaseDetail();
            invDto.setMrpPrice(purchaseDetail.getMrpPrice());
            invDto.setPurchasePrice(purchaseDetail.getPurchasePrice());
        }
        if (inventory.getOrganization() != null) {
            Organization org = inventory.getOrganization();
            invDto.setOrganizationId(org.getId());
            invDto.setOrganizationName(org.getName());
        }
        if (inventory.getBranch() != null) {
            Branch branch = inventory.getBranch();
            invDto.setBranchId(branch.getId());
            invDto.setBranchName(branch.getName());
        }
        if (inventory.getWarehouse() != null) {
            Warehouse warehouse = inventory.getWarehouse();
            invDto.setWarehouseId(warehouse.getId());
            invDto.setWarehouseName(warehouse.getName());
        }
        if (inventory.getSupplier() != null) {
            Supplier supplier = inventory.getSupplier();
            invDto.setSupplierId(supplier.getId());
            invDto.setSupplierName(supplier.getName());
        }
        invDto.setInvoiceDate(inventory.getInvoiceDate());
        invDto.setStockIn(inventory.getStockIn());
        invDto.setStockOut(inventory.getStockOut());
        invDto.setCreatedAt(inventory.getCreatedAt());
        return invDto;
    }
}
