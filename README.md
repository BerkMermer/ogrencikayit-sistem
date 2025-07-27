# Öğrenci Kayıt Sistemi

Spring Boot 3 ile geliştirilmiş, öğrenci, ders, öğretmen, kayıt ve not yönetimi sağlayan, çok katmanlı ve güvenli bir RESTful API.

## Özellikler

- Öğrenci, ders, öğretmen, kayıt ve not işlemleri (CRUD)
- Kullanıcı girişi (login), kayıt olma (register), şifre sıfırlama
- Rol ve yetki kontrolü (Öğrenci, Öğretmen, Admin)
- Spring Security ile şifre hashleme
- PostgreSQL ve H2 desteği
- Docker ve Kubernetes ile dağıtım desteği
- Unit ve integration testler

## Teknolojiler

- Java 17
- Spring Boot 3.5.3
- Spring Data JPA & JDBC
- Spring Security
- PostgreSQL / H2
- Maven

## Kurulum

1. **Veritabanı Oluşturma**
   - PostgreSQL'de `ogrencikayit_yeni` adında bir veritabanı oluşturun.
2. **Veritabanı Ayarları**
   - `src/main/resources/application.properties` dosyasındaki veritabanı bağlantı bilgilerini güncelleyin.
3. **Projeyi Başlatma**
   ```bash
   mvn spring-boot:run
   ```
   veya IntelliJ IDEA ile `OgrencikayitApplication.java` dosyasını çalıştırın.

## API Endpointleri

### Kimlik Doğrulama ve Kullanıcı Yönetimi

- `POST /api/kullanici/login` — Kullanıcı girişi
- `POST /api/kullanici/register` — Kayıt olma
- `POST /api/kullanici/forgot-password` — Şifre sıfırlama token'ı oluşturma
- `POST /api/kullanici/reset-password` — Şifreyi sıfırlama
- `GET /api/kullanici/rol/{kullaniciAdi}` — Kullanıcı rolünü sorgulama

### Öğrenci
- `GET /api/ogrenciler` — Tüm öğrenciler (rol kontrolü ile)
- `POST /api/ogrenciler` — Yeni öğrenci ekle
- `PUT /api/ogrenciler/{id}` — Öğrenci güncelle
- `DELETE /api/ogrenciler/{id}` — Öğrenci sil

### Ders
- `GET /api/dersler` — Tüm dersler (rol kontrolü ile)
- `POST /api/dersler` — Yeni ders ekle
- `PUT /api/dersler/{id}` — Ders güncelle
- `DELETE /api/dersler/{id}` — Ders sil

### Kayıt ve Not
- `GET /api/kayitlar` — Kayıt listesi
- `POST /api/kayitlar` — Kayıt ekle
- `GET /api/notlar` — Not listesi
- `POST /api/notlar` — Not ekle/güncelle

### Dashboard
- `GET /api/dashboard` — Genel istatistikler

## Proje Yapısı

```
src/main/java/com/berkmermer/ogrencikayit/
├── controller/     # REST Controllers (API endpointleri)
├── dao/            # Data Access Objects (JDBC/JPA)
├── model/          # Entity modelleri
├── service/        # İş mantığı (business logic)
├── config/         # Güvenlik ve genel konfigürasyonlar
└── OgrencikayitApplication.java # Ana uygulama dosyası
```

## Testler

- Unit ve integration testler `src/test/java/com/berkmermer/ogrencikayit/` altında bulunur.
- Test veritabanı olarak H2 kullanılır.

## Gelişmiş Özellikler

- Şifreler BCrypt ile hash'lenir.
- Rol bazlı erişim kontrolü (endpointlerde yetki kontrolü)
- Şifre sıfırlama için token üretimi (gerçek projede e-posta ile gönderim önerilir)
- Docker ve Kubernetes ile kolay dağıtım

## Katkı ve Geliştirme

- Pull request ve issue açabilirsiniz.
- Frontend (React) entegrasyonu için API endpointleri hazırdır.

## Lisans

MIT

---

**Not:** Proje, gerçek bir okul ortamında kullanılacaksa, şifre sıfırlama ve e-posta gönderimi için ek güvenlik önlemleri alınmalıdır. 