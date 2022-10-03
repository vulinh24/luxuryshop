package com.luxuryshop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.luxuryshop.entities.primarykey.PKOfCart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rate_product")
public class RateProduct implements Serializable {

    /**
     *
	 */
	private static final long serialVersionUID = 8705928410389589562L;

	@EmbeddedId
	private PKOfCart pk;

	@Column(name = "rate", nullable = false)
	private Integer rate;

	@ManyToOne
	@MapsId(value = "productId")
	@JoinColumn(name = "product_id")
	@JsonIgnore
	private Product product;

	@ManyToOne
	@MapsId(value = "userId")
	@JsonIgnore
	@JoinColumn(name = "user_id")
	private User user;

}
