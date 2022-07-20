package com.luxuryshop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.luxuryshop.entities.Product;
import com.luxuryshop.entities.ProductImages;
import com.luxuryshop.entities.User;
import com.luxuryshop.repositories.ImageRepository;
import com.luxuryshop.repositories.UserRepository;
import com.luxuryshop.services.ProductService;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class Test {

	public static void main(String[] args) {
		ApplicationContext context =  SpringApplication.run(Test.class, args);
		ProductService service = context.getBean(ProductService.class);
		Product product = service.findBySeo("sofa_da_elise").get(0);
		System.out.println(product.getTitle());
		List<ProductImages> pi = context.getBean(ImageRepository.class).findByProduct(product);
		HashMap<Integer, ProductImages> images = new HashMap<>();
		for (int i = 0; i < pi.size(); ++i) {
			images.put(i+1,pi.get(i));
		}
		Set<Integer> keys = images.keySet();
		keys.forEach(i -> {
			System.out.println(images.get(i).getPath());
		});
	}

}
