package com.alten.shop.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

@Component
@ConfigurationProperties(prefix = "shop")
@Getter
@Setter
@Validated
public class AppProperties {

    @NotEmpty
    @Email
    private String adminEmail;
}
