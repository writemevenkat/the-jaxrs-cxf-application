package com.vm.rest.auth.service.impl;

public class DealsServiceImpl implements DealsService {

	@Override
	public String list(String id) {
		System.out.println("id ::" + id);
		return "here is the id " + id;
	}

}
