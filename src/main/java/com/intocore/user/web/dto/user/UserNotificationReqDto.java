package com.intocore.user.web.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class UserNotificationReqDto {
	
	@JsonProperty("isApprovalEnabled")
	private boolean isApprovalEnabled;
	
	@JsonProperty("isNoticeEnabled")
    private boolean isNoticeEnabled;
    
}
