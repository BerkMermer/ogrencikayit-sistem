package com.berkmermer.ogrencikayit.dao;

import com.berkmermer.ogrencikayit.model.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class NotDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Not> rowMapper = new RowMapper<Not>() {
        @Override
        public Not mapRow(ResultSet rs, int rowNum) throws SQLException {
            Not n = new Not();
            n.setId(rs.getLong("id"));
            n.setOgrenciId(rs.getLong("ogrenci_id"));
            n.setDersId(rs.getLong("ders_id"));
            n.setVize(rs.getDouble("vize"));
            n.setFinalNotu(rs.getDouble("final_notu"));
            n.setOrtalama(rs.getDouble("ortalama"));
            n.setHarfNotu(rs.getString("harf_notu"));
            n.setGirenOgretmenId(rs.getLong("giren_ogretmen_id"));
            n.setTarih(rs.getString("tarih"));
            return n;
        }
    };

    public void notEkle(Not not) {
        jdbcTemplate.update(
            "INSERT INTO notlar (ogrenci_id, ders_id, vize, final_notu, ortalama, harf_notu, giren_ogretmen_id, tarih) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
            not.getOgrenciId(), not.getDersId(), not.getVize(), not.getFinalNotu(), not.getOrtalama(), not.getHarfNotu(), not.getGirenOgretmenId(), not.getTarih()
        );
    }

    public void notGuncelle(Not not) {
        jdbcTemplate.update(
            "UPDATE notlar SET vize = ?, final_notu = ?, ortalama = ?, harf_notu = ?, giren_ogretmen_id = ?, tarih = ? WHERE id = ?",
            not.getVize(), not.getFinalNotu(), not.getOrtalama(), not.getHarfNotu(), not.getGirenOgretmenId(), not.getTarih(), not.getId()
        );
    }

    public void notSil(Long id) {
        jdbcTemplate.update("DELETE FROM notlar WHERE id = ?", id);
    }

    public List<Not> ogrencininNotlari(Long ogrenciId) {
        return jdbcTemplate.query("SELECT * FROM notlar WHERE ogrenci_id = ?", rowMapper, ogrenciId);
    }

    public List<Not> dersinNotlari(Long dersId) {
        return jdbcTemplate.query("SELECT * FROM notlar WHERE ders_id = ?", rowMapper, dersId);
    }

    public List<Not> tumNotlar() {
        return jdbcTemplate.query("SELECT * FROM notlar", rowMapper);
    }

    public int ogrencininDersTekrarSayisi(Long ogrenciId, Long dersId) {
        String sql = "SELECT COUNT(*) FROM notlar WHERE ogrenci_id = ? AND ders_id = ? AND harf_notu = 'FF'";
        return jdbcTemplate.queryForObject(sql, Integer.class, ogrenciId, dersId);
    }

    public boolean ogrenciOnKosulDersiGectiMi(Long ogrenciId, Long onKosulDersId) {
        String sql = "SELECT COUNT(*) FROM notlar WHERE ogrenci_id = ? AND ders_id = ? AND harf_notu <> 'FF'";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, ogrenciId, onKosulDersId);
        return count > 0;
    }
} 