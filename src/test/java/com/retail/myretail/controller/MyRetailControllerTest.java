package com.retail.myretail.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.retail.myretail.bean.PriceBean;
import com.retail.myretail.bean.ProductResponse;
import com.retail.myretail.bean.ServiceConstants;
import com.retail.myretail.exception.ProductNotFoundException;
import com.retail.myretail.service.MyRetailService;

@RunWith(MockitoJUnitRunner.class)
public class MyRetailControllerTest {

	@InjectMocks
	MyRetailController myRetailController;
	
	@Mock
	private MyRetailService myRetailService;
	
	@Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
	
	@Test
	public void getProductService() {
		String title="abc";
		when(myRetailService.getProductInfo(1234L)).thenReturn(title);
		ProductResponse productResponse = myRetailController.getProductData("1234");
		assertNotNull(productResponse);
		assertEquals(productResponse.getName(), title);
	}
	
	@Test(expected = ProductNotFoundException.class)
	public void getProductServiceNotFoundException() {
		when(myRetailService.getProductInfo(1234L)).thenThrow(new ProductNotFoundException("1234"));
		ProductResponse productResponse = myRetailController.getProductData("1234");
		assertNotNull(productResponse);
	}
	
	@Test
	public void getPriceService() {
		PriceBean priceBean = new PriceBean();
		priceBean.setValue(13.49f);
		priceBean.setCurrency_code(ServiceConstants.USD);
		when(myRetailService.getProductPriceDetails(1234L)).thenReturn(priceBean);
		PriceBean priceResponse = myRetailController.getProductPrice("1234");
		assertNotNull(priceResponse);
		assertEquals(priceResponse, priceBean);
	}
	
	@Test(expected = ProductNotFoundException.class)
	public void getPriceServiceNotFoundException() {
		PriceBean priceBean = new PriceBean();
		priceBean.setValue(13.49f);
		priceBean.setCurrency_code(ServiceConstants.USD);
		when(myRetailService.getProductPriceDetails(1234L)).thenThrow(new ProductNotFoundException("1234"));
		PriceBean priceResponse = myRetailController.getProductPrice("1234");
		assertNotNull(priceResponse);
	}
}
