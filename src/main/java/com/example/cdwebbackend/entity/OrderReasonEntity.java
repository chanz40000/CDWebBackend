package com.example.cdwebbackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_reason")
public class OrderReasonEntity  extends BaseEntity{
    @Column(name = "reason", nullable = false)
    private String reason;
    @Column(name = "reason_group")  // phan loai ly do
    private String reasonGroup;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReasonGroup() {
        return reasonGroup;
    }

    public void setReasonGroup(String reasonGroup) {
        this.reasonGroup = reasonGroup;
    }
}
