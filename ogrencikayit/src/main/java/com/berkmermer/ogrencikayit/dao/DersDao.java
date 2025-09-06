package com.berkmermer.ogrencikayit.dao;

import com.berkmermer.ogrencikayit.model.Ders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class DersDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Ders> rowMapper = new RowMapper<Ders>() {
        @Override
        public Ders mapRow(ResultSet rs, int rowNum) throws SQLException {
            Ders d = new Ders();
            d.setId(rs.getLong("id"));
            d.setDersKodu(rs.getString("ders_kodu"));
            d.setDersAdi(rs.getString("ders_adi"));
            d.setKontenjan(rs.getInt("kontenjan"));
            d.setOgretmenAdi(rs.getString("ogretmen_adi"));
            d.setKapasite(rs.getInt("kapasite"));
            d.setKredi(rs.getInt("kredi"));
            d.setGun(rs.getString("gun"));
            d.setSaat(rs.getString("saat"));
            d.setSinif(rs.getString("sinif"));
            d.setOgretmenId(rs.getLong("ogretmen_id"));
            d.setOnKosulDersId(rs.getLong("on_kosul_ders_id"));
            d.setMinOgrenciSayisi(rs.getInt("min_ogrenci_sayisi"));
            d.setDurum(rs.getString("durum"));
            return d;
        }
    };

    public List<Ders> findAll() {
        return jdbcTemplate.query("SELECT * FROM dersler", rowMapper);
    }

    public Ders findById(Long id) {
        try {
        return jdbcTemplate.queryForObject("SELECT * FROM dersler WHERE id = ?", rowMapper, id);
        } catch (Exception e) {
            return null;
        }
    }

    public void save(Ders ders) {
        jdbcTemplate.update("INSERT INTO dersler (ders_kodu, ders_adi, kontenjan, ogretmen_adi, kapasite, kredi, gun, saat, sinif, ogretmen_id, on_kosul_ders_id, min_ogrenci_sayisi, durum) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", 
            ders.getDersKodu(), ders.getDersAdi(), ders.getKontenjan(), ders.getOgretmenAdi(), ders.getKapasite(), ders.getKredi(), ders.getGun(), ders.getSaat(), ders.getSinif(), ders.getOgretmenId(), ders.getOnKosulDersId(), ders.getMinOgrenciSayisi(), ders.getDurum());
    }

    public void update(Ders ders) {
        jdbcTemplate.update("UPDATE dersler SET ders_kodu = ?, ders_adi = ?, kontenjan = ?, ogretmen_adi = ?, kapasite = ?, kredi = ?, gun = ?, saat = ?, sinif = ?, ogretmen_id = ?, on_kosul_ders_id = ?, min_ogrenci_sayisi = ?, durum = ? WHERE id = ?", 
            ders.getDersKodu(), ders.getDersAdi(), ders.getKontenjan(), ders.getOgretmenAdi(), ders.getKapasite(), ders.getKredi(), ders.getGun(), ders.getSaat(), ders.getSinif(), ders.getOgretmenId(), ders.getOnKosulDersId(), ders.getMinOgrenciSayisi(), ders.getDurum(), ders.getId());
    }

    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM kayitlar WHERE ders_id = ?", id);
        jdbcTemplate.update("DELETE FROM notlar WHERE ders_id = ?", id);
        jdbcTemplate.update("DELETE FROM dersler WHERE id = ?", id);
    }

    public void guncelleKontenjan(Long dersId, int degisim) {
        jdbcTemplate.update("UPDATE dersler SET kontenjan = kontenjan + ? WHERE id = ?", degisim, dersId);
    }

    public List<Ders> findAktifDersler() {
        return jdbcTemplate.query("SELECT * FROM dersler WHERE durum = 'ACIK'", rowMapper);
    }
} 