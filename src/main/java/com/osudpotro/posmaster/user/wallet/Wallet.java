package com.osudpotro.posmaster.user.wallet;

import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.sale.Sale;
import com.osudpotro.posmaster.user.User;
import com.osudpotro.posmaster.user.UserType;
import com.osudpotro.posmaster.user.customer.Customer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@Entity
@Table(name = "wallets")
public class Wallet extends BaseEntity {
    @Enumerated(EnumType.STRING)
    private UserType userType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;
    private BigDecimal creditAmount;
    private BigDecimal debitAmount;
    //1=Sign Up Bonus Credit,2=For Customer order Credit,3=Employee Credit,4=For Customer order Return Debit
    private Integer walletType;
    private String note;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id")
    private Sale sale;
    private String saleRef;//order Ref
}
