package com.osudpotro.posmaster.purchase.purchasecart;

import com.osudpotro.posmaster.user.auth.AuthService;
import com.osudpotro.posmaster.common.PagedResponse;
import com.osudpotro.posmaster.product.*;
import com.osudpotro.posmaster.product.ProductDetailNotFoundException;
import com.osudpotro.posmaster.product.ProductDetailRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PurchaseCartService {
    private final PurchaseCartRepository purchaseCartRepository;
    private final PurchaseCartItemRepository purchaseCartItemRepository;
    private final ProductRepository productRepository;
    private final ProductDetailRepository productDetailRepository;
    private final PurchaseCartMapper purchaseCartMapper;
    private final PurchaseCartItemMapper purchaseCartItemMapper;
    private final AuthService authService;

    public List<PurchaseCartDto> getAllPurchaseCarts() {
        return purchaseCartRepository.findAll()
                .stream()
                .map(purchaseCartMapper::toDto)
                .toList();
    }

    public Page<PurchaseCartDto> getPurchaseCarts(PurchaseCartFilter filter, Pageable pageable) {
        return purchaseCartRepository.findAll(PurchaseCartSpecification.filter(filter), pageable).map(purchaseCartMapper::toDto);
    }

    public PurchaseCartDto getPurchaseCart(Long purchaseCartId) {
        PurchaseCart purchaseCart = purchaseCartRepository.findById(purchaseCartId).orElseThrow(PurchaseCartNotFoundException::new);
        return purchaseCartMapper.toDto(purchaseCart);
    }

    public PurchaseCartWithItemPageResponse getPurchaseCartWithItemPagination(Long purchaseCartId, Pageable pageable, PurchaseCartItemFilter filter) {
        PurchaseCart purchaseCart = purchaseCartRepository.findPurchaseCartById(purchaseCartId).orElseThrow(PurchaseCartNotFoundException::new);
        Page<PurchaseCartItemDto> result = purchaseCartItemRepository.findPurchaseCartItems(purchaseCartId, filter.getName(), pageable).map(purchaseCartItemMapper::toDto);
        PagedResponse<PurchaseCartItemDto> pagedResponse = new PagedResponse<PurchaseCartItemDto>(result);
        return purchaseCartMapper.toMinDto(purchaseCart, result);
    }

    public PurchaseCartDto createPurchaseCart(PurchaseCartCreateRequest request) {
        var user = authService.getCurrentUser();
        PurchaseCart purchaseCart = new PurchaseCart();
        purchaseCart.setName(request.getName());
        List<PurchaseCartItem> items = new ArrayList<>();
        for (PurchaseCartItemAddRequest item : request.getItems()) {
            var product = productRepository.findById(item.getProductId()).orElse(null);
            if (product == null) {
                throw new ProductNotFoundException();
            }
            var productDetail = productDetailRepository.findById(item.getProductId()).orElse(null);
            if (productDetail == null) {
                throw new ProductDetailNotFoundException();
            }
            PurchaseCartItem purchaseCartItem = new PurchaseCartItem();
            purchaseCartItem.setPurchaseCart(purchaseCart);
            purchaseCartItem.setProduct(product);
            purchaseCartItem.setProductDetail(productDetail);
            purchaseCartItem.setPurchaseQty(item.getPurchaseQty());
            items.add(purchaseCartItem);
        }
        purchaseCart.setItems(items);
        purchaseCart.setCreatedBy(user);
        purchaseCartRepository.save(purchaseCart);
        return purchaseCartMapper.toDto(purchaseCart);
    }

    public PurchaseCartDto updatePurchaseCart(Long purchaseCartId, PurchaseCartUpdateRequest request) {
        var purchaseCart = purchaseCartRepository.findById(purchaseCartId).orElseThrow(PurchaseCartNotFoundException::new);
        var user = authService.getCurrentUser();
        purchaseCart.setName(request.getName());
        purchaseCart.setUpdatedBy(user);
        purchaseCartRepository.save(purchaseCart);
        return purchaseCartMapper.toDto(purchaseCart);
    }

    public PurchaseCartItemDto addPurchaseCartItem(Long purchaseCartId, PurchaseCartItemAddRequest request) {
        PurchaseCart purchaseCart = purchaseCartRepository.findById(purchaseCartId).orElse(null);
        if (purchaseCart == null) {
            throw new PurchaseCartNotFoundException();
        }
        var product = productRepository.findById(request.getProductId()).orElse(null);
        if (product == null) {
            throw new ProductNotFoundException();
        }
        var productDetail = productDetailRepository.findById(request.getProductDetailId()).orElse(null);
        if (productDetail == null) {
            throw new ProductDetailNotFoundException();
        }
        var findPurchaseCartItem = purchaseCartItemRepository.findPurchaseCartItem(purchaseCartId, request.getProductId(), request.getProductDetailId()).orElse(null);
        if (findPurchaseCartItem != null) {
            throw new PurchaseCartItemDuplicateException();
        }
        PurchaseCartItem purchaseCartItem = new PurchaseCartItem();
        purchaseCartItem.setPurchaseCart(purchaseCart);
        purchaseCartItem.setProduct(product);
        purchaseCartItem.setProductDetail(productDetail);
        purchaseCartItem.setPurchaseQty(request.getPurchaseQty());
        purchaseCartItemRepository.save(purchaseCartItem);
        return purchaseCartItemMapper.toDto(purchaseCartItem);
    }

    public PurchaseCartItemDto updatePurchaseCartItem(Long purchaseCartId, Long purchaseCartItemId, PurchaseCartItemUpdateRequest request) {
        PurchaseCart purchaseCart = purchaseCartRepository.findById(purchaseCartId).orElse(null);
        if (purchaseCart == null) {
            throw new PurchaseCartNotFoundException();
        }
        PurchaseCartItem purchaseCartItem = purchaseCartItemRepository.findById(purchaseCartItemId).orElse(null);
        if (purchaseCartItem == null) {
            throw new PurchaseCartItemNotFoundException();
        }
        purchaseCartItem.setPurchaseCart(purchaseCart);
        purchaseCartItem.setPurchaseQty(request.getPurchaseQty());
        purchaseCartItemRepository.save(purchaseCartItem);
        return purchaseCartItemMapper.toDto(purchaseCartItem);
    }

    public PurchaseCartItemDto removePurchaseCartItem(Long purchaseCartId, Long purchaseCartItemId) {
        PurchaseCart purchaseCart = purchaseCartRepository.findById(purchaseCartId).orElse(null);
        if (purchaseCart == null) {
            throw new PurchaseCartNotFoundException();
        }
        PurchaseCartItem purchaseCartItem = purchaseCartItemRepository.findById(purchaseCartItemId).orElse(null);
        if (purchaseCartItem == null) {
            throw new PurchaseCartItemNotFoundException();
        }
        purchaseCart.getItems().remove(purchaseCartItem);
        purchaseCartItem.setPurchaseCart(null);
        purchaseCartRepository.save(purchaseCart);
        return purchaseCartItemMapper.toDto(purchaseCartItem);
    }

    public void clearPurchaseCart(Long purchaseCartId) {
        PurchaseCart purchaseCart = purchaseCartRepository.findById(purchaseCartId).orElse(null);
        if (purchaseCart == null) {
            throw new PurchaseCartNotFoundException();
        }
        purchaseCart.getItems().clear();
        purchaseCartRepository.save(purchaseCart);
    }

    public PurchaseCartDto deletePurchaseCart(Long purchaseCartId) {
        var purchaseCart = purchaseCartRepository.findById(purchaseCartId).orElseThrow(() -> new PurchaseCartNotFoundException("purchaseCartId not found with ID: " + purchaseCartId));
        var user = authService.getCurrentUser();
        purchaseCart.setStatus(3);
        purchaseCart.setUpdatedBy(user);
        purchaseCartRepository.delete(purchaseCart);
        return purchaseCartMapper.toDto(purchaseCart);
    }

    public int removeBulkPurchaseCartItem(Long purchaseCartId, List<Long> purchaseCartItemIds) {
        return purchaseCartItemRepository.removeBulkPurchaseCartItem(purchaseCartId, purchaseCartItemIds);
    }
}
