package com.sistema.sistema.infra.exceptions;

import com.sistema.sistema.domain.dto.MessageErrorDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ListErrorResponse {
    private List<MessageErrorDTO> message;
    private int statusCode;

    public ListErrorResponse(List<MessageErrorDTO> message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

}
