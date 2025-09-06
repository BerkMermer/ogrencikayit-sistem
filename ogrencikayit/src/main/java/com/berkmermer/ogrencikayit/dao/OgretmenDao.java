package com.berkmermer.ogrencikayit.dao;

import com.berkmermer.ogrencikayit.model.Ogretmen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OgretmenDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int ogretmenEkle(Ogretmen ogretmen) {
        String sql = "INSERT INTO ogretmen (ad, soyad, branş) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, ogretmen.getAd(), ogretmen.getSoyad(), ogretmen.getBranş());
    }

    public List<Ogretmen> tumOgretmenler() {
        String sql = "SELECT * FROM ogretmen";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Ogretmen o = new Ogretmen();
            o.setId(rs.getLong("id"));
            o.setAd(rs.getString("ad"));
            o.setSoyad(rs.getString("soyad"));
            o.setBranş(rs.getString("branş"));
            return o;
        });
    }
} 