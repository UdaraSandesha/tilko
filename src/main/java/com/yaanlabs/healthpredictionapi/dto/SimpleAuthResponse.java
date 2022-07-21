package com.yaanlabs.healthpredictionapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SimpleAuthResponse {

    private String token;
    private String txId;
    private String cxId;
    private String reqTxId;
    private String privateAuthType;
    private String username;
    private String birthDate;
    private String userCellPhoneNumber;
}
