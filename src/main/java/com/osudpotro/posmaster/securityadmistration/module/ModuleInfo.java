package com.osudpotro.posmaster.securityadmistration.module;


import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.securityadmistration.resource.Resource;
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
@Table(name = "module_info")
public class ModuleInfo extends BaseEntity {
    private String moduleCode;
    private String moduleName;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "parent_id", nullable = true)
    private ModuleInfo parentModule;
    @OneToMany(mappedBy = "moduleInfo", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Resource> list=new ArrayList<>();

}
