import React from 'react';
import { Box, Typography, Card, CardContent, List, ListItem, ListItemText } from '@mui/material';

export default function Help() {
  return (
    <Box sx={{ mt: 4, px: 2, display: 'flex', justifyContent: 'center' }}>
      <Card sx={{ maxWidth: 600, width: '100%', boxShadow: 4, borderRadius: 3 }}>
        <CardContent>
          <Typography variant="h4" gutterBottom>Yardım & Destek</Typography>
          <Typography variant="body1" sx={{ mb: 2 }}>
            Bu sistemde öğrenci, öğretmen ve ders işlemlerinizi kolayca yönetebilirsiniz. Aşağıda sık sorulan sorular ve iletişim bilgileri yer almaktadır.
          </Typography>
          <Typography variant="h6" sx={{ mt: 2 }}>Sık Sorulan Sorular</Typography>
          <List>
            <ListItem>
              <ListItemText primary="Giriş yapamıyorum, ne yapmalıyım?" secondary="Kullanıcı adınızı ve şifrenizi doğru girdiğinizden emin olun. Sorun devam ederse yöneticinizle iletişime geçin." />
            </ListItem>
            <ListItem>
              <ListItemText primary="Şifremi unuttum, nasıl sıfırlayabilirim?" secondary="Şifre sıfırlama özelliği için yöneticinizle iletişime geçin." />
            </ListItem>
            <ListItem>
              <ListItemText primary="Ders/öğrenci ekleyemiyorum, neden?" secondary="Yetkiniz olup olmadığını ve gerekli alanları doldurduğunuzu kontrol edin." />
            </ListItem>
          </List>
          <Typography variant="h6" sx={{ mt: 2 }}>İletişim</Typography>
          <Typography variant="body2">
            Destek için: <a href="mailto:destek@xyzuniversitesi.edu.tr">destek@xyzuniversitesi.edu.tr</a>
          </Typography>
        </CardContent>
      </Card>
    </Box>
  );
} 