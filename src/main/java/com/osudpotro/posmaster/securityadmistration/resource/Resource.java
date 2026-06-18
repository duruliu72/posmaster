package com.osudpotro.posmaster.securityadmistration.resource;

import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.multimedia.Multimedia;
import com.osudpotro.posmaster.securityadmistration.module.ModuleInfo;
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
@Table(name = "resources")
public class Resource extends BaseEntity {
    private String name;
    @Column(name = "resource_key", nullable = true, unique = true, length = 50)
    private String resourceKey;
    @Column(name = "url")
    private String url;
    private String icon;
//    1=Dashboard , 2=Web App
    private Integer resourceType;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "parent_id", nullable = true)
    private Resource parentResource;
    private Integer orderNo;
    private boolean isSideLoc=true;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id")
    private ModuleInfo moduleInfo;
    @OneToMany(mappedBy = "resource", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ResourceAction> resourceActions = new ArrayList<>();
}
