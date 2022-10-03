package com.luxuryshop.entities;

import com.luxuryshop.entities.primarykey.PKOfRoleUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_role")
public class RoleOfUser implements Serializable {
    /**
     *
     */
	private static final long serialVersionUID = -4577245630479225066L;
	@EmbeddedId
	private PKOfRoleUser pk;
}
