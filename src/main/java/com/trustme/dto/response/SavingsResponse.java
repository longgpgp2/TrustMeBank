package com.trustme.dto.response;

import com.trustme.dto.SavingDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class SavingsResponse extends Response<List<SavingDto>>{
    public SavingsResponse(int code, String message, List<SavingDto> result) {
        super(code, message, result);
    }
}
