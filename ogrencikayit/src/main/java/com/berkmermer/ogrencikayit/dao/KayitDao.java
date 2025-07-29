package com.berkmermer.ogrencikayit.dao;

import com.berkmermer.ogrencikayit.model.Ders;
import com.berkmermer.ogrencikayit.model.Kayit;
import com.berkmermer.ogrencikayit.model.Ogrenci;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class KayitDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void kayitEkle(Long ogrenciId, Long dersId, String donem, String onayDurumu, Long danismanOgretmenId) {
        jdbcTemplate.update("INSERT INTO kayitlar (ogrenci_id, ders_id, donem, durum, danisman_ogretmen_id) VALUES (?, ?, ?, ?, ?)", ogrenciId, dersId, donem, onayDurumu, danismanOgretmenId);
    }

    public void kayitOnayla(Long kayitId) {
        jdbcTemplate.update("UPDATE kayitlar SET durum = 'ONAYLANDI' WHERE id = ?", kayitId);
    }

    public void kayitReddet(Long kayitId) {
        jdbcTemplate.update("UPDATE kayitlar SET durum = 'REDDEDILDI' WHERE id = ?", kayitId);
    }

    public List<Kayit> ogrencininOnayBekleyenKayitlari(Long ogrenciId) {
        return jdbcTemplate.query("SELECT * FROM kayitlar WHERE ogrenci_id = ? AND durum = 'BEKLIYOR'", (rs, rowNum) -> {
            Kayit k = new Kayit();
            k.setId(rs.getLong("id"));
            k.setOgrenciId(rs.getLong("ogrenci_id"));
            k.setDersId(rs.getLong("ders_id"));
            k.setDonem(rs.getString("donem"));
            k.setOnayDurumu(rs.getString("durum"));
            k.setDanismanOgretmenId(rs.getLong("danisman_ogretmen_id"));
            return k;
        }, ogrenciId);
    }

    public List<Kayit> tumKayitlar() {
        return jdbcTemplate.query("SELECT * FROM kayitlar", (rs, rowNum) -> {
            Kayit k = new Kayit();
            k.setId(rs.getLong("id"));
            k.setOgrenciId(rs.getLong("ogrenci_id"));
            k.setDersId(rs.getLong("ders_id"));
            k.setDonem(rs.getString("donem"));
            k.setOnayDurumu(rs.getString("durum"));
            k.setDanismanOgretmenId(rs.getLong("danisman_ogretmen_id"));
            return k;
        });
    }

    public void kayitSil(Long id) {
        jdbcTemplate.update("DELETE FROM kayitlar WHERE id = ?", id);
    }

    public boolean ogrenciDerseKayitliMi(Long ogrenciId, Long dersId) {
        try {
        String sql = "SELECT COUNT(*) FROM kayitlar WHERE ogrenci_id = ? AND ders_id = ?";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, ogrenciId, dersId);
            return count != null && count > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public int derseKayitliOgrenciSayisi(Long dersId) {
        try {
        String sql = "SELECT COUNT(*) FROM kayitlar WHERE ders_id = ?";
            Integer result = jdbcTemplate.queryForObject(sql, Integer.class, dersId);
            return result != null ? result : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    public int ogrencininToplamKredisi(Long ogrenciId) {
        try {
        String sql = "SELECT COALESCE(SUM(d.kredi), 0) FROM kayitlar kd " +
                    "JOIN dersler d ON kd.ders_id = d.id " +
                    "WHERE kd.ogrenci_id = ?";
            Integer result = jdbcTemplate.queryForObject(sql, Integer.class, ogrenciId);
            return result != null ? result : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean zamanCakismasiVarMi(Long ogrenciId, String gun, String saat) {
        try {
        String sql = "SELECT COUNT(*) FROM kayitlar kd " +
                    "JOIN dersler d ON kd.ders_id = d.id " +
                    "WHERE kd.ogrenci_id = ? AND d.gun = ? AND d.saat = ?";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, ogrenciId, gun, saat);
            return count != null && count > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public void ogrenciyiDerstenCikar(Long ogrenciId, Long dersId) {
        jdbcTemplate.update("DELETE FROM kayitlar WHERE ogrenci_id = ? AND ders_id = ?", ogrenciId, dersId);
    }

    public List<Ders> ogrencininDersleri(Long ogrenciId) {
        String sql = "SELECT d.* FROM dersler d " +
                    "JOIN kayitlar kd ON d.id = kd.ders_id " +
                    "WHERE kd.ogrenci_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Ders d = new Ders();
            d.setId(rs.getLong("id"));
            d.setDersAdi(rs.getString("ders_adi"));
            d.setKapasite(rs.getInt("kapasite"));
            d.setKredi(rs.getInt("kredi"));
            d.setGun(rs.getString("gun"));
            d.setSaat(rs.getString("saat"));
            d.setSinif(rs.getString("sinif"));
            d.setOgretmenId(rs.getLong("ogretmen_id"));
            return d;
        }, ogrenciId);
    }

    public List<Ogrenci> derseKayitliOgrenciler(Long dersId) {
        String sql = "SELECT o.* FROM ogrenciler o " +
                    "JOIN kayitlar kd ON o.id = kd.ogrenci_id " +
                    "WHERE kd.ders_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Ogrenci o = new Ogrenci();
            o.setId(rs.getLong("id"));
            o.setAd(rs.getString("ad"));
            o.setSoyad(rs.getString("soyad"));
            return o;
        }, dersId);
    }

    public List<Kayit> ogrencininDonemKayitlari(Long ogrenciId, String donem) {
        return jdbcTemplate.query("SELECT * FROM kayitlar WHERE ogrenci_id = ? AND donem = ?", (rs, rowNum) -> {
            Kayit k = new Kayit();
            k.setId(rs.getLong("id"));
            k.setOgrenciId(rs.getLong("ogrenci_id"));
            k.setDersId(rs.getLong("ders_id"));
            k.setDonem(rs.getString("donem"));
            k.setOnayDurumu(rs.getString("durum"));
            k.setDanismanOgretmenId(rs.getLong("danisman_ogretmen_id"));
            return k;
        }, ogrenciId, donem);
    }
} 