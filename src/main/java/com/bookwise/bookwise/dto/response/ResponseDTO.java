package com.bookwise.bookwise.dto.response;

import lombok.*;

@Getter @Setter @ToString
@AllArgsConstructor @RequiredArgsConstructor
public class ResponseDTO {

    private String status;

    private String message;

}
