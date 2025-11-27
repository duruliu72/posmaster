package com.osudpotro.posmaster.resource;

import com.osudpotro.posmaster.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ui_resources")
public class UiResource extends BaseEntity {
    private String name;
    @Column(name = "ui_resource_key", nullable = false, unique = true, length = 50)
    private String uiResourceKey;
    @Column(name = "ui_url", unique = true)
    private String pageUrl;
    private String icon;
    private Long parentId;
    private int orderNo;
    private boolean isSideLoc;
    @OneToMany(mappedBy = "uiResource", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<UiResourceDetails> actions = new ArrayList<>();
}
