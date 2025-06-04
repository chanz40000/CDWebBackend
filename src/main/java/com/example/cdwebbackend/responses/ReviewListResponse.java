package com.example.cdwebbackend.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ReviewListResponse {
    private List<ReviewResponse> reviews;
    private List<ReviewStatsResponse> stats;
}
