package com.yaanlabs.healthpredictionapi.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor@AllArgsConstructor
public class SimpleAuthRequest {

    @NonNull
    private String privateAuthType;
    @NonNull
    private String username;
    @NonNull
    private String birthDate;
    @NonNull
    private String userCellPhoneNumber;

}
