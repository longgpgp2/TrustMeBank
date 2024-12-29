package com.trustme.dto.response;

import com.trustme.dto.TransferDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class TransfersResponse extends Response<List<TransferDto>> {
    public TransfersResponse(int code, String message, List<TransferDto> result) {
        super(code, message, result);
    }
}
