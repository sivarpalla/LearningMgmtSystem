package com.mks.lms.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ChangePasswordRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String oldPassword;

    @NotBlank
    private String newPassword;
}
