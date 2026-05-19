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

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private Branch requesterBranch;
    @ManyToOne(fetch = FetchType.LAZY)
    private Branch acceptorBranch;
    @ManyToOne(fetch = FetchType.LAZY)
    private Warehouse warehouse;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "send_by_requester", nullable = true)
    private User sendByRequester;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime sendAtByRequester;
    private String sendNoteByRequester;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "accept_by_requester", nullable = true)
    private User acceptByRequester;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime acceptAtByRequester;
    private String acceptNoteByRequester;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "accept_by_acceptor", nullable = true)
    private User acceptByAcceptor;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime acceptAtByAcceptor;
    private String acceptNoteByAcceptor;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "send_by_acceptor", nullable = true)
    private User sendByAcceptor;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime sendAtByAcceptor;
    private String sendNoteByAcceptor;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "updated_by")
    private User updatedBy;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "created_by", nullable = true)
    private User createdBy;
    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
    //1,Dispatch Req created By Requested Branch,2=Send Dispatch Req By reqBranch,3=Accept Dispatch By request Receive Branch,4=Send Dispatch By request Receive Branch,5=Accept and Add to Inventory By Requester Branch
    private Integer dispatchStatus = 1;
    @OneToMany(mappedBy = "dispatch", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DispatchItem> items = new ArrayList<>();
    public int getTotalQty() {
        return items.stream()
                .filter(i ->
                        i.getDispatchQty() != null
                )
                .mapToInt(DispatchItem::getDispatchQty)
                .sum();
    }

    public BigDecimal getTotalPurchasePrice() {
        return items.stream()
                .filter(i ->
                        i.getDispatchQty() != null && i.getProductDetail().getPurchasePrice() != null
                )
                .map(i ->
                        i.getProductDetail().getPurchasePrice()
                                .multiply(BigDecimal.valueOf(i.getDispatchQty()))
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
    }

    public String getGenerateDispatchInvoice() {
        String tripPrefix = "INV";
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
