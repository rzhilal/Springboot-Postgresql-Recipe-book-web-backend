package com.arutalalab.model.response;

import java.util.List;

import com.arutalalab.model.FavoriteFoods;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ToggleFavoriteResponse {
    private int total;
    private List<FavoriteFoods> data;
    private String Message;
    private int statusCode;
    private String status;
}

