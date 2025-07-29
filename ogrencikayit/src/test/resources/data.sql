-- Admin
INSERT INTO kullanicilar (kullanici_adi, sifre, email, rol, aktif) VALUES ('admin', 'admin123', 'admin@mail.com', 'ADMIN', true);
-- Öğretmenler
INSERT INTO kullanicilar (kullanici_adi, sifre, email, rol, aktif) VALUES ('ogretmen1', 'ogretmen123', 'ogretmen1@mail.com', 'OGRETMEN', true);
INSERT INTO kullanicilar (kullanici_adi, sifre, email, rol, aktif) VALUES ('ogretmen2', 'ogretmen123', 'ogretmen2@mail.com', 'OGRETMEN', true);
-- Öğrenciler
INSERT INTO kullanicilar (kullanici_adi, sifre, email, rol, aktif) VALUES ('ogrenci1', 'ogrenci123', 'ogrenci1@mail.com', 'OGRENCI', true);
INSERT INTO kullanicilar (kullanici_adi, sifre, email, rol, aktif) VALUES ('ogrenci2', 'ogrenci123', 'ogrenci2@mail.com', 'OGRENCI', true);
INSERT INTO ogrenciler (ogrenci_no, ad, soyad, email, telefon) VALUES ('2023001', 'Ali', 'Yılmaz', 'ogrenci1@mail.com', '5551111111');
INSERT INTO ogrenciler (ogrenci_no, ad, soyad, email, telefon) VALUES ('2023002', 'Ayşe', 'Demir', 'ogrenci2@mail.com', '5552222222');
-- Dersler
INSERT INTO dersler (ders_kodu, ders_adi, kontenjan, ogretmen_adi, kapasite, kredi, gun, saat, sinif, ogretmen_id, min_ogrenci_sayisi, durum) VALUES ('DRS101', 'Matematik', 30, 'ogretmen1', 30, 5, 'Pazartesi', '09:00', 'A101', 2, 1, 'ACIK');
INSERT INTO dersler (ders_kodu, ders_adi, kontenjan, ogretmen_adi, kapasite, kredi, gun, saat, sinif, ogretmen_id, min_ogrenci_sayisi, durum) VALUES ('DRS102', 'Fizik', 30, 'ogretmen2', 30, 5, 'Salı', '10:00', 'A102', 3, 1, 'ACIK');
-- Kayıtlar
INSERT INTO kayitlar (ogrenci_id, ders_id, donem, durum, danisman_ogretmen_id) VALUES (1, 1, '2024-2025', 'ONAYLANDI', 2);
INSERT INTO kayitlar (ogrenci_id, ders_id, donem, durum, danisman_ogretmen_id) VALUES (2, 2, '2024-2025', 'ONAYLANDI', 3);
-- Notlar
INSERT INTO notlar (ogrenci_id, ders_id, "not", ortalama) VALUES (1, 1, 85, 85);
INSERT INTO notlar (ogrenci_id, ders_id, "not", ortalama) VALUES (2, 2, 90, 90); 