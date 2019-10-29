package com.rbkmoney.fraudo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PaymentModel extends BaseModel {

    private String bin;
    private String pan;
    private String binCountryCode;
    private String cardToken;
    private String shopId;
    private String partyId;

}
