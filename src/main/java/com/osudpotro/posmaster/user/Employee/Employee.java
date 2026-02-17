package com.osudpotro.posmaster.user.Employee;

import com.osudpotro.posmaster.common.BaseEntity;
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
@Table(name = "employees", indexes = {
        @Index(name = "idx_employee_user_id", columnList = "user_id")
})
public class Employee extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "user_id", unique = true,nullable = false)
    private User user;
}
