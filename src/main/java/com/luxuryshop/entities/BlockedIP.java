package com.luxuryshop.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_blocked_id")
public class BlockedIP {
	
	@Id
	@Column(name = "ip")
	private String ip;
	
	@Column(name = "last_login_failed")
	private Long lastLogin;
	
	@Column(name = "num_wrong")
	private Integer numWrong; 
}
