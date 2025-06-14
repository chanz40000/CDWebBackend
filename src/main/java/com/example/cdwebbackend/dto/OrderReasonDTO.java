package com.example.cdwebbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderReasonDTO  extends AbstractDTO<OrderReasonDTO>{
    @JsonProperty("id")
    private Long id;
    @JsonProperty("reason")
    private String reason;

    @JsonProperty("reason_group")
    private String reasonGroup;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

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
