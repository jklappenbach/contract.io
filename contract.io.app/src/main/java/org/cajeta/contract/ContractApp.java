package org.cajeta.contract;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cajeta.contract.plugin.Encoding;
import org.cajeta.contract.plugin.Protocol;

/**
 * Hello world!
 * 
 */
public class ContractApp {
	public static void main(String[] args) {
		BigDecimal account = new BigDecimal(0.0);
		int years = 25;
		BigDecimal monthlyDeposit = new BigDecimal(3000.0);
		BigDecimal rate = new BigDecimal(0.05);
		for (int year = 0; year < years; year++) {
			for (int month = 0; month < 12; month++) {
				account = account.add(monthlyDeposit);
			}
			account = account.add(account.multiply(rate));
		}
		System.out.println("Total: " + account.toPlainString());
		
		Contract contract = Contract.getInstance();
		try {
			contract.process(args);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}	
}
