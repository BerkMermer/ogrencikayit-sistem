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
import IconButton from '@mui/material/IconButton';
import Dialog from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import DialogContent from '@mui/material/DialogContent';
import DialogActions from '@mui/material/DialogActions';
import TextField from '@mui/material/TextField';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import AddIcon from '@mui/icons-material/Add';
import axios from 'axios';
import { Box, Grid, Card, CardContent } from '@mui/material';

const initialForm = { ogrenciNo: '', ad: '', soyad: '', email: '', telefon: '' };

export default function OgrenciPanel() {
  const [ogrenciler, setOgrenciler] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [open, setOpen] = useState(false);
  const [form, setForm] = useState(initialForm);
  const [editId, setEditId] = useState(null);
  const [formError, setFormError] = useState('');

  const fetchOgrenciler = () => {
    setLoading(true);
    axios.get('/api/ogrenciler?rol=ADMIN')
      .then(res => {
        setOgrenciler(res.data.data || []);
        setLoading(false);
      })
      .catch(err => {
        setError('Öğrenciler yüklenemedi');
        setLoading(false);
      });
  };

  useEffect(() => {
    fetchOgrenciler();
  }, []);

  const handleOpen = (ogrenci = null) => {
    if (ogrenci) {
      setForm({
        ogrenciNo: ogrenci.ogrenciNo,
        ad: ogrenci.ad,
        soyad: ogrenci.soyad,
        email: ogrenci.email,
        telefon: ogrenci.telefon
      });
      setEditId(ogrenci.id);
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
    if (!form.ogrenciNo || !form.ad || !form.soyad || !form.email) {
      setFormError('Tüm alanlar zorunlu!');
      return;
    }
    try {
      if (editId) {
        // Güncelleme
        await axios.put(`/api/ogrenciler/${editId}?rol=ADMIN`, form);
      } else {
        // Ekleme
        await axios.post('/api/ogrenciler?rol=ADMIN', form);
      }
      handleClose();
      fetchOgrenciler();
    } catch (err) {
      setFormError(err.response?.data?.message || 'İşlem başarısız!');
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Öğrenci silinsin mi?')) return;
    try {
      await axios.delete(`/api/ogrenciler/${id}?rol=ADMIN`);
      fetchOgrenciler();
    } catch (err) {
      alert('Silme işlemi başarısız!');
    }
  };

  // Dummy veriler, gerçek API'den alınabilir
  const stats = [
    { label: 'Toplam Öğrenci', value: ogrenciler.length, color: '#1976d2' },
    { label: 'Toplam Not', value: 12, color: '#388e3c' },
    { label: 'Ortalama Not', value: 78, color: '#fbc02d', textColor: '#333' },
  ];

  return (
    <Box sx={{ mt: 4, px: 2 }}>
      <Typography variant="h4" sx={{ mb: 4, textAlign: 'center' }}>Öğrenci Paneli</Typography>
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
      {/* Mevcut panel içeriği */}
      <Container maxWidth="md" sx={{ mt: 2 }}>
        <Button variant="contained" startIcon={<AddIcon />} sx={{ mb: 2 }} onClick={() => handleOpen()}>Yeni Öğrenci Ekle</Button>
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
                  <TableCell>Öğrenci No</TableCell>
                  <TableCell>Ad</TableCell>
                  <TableCell>Soyad</TableCell>
                  <TableCell>Email</TableCell>
                  <TableCell>Telefon</TableCell>
                  <TableCell>İşlemler</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {ogrenciler.map((ogrenci) => (
                  <TableRow key={ogrenci.id}>
                    <TableCell>{ogrenci.id}</TableCell>
                    <TableCell>{ogrenci.ogrenciNo}</TableCell>
                    <TableCell>{ogrenci.ad}</TableCell>
                    <TableCell>{ogrenci.soyad}</TableCell>
                    <TableCell>{ogrenci.email}</TableCell>
                    <TableCell>{ogrenci.telefon}</TableCell>
                    <TableCell>
                      <IconButton color="primary" onClick={() => handleOpen(ogrenci)}><EditIcon /></IconButton>
                      <IconButton color="error" onClick={() => handleDelete(ogrenci.id)}><DeleteIcon /></IconButton>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        )}
        <Dialog open={open} onClose={handleClose}>
          <DialogTitle>{editId ? 'Öğrenciyi Düzenle' : 'Yeni Öğrenci Ekle'}</DialogTitle>
          <DialogContent sx={{ minWidth: 350 }}>
            <TextField
              margin="normal"
              fullWidth
              label="Öğrenci No"
              name="ogrenciNo"
              value={form.ogrenciNo}
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
              label="Telefon"
              name="telefon"
              value={form.telefon}
              onChange={handleChange}
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