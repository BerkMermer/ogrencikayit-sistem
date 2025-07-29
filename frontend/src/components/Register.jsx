import * as React from 'react';
import { useState } from 'react';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import Alert from '@mui/material/Alert';
import axios from '../utils/axios';
import { useNavigate } from 'react-router-dom';

export default function Register() {
  const [form, setForm] = useState({
    name: '',
    surname: '',
    username: '',
    email: '',
    password: '',
    password2: ''
  });
  const [errors, setErrors] = useState({});
  const [apiError, setApiError] = useState('');
  const [apiSuccess, setApiSuccess] = useState('');
  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const validate = () => {
    let temp = {};
    temp.name = form.name ? '' : 'Ad zorunlu';
    temp.surname = form.surname ? '' : 'Soyad zorunlu';
    temp.username = form.username ? '' : 'Kullanıcı adı zorunlu';
    temp.email = /.+@.+\..+/.test(form.email) ? '' : 'Geçerli e-posta girin';
    temp.password = form.password.length >= 6 ? '' : 'Şifre en az 6 karakter olmalı';
    temp.password2 = form.password2 === form.password ? '' : 'Şifreler eşleşmiyor';
    setErrors(temp);
    return Object.values(temp).every(x => x === '');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setApiError('');
    setApiSuccess('');
    if (validate()) {
      try {
        const payload = {
          ad: form.name,
          soyad: form.surname,
          kullaniciAdi: form.username,
          email: form.email,
          sifre: form.password,
          rol: "OGRENCI"
        };
        await axios.post('/api/kullanici/register', payload);
        setApiSuccess('Kayıt başarılı! Giriş sayfasına yönlendiriliyorsunuz...');
        setTimeout(() => navigate('/login'), 1500);
      } catch (err) {
        setApiError(err.response?.data?.message || 'Kayıt başarısız. Lütfen bilgilerinizi kontrol edin.');
      }
    }
  };

  return (
    <Container component="main" maxWidth="xs">
      <Box
        sx={{
          marginTop: 8,
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
        }}
      >
        <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
          <LockOutlinedIcon />
        </Avatar>
        <Typography component="h1" variant="h5">
          Kayıt Ol
        </Typography>
        {apiError && <Alert severity="error" sx={{ width: '100%', mt: 2 }}>{apiError}</Alert>}
        {apiSuccess && <Alert severity="success" sx={{ width: '100%', mt: 2 }}>{apiSuccess}</Alert>}
        <Box component="form" onSubmit={handleSubmit} sx={{ mt: 1 }}>
          <TextField
            margin="normal"
            required
            fullWidth
            id="name"
            label="Ad"
            name="name"
            autoFocus
            value={form.name}
            onChange={handleChange}
            error={!!errors.name}
            helperText={errors.name}
          />
          <TextField
            margin="normal"
            required
            fullWidth
            id="surname"
            label="Soyad"
            name="surname"
            value={form.surname}
            onChange={handleChange}
            error={!!errors.surname}
            helperText={errors.surname}
          />
          <TextField
            margin="normal"
            required
            fullWidth
            id="username"
            label="Kullanıcı Adı"
            name="username"
            value={form.username}
            onChange={handleChange}
            error={!!errors.username}
            helperText={errors.username}
          />
          <TextField
            margin="normal"
            required
            fullWidth
            id="email"
            label="E-posta"
            name="email"
            value={form.email}
            onChange={handleChange}
            error={!!errors.email}
            helperText={errors.email}
          />
          <TextField
            margin="normal"
            required
            fullWidth
            name="password"
            label="Şifre"
            type="password"
            id="password"
            value={form.password}
            onChange={handleChange}
            error={!!errors.password}
            helperText={errors.password}
          />
          <TextField
            margin="normal"
            required
            fullWidth
            name="password2"
            label="Şifre (Tekrar)"
            type="password"
            id="password2"
            value={form.password2}
            onChange={handleChange}
            error={!!errors.password2}
            helperText={errors.password2}
          />
          <Button
            type="submit"
            fullWidth
            variant="contained"
            sx={{ mt: 3, mb: 2 }}
          >
            Kayıt Ol
          </Button>
        </Box>
      </Box>
    </Container>
  );
} 