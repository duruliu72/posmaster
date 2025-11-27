package com.osudpotro.posmaster.genericunit;

import com.osudpotro.posmaster.common.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class GenericUnit extends BaseEntity {
    //IU,IU/GM,Billion,gm/Vial,&
    private String name;
}