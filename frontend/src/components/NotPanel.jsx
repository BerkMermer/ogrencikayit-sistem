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

const initialForm = { ogrenciId: '', dersId: '', not: '' };

export default function NotPanel() {
  const [notlar, setNotlar] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [open, setOpen] = useState(false);
  const [form, setForm] = useState(initialForm);
  const [editId, setEditId] = useState(null);
  const [formError, setFormError] = useState('');

  const fetchNotlar = () => {
    setLoading(true);
    axios.get('/api/notlar')
      .then(res => {
        setNotlar(res.data.data || []);
        setLoading(false);
      })
      .catch(err => {
        setError('Notlar yüklenemedi');
        setLoading(false);
      });
  };

  useEffect(() => {
    fetchNotlar();
  }, []);

  const handleOpen = (not = null) => {
    if (not) {
      setForm({ ogrenciId: not.ogrenciId, dersId: not.dersId, not: not.not });
      setEditId(not.id);
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
    if (!form.ogrenciId || !form.dersId || form.not === '') {
      setFormError('Tüm alanlar zorunlu!');
      return;
    }
    try {
      if (editId) {
        // Güncelleme
        await axios.put(`/api/notlar/${editId}`, form);
      } else {
        // Ekleme
        await axios.post('/api/notlar', form);
      }
      handleClose();
      fetchNotlar();
    } catch (err) {
      setFormError(err.response?.data?.message || 'İşlem başarısız!');
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Not silinsin mi?')) return;
    try {
      await axios.delete(`/api/notlar/${id}`);
      fetchNotlar();
    } catch (err) {
      alert('Silme işlemi başarısız!');
    }
  };

  return (
    <Container maxWidth="md" sx={{ mt: 8 }}>
      <Typography variant="h4" gutterBottom>Not Paneli</Typography>
      <Button variant="contained" startIcon={<AddIcon />} sx={{ mb: 2 }} onClick={() => handleOpen()}>Yeni Not Ekle</Button>
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
                <TableCell>Öğrenci ID</TableCell>
                <TableCell>Ders ID</TableCell>
                <TableCell>Not</TableCell>
                <TableCell>İşlemler</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {notlar.map((not) => (
                <TableRow key={not.id}>
                  <TableCell>{not.id}</TableCell>
                  <TableCell>{not.ogrenciId}</TableCell>
                  <TableCell>{not.dersId}</TableCell>
                  <TableCell>{not.not}</TableCell>
                  <TableCell>
                    <IconButton color="primary" onClick={() => handleOpen(not)}><EditIcon /></IconButton>
                    <IconButton color="error" onClick={() => handleDelete(not.id)}><DeleteIcon /></IconButton>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      )}
      <Dialog open={open} onClose={handleClose}>
        <DialogTitle>{editId ? 'Notu Düzenle' : 'Yeni Not Ekle'}</DialogTitle>
        <DialogContent sx={{ minWidth: 350 }}>
          <TextField
            margin="normal"
            fullWidth
            label="Öğrenci ID"
            name="ogrenciId"
            value={form.ogrenciId}
            onChange={handleChange}
            required
          />
          <TextField
            margin="normal"
            fullWidth
            label="Ders ID"
            name="dersId"
            value={form.dersId}
            onChange={handleChange}
            required
          />
          <TextField
            margin="normal"
            fullWidth
            label="Not"
            name="not"
            value={form.not}
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
  );
} 