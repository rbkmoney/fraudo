package com.rbkmoney.fraudo.model;

import lombok.Data;

@Data
public class FraudModel {

    private String ip;
    private String email;
    private String bin;
    private String fingerprint;
    private String shopId;
    private String partyId;
    private Long amount;

}
