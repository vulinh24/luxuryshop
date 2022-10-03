package com.luxuryshop.entities;

import com.luxuryshop.entities.primarykey.PKOfCart;
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
@Table(name = "favorite_product")
public class FavoriteProduct implements Serializable {
    /**
     *
     */
	private static final long serialVersionUID = -4577245630479225066L;
	@EmbeddedId
	private PKOfCart pk;
}
