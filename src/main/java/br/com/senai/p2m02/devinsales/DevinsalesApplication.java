package br.com.senai.p2m02.devinsales;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "br.com.senai.p2m02.devinsales")
public class DevinsalesApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevinsalesApplication.class, args);
	}

}
