package com.osudpotro.posmaster.salecart;

import com.osudpotro.posmaster.branch.Branch;
import com.osudpotro.posmaster.branch.BranchNotFoundException;
import com.osudpotro.posmaster.branch.BranchRepository;
import com.osudpotro.posmaster.purchase.*;
import com.osudpotro.posmaster.user.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
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

    public SaleCartDto createSaleCart(SaleCartCreateRequest request) {
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
            saleCartItem.setSaleCart(saleCart);
            items.add(saleCartItem);
        }
        saleCart.setItems(items);
        saleCart.setBranch(branch);
        saleCart.setCreatedBy(authUser);
        saleCartRepo.save(saleCart);
        return null;
    }

    public SaleCartDto updateSaleCart(Long saleCartId, UpdateSaleCartRequest request) {
        var saleCart = saleCartRepo.findById(saleCartId).orElseThrow(PurchaseException::new);
        var authUser = authService.getCurrentUser();
        saleCart.setEmail(request.getEmail());
        saleCart.setMobile(request.getMobile());
        saleCart.setUpdatedBy(authUser);
        return null;
    }

    public SaleCartDto addSaleCartItem(Long saleCartId, SaleCartItemAddRequest request) {
        var authUser = authService.getCurrentUser();
        Branch branch = branchRepo.findById(authUser.getBranch().getId()).orElseThrow(BranchNotFoundException::new);
        SaleCart saleCart = saleCartRepo.findByIdAndBranch(saleCartId, branch).orElse(null);
        if (saleCart == null) {
            saleCart = new SaleCart();
        }
        saleCart.setBranch(branch);

        return null;
    }

    public SaleCartDto updateSaleCartItem(Long saleCartId, SaleCartItemAddRequest request) {
        var authUser = authService.getCurrentUser();
        Branch branch = branchRepo.findById(authUser.getBranch().getId()).orElseThrow(BranchNotFoundException::new);
        SaleCart saleCart = saleCartRepo.findByIdAndBranch(saleCartId, branch).orElse(null);
        if (saleCart == null) {
            saleCart = new SaleCart();
        }
        saleCart.setBranch(branch);

        return null;
    }
}
