package com.osudpotro.posmaster.salecart;

import com.osudpotro.posmaster.branch.Branch;
import com.osudpotro.posmaster.branch.BranchNotFoundException;
import com.osudpotro.posmaster.branch.BranchRepository;
import com.osudpotro.posmaster.common.EntityException;
import com.osudpotro.posmaster.common.EntityNotFoundException;
import com.osudpotro.posmaster.offerhub.promotion.PromotionOfferDto;
import com.osudpotro.posmaster.purchase.*;
import com.osudpotro.posmaster.user.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SaleCartService {
    @Autowired
    private SaleCartRepository saleCartRepo;
    @Autowired
    private SaleCartItemRepository saleCartItemRepo;
    @Autowired
    private AuthService authService;
    @Autowired
    private BranchRepository branchRepo;
    @Autowired
    private PurchaseRepository purchaseRepo;
    @Autowired
    private PurchaseDetailRepository purchaseDetailRepo;
    @Autowired
    private SaleCartMapper saleCartMapper;
    @Autowired
    private SaleCartItemMapper saleCartItemMapper;

    public List<SaleCartDto> getAllEntities() {
        return saleCartRepo.findAll()
                .stream()
                .map(saleCartMapper::toDto)
                .toList();
    }

    public Page<SaleCartDto> getAllEntities(SaleCartFilter filter, Pageable pageable) {
        return saleCartRepo.findAll(SaleCartSpecification.filter(filter), pageable).map(saleCartMapper::toDto);
    }

    public SaleCartDto getEntity(Long entityId) {
        var entity = saleCartRepo.findById(entityId).orElseThrow(() -> new EntityNotFoundException("Sale Cart not found with ID: " + entityId));
        return saleCartMapper.toDto(entity);
    }

    public SaleCartDto createEntity(SaleCartCreateRequest request) {
        var authUser = authService.getCurrentUser();
        Branch branch = branchRepo.findById(authUser.getBranch().getId()).orElseThrow(BranchNotFoundException::new);
        SaleCart saleCart = new SaleCart();
        saleCart.setEmail(request.getEmail());
        saleCart.setMobile(request.getMobile());
        List<SaleCartItem> items = new ArrayList<>();
        if (request.getCartItem() != null) {
            SaleCartItemAddRequest itemRequest = request.getCartItem();
            SaleCartItem saleCartItem = new SaleCartItem();
            if (itemRequest.getPurchaseId() != null) {
                Purchase purchase = purchaseRepo.findById(itemRequest.getPurchaseId()).orElseThrow(PurchaseException::new);
                saleCartItem.setPurchase(purchase);
            }
            if (itemRequest.getPurchaseDetailId() != null) {
                PurchaseDetail purchaseDetail = purchaseDetailRepo.findById(itemRequest.getPurchaseDetailId()).orElseThrow(PurchaseException::new);
                saleCartItem.setPurchaseDetail(purchaseDetail);
            }
            if (itemRequest.getSaleQty() != null) {
                saleCartItem.setSaleQty(itemRequest.getSaleQty());
            }
            saleCartItemRepo.save(saleCartItem);
            items.add(saleCartItem);
            saleCartItem.setSaleCart(saleCart);

        }
        saleCart.setItems(items);
        saleCart.setBranch(branch);
        saleCart.setCreatedBy(authUser);
        saleCartRepo.save(saleCart);
        return saleCartMapper.toDto(saleCart);
    }

    public SaleCartDto updateEntity(Long saleCartId, UpdateSaleCartRequest request) {
        var saleCart = saleCartRepo.findById(saleCartId).orElseThrow(PurchaseException::new);
        var authUser = authService.getCurrentUser();
        saleCart.setEmail(request.getEmail());
        saleCart.setMobile(request.getMobile());
        saleCart.setUpdatedBy(authUser);
        return saleCartMapper.toDto(saleCart);
    }

    public SaleCartItemDto addSaleCartItem(Long saleCartId, SaleCartItemAddRequest request) {
        var authUser = authService.getCurrentUser();
        Branch branch = branchRepo.findById(authUser.getBranch().getId()).orElseThrow(BranchNotFoundException::new);
        SaleCart saleCart = saleCartRepo.findByIdAndBranch(saleCartId, branch).orElse(null);
        if (saleCart == null) {
            throw new EntityNotFoundException("Sale Cart Not Found");
        }
        Purchase purchase = purchaseRepo.findById(request.getPurchaseId()).orElseThrow(PurchaseException::new);
        PurchaseDetail purchaseDetail = purchaseDetailRepo.findById(request.getPurchaseDetailId()).orElseThrow(PurchaseException::new);
        SaleCartItem saleCartItem = saleCartItemRepo.findBySaleCartAndPurchaseAndPurchaseDetail(saleCart, purchase, purchaseDetail).orElse(null);
        if (saleCartItem != null) {
            throw new EntityException("Sale Cart Already Added");
        }
        saleCartItem = new SaleCartItem();
        if (request.getPurchaseId() != null) {
            saleCartItem.setPurchase(purchase);
        }
        if (request.getPurchaseDetailId() != null) {
            saleCartItem.setPurchaseDetail(purchaseDetail);
        }
        if (request.getSaleQty() != null) {
            saleCartItem.setSaleQty(request.getSaleQty());
        }
        saleCartItem.setSaleCart(saleCart);
        saleCartItemRepo.save(saleCartItem);
        return saleCartItemMapper.toDto(saleCartItem);
    }

    public SaleCartItemDto updateSaleCartItem(Long saleCartId, Long saleCartItemId, UpdateSaleCartItemRequest request) {
        var authUser = authService.getCurrentUser();
        Branch branch = branchRepo.findById(authUser.getBranch().getId()).orElseThrow(BranchNotFoundException::new);
        SaleCart saleCart = saleCartRepo.findByIdAndBranch(saleCartId, branch).orElse(null);
        SaleCartItem saleCartItem = saleCartItemRepo.findById(saleCartItemId).orElse(null);
        if (saleCart == null) {
            throw new EntityNotFoundException("Sale Cart Not Found");
        }
        if (saleCartItem == null) {
            throw new EntityNotFoundException("Sale Cart Item Not Found");
        }
        saleCartItem.setSaleQty(request.getSaleQty());
        return saleCartItemMapper.toDto(saleCartItem);
    }

    public SaleCartDto deleteEntity(Long entityId) {
        SaleCart saleCart = saleCartRepo.findById(entityId).orElseThrow(() -> new EntityNotFoundException("Sale Cart not found with ID: " + entityId));
        var user = authService.getCurrentUser();
        saleCart.setStatus(3);
        saleCart.setUpdatedBy(user);
        saleCartRepo.save(saleCart);
        return saleCartMapper.toDto(saleCart);
    }
}
