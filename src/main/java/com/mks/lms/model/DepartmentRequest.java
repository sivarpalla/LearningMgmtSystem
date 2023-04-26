package com.mks.lms.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepartmentRequest {

    public String departmentName;
    public String departmentDescription;
}
