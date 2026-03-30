package com.osudpotro.posmaster.dispatch;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.osudpotro.posmaster.branch.Branch;
import com.osudpotro.posmaster.organization.Organization;
import com.osudpotro.posmaster.purchase.Purchase;
import com.osudpotro.posmaster.supplier.Supplier;
import com.osudpotro.posmaster.user.User;
import com.osudpotro.posmaster.warehouse.Warehouse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "dispatches")
public class Dispatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;
    private String dispatchInvoice;
    @ManyToOne(fetch = FetchType.LAZY)
    private Organization organization;
    @ManyToOne(fetch = FetchType.LAZY)
    private Branch rootBranch;
    @ManyToOne(fetch = FetchType.LAZY)
    private Branch senderBranch;
    @ManyToOne(fetch = FetchType.LAZY)
    private Branch receiverBranch;
    @ManyToOne(fetch = FetchType.LAZY)
    private Warehouse warehouse;
    @ManyToOne(fetch = FetchType.LAZY)
    private Supplier supplier;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "dispatch_by", nullable = true)
    private User dispatchBy;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "updated_by")
    private User updatedBy;
    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dispatchAt;
    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
    @OneToMany(mappedBy = "dispatch", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<DispatchDetail> items = new ArrayList<>();
    public String getGenerateDispatchInvoice() {
        String tripPrefix="DIS";
        String datePart = new SimpleDateFormat("yyyyMMdd").format(new Date());
        long nextSeq = 1;
        if (this.getDispatchInvoice() != null) {
            String lastTripRef = this.getDispatchInvoice();
            String lastPart = lastTripRef.length() > 5 ? lastTripRef.substring(lastTripRef.length() - 6) : lastTripRef;
            if (!lastPart.isEmpty()) {
                try {
                    nextSeq = Long.parseLong(lastPart) + 1;
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
        //Format String
        return String.format("%s-%s-%06d",tripPrefix, datePart, nextSeq);
    }
}
