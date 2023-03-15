package com.caito.tutorialwebflux.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserNewDTO {
    private String username;
    private String email;
    private String password;
}
