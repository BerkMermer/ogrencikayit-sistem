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


## Ekran Görüntüleri

Ana Panel:
![Ana Panel](screenshots/ana%20panel.png)

Giriş Paneli:
![Giriş Paneli](screenshots/giriş%20paneli.png)

Admin Paneli:
![Admin Paneli](screenshots/admin%20paneli.png)

Yardım ve Destek:
![Yardım ve Destek](screenshots/yardım%20ve%20destek.png) 
