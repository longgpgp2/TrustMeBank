package com.trustme.constant;

import com.trustme.dto.response.*;
import com.trustme.enums.ErrorCode;

public class ConstantResponses {
    public static final UserResponse USER_EXISTED =  new UserResponse(ErrorCode.USER_EXISTS.getHttpStatus(), ErrorCode.USER_EXISTS.getErrorMessage(), null);
    public static final LoginResponse INVALID_CREDENTIALS = new LoginResponse(ErrorCode.INVALID_CREDENTIALS.getHttpStatus(), ErrorCode.INVALID_CREDENTIALS.getErrorMessage(), null);
    public static final LoginResponse USER_NOT_FOUND = new LoginResponse(ErrorCode.USER_NOT_FOUND.getHttpStatus(), ErrorCode.USER_NOT_FOUND.getErrorMessage(), null);
    public static final TransfersResponse GET_TRANSFERS_ERROR = new TransfersResponse(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus(), ErrorCode.INTERNAL_SERVER_ERROR.getErrorMessage(), null);
    public static final UserEditResponse INVALID_USER_TO_EDIT = new UserEditResponse(ErrorCode.INVALID_USER.getHttpStatus(), ErrorCode.INVALID_USER.getErrorMessage(), null);
    public static final TransferResponse INVALID_RECEIVER = new TransferResponse(ErrorCode.RECEIVER_NOT_FOUND.getHttpStatus(), ErrorCode.RECEIVER_NOT_FOUND.getErrorMessage(), null);
    public static final TransferResponse INVALID_AMOUNT = new TransferResponse(ErrorCode.INVALID_AMOUNT.getHttpStatus(), ErrorCode.INVALID_AMOUNT.getErrorMessage(), null);
}
