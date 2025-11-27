package com.osudpotro.posmaster.picture;

import com.osudpotro.posmaster.common.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class Picture extends BaseEntity {
    private String name;
    private String imageUrl;
    private boolean isLinked=false;
}
