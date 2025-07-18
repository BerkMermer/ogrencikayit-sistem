# Öğrenci Kayıt Sistemi

Spring Boot ile geliştirilmiş öğrenci ve ders kayıt yönetim sistemi.

## Teknolojiler

- **Spring Boot 3.5.3**
- **Java 17**
- **PostgreSQL**
- **Maven**
- **Spring Web MVC**
- **Spring Data JPA**
- **Spring JDBC**

## Kurulum

1. PostgreSQL'de `ogrencikayit_yeni` veritabanını oluşturun
2. `application.properties` dosyasındaki veritabanı bilgilerini güncelleyin
3. Projeyi çalıştırın:
```bash
mvn spring-boot:run
```

## Veritabanı Yapısı

### Tablolar
- `ogrenci` - Öğrenci bilgileri
- `ders` - Ders bilgileri
- `ogrenci_ders` - Öğrenci-Ders ilişki tablosu

### Otomatik Tablo Oluşturma
JPA `ddl-auto=update` ayarı ile tablolar otomatik oluşturulur.

## API Endpoints

### Öğrenci
- `GET /ogrenci` - Tüm öğrenciler
- `POST /ogrenci` - Yeni öğrenci ekle
- `PUT /ogrenci/{id}` - Öğrenci güncelle
- `DELETE /ogrenci/{id}` - Öğrenci sil

### Ders
- `GET /ders` - Tüm dersler
- `POST /ders` - Yeni ders ekle
- `PUT /ders/{id}` - Ders güncelle
- `DELETE /ders/{id}` - Ders sil

## Proje Yapısı

```
src/main/java/com/berkmermer/ogrencikayit/
├── controller/     # REST Controllers
├── dao/           # JDBC Data Access Objects
│   ├── OgrenciDao.java
│   └── DersDao.java
├── model/         # JPA Entity Models
│   ├── Ogrenci.java
│   └── Ders.java
└── repository/    # Repository Layer
```

## Mimari

- **Model Katmanı**: JPA Entity'leri (@Entity, @Table, @ManyToMany)
- **DAO Katmanı**: JDBC ile veritabanı işlemleri
- **Controller Katmanı**: REST API endpoints
- **Many-to-Many İlişki**: Öğrenci ↔ Ders 