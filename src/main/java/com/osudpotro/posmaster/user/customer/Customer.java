package com.osudpotro.posmaster.user.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.multimedia.Multimedia;
import com.osudpotro.posmaster.offerhub.membership.Membership;
import com.osudpotro.posmaster.user.User;
import com.osudpotro.posmaster.user.customer.address.Address;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "customers", indexes = {
        @Index(name = "idx_customer_user_id", columnList = "user_id")
})
public class Customer extends BaseEntity {
    private String userName;
    private String firstName;
    private String lastName;
    @Column(unique = true, nullable = true)
    private String email;
    @Column(unique = true, nullable = true)
    private String mobile;
    private String password;
    private String secondaryEmail;
    private String secondaryMobile;
    private Boolean isValidAccount;
    //    like Male=1, Female=2, Third=3
    private Integer gender;
    private String provider;
    private String providerId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "multimedia_id")
    private Multimedia profilePic;
    private String otpCode;
    private LocalDateTime otpRequestDateTime;
    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;
    private Boolean isAllowCredit;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "membership_id", nullable = true)
    private Membership membership;
    @JsonIgnore
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    @OrderBy("id DESC")
    private List<Address> addresses = new ArrayList<>();
}