package com.mks.lms.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryRequestModel {

    private String categoryName;
    private String categoryDescription;
}
