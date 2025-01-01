package com.trustme.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum SavingStatus {
    ACTIVE( "Saving is active!"),
    INACTIVE( "Saving is inactive!"),
    CLOSED( "Saving was closed!"),
    SUSPENDED( "Saving was suspended!");

    private String statusMessage;

}
