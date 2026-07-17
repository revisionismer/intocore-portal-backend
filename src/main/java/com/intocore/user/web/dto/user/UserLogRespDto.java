package com.intocore.user.web.dto.user;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.intocore.user.domain.UserLog;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class UserLogRespDto {

	private Long id;
	private String username;
    private String ip;
    private String device;
    private String country;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;
    
    public UserLogRespDto(UserLog userLog, String username) {
        this.id = userLog.getId();
        this.username = username;
        this.ip = userLog.getIp();
        this.device = userLog.getDevice();
        this.country = userLog.getCountry();
        this.createdDate = userLog.getCreatedDate();
    }
}
