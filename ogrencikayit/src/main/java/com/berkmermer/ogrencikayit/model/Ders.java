package com.berkmermer.ogrencikayit.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "ders")
public class Ders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "ders_adi", nullable = false, unique = true)
    private String dersAdi;
    
    @ManyToMany(mappedBy = "dersler", fetch = FetchType.LAZY)
    private List<Ogrenci> ogrenciler;

    // Constructors
    public Ders() {}
    
    public Ders(String dersAdi) {
        this.dersAdi = dersAdi;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDersAdi() { return dersAdi; }
    public void setDersAdi(String dersAdi) { this.dersAdi = dersAdi; }
    public List<Ogrenci> getOgrenciler() { return ogrenciler; }
    public void setOgrenciler(List<Ogrenci> ogrenciler) { this.ogrenciler = ogrenciler; }
}
