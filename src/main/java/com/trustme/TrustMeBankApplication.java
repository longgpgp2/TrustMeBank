package com.trustme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
//@EnableCaching
public class TrustMeBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrustMeBankApplication.class, args);
	}

}
