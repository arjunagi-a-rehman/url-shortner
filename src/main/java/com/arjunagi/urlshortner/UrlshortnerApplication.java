package com.arjunagi.urlshortner;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Short URL service",
				description = "This service provide the api's to convert long and space consuming url's to short urls and also you can control the url expiry time",
				contact = @Contact(
						name = "Arjunagi A Rehman",
						email = "abdul123arj@gmail.com"
				)
		),
		servers = {
				// this should point to your sever if reverse proxy is not setup in your sever you can just delete this feild or make it point localhost
				@Server(url = "https://sus9.in")
		}
)
@EnableAsync

public class UrlshortnerApplication {
	public static void main(String[] args) {
		SpringApplication.run(UrlshortnerApplication.class, args);
	}

}
