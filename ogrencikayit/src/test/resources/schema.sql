-- Tüm tabloları sil
DROP TABLE IF EXISTS notlar CASCADE;
DROP TABLE IF EXISTS kayitlar CASCADE;
DROP TABLE IF EXISTS dersler CASCADE;
DROP TABLE IF EXISTS ogrenciler CASCADE;
DROP TABLE IF EXISTS kullanicilar CASCADE;

-- Kullanıcılar
CREATE TABLE kullanicilar (
    id SERIAL PRIMARY KEY,
    kullanici_adi VARCHAR(50) UNIQUE NOT NULL,
    sifre VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    rol VARCHAR(20) NOT NULL,
    aktif BOOLEAN NOT NULL DEFAULT TRUE,
    sifre_sifirlama_token VARCHAR(255)
);

-- Öğrenciler
CREATE TABLE ogrenciler (
    id SERIAL PRIMARY KEY,
    ogrenci_no VARCHAR(20) UNIQUE,
    ad VARCHAR(50),
    soyad VARCHAR(50),
    email VARCHAR(100),
    telefon VARCHAR(20)
);

-- Dersler
CREATE TABLE dersler (
    id SERIAL PRIMARY KEY,
    ders_kodu VARCHAR(20) UNIQUE,
    ders_adi VARCHAR(100),
    kontenjan INT,
    ogretmen_adi VARCHAR(50),
    kapasite INT,
    kredi INT,
    gun VARCHAR(20),
    saat VARCHAR(20),
    sinif VARCHAR(20),
    ogretmen_id INT,
    min_ogrenci_sayisi INT,
    durum VARCHAR(20)
);

-- Kayıtlar
CREATE TABLE kayitlar (
    id SERIAL PRIMARY KEY,
    ogrenci_id INT REFERENCES ogrenciler(id) ON DELETE CASCADE,
    ders_id INT REFERENCES dersler(id) ON DELETE CASCADE,
    donem VARCHAR(20),
    durum VARCHAR(20),
    danisman_ogretmen_id INT
);

-- Notlar
CREATE TABLE notlar (
    id SERIAL PRIMARY KEY,
    ogrenci_id INT REFERENCES ogrenciler(id) ON DELETE CASCADE,
    ders_id INT REFERENCES dersler(id) ON DELETE CASCADE,
    "not" INT,
    ortalama INT
); 