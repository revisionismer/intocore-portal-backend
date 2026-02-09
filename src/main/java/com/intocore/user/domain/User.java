package com.intocore.user.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
	
	private Long id;       
	
    private String username;
    
    private String password;
    
    private String role;
}
