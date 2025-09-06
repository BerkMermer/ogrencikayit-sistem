import * as React from 'react';
import { useEffect, useState } from 'react';
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import axios from '../utils/axios';

export default function OgrenciDashboard() {
  const user = JSON.parse(localStorage.getItem('user') || 'null');
  const [dersler, setDersler] = useState([]);
  const [notlar, setNotlar] = useState([]);
  const [tumDersler, setTumDersler] = useState([]);
  const [profil, setProfil] = useState({ email: user?.email || '', sifre: '' });
  const [profilMsg, setProfilMsg] = useState('');
  const [successMessage, setSuccessMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [stats, setStats] = useState([
    { label: 'Kayıtlı Dersler', value: 0, color: '#1976d2' },
    { label: 'Toplam Ders', value: 0, color: '#388e3c' },
    { label: 'Ortalama Not', value: 0, color: '#fbc02d', textColor: '#333' },
  ]);

  useEffect(() => {
    if (user) {
      Promise.all([
        axios.get(`/api/kayitlar/ogrenci/1?rol=OGRENCI`), // Sabit öğrenci ID'si
        axios.get('/api/dersler?rol=OGRENCI'),
        axios.get(`/api/notlar/ogrenci/1?rol=OGRENCI`) // Sabit öğrenci ID'si
      ]).then(([derslerRes, tumDerslerRes, notlarRes]) => {
        setDersler(derslerRes.data.data || []);
        setTumDersler(tumDerslerRes.data.data || []);
        setNotlar(notlarRes.data.data || []);
        
        // Stats güncelle
        setStats([
          { label: 'Kayıtlı Dersler', value: derslerRes.data.data?.length || 0, color: '#1976d2' },
          { label: 'Toplam Ders', value: tumDerslerRes.data.data?.length || 0, color: '#388e3c' },
          { label: 'Ortalama Not', value: notlarRes.data.data?.length || 0, color: '#fbc02d', textColor: '#333' },
        ]);
      });
    }
  }, [user]);

  // Profil güncelleme
  const handleProfilChange = e => {
    setProfil({ ...profil, [e.target.name]: e.target.value });
  };
  const handleProfilSubmit = async e => {
    e.preventDefault();
    try {
      await axios.put(`/api/kullanici/${user.id}?rol=OGRENCI`, {
        ...user,
        email: profil.email,
        sifre: profil.sifre
      });
      setProfilMsg('Profil başarıyla güncellendi!');
    } catch (err) {
      setProfilMsg('Profil güncellenemedi!');
    }
  };

  // Kayıt olabileceği dersler (henüz kayıtlı olmadığı dersler)
  const kayitliDersIdler = dersler.map(k => k.dersId);
  const kayitOlunabilirDersler = tumDersler.filter(d => !kayitliDersIdler.includes(d.id));

  // Derse kayıt ol
  const handleDerseKayit = async dersId => {
    setSuccessMessage("");
    setErrorMessage("");
    try {
      const response = await axios.post('/api/kayitlar?rol=OGRENCI', {
        ogrenciId: 1, // Sabit öğrenci ID'si (ogrenciler tablosunda ID 1)
        dersId,
        danismanOgretmenId: null
      });
      setSuccessMessage(response.data.message || "Derse kayıt başarılı!");
      // Kayıt başarılıysa tüm verileri tekrar çek
      Promise.all([
        axios.get(`/api/kayitlar/ogrenci/1?rol=OGRENCI`), // Sabit öğrenci ID'si
        axios.get('/api/dersler?rol=OGRENCI'),
        axios.get(`/api/notlar/ogrenci/1?rol=OGRENCI`) // Sabit öğrenci ID'si
      ]).then(([derslerRes, tumDerslerRes, notlarRes]) => {
        setDersler(derslerRes.data.data || []);
        setTumDersler(tumDerslerRes.data.data || []);
        setNotlar(notlarRes.data.data || []);
      });
    } catch (err) {
      setErrorMessage(
        err.response?.data?.message ||
        err.response?.data?.error ||
        "Derse kayıt başarısız!"
      );
    }
  };

  return (
    <Container maxWidth="md" sx={{ mt: 8 }}>
      <Typography variant="h4" gutterBottom>Öğrenci Paneli</Typography>
      {user && (
        <>
          <Typography variant="h6">Hoş geldin, {user.kullaniciAdi}!</Typography>
          <Typography>E-posta: {user.email}</Typography>
          <Typography>Rol: {user.rol}</Typography>
        </>
      )}
      
      {/* Stats Kartları */}
      <Box sx={{ mt: 4, mb: 4 }}>
        <Grid container spacing={3}>
          {stats.map((stat) => (
            <Grid item xs={12} sm={4} key={stat.label}>
              <Card sx={{ 
                bgcolor: stat.color, 
                color: stat.textColor || 'white',
                textAlign: 'center'
              }}>
                <CardContent>
                  <Typography variant="h4" component="div">
                    {stat.value}
                  </Typography>
                  <Typography variant="body2">
                    {stat.label}
                  </Typography>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      </Box>
      <Box sx={{ mt: 4 }}>
        <Typography variant="h6">Kayıtlı Dersler ve Notlar</Typography>
        <TableContainer component={Paper} sx={{ mb: 4 }}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Ders Adı</TableCell>
                <TableCell>Not</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {dersler.map((k, i) => {
                const ders = tumDersler.find(d => d.id === k.dersId);
                const notObj = notlar.find(n => n.dersId === k.dersId);
                return (
                  <TableRow key={i}>
                    <TableCell>{ders?.dersAdi || '-'}</TableCell>
                    <TableCell>{notObj ? (notObj.not || notObj.ortalama) : '-'}</TableCell>
                  </TableRow>
                );
              })}
            </TableBody>
          </Table>
        </TableContainer>
        <Typography variant="h6">Kayıt Olabileceğin Dersler</Typography>
        {successMessage && <Typography color="success.main" sx={{ mt: 1 }}>{successMessage}</Typography>}
        {errorMessage && <Typography color="error.main" sx={{ mt: 1 }}>{errorMessage}</Typography>}
        <TableContainer component={Paper} sx={{ mb: 4 }}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Ders Adı</TableCell>
                <TableCell></TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {kayitOlunabilirDersler.map((d) => (
                <TableRow key={d.id}>
                  <TableCell>{d.dersAdi || d.ad}</TableCell>
                  <TableCell>
                    <Button variant="contained" size="small" onClick={() => handleDerseKayit(d.id)}>
                      Kayıt Ol
                    </Button>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
        <Typography variant="h6">Profilini Güncelle</Typography>
        <Box component="form" onSubmit={handleProfilSubmit} sx={{ mb: 4 }}>
          <TextField
            margin="normal"
            fullWidth
            label="E-posta"
            name="email"
            value={profil.email}
            onChange={handleProfilChange}
            required
          />
          <TextField
            margin="normal"
            fullWidth
            label="Yeni Şifre (değiştirmek istemiyorsan boş bırak)"
            name="sifre"
            type="password"
            value={profil.sifre}
            onChange={handleProfilChange}
          />
          <Button type="submit" variant="contained" sx={{ mt: 2 }}>Güncelle</Button>
          {profilMsg && <Typography color="success.main" sx={{ mt: 1 }}>{profilMsg}</Typography>}
        </Box>
      </Box>
    </Container>
  );
} 