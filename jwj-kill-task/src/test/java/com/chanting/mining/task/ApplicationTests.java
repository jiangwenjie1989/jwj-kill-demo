//package com.chanting.mining.task;
//
//
//
//import java.math.BigDecimal;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.SpringApplicationConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//
//import TaskApplication;
//import com.virtual.wallet.task.service.impl.Web3jService;
//
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = TaskApplication.class)
//@WebAppConfiguration
//public class ApplicationTests {
//	
//	@Autowired
//	private Web3jService web3jService;
//	
//	@Test
//	public void getBalance() {
//		BigDecimal balance = web3jService.getBalance("0xe6730d8afd643650ab246cde4f71085766f681a7");
//		System.out.println(balance.toString());
//		
////		String transaction = web3jService.transaction("0x7f4c7030801efc283c13b70ee26738f9c0091cf3", "F470BB2F4B36A4715C74AAB6AD89A6CF518EF7B6229927FD17A3DE91F0481F1F", "0xe6730d8afd643650ab246cde4f71085766f681a7", new BigDecimal("1"));
////		System.out.println(transaction);
//	}
//
//}
