package shop.cashier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class CashRegisterApplication{

	public static void main(String[] args) {
		SpringApplication.run(CashRegisterApplication.class, args);
	}

}
