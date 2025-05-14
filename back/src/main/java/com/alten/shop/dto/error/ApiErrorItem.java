package com.alten.shop.dto.error;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiErrorItem implements Serializable {
    private static final long serialVersionUID = -31234567890120L;

    private String target;
    private String message;
    private String[] params;
}
