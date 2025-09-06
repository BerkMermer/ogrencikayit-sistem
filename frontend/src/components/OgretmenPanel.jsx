import React, { useEffect, useState } from 'react';
import { Box, Grid, Card, CardContent, Typography, Container, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Button, IconButton, Dialog, DialogTitle, DialogContent, DialogActions, TextField } from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import AddIcon from '@mui/icons-material/Add';
import axios from '../utils/axios';

const initialForm = { ad: '', kredi: '' };

export default function OgretmenPanel() {
  // Ders yönetimi state'leri
  const [dersler, setDersler] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [open, setOpen] = useState(false);
  const [form, setForm] = useState(initialForm);
  const [editId, setEditId] = useState(null);
  const [formError, setFormError] = useState('');
  const [stats, setStats] = useState([
    { label: 'Toplam Ders', value: 0, color: '#1976d2' },
    { label: 'Toplam Öğrenci', value: 0, color: '#388e3c' },
    { label: 'Toplam Not', value: 0, color: '#fbc02d', textColor: '#333' }
  ]);

  const fetchDersler = () => {
    setLoading(true);
    axios.get('/api/dersler?rol=OGRETMEN')
      .then(res => {
        setDersler(res.data.data || []);
        setLoading(false);
      })
      .catch(err => {
        setError('Dersler yüklenemedi');
        setLoading(false);
      });
  };

  useEffect(() => {
    fetchDersler();
    fetchStats();
  }, []);

  const fetchStats = () => {
    axios.get('/api/dashboard/stats?rol=OGRETMEN')
      .then(res => {
        if (res.data.success) {
          const data = res.data.data;
          setStats([
            { label: 'Toplam Ders', value: data.toplamDers || 0, color: '#1976d2' },
            { label: 'Toplam Öğrenci', value: data.toplamOgrenci || 0, color: '#388e3c' },
            { label: 'Toplam Not', value: 0, color: '#fbc02d', textColor: '#333' },
          ]);
        }
      })
      .catch(err => {
        console.error('İstatistikler yüklenemedi:', err);
      });
  };

  const handleOpen = (ders = null) => {
    if (ders) {
      setForm({
        ad: ders.dersAdi || ders.ad || '',
        kredi: ders.kredi?.toString() || ''
      });
      setEditId(ders.id);
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
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async () => {
    if (!form.ad || !form.kredi) {
      setFormError('Ders adı ve kredi zorunlu!');
      return;
    }
    try {
      const dersPayload = { ...form, dersAdi: form.ad };
      delete dersPayload.ad;
      if (editId) {
        // Güncelleme
        await axios.put(`/api/dersler/${editId}?rol=OGRETMEN`, dersPayload);
      } else {
        // Ekleme
        await axios.post('/api/dersler?rol=OGRETMEN', dersPayload);
      }
      handleClose();
      fetchDersler();
    } catch (err) {
      setFormError(err.response?.data?.message || 'İşlem başarısız!');
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Ders silinsin mi?')) return;
    try {
      await axios.delete(`/api/dersler/${id}?rol=OGRETMEN`);
      fetchDersler();
    } catch (err) {
      alert('Silme işlemi başarısız!');
    }
  };

  return (
    <Box sx={{ mt: 4, px: 2 }}>
      <Typography variant="h4" sx={{ mb: 4, textAlign: 'center' }}>Öğretmen Paneli</Typography>
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
      {/* Ders Yönetimi */}
      <Container maxWidth="md" sx={{ mt: 2 }}>
        <Typography variant="h6" gutterBottom>Dersler</Typography>
        <Button variant="contained" startIcon={<AddIcon />} sx={{ mb: 2 }} onClick={() => handleOpen()}>Yeni Ders Ekle</Button>
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
                  <TableCell>Ders Adı</TableCell>
                  <TableCell>Kredi</TableCell>
                  <TableCell>İşlemler</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {dersler.map((ders) => (
                  <TableRow key={ders.id}>
                    <TableCell>{ders.id}</TableCell>
                    <TableCell>{ders.dersAdi || ders.ad}</TableCell>
                    <TableCell>{ders.kredi}</TableCell>
                    <TableCell>
                      <IconButton color="primary" onClick={() => handleOpen(ders)}><EditIcon /></IconButton>
                      <IconButton color="error" onClick={() => handleDelete(ders.id)}><DeleteIcon /></IconButton>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        )}
        <Dialog open={open} onClose={handleClose}>
          <DialogTitle>{editId ? 'Dersi Düzenle' : 'Yeni Ders Ekle'}</DialogTitle>
          <DialogContent sx={{ minWidth: 350 }}>
            <TextField
              margin="normal"
              fullWidth
              label="Ders Adı"
              name="ad"
              value={form.ad || ''}
              onChange={handleChange}
              required
            />
            <TextField
              margin="normal"
              fullWidth
              label="Kredi"
              name="kredi"
              value={form.kredi || ''}
              onChange={handleChange}
              required
              type="number"
            />
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