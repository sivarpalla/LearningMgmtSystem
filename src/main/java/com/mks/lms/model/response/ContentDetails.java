package com.mks.lms.model.response;

import java.util.List;

import com.mks.lms.entity.LmsContentEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentDetails {

	private String department;
	private String account;
	private String category;
	private List<LmsContentEntity> lmscontent;
}
