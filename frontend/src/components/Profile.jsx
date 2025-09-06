import React, { useState } from 'react';
import { Card, CardContent, Typography, Box, Button, TextField, Snackbar, Alert } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

export default function Profile() {
  const user = React.useMemo(() => {
    const u = localStorage.getItem('user');
    return u ? JSON.parse(u) : null;
  }, []);
  const navigate = useNavigate();
  const [editMode, setEditMode] = useState(false);
  const [form, setForm] = useState({
    kullaniciAdi: user?.kullaniciAdi || '',
    email: user?.email || '',
    sifre: '',
  });
  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'info' });
  const [loading, setLoading] = useState(false);

  if (!user) return null;

  const handleGoPanel = () => {
    if (user.rol === 'ADMIN') navigate('/admin');
    else if (user.rol === 'OGRETMEN') navigate('/ogretmen');
    else if (user.rol === 'OGRENCI') navigate('/ogrenci-dashboard');
    else navigate('/');
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleUpdate = async () => {
    setLoading(true);
    try {
      await axios.put(`/api/kullanici/${user.id}`, {
        kullaniciAdi: form.kullaniciAdi,
        email: form.email,
        sifre: form.sifre || undefined,
      });
      setSnackbar({ open: true, message: 'Profil başarıyla güncellendi.', severity: 'success' });
      setEditMode(false);
    } catch (err) {
      setSnackbar({ open: true, message: err.response?.data?.message || 'Güncelleme başarısız!', severity: 'error' });
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: { xs: '50vh', sm: '60vh' }, px: 1 }}>
      <Card sx={{ minWidth: 280, maxWidth: 400, width: '100%', boxShadow: 4, borderRadius: 3 }}>
        <CardContent>
          <Typography variant="h5" gutterBottom>Profil Bilgileri</Typography>
          {editMode ? (
            <>
              <TextField
                label="Kullanıcı Adı"
                name="kullaniciAdi"
                value={form.kullaniciAdi}
                onChange={handleChange}
                fullWidth
                margin="normal"
              />
              <TextField
                label="E-posta"
                name="email"
                value={form.email}
                onChange={handleChange}
                fullWidth
                margin="normal"
              />
              <TextField
                label="Yeni Şifre"
                name="sifre"
                type="password"
                value={form.sifre}
                onChange={handleChange}
                fullWidth
                margin="normal"
                helperText="Boş bırakılırsa şifre değişmez."
              />
              <Button variant="contained" fullWidth sx={{ mt: 2 }} onClick={handleUpdate} disabled={loading}>
                Kaydet
              </Button>
              <Button fullWidth sx={{ mt: 1 }} onClick={() => setEditMode(false)} disabled={loading}>
                İptal
              </Button>
            </>
          ) : (
            <>
              <Typography variant="body1"><b>Kullanıcı Adı:</b> {user.kullaniciAdi}</Typography>
              <Typography variant="body1"><b>Rol:</b> {user.rol}</Typography>
              {user.email && <Typography variant="body1"><b>E-posta:</b> {user.email}</Typography>}
              <Button variant="contained" fullWidth sx={{ mt: 3 }} onClick={() => setEditMode(true)}>
                Bilgilerimi Güncelle
              </Button>
              <Button fullWidth sx={{ mt: 1 }} onClick={handleGoPanel}>
                Panele Dön
              </Button>
            </>
          )}
        </CardContent>
      </Card>
      <Snackbar open={snackbar.open} autoHideDuration={3000} onClose={() => setSnackbar({ ...snackbar, open: false })} anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}>
        <Alert onClose={() => setSnackbar({ ...snackbar, open: false })} severity={snackbar.severity} sx={{ width: '100%' }}>
          {snackbar.message}
        </Alert>
      </Snackbar>
    </Box>
  );
} 