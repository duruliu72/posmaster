package com.osudpotro.posmaster.dispatch;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.osudpotro.posmaster.branch.Branch;
import com.osudpotro.posmaster.organization.Organization;
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
    @Column(unique = true)
    private String dispatchRef;
    private String dispatchInvoice;
    @ManyToOne(fetch = FetchType.LAZY)
    private Organization organization;
    @ManyToOne(fetch = FetchType.LAZY)
    private Branch reqestedBranch;
    @ManyToOne(fetch = FetchType.LAZY)
    private Branch requestReceivedBranch;
    @ManyToOne(fetch = FetchType.LAZY)
    private Warehouse warehouse;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "requested_by", nullable = true)
    private User requestedBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime requestedAt;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "request_received_by", nullable = true)
    private User requestReceivedBy;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "dispatch_by", nullable = true)
    private User dispatchBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dispatchAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime requestReceivedAt;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "updated_by")
    private User updatedBy;
    @OneToMany(mappedBy = "dispatch", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DispatchDetail> items = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY ,optional = true)
    @JoinColumn(name = "created_by", nullable = true)
    private User createdBy;
    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private  LocalDateTime updatedAt;
    //1=Send Dispatch Req By reqBranch,2=Accept Dispatch By senderBranch,3=Accept By  and to InventorySummary By
    private Integer dispatchStatus = 1;
    public String getGenerateDispatchInvoice() {
        String tripPrefix = "DIS";
        String datePart = new SimpleDateFormat("yyyyMMdd").format(new Date());
        long nextSeq = 1;
        if (this.getDispatchInvoice() != null) {
            String lastItem = this.getDispatchInvoice();
            String lastPart = lastItem.length() > 5 ? lastItem.substring(lastItem.length() - 6) : lastItem;
            if (!lastPart.isEmpty()) {
                try {
                    nextSeq = Long.parseLong(lastPart) + 1;
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
        //Format String
        return String.format("%s-%s-%06d", tripPrefix, datePart, nextSeq);
    }
    public String getGenerateDispatchRef() {
        String tripPrefix = "DIS";
        String datePart = new SimpleDateFormat("yyyyMMdd").format(new Date());
        long nextSeq = 1;
        if (this.getDispatchRef() != null) {
            String lastItem = this.getDispatchRef();
            String lastPart = lastItem.length() > 5 ? lastItem.substring(lastItem.length() - 6) : lastItem;
            if (!lastPart.isEmpty()) {
                try {
                    nextSeq = Long.parseLong(lastPart) + 1;
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
        //Format String
        return String.format("%s-%s-%06d", tripPrefix, datePart, nextSeq);
    }
}
