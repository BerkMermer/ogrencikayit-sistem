# Öğrenci Kayıt Sistemi

Çok katmanlı mimariye sahip, Spring Boot (backend) ve React (frontend) ile geliştirilmiş, öğrenci, ders, öğretmen, kayıt ve not yönetimi sağlayan, güvenli ve modern bir web uygulaması.

## Özellikler
- Öğrenci, ders, öğretmen, kayıt ve not işlemleri (CRUD)
- Kullanıcı girişi, kayıt olma, şifre sıfırlama
- Rol ve yetki kontrolü (Öğrenci, Öğretmen, Admin)
- Spring Security ile şifre hashleme
- **PostgreSQL (ana veritabanı), H2 (sadece testler için)**
- Docker ve Kubernetes ile dağıtım
- Unit ve integration testler
- Modern React arayüzü

## Mimarî
```
Kullanıcı
   │
Frontend (React)
   │ REST API
Backend (Spring Boot)
   │
PostgreSQL
```

## Klasör Yapısı
```
frontend/         # React arayüzü
ogrencikayit/     # Spring Boot backend ve k8s dosyaları
```

## Kurulum ve Çalıştırma

### 1. Veritabanı Oluşturma
- PostgreSQL'de `ogrencikayit_yeni` adında bir veritabanı oluşturun.

### 2. Backend (Spring Boot)
- `ogrencikayit/src/main/resources/application.properties` dosyasındaki veritabanı bağlantı bilgilerini güncelleyin.
- Proje kökünde:
  ```bash
  cd ogrencikayit
  mvn spring-boot:run
  ```

### 3. Frontend (React)
- `frontend` klasöründe:
  ```bash
  npm install
  npm run dev
  ```
- Varsayılan olarak [http://localhost:5173](http://localhost:5173) adresinde çalışır.

## Docker ile Çalıştırma

1. Proje kökünde Docker imajı oluşturun:
   ```bash
   docker build -t ogrenci-kayit:latest .
   ```
2. Docker Compose veya manuel olarak PostgreSQL ve uygulamayı başlatabilirsiniz.

## Kubernetes ile Dağıtım

1. `ogrencikayit/k8s` dizinine gidin:
   ```bash
   cd ogrencikayit/k8s
   chmod +x deploy.sh
   ./deploy.sh
   ```
2. Tüm adımlar ve detaylar için `ogrencikayit/k8s/README.md` dosyasına bakabilirsiniz.

## API Endpointleri (Özet)
- `POST /api/kullanici/login` — Kullanıcı girişi
- `POST /api/kullanici/register` — Kayıt olma
- `GET /api/ogrenciler` — Tüm öğrenciler (rol kontrolü ile)
- `GET /api/dersler` — Tüm dersler (rol kontrolü ile)
- `GET /api/dashboard` — Genel istatistikler

## Testler
- Backend testleri: `ogrencikayit/src/test/java/com/berkmermer/ogrencikayit/`
- Test veritabanı: **H2 (sadece testler için)**
- Çalıştırmak için:
  ```bash
  cd ogrencikayit
  mvn test
  ```

## Katkı ve Geliştirme
- Pull request ve issue açabilirsiniz.
- Frontend (React) ve backend (Spring Boot) için katkılar memnuniyetle karşılanır.

## Lisans
MIT

---
**Not:** Proje gerçek bir okul ortamında kullanılacaksa, şifre sıfırlama ve e-posta gönderimi için ek güvenlik önlemleri alınmalıdır. 

## Ekran Görüntüleri

Ana Sayfa:
![Ana Sayfa](screenshots/ana%20sayfa.png)

Giriş Sayfası:
![Giriş Sayfası](screenshots/giriş%20sayfası.png)

Kayıt Ol Sayfası:
![Kayıt Ol Sayfası](screenshots/kayıt%20ol%20sayfası.png)

Öğretmen Paneli:
![Öğretmen Paneli](screenshots/öğretmen%20paneli.png)

Yardım ve Destek Sayfası:
![Yardım ve Destek Sayfası](screenshots/yardım%20ve%20destek%20sayfası.png) 