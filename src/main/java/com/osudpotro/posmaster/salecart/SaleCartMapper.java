package com.osudpotro.posmaster.salecart;

import com.osudpotro.posmaster.branch.Branch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SaleCartMapper {
    @Autowired
    private SaleCartItemMapper saleCartItemMapper;

    //Mapping Here
    //Entity → DTO
    public SaleCartDto toDto(SaleCart saleCart) {
        SaleCartDto saleCartDto = new SaleCartDto();
        saleCartDto.setId(saleCart.getId());
        saleCartDto.setEmail(saleCart.getEmail());
        saleCartDto.setOverallDiscount(saleCart.getOverallDiscount());
        if (saleCart.getBranch() != null) {
            Branch branch = saleCart.getBranch();
            saleCartDto.setBranchId(branch.getId());
            saleCartDto.setBranchName(branch.getName());
        }
        List<SaleCartItemDto> items = new ArrayList<>();
        if (saleCart.getItems() != null) {
            List<SaleCartItem> saleCartItems = saleCart.getItems();
            for (SaleCartItem item : saleCart.getItems()) {
                var saleCartItemDto = saleCartItemMapper.toDto(item);
                items.add(saleCartItemDto);
            }
        }
        saleCartDto.setItems(items);
        return saleCartDto;
    }
}
