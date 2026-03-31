package com.osudpotro.posmaster.inventory;

import com.osudpotro.posmaster.branch.Branch;
import com.osudpotro.posmaster.organization.Organization;
import com.osudpotro.posmaster.product.Product;
import com.osudpotro.posmaster.product.ProductDetail;
import com.osudpotro.posmaster.purchase.Purchase;
import com.osudpotro.posmaster.purchase.PurchaseDetail;
import com.osudpotro.posmaster.supplier.Supplier;
import com.osudpotro.posmaster.variantunit.VariantUnit;
import com.osudpotro.posmaster.warehouse.Warehouse;
import org.springframework.stereotype.Component;

@Component
public class InventorySummaryMapper {
    public InventorySummaryDto toDto(InventorySummary inventorySummary) {
        if(inventorySummary==null){
            return null;
        }
        InventorySummaryDto invSummaryDto = new InventorySummaryDto();
        invSummaryDto.setId(inventorySummary.getId());
        invSummaryDto.setInvoiceId(inventorySummary.getInvoiceId());
        invSummaryDto.setInvoiceDetailId(inventorySummary.getInvoiceDetailId());
        invSummaryDto.setInvoiceType(inventorySummary.getInvoiceType());
        invSummaryDto.setPurchaseRef(inventorySummary.getPurchaseRef());
        invSummaryDto.setPurchaseBatchNo(inventorySummary.getPurchaseBatchNo());
        invSummaryDto.setProductionBatchNo(inventorySummary.getProductionBatchNo());
        invSummaryDto.setManufactureDate(inventorySummary.getManufactureDate());
        invSummaryDto.setExpiredDate(inventorySummary.getExpiredDate());
        if(inventorySummary.getProduct()!=null){
            Product product=inventorySummary.getProduct();
            invSummaryDto.setProductId(product.getId());
            invSummaryDto.setProductName(product.getProductName());
            invSummaryDto.setProductCode(product.getProductCode());
            invSummaryDto.setProductBarCode(product.getProductBarCode());
        }
        if(inventorySummary.getProductDetail()!=null){
            ProductDetail productDetail=inventorySummary.getProductDetail();
            invSummaryDto.setProductDetailId(productDetail.getId());
            invSummaryDto.setProductDetailCode(productDetail.getProductDetailCode());
            invSummaryDto.setProductDetailBarCode(productDetail.getProductDetailBarCode());
            invSummaryDto.setProductDetailSku(productDetail.getProductDetailSku());
            invSummaryDto.setSellPrice(productDetail.getSellPrice());
            if(productDetail.getSize()!=null){
                VariantUnit size=productDetail.getSize();
                invSummaryDto.setSizeId(size.getId());
                invSummaryDto.setSizeName(size.getName());
            }
        }
        if(inventorySummary.getPurchase()!=null){
            Purchase purchase=inventorySummary.getPurchase();
        }
        if(inventorySummary.getPurchaseDetail()!=null){
            PurchaseDetail purchaseDetail = inventorySummary.getPurchaseDetail();
            invSummaryDto.setMrpPrice(purchaseDetail.getMrpPrice());
            invSummaryDto.setPurchasePrice(purchaseDetail.getPurchasePrice());
        }
        if(inventorySummary.getOrganization()!=null){
            Organization org=inventorySummary.getOrganization();
            invSummaryDto.setOrganizationId(org.getId());
            invSummaryDto.setOrganizationName(org.getName());
        }
        if(inventorySummary.getBranch()!=null){
            Branch branch=inventorySummary.getBranch();
            invSummaryDto.setBranchId(branch.getId());
            invSummaryDto.setBranchName(branch.getName());
        }
        if(inventorySummary.getWarehouse()!=null){
            Warehouse warehouse=inventorySummary.getWarehouse();
            invSummaryDto.setWarehouseId(warehouse.getId());
            invSummaryDto.setWarehouseName(warehouse.getName());
        }
        if(inventorySummary.getSupplier()!=null){
            Supplier supplier=inventorySummary.getSupplier();
            invSummaryDto.setSupplierId(supplier.getId());
            invSummaryDto.setSupplierName(supplier.getName());
        }
        invSummaryDto.setInvoiceDate(inventorySummary.getInvoiceDate());
        invSummaryDto.setStockIn(inventorySummary.getStockIn());
        invSummaryDto.setStockOut(inventorySummary.getStockOut());
        invSummaryDto.setCreatedAt(inventorySummary.getCreatedAt());
        return invSummaryDto;
    }

}
