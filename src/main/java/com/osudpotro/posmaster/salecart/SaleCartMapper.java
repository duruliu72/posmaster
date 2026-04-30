package com.osudpotro.posmaster.salecart;

import com.osudpotro.posmaster.branch.Branch;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SaleCartMapper {
    //Mapping Here
    //Entity → DTO
    public SaleCartDto toDto(SaleCart saleCart) {
        SaleCartDto saleCartDto = new SaleCartDto();
        saleCartDto.setId(saleCart.getId());
        saleCartDto.setEmail(saleCart.getEmail());
        if (saleCart.getBranch() != null) {
            Branch branch = saleCart.getBranch();
            saleCartDto.setBranchId(branch.getId());
            saleCartDto.setBranchName(branch.getName());
        }
        List<SaleCartItemDto> items = new ArrayList<>();
        if (saleCart.getItems() != null) {
            List<SaleCartItem> saleCartItem = saleCart.getItems();
            for (SaleCartItem item : saleCart.getItems()) {
                SaleCartItemDto saleCartItemDto = new SaleCartItemDto();
                saleCartItemDto.setSaleQty(saleCartItem.getLast().getSaleQty());

            }
        }
        saleCartDto.setItems(items);
        return saleCartDto;
    }
}
