package com.hyc.duiba.beans;

import cn.com.duiba.credits.sdk.CreditConsumeResult;

/**
 * 兑吧虚拟接口响应参数包
 * @author yanglang
 * 
 * @date 2015-12-24
 *
 */
public class CreditConsumeVirtualResult extends CreditConsumeResult {

	private String supplierBizId;

	private boolean success;

	private String message;

	public CreditConsumeVirtualResult(boolean success) {
		super(success);
		this.success = success;
	}

	public String getSupplierBizId() {
		return supplierBizId;
	}

	public void setSupplierBizId(String supplierBizId) {
		this.supplierBizId = supplierBizId;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String toString() {
		if (this.success) {
			return "{'status':'ok','message':'" + this.message
					+ "','errorMessage':'','supplierBizId':'" + this.getSupplierBizId()
					+ "','credits':'" + this.getCredits() + "'}";
		}
		return "{'status':'fail','message':'" + this.message
				+ "','errorMessage':'" + this.getErrorMessage()
				+ "','supplierBizId':'" + this.getSupplierBizId()
				+ "','credits':'" + this.getCredits() + "'}";
	}

}
