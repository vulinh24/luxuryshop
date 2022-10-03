package com.luxuryshop.apis;

import com.luxuryshop.entities.Product;
import com.luxuryshop.entities.User;
import com.luxuryshop.repositories.UserRepository;
import com.luxuryshop.services.MyUserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;




@Transactional
@RestController
public class FavoriteProductAPI {
	
	@Autowired
	EntityManager entityManager;
	
	@Autowired
	UserRepository userRepository;
	
	@RequestMapping(value = "/add-favorite/{id}", method = RequestMethod.GET)
	public String index(@PathVariable Integer id,HttpServletRequest request) {
		if (request.getSession().getAttribute("USER") == null) return "usernotfound";
		MyUserDetail detail = 
				(MyUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		
		User user = userRepository.getById(detail.getUser().getId());
		List<Product> likedProduct = user.getFProducts();
		for (Product p : likedProduct) {
			if (p.getId().equals(id)) {
				String sql = "delete from favorite_product where user_id = ?1 and product_id = ?2";
				Query query = entityManager.createNativeQuery(sql);
				query.setParameter(1, user.getId());
				query.setParameter(2, id);
				query.executeUpdate();
				return "remove";
			}
		}
		String sql = "insert into favorite_product values(?1,?2)";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter(1, user.getId());
		query.setParameter(2, id);
		query.executeUpdate();
		return "add";
	}
	
	@RequestMapping(value = "/remove-favorite/{id}", method = RequestMethod.GET)
	public String remove(@PathVariable Integer id) {
		MyUserDetail detail = 
				(MyUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		User user = detail.getUser();
		String sql = "delete from favorite_product where user_id = ?1 and product_id = ?2";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter(1, user.getId());
		query.setParameter(2, id);
		query.executeUpdate();
		return "ok";
	}
}
