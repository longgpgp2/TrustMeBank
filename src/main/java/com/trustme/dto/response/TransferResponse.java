package com.trustme.dto.response;

import com.trustme.dto.TransferDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

@Getter
@Setter
@NoArgsConstructor
public class TransferResponse extends Response<TransferDto> {
    public TransferResponse(int code, String message, TransferDto result) {
        super(code, message, result);
    }
}
