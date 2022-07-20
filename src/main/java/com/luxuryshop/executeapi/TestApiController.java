package com.luxuryshop.executeapi;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.slugify.Slugify;
import com.luxuryshop.WebConstants;
import com.luxuryshop.SolveException.APIException;
import com.luxuryshop.entities.Product;
import com.luxuryshop.entities.ProductImages;
import com.luxuryshop.repositories.CategoryRepository;
import com.luxuryshop.repositories.CollectionRepository;
import com.luxuryshop.repositories.FProductRepository;
import com.luxuryshop.repositories.ImageRepository;
import com.luxuryshop.repositories.ProductDetailRepository;
import com.luxuryshop.repositories.ProductRepository;
import com.luxuryshop.services.MyUserDetail;

@RestController
public class TestApiController {

	@Autowired
	ProductRepository productRepository;

	@Autowired
	ProductDetailRepository detailRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	CollectionRepository collectionRepository;
	
	@Autowired
	ImageRepository imageRepository;
	
	@Autowired
	FProductRepository fProductRepository;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JWTGenerateToken tokenProvider;
	
	@Autowired
	BlockIP blockIP;

	@RequestMapping(value = "/api/login", method = RequestMethod.POST)
	public LoginResponse authenticateUser(@RequestBody LoginRequest loginRequest, final HttpServletRequest request) {
		
		if (blockIP.checkIp(request) == false) return new LoginResponse("", "ip is blocked");
		try {
			// Xác thực từ username và password.
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
			// Nếu không xảy ra exception tức là thông tin hợp lệ
			// Set thông tin authentication vào Security Context
			SecurityContextHolder.getContext().setAuthentication(authentication);

			// Trả về jwt cho người dùng.
			String jwt = tokenProvider.generateToken((MyUserDetail) authentication.getPrincipal());
			return new LoginResponse(jwt, "authenticate successfully");
		} catch (Exception e) {
			blockIP.blockip(request);
			return new LoginResponse("", "username or password is wrong");
		}
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/api/v1/list", method = RequestMethod.GET)
	protected ResponseEntity getAll() throws APIException{
		try {
			List<Product> prods = productRepository.findAll();
			return ResponseEntity.ok(prods);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT) // server cannot connect to another server
					.body(new ErrorResponse("can not connect to server"));
		}
	}
	
	@Transactional
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/api/v1/change/add", method = RequestMethod.POST)
	protected ResponseEntity addNewProduct(@RequestBody ProductModel product) throws APIException{
		try {
			Product newProd = new Product();
			newProd.setTitle(product.getTitle());
			newProd.setPrice(product.getPrice());
			newProd.setPriceOld(product.getPriceOld());
			newProd.setShortDescription(product.getShortDescription());
			newProd.setIsHot(product.getIsHot());
			newProd.setIsNew(product.getIsNew());
			newProd.setIsSale(product.getIsSale());
			newProd.setRate(product.getRate());
			newProd.setProductImages(product.getProductImages());
			newProd.setCategory(categoryRepository.getById(product.getCategoryId()));
			newProd.setCollection(collectionRepository.getById(product.getCollectionId()));
			newProd.setProductDetail(product.getProductDetail());
			Slugify slg = new Slugify();
			String seo = slg.slugify(product.getTitle() + "-" + System.currentTimeMillis());
			newProd.setSeo(seo);
			newProd.setCreatedDate(LocalDateTime.now());
			newProd = productRepository.save(newProd);
			return ResponseEntity
					.status(HttpStatus.CREATED)
					.body(newProd);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorResponse("something is wrong"));
		}
	}
	
	@Transactional
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/api/v1/change/update/{id}", method = RequestMethod.PUT)
	protected ResponseEntity updateProduct(@PathVariable Integer id ,@RequestBody ProductModel product) throws APIException{
		try {
			Product newProd = null;
			try {
				newProd = productRepository.findById(id).get();
				if (newProd == null) throw new EntityNotFoundException();
			} catch (Exception e) {
				return ResponseEntity
						.status(HttpStatus.BAD_REQUEST)
						.body(new ErrorResponse("product not found"));
			}
			newProd.setTitle(product.getTitle());
			newProd.setPrice(product.getPrice());
			newProd.setPriceOld(product.getPriceOld());
			newProd.setShortDescription(product.getShortDescription());
			newProd.setIsHot(product.getIsHot());
			newProd.setIsNew(product.getIsNew());
			newProd.setIsSale(product.getIsSale());
			newProd.setRate(product.getRate());
			newProd.setProductImages(product.getProductImages());
			newProd.setCategory(categoryRepository.getById(product.getCategoryId()));
			newProd.setCollection(collectionRepository.getById(product.getCollectionId()));
			newProd.setProductDetail(product.getProductDetail());
			Slugify slg = new Slugify();
			String seo = slg.slugify(product.getTitle() + "-" + System.currentTimeMillis());
			newProd.setSeo(seo);
			newProd.setUpdatedDate(LocalDateTime.now());
			newProd = productRepository.save(newProd);
//			ProductDetail detail = newProd.getProductDetail();
//			detailRepository.save(detail);
			return ResponseEntity
					.status(HttpStatus.CREATED)
					.body(newProd);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorResponse("something is wrong"));
		}
	}
	
	@Transactional
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/api/v1/change/delete/{id}", method = RequestMethod.DELETE)
	protected ResponseEntity deleteProduct(@PathVariable Integer id ) throws APIException{
		try {
			Product oldProd = null;
			try {
				oldProd = productRepository.findById(id).get();
				if (oldProd == null) throw new EntityNotFoundException();
				List<ProductImages> images = oldProd.getProductImages();
				for (ProductImages pi : images) {
					File file = new File(WebConstants.IMG_URL + pi.getPath());
					file.delete();
				}
				imageRepository.deleteAll(images);
				fProductRepository.deleteProduct(id);
			} catch (Exception e) {
				return ResponseEntity
						.status(HttpStatus.BAD_REQUEST)
						.body(new ErrorResponse("product not found"));
			}
			
			productRepository.delete(oldProd);
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(oldProd);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorResponse("something is wrong"));
		}
	}

}
