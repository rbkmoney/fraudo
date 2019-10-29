package com.rbkmoney.fraudo.model;

import lombok.Data;

@Data
public class P2PModel {

    private String ip;
    private String email;
    private String bin;
    private String pan;
    private String country;
    private String cardTokenFrom;
    private String cardTokenTo;
    private String fingerprint;
    private String identityId;
    private Long amount;
    private String currency;

}
