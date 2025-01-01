package com.trustme.dto.response;

import com.trustme.dto.SavingDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SavingResponse extends Response<SavingDto>{
    public SavingResponse(int code, String message, SavingDto result) {
        super(code, message, result);
    }
}
