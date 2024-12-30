package com.trustme.dto.request;

import com.trustme.reflection.annotation.GreaterThan;
import com.trustme.reflection.annotation.NullToEmpty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {
    @GreaterThan(value = 0, message = "The amount must be greater than zero!")
    Double amount;
    @NotNull
    String receiver;
    @Nullable
    String description;

}
