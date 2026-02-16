package com.osudpotro.posmaster.user.customer;

import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.multimedia.Multimedia;
import com.osudpotro.posmaster.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "customers",indexes = {
        @Index(name = "idx_customer_email", columnList = "email"),
        @Index(name = "idx_customer_mobile", columnList = "mobile"),
        @Index(name = "idx_customer_user_id", columnList = "user_id")
})
public class Customer extends BaseEntity {
    public Customer() {
        this.email = this.randomEmail();
    }
    private String userName;
    private String firstName;
    private String lastName;
    @Column(unique = true, nullable = true)
    private String email;
    @Column(unique = true, nullable = true)
    private String mobile;
    private Boolean isValidEmail = true;
    private String password;
    private String secondaryEmail;
    private String secondaryMobile;
    //    like Male=1, Female=2, Third=3
    private Integer gender;
    private String provider;
    private String providerId;
    private String otpCode;
    private LocalDateTime otpRequestDateTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "multimedia_id")
    private Multimedia profilePic;
    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;
    public String randomEmail() {
        return "user_" + UUID.randomUUID().toString().substring(0, 8) + "@osudpotro.com";
    }
}
