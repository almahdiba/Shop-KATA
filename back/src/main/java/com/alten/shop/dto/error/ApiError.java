package com.alten.shop.dto.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.ObjectUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ApiError implements Serializable {
    private static final long serialVersionUID = -3571893142664028282L;

    private String type;    
    private String title;   
    private String detail;   
    private String instance;
    private Integer status; 
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String traceId; 
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ApiErrorItem> errors = null;

    public ApiError addError(String msg, String target) {
        this.errors.add(ApiErrorItem.builder()
                .message(msg)
                .target(target)
                .build());
        return this;
    }

    public ApiError addError(String msg, String target, Serializable[] params) {
        this.errors.add(ApiErrorItem.builder()
                .message(msg)
                .target(target)
                .params(Arrays.stream(params)
                        .map(e -> ObjectUtils.defaultIfNull(e, "NA"))
                        .map(Objects::toString)
                        .toArray(String[]::new)
                )
                .build());
        return this;
    }
}
