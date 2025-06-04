package com.example.cdwebbackend.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewStatsResponse {
    private Integer stars;
    private long count;
}
