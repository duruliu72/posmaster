package com.osudpotro.posmaster.user.customer;

import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.multimedia.Multimedia;
import com.osudpotro.posmaster.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "customers",indexes = {
        @Index(name = "idx_customer_user_id", columnList = "user_id")
})
public class Customer extends BaseEntity {
    private String otpCode;
    private LocalDateTime otpRequestDateTime;
    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;
}
