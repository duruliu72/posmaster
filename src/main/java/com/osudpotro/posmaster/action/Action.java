package com.osudpotro.posmaster.action;

import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "actions")
public class Action  extends BaseEntity {
    private String name; // e.g., VIEW, CREATE, UPDATE, DELETE
}