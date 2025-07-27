package com.berkmermer.ogrencikayit.dao;

import com.berkmermer.ogrencikayit.model.Ogrenci;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class OgrenciDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Ogrenci> rowMapper = new RowMapper<Ogrenci>() {
        @Override
        public Ogrenci mapRow(ResultSet rs, int rowNum) throws SQLException {
            Ogrenci o = new Ogrenci();
            o.setId(rs.getLong("id"));
            o.setAd(rs.getString("ad"));
            o.setSoyad(rs.getString("soyad"));
            o.setOgrenciNo(rs.getString("ogrenci_no"));
            o.setEmail(rs.getString("email"));
            o.setTelefon(rs.getString("telefon"));
            return o;
        }
    };

    public List<Ogrenci> findAll() {
        return jdbcTemplate.query("SELECT * FROM ogrenciler", rowMapper);
    }

    public Ogrenci findById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM ogrenciler WHERE id = ?", rowMapper, id);
    }

    public void save(Ogrenci ogrenci) {
        jdbcTemplate.update("INSERT INTO ogrenciler (ad, soyad, ogrenci_no, email, telefon) VALUES (?, ?, ?, ?, ?)", 
            ogrenci.getAd(), ogrenci.getSoyad(), ogrenci.getOgrenciNo(), ogrenci.getEmail(), ogrenci.getTelefon());
    }

    public void update(Ogrenci ogrenci) {
        jdbcTemplate.update("UPDATE ogrenciler SET ad = ?, soyad = ?, ogrenci_no = ?, email = ?, telefon = ? WHERE id = ?", 
            ogrenci.getAd(), ogrenci.getSoyad(), ogrenci.getOgrenciNo(), ogrenci.getEmail(), ogrenci.getTelefon(), ogrenci.getId());
    }

    public void updateAd(Long id, String yeniAd) {
        jdbcTemplate.update("UPDATE ogrenciler SET ad = ? WHERE id = ?", yeniAd, id);
    }

    public void delete(Long id) {
        // Önce kayıtları sil (eğer kayıt tablosu varsa)
        try {
            jdbcTemplate.update("DELETE FROM kayitlar WHERE ogrenci_id = ?", id);
        } catch (Exception e) {
            // Kayıt tablosu yoksa devam et
        }
        // Sonra notları sil
        try {
            jdbcTemplate.update("DELETE FROM notlar WHERE ogrenci_id = ?", id);
        } catch (Exception e) {
            // Not tablosu yoksa devam et
        }
        // En son öğrenciyi sil
        jdbcTemplate.update("DELETE FROM ogrenciler WHERE id = ?", id);
    }
} 