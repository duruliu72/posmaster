package com.osudpotro.posmaster.resource.ui;

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
    @Column(name = "ui_resource_key", nullable = true, unique = true, length = 50)
    private String uiResourceKey;
    @Column(name = "ui_url")
    private String pageUrl;
    private String icon;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "parent_id", nullable = true)
    private UiResource parentUiResource;
    private Integer orderNo;
    private boolean isSideLoc=true;
    @OneToMany(mappedBy = "uiResource", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<UiResourceAction> uiResourceActions = new ArrayList<>();
}
