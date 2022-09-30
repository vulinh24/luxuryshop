package com.luxuryshop.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_users")
public class User extends ParentEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8688323980624037918L;
	
	@Column(name = "enabled", nullable = false)
	private boolean enabled = Boolean.TRUE;

	@NotBlank(message = "không được để trống!")
	@Size(min = 5, max = 50, message = "tên đăng nhập từ 5 tới 50 kí tự")
	@Column(name = "username", length = 45, nullable = false)
	private String username;

	@NotBlank(message = "không được để trống!")
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[A-Za-z])(?=\\S+$).{6,}$", message = "ít nhất 6 kí tự, có ít nhất một chữ số và một chữ cái")
	@Column(name = "password", length = 60, nullable = false)
	private String password;

	@NotBlank(message = "không được để trống!")
	@Email(message = "email không đúng định dạng")
	@Column(name = "email", length = 45, nullable = false)
	private String email;

	@Column(name = "name", length = 100, nullable = true)
	private String name;

	@Size(min = 5, max = 13, message = "số điện thoại không đúng định dạng")
	@Column(name = "phone", length = 15, nullable = true)
	private String phone;

	@Column(name = "address", nullable = true, columnDefinition = "TEXT")
	private String address;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "tbl_user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	List<Role> roles;

	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	private List<Cart> carts;

	@OneToMany(mappedBy = "user")
	List<DetailOrder> detailOrder;
	
	@ManyToMany(fetch = FetchType.LAZY) 
	@JoinTable(name = "tbl_favorite_products", 
				joinColumns = @JoinColumn(name = "user_id"),
				inverseJoinColumns = @JoinColumn(name="product_id"))
	List<Product> fProducts; // focus
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	List<RateProduct> rates; //focus
	
	@PreRemove
	void deletefk() {
		List<DetailOrder> deleOrder = this.getDetailOrder();
		for (DetailOrder o : deleOrder) {
			o.setUser(null);
		}
	}
}
