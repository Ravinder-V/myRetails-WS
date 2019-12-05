package com.retail.myretail.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.retail.myretail.bean.PriceBean;
import com.retail.myretail.bean.ProdcutPriceBean;
import com.retail.myretail.bean.ProductResponse;
import com.retail.myretail.bean.ServiceConstants;
import com.retail.myretail.exception.ProductNotFoundException;
import com.retail.myretail.repository.ProductPriceRepository;

@Service
public class MyRetailServiceImpl implements MyRetailService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MyRetailServiceImpl.class);
	
	@Autowired
	ProductPriceRepository priceRepository;
	
	String url = "https://redsky.target.com/v2/pdp/tcin/"
			+ "{productId}?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics";
	
	@Override
	public String getProductInfo(Long producId) {
		LOGGER.info("MyRetailService getProductInfo Starts");
		String title = null;
		try {
			Map<String, Long> urlParams = new HashMap<String, Long>();
			urlParams.put("productId", producId);
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
			RestTemplate restTemplate = new RestTemplate();
			//ResponseEntity<String> response = restTemplate.getForObject(builder.buildAndExpand(urlParams).toUri() , String.class);
			ResponseEntity<String> response = restTemplate.exchange(builder.buildAndExpand(urlParams).toUri(), HttpMethod.GET, null, String.class);
			if(response.getStatusCodeValue() == 200) {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode root = mapper.readTree(response.getBody()).get("product");
				if(root != null) {
					JsonNode item = root.get("item");
					if(item != null) {
						title = item.get("product_description")
								.get("title").asText();
					}
				}
			}
		}catch (final HttpClientErrorException e) {
			if(e.getRawStatusCode() == 404) {
				throw new ProductNotFoundException(String.valueOf(producId));
			}
		}catch (Exception e) {
			LOGGER.error("MyRetailService getProductInfo Error",e);
		}
		LOGGER.info("MyRetailService getProductInfo Ends");
		return title;
		
	}

	@Override
	public void setPriceDeatils(ProductResponse productResponse) {
		LOGGER.info("MyRetailService setPriceDeatils Starts");
		try {
			PriceBean priceBean = invokePriceApi(productResponse.getId());
			productResponse.setCurrent_price(priceBean);
		}catch (Exception e) {
			LOGGER.error("MyRetailService getProductInfo Error",e);
		}
		LOGGER.info("MyRetailService setPriceDeatils Ends");
		
	}
	
	public PriceBean invokePriceApi(long producId) throws JsonMappingException, JsonProcessingException {
		String url = "http://localhost:9090/product/{id}/price";
		Map<String, Long> urlParams = new HashMap<String, Long>();
		urlParams.put("id", producId);
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
		RestTemplate restTemplate = new RestTemplate();
		PriceBean priceBean = new PriceBean();
		try {
			ResponseEntity<String> response = restTemplate.exchange(builder.buildAndExpand(urlParams).toUri(), HttpMethod.GET, null, String.class);
			if(response.getStatusCodeValue() == 200) {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode root = mapper.readTree(response.getBody());
				if(root != null) {
					priceBean.setValue(Float.valueOf(root.get("value").asText()));
					priceBean.setCurrency_code(root.get("currency_code").asText());
					
				}
			}
		}catch (final HttpClientErrorException e) {
			if(e.getRawStatusCode() == 404) {
				throw new ProductNotFoundException(String.valueOf(producId));
			}
		}
		return priceBean;
	}

	@Override
	public PriceBean getProductPriceDetails(long producId) {
		PriceBean priceBean = new PriceBean();
		priceBean.setValue(13.49f);
		priceBean.setCurrency_code(ServiceConstants.USD);
		return priceBean;
	}

	@Override
	public Integer saveProductPrice(ProdcutPriceBean prodcutPriceBean) {
		LOGGER.info("MyRetailService saveProductPrice Starts");
		Integer id = null;
		try {
			ProdcutPriceBean saveProdcutPriceBean = priceRepository.save(prodcutPriceBean);
			if(saveProdcutPriceBean != null) {
				id = saveProdcutPriceBean.getId();
			}
		}catch (Exception e) {
			LOGGER.error("MyRetailService saveProductPrice Error",e);
		}
		LOGGER.info("MyRetailService saveProductPrice Ends");
		return id;
	}

}
