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
            d.setDersAdi(rs.getString("dersadi"));
            return d;
        }
    };

    public List<Ders> findAll() {
        return jdbcTemplate.query("SELECT * FROM ders", rowMapper);
    }

    public Ders findById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM ders WHERE id = ?", rowMapper, id);
    }

    public void save(Ders ders) {
        jdbcTemplate.update("INSERT INTO ders (dersadi) VALUES (?)", ders.getDersAdi());
    }

    public void update(Ders ders) {
        jdbcTemplate.update("UPDATE ders SET dersadi = ? WHERE id = ?", ders.getDersAdi(), ders.getId());
    }

    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM ders WHERE id = ?", id);
    }
} 