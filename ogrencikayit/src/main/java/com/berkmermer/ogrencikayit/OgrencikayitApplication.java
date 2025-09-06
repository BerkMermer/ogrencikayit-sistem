package com.berkmermer.ogrencikayit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.berkmermer.ogrencikayit.model.Kullanici;
import com.berkmermer.ogrencikayit.dao.KullaniciDao;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.berkmermer.ogrencikayit.model.Ders;
import com.berkmermer.ogrencikayit.dao.DersDao;
import com.berkmermer.ogrencikayit.model.Kayit;
import com.berkmermer.ogrencikayit.dao.KayitDao;
import com.berkmermer.ogrencikayit.model.Not;
import com.berkmermer.ogrencikayit.dao.NotDao;
import com.berkmermer.ogrencikayit.model.Ogrenci;
import com.berkmermer.ogrencikayit.dao.OgrenciDao;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
@ComponentScan(basePackages = {"com.berkmermer.ogrencikayit"})
public class OgrencikayitApplication {

	public static void main(String[] args) {
		SpringApplication.run(OgrencikayitApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("http://localhost:5173")
						.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
						.allowedHeaders("*")
						.allowCredentials(true);
			}
		};
	}

}
