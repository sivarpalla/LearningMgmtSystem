package com.mks.lms.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DefaultResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String status;
    private String message;
}