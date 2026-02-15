package com.osudpotro.posmaster.user.Employee;

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
@Table(name = "employees", indexes = {
        @Index(name = "idx_employee_email", columnList = "email"),
        @Index(name = "idx_employee_mobile", columnList = "mobile"),
        @Index(name = "idx_employee_user_id", columnList = "user_id")
})
public class Employee extends BaseEntity {
    private String userName;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private Boolean isValidEmail;
    @Column(unique = true)
    private String mobile;
    private String password;
    private String secondaryEmail;
    private String secondaryMobile;
    //    like Male=1, Female=2, Third=3
    private Integer gender;
    private String provider;
    private String providerId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "multimedia_id")
    private Multimedia profilePic;
    @OneToOne
    @JoinColumn(name = "user_id", unique = true,nullable = false)
    private User user;
}
