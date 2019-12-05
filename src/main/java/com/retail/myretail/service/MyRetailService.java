package com.retail.myretail.service;

import com.retail.myretail.bean.PriceBean;
import com.retail.myretail.bean.ProdcutPriceBean;
import com.retail.myretail.bean.ProductResponse;

public interface MyRetailService {

	public String getProductInfo(Long producId);
	
	public void setPriceDeatils(ProductResponse productResponse);
	
	public PriceBean getProductPriceDetails(long producId);
	
	public Integer saveProductPrice(ProdcutPriceBean prodcutPriceBean);
}
