package shop.cashregister;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(//exclude = { SecurityAutoConfiguration.class },
		scanBasePackages={"shop.cashregister.security", "shop.cashregister.model", "shop.cashregister.control", "shop.cashregister.config"})
@EnableTransactionManagement
public class CashRegisterApplication{

	public static void main(String[] args) {
		SpringApplication.run(CashRegisterApplication.class, args);
	}

}
