package com.bookwise.bookwise.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDTO {

    private String statusCode;

    private String statusMsg;

}
