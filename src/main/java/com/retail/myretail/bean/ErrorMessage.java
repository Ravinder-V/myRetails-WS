package com.retail.myretail.bean;

public class ErrorMessage {

	private String message;
	
	public ErrorMessage(String message) {
        super();
        this.message = message;
    }

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
