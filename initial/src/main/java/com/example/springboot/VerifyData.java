package com.example.springboot;

import java.nio.ByteBuffer;

public class VerifyData {

	private String originalData;
	private String signedData;

	public String getOriginalData() {
		return originalData;
	}

	public void setOriginalData(String originalData) {
		this.originalData = originalData;
	}

	public String getSignedData() {
		return signedData;
	}

	public void setSignedData(String signedData) {
		this.signedData = signedData;
	}

}