package com.tnx.posBilling.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxDetail {
    private String taxName;
    private double taxper;
    private double taxableValue;
    private double taxAmount;
}
