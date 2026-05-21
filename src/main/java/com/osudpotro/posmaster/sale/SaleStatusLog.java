package com.osudpotro.posmaster.sale;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.osudpotro.posmaster.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "sale_status_log")
public class SaleStatusLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id")
    private Sale sale;
    //1=Pending,2=Processing (After review by customer care) ,3=Accepted by Pharmacy,4=Packaging by Pharmacy,5=Dispatch by Rider(Head)/fleet(head),5=On the way by rider(through Apps),6=Delivered On the way by rider(App),7=Cancelled After review by customer care
    private Integer saleStatus;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "created_by", nullable = true)
    private User createdBy;
    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
}
