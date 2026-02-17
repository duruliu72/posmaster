package com.osudpotro.posmaster.user.admin;

import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.multimedia.Multimedia;
import com.osudpotro.posmaster.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "admin_users", indexes = {
        @Index(name = "idx_admin_user_user_id", columnList = "user_id")
})
public class AdminUser extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "user_id", unique = true,nullable = false)
    private User user;
}
