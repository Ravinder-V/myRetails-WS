package com.retail.myretail.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.retail.myretail.bean.PriceBean;
import com.retail.myretail.bean.ProdcutPriceBean;
import com.retail.myretail.bean.ProductResponse;
import com.retail.myretail.exception.ProductNotFoundException;
import com.retail.myretail.service.MyRetailService;

@RestController
@RequestMapping("/product")
public class MyRetailController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MyRetailController.class);

	@Autowired
	MyRetailService myRetailService; 
	
	@GetMapping("/{id}")
    public ProductResponse getProductData(@PathVariable(name="id") String productId) {
		LOGGER.info("MyRetailController getProductData Starts");
		ProductResponse productResponse = new ProductResponse();
		try {
			productResponse.setId(Long.parseLong(productId));
			String productName = myRetailService.getProductInfo(Long.parseLong(productId));
			productResponse.setName(productName);
			myRetailService.setPriceDeatils(productResponse);
		}catch (ProductNotFoundException e) {
			throw new ProductNotFoundException(e.getMessage());
		}catch (Exception e) {
			LOGGER.error("MyRetailController getProductData Error",e);
		}
		LOGGER.info("MyRetailController getProductData Ends");
		return productResponse;
	}
	
	@GetMapping("/{id}/price")
	public PriceBean getProductPrice(@PathVariable(name="id") String productId) {
		LOGGER.info("MyRetailController getProductPrice Starts");
		PriceBean priceBean = new PriceBean();
		try {
			priceBean = myRetailService.getProductPriceDetails(Long.parseLong(productId));
		}catch (ProductNotFoundException e) {
			throw new ProductNotFoundException(e.getMessage());
		}catch (Exception e) {
			LOGGER.error("MyRetailController getProductPrice Error",e);
		}
		LOGGER.info("MyRetailController getProductPrice Ends");
		return priceBean;
	}
	
	@PostMapping("/{id}")
	public String saveProductPrice(@PathVariable(name="id") String productId,
			@RequestBody ProdcutPriceBean prodcutPriceBean) {
		LOGGER.info("MyRetailController getProductPrice Starts");
		String message = "Failure";
		try {
			Integer id = null;
			if(prodcutPriceBean != null) {
				prodcutPriceBean.setProductId(Long.valueOf(productId));
				id = myRetailService.saveProductPrice(prodcutPriceBean);
				if(id != null) {
					message = "success"; 
				}
			}
		}catch (Exception e) {
			LOGGER.error("MyRetailController getProductPrice Error",e);
		}
		LOGGER.info("MyRetailController getProductPrice Ends");
		return message;
	}
	

}
