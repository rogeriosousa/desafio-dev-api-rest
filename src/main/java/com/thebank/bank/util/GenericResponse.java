package com.thebank.bank.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GenericResponse {

	@JsonProperty()
	private String mensage;

	public GenericResponse(String mensage) {
		super();
		this.mensage = mensage;
	}

	public String getMensage() {
		return mensage;
	}


}
