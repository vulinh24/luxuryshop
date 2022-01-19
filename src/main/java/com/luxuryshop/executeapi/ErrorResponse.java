package com.luxuryshop.executeapi;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErrorResponse {
	
	private LocalDateTime time;
	private String message;

	public ErrorResponse(String message) {
		this.time = LocalDateTime.now();
		this.message = message;
	}
	
}
