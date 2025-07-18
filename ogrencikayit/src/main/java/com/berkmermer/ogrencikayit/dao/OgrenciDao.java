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
            return o;
        }
    };

    public List<Ogrenci> findAll() {
        return jdbcTemplate.query("SELECT * FROM ogrenci", rowMapper);
    }

    public Ogrenci findById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM ogrenci WHERE id = ?", rowMapper, id);
    }

    public void save(Ogrenci ogrenci) {
        jdbcTemplate.update("INSERT INTO ogrenci (ad, soyad) VALUES (?, ?)", ogrenci.getAd(), ogrenci.getSoyad());
    }

    public void update(Ogrenci ogrenci) {
        jdbcTemplate.update("UPDATE ogrenci SET ad = ?, soyad = ? WHERE id = ?", ogrenci.getAd(), ogrenci.getSoyad(), ogrenci.getId());
    }

    public void updateAd(Long id, String yeniAd) {
        jdbcTemplate.update("UPDATE ogrenci SET ad = ? WHERE id = ?", yeniAd, id);
    }

    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM ogrenci WHERE id = ?", id);
    }
} 