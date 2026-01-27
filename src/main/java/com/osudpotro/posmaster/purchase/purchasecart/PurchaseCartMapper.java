package com.osudpotro.posmaster.purchase.purchasecart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PurchaseCartMapper {
    @Autowired
    private PurchaseCartItemMapper purchaseCartItemMapper;

    //Mapping Here
    //Entity → DTO
    public PurchaseCartDto toDto(PurchaseCart purchaseCart) {
        PurchaseCartDto purchaseCartDto = new PurchaseCartDto();
        purchaseCartDto.setId(purchaseCart.getId());
        purchaseCartDto.setName(purchaseCart.getName());
        purchaseCartDto.setTotalPrice(purchaseCart.getTotalPrice());
        List<PurchaseCartItemDto> items = new ArrayList<>();
        if (purchaseCart.getItems() != null) {
            for (PurchaseCartItem item : purchaseCart.getItems()) {
                items.add(purchaseCartItemMapper.toDto(item));
            }
        }
        purchaseCartDto.setItems(items);
        return purchaseCartDto;
    }

    public PurchaseCartWithItemPageResponse toMinDto(PurchaseCart purchaseCart, Page<PurchaseCartItemDto> page) {
        PurchaseCartWithItemPageResponse pageResponse = new PurchaseCartWithItemPageResponse();
        pageResponse.setId(purchaseCart.getId());
        pageResponse.setName(purchaseCart.getName());
        pageResponse.setTotalPrice(purchaseCart.getTotalPrice());
        pageResponse.setContent(page.getContent());
        pageResponse.setTotalElements(page.getTotalElements());
        pageResponse.setPageNumber(page.getNumber());
        pageResponse.setPageSize(page.getSize());
        pageResponse.setTotalPages(page.getTotalPages());
        return pageResponse;
    }
}
