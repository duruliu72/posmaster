package com.osudpotro.posmaster.multimedia;


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
public class Multimedia extends BaseEntity {
    private String name;
    private String imageUrl;
//    1=Local,2=https://cdn.osudpotro.com
    private Integer sourceLink=1;
    private boolean isLinked=false;
//    1=Image ,2= Video
    private Integer mediaType=1;
}
