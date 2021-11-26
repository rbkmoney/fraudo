package com.rbkmoney.fraudo.model;

import lombok.Data;

@Data
public class BaseModel {

    private String ip;
    private String email;
    private String phone;
    private String fingerprint;
    private Long amount;
    private String currency;
    private String paymentSystem;
    private String cardCategory;

}
