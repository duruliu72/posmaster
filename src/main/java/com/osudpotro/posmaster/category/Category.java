package com.osudpotro.posmaster.category;


import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.picture.Picture;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "categories")
public class Category extends BaseEntity {
    private String name;
    @Lob
    private String description;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "parent_category_id", nullable = true)
    private Category parentCat;
    @OneToMany(mappedBy = "parentCat", fetch = FetchType.LAZY)
    private List<Category> children = new ArrayList<>();
    @OneToOne(fetch = FetchType.LAZY, optional = true)
    private Picture picture;
}