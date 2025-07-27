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

	@Bean
	public CommandLineRunner demoUser(KullaniciDao kullaniciDao, PasswordEncoder passwordEncoder) {
		return args -> {
			if (kullaniciDao.findByKullaniciAdi("ogrenci1").isEmpty()) {
				Kullanici k = new Kullanici();
				k.setKullaniciAdi("ogrenci1");
				k.setSifre(passwordEncoder.encode("123456"));
				k.setEmail("ogrenci1@uni.edu");
				k.setRol("OGRENCI");
				k.setAktif(true);
				kullaniciDao.save(k);
			}
		};
	}

	@Bean
	public CommandLineRunner demoTestData(
		KullaniciDao kullaniciDao,
		DersDao dersDao,
		KayitDao kayitDao,
		NotDao notDao,
		PasswordEncoder passwordEncoder,
		OgrenciDao ogrenciDao,
		JdbcTemplate jdbcTemplate
	) {
		return args -> {
			System.out.println("[TEST VERISI] Otomatik test verisi ekleniyor...");
			
			// Önce tüm tabloları temizle (admin dahil)
			try {
				jdbcTemplate.execute("DELETE FROM notlar");
				jdbcTemplate.execute("DELETE FROM kayitlar");
				jdbcTemplate.execute("DELETE FROM dersler");
				jdbcTemplate.execute("DELETE FROM ogrenciler");
				jdbcTemplate.execute("DELETE FROM kullanici");
				System.out.println("[TEST VERISI] Eski veriler TAMAMEN temizlendi.");
			} catch (Exception e) {
				System.out.println("[TEST VERISI] Veri temizleme hatası: " + e.getMessage());
			}
			// Sonra admin ve diğer test verilerini ekle
			
			// Admin kullanıcısı ekle
			if (kullaniciDao.findByKullaniciAdi("admin").isEmpty()) {
				Kullanici admin = new Kullanici();
				admin.setKullaniciAdi("admin");
				admin.setSifre(passwordEncoder.encode("admin123"));
				admin.setEmail("admin@uni.edu");
				admin.setRol("ADMIN");
				admin.setAktif(true);
				kullaniciDao.save(admin);
				System.out.println("[TEST VERISI] Admin eklendi: admin");
			}
			// 3 öğretmen ekle
			for (int i = 1; i <= 3; i++) {
				String username = "ogretmen" + i;
				if (kullaniciDao.findByKullaniciAdi(username).isEmpty()) {
					Kullanici ogretmen = new Kullanici();
					ogretmen.setKullaniciAdi(username);
					ogretmen.setSifre(passwordEncoder.encode("ogretmen" + i));
					ogretmen.setEmail(username + "@uni.edu");
					ogretmen.setRol("OGRETMEN");
					ogretmen.setAktif(true);
					kullaniciDao.save(ogretmen);
					System.out.println("[TEST VERISI] Ogretmen eklendi: " + username);
				}
			}
			// Öğrencileri ve dersleri ekle
			for (int i = 1; i <= 10; i++) {
				String ogrenciKullaniciAdi = "ogrenci" + i;
				if (kullaniciDao.findByKullaniciAdi(ogrenciKullaniciAdi).isEmpty()) {
					// Kullanici ekle
					Kullanici kullanici = new Kullanici();
					kullanici.setKullaniciAdi(ogrenciKullaniciAdi);
					kullanici.setSifre(passwordEncoder.encode("ogrenci123"));
					kullanici.setEmail(ogrenciKullaniciAdi + "@mail.com");
					kullanici.setRol("OGRENCI");
					kullanici.setAktif(true);
					kullaniciDao.save(kullanici);
				}
				// Ogrenci ekle
				Ogrenci ogrenci = new Ogrenci();
				ogrenci.setAd("Ad" + i);
				ogrenci.setSoyad("Soyad" + i);
				ogrenci.setOgrenciNo("202300" + i);
				ogrenci.setEmail("ogrenci" + i + "@mail.com");
				ogrenci.setTelefon("55500000" + i);
				ogrenciDao.save(ogrenci);
			}
			for (int i = 1; i <= 5; i++) {
				try {
					Ders ders = new Ders();
					ders.setDersAdi("Ders " + i);
					ders.setDersKodu("DRS" + (100 + i));
					ders.setKredi(5);
					ders.setKapasite(30);
					ders.setKontenjan(30);
					ders.setOgretmenId((long) ((i - 1) % 3 + 1));
					ders.setOgretmenAdi("ogretmen" + ((i - 1) % 3 + 1));
					ders.setDurum("ACIK");
					ders.setOnKosulDersId(null); // Ön koşul yok
					ders.setGun("Pazartesi");
					ders.setSaat("09:00");
					ders.setSinif("A101");
					ders.setMinOgrenciSayisi(1);
					dersDao.save(ders);
					System.out.println("[TEST VERISI] Ders eklendi: " + ders.getDersAdi());
				} catch (Exception e) {
					System.out.println("[TEST VERISI] Ders eklenemedi: " + e.getMessage());
				}
			}
			// Sadece ilk öğrenciye 2 ders kaydı ekle
			try {
				kayitDao.kayitEkle(1L, 1L, "2024-2025", "ONAYLANDI", 1L);
				System.out.println("[TEST VERISI] Kayıt eklendi: ogrenci1 - Ders 1");
			} catch (Exception e) {
				System.out.println("[TEST VERISI] Kayıt eklenemedi: ogrenci1 - Ders 1: " + e.getMessage());
			}
			try {
				kayitDao.kayitEkle(1L, 2L, "2024-2025", "ONAYLANDI", 1L);
				System.out.println("[TEST VERISI] Kayıt eklendi: ogrenci1 - Ders 2");
			} catch (Exception e) {
				System.out.println("[TEST VERISI] Kayıt eklenemedi: ogrenci1 - Ders 2: " + e.getMessage());
			}
			// Her öğrenciye her dersten not ver
			var kayitlar = kayitDao.tumKayitlar();
			for (Kayit kayit : kayitlar) {
				boolean notVar = notDao.tumNotlar().stream()
					.anyMatch(n -> n.getOgrenciId().equals(kayit.getOgrenciId()) && n.getDersId().equals(kayit.getDersId()));
				if (!notVar) {
					Not not = new Not();
					not.setOgrenciId(kayit.getOgrenciId());
					not.setDersId(kayit.getDersId());
					not.setVize(30 + Math.random() * 30); // 30-60 arası
					not.setFinalNotu(40 + Math.random() * 40); // 40-80 arası
					not.setOrtalama((not.getVize() + not.getFinalNotu()) / 2);
					not.setHarfNotu(not.getOrtalama() >= 60 ? "CC" : "FF");
					not.setGirenOgretmenId(null);
					not.setTarih("2024-01-01");
					notDao.notEkle(not);
					System.out.println("[TEST VERISI] Not eklendi: ogrenciId=" + kayit.getOgrenciId() + ", dersId=" + kayit.getDersId());
				}
			}
			System.out.println("[TEST VERISI] Otomatik test verisi ekleme tamamlandı!");
		};
	}
}
