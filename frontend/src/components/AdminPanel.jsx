import React, { useEffect, useState } from 'react';
import { Box, Grid, Card, CardContent, Typography, Container, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Button, IconButton, Dialog, DialogTitle, DialogContent, DialogActions, TextField, MenuItem } from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import AddIcon from '@mui/icons-material/Add';
import axios from '../utils/axios';

const initialForm = { kullaniciAdi: '', email: '', sifre: '', rol: 'OGRENCI', aktif: true, ad: '', soyad: '' };

export default function AdminPanel() {
  // Gerçek veriler için state
  const [stats, setStats] = useState([
    { label: 'Toplam Öğrenci', value: 0, color: '#1976d2' },
    { label: 'Toplam Öğretmen', value: 0, color: '#388e3c' },
    { label: 'Toplam Ders', value: 0, color: '#fbc02d', textColor: '#333' },
  ]);

  // Kullanıcı yönetimi state'leri
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [open, setOpen] = useState(false);
  const [form, setForm] = useState(initialForm);
  const [editId, setEditId] = useState(null);
  const [formError, setFormError] = useState('');

  const fetchUsers = () => {
    setLoading(true);
    axios.get('/api/kullanici?rol=ADMIN')
      .then(res => {
        setUsers(res.data.data || []);
        setLoading(false);
      })
      .catch(err => {
        setError('Kullanıcılar yüklenemedi');
        setLoading(false);
      });
  };

  useEffect(() => {
    fetchUsers();
    fetchStats();
  }, []);

  const fetchStats = () => {
    axios.get('/api/dashboard/stats?rol=ADMIN')
      .then(res => {
        if (res.data.success) {
          const data = res.data.data;
          setStats([
            { label: 'Toplam Öğrenci', value: data.toplamOgrenci || 0, color: '#1976d2' },
            { label: 'Toplam Öğretmen', value: data.toplamOgretmen || 0, color: '#388e3c' },
            { label: 'Toplam Ders', value: data.toplamDers || 0, color: '#fbc02d', textColor: '#333' },
          ]);
        }
      })
      .catch(err => {
        console.error('İstatistikler yüklenemedi:', err);
      });
  };

  const handleOpen = (user = null) => {
    if (user) {
      setForm({
        kullaniciAdi: user.kullaniciAdi,
        email: user.email,
        sifre: '',
        rol: user.rol,
        aktif: user.aktif,
        ad: user.ad || '',
        soyad: user.soyad || ''
      });
      setEditId(user.id);
    } else {
      setForm(initialForm);
      setEditId(null);
    }
    setFormError('');
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
    setFormError('');
  };

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setForm((prev) => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
  };

  const handleSubmit = async () => {
    if (!form.kullaniciAdi || !form.email || !form.ad || !form.soyad || (!editId && !form.sifre)) {
      setFormError('Kullanıcı adı, ad, soyad, e-posta ve (eklemede) şifre zorunlu!');
      return;
    }
    try {
      if (editId) {
        // Güncelleme
        await axios.put(`/api/kullanici/${editId}`, {
          ...form,
          sifre: form.sifre || undefined // Şifre boşsa değiştirme
        });
      } else {
        // Ekleme
        await axios.post('/api/kullanici/register', {
          kullaniciAdi: form.kullaniciAdi,
          sifre: form.sifre,
          email: form.email,
          rol: form.rol,
          aktif: form.aktif,
          ad: form.ad,
          soyad: form.soyad
        });
      }
      handleClose();
      fetchUsers();
    } catch (err) {
      setFormError(err.response?.data?.message || 'İşlem başarısız!');
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Kullanıcı silinsin mi?')) return;
    try {
      await axios.delete(`/api/kullanici/${id}?rol=ADMIN`);
      fetchUsers();
    } catch (err) {
      alert('Silme işlemi başarısız!');
      console.error('Silme hatası:', err.response?.data);
    }
  };

  return (
    <Box sx={{ mt: 4, px: 2 }}>
      <Typography variant="h4" sx={{ mb: 4, textAlign: 'center' }}>Admin Paneli</Typography>
      {/* İstatistik Kartları */}
      <Grid container spacing={3} justifyContent="center" sx={{ mb: 4 }}>
        {stats.map((stat) => (
          <Grid item xs={12} sm={6} md={4} key={stat.label}>
            <Card sx={{ boxShadow: 4, borderRadius: 3, bgcolor: stat.color, color: stat.textColor || 'white' }}>
              <CardContent>
                <Typography variant="h6" gutterBottom>{stat.label}</Typography>
                <Typography variant="h3" fontWeight={700}>{stat.value}</Typography>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
      {/* Kullanıcı Yönetimi */}
      <Container maxWidth="md" sx={{ mt: 2 }}>
        <Typography variant="h6" gutterBottom>Kullanıcılar</Typography>
        <Button variant="contained" startIcon={<AddIcon />} sx={{ mb: 2 }} onClick={() => handleOpen()}>Yeni Kullanıcı Ekle</Button>
        {loading ? (
          <Typography>Yükleniyor...</Typography>
        ) : error ? (
          <Typography color="error">{error}</Typography>
        ) : (
          <TableContainer component={Paper}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>ID</TableCell>
                  <TableCell>Ad</TableCell>
                  <TableCell>Soyad</TableCell>
                  <TableCell>Kullanıcı Adı</TableCell>
                  <TableCell>Email</TableCell>
                  <TableCell>Rol</TableCell>
                  <TableCell>Aktif</TableCell>
                  <TableCell>İşlemler</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {users.map((user) => (
                  <TableRow key={user.id}>
                    <TableCell>{user.id}</TableCell>
                    <TableCell>{user.ad || '-'}</TableCell>
                    <TableCell>{user.soyad || '-'}</TableCell>
                    <TableCell>{user.kullaniciAdi}</TableCell>
                    <TableCell>{user.email}</TableCell>
                    <TableCell>{user.rol}</TableCell>
                    <TableCell>{user.aktif ? 'Evet' : 'Hayır'}</TableCell>
                    <TableCell>
                      <IconButton color="primary" onClick={() => handleOpen(user)}><EditIcon /></IconButton>
                      <IconButton color="error" onClick={() => handleDelete(user.id)}><DeleteIcon /></IconButton>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        )}
        <Dialog open={open} onClose={handleClose}>
          <DialogTitle>{editId ? 'Kullanıcıyı Düzenle' : 'Yeni Kullanıcı Ekle'}</DialogTitle>
          <DialogContent sx={{ minWidth: 350 }}>
            <TextField
              margin="normal"
              fullWidth
              label="Kullanıcı Adı"
              name="kullaniciAdi"
              value={form.kullaniciAdi}
              onChange={handleChange}
              required
            />
            <TextField
              margin="normal"
              fullWidth
              label="Ad"
              name="ad"
              value={form.ad}
              onChange={handleChange}
              required
            />
            <TextField
              margin="normal"
              fullWidth
              label="Soyad"
              name="soyad"
              value={form.soyad}
              onChange={handleChange}
              required
            />
            <TextField
              margin="normal"
              fullWidth
              label="E-posta"
              name="email"
              value={form.email}
              onChange={handleChange}
              required
            />
            <TextField
              margin="normal"
              fullWidth
              label="Şifre"
              name="sifre"
              type="password"
              value={form.sifre}
              onChange={handleChange}
              required={!editId}
              helperText={editId ? 'Boş bırakılırsa şifre değişmez' : ''}
            />
            <TextField
              margin="normal"
              fullWidth
              select
              label="Rol"
              name="rol"
              value={form.rol}
              onChange={handleChange}
            >
              <MenuItem value="OGRENCI">Öğrenci</MenuItem>
              <MenuItem value="OGRETMEN">Öğretmen</MenuItem>
              <MenuItem value="ADMIN">Admin</MenuItem>
            </TextField>
            <TextField
              margin="normal"
              fullWidth
              select
              label="Aktif mi?"
              name="aktif"
              value={form.aktif ? 'true' : 'false'}
              onChange={e => setForm(f => ({ ...f, aktif: e.target.value === 'true' }))}
            >
              <MenuItem value="true">Evet</MenuItem>
              <MenuItem value="false">Hayır</MenuItem>
            </TextField>
            {formError && <Typography color="error" sx={{ mt: 1 }}>{formError}</Typography>}
          </DialogContent>
          <DialogActions>
            <Button onClick={handleClose}>İptal</Button>
            <Button onClick={handleSubmit} variant="contained">Kaydet</Button>
          </DialogActions>
        </Dialog>
      </Container>
    </Box>
  );
} 