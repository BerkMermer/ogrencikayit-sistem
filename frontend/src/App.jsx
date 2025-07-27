import * as React from 'react';
import { BrowserRouter as Router, Routes, Route, useNavigate, Navigate, useLocation } from 'react-router-dom';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import Button from '@mui/material/Button';
import Login from './components/Login';
import Register from './components/Register';
import AdminPanel from './components/AdminPanel';
import OgretmenPanel from './components/OgretmenPanel';
import OgrenciPanel from './components/OgrenciPanel';
import NotPanel from './components/NotPanel';
import OgrenciDashboard from './components/OgrenciDashboard';
import logo from './assets/react.svg';
import { Card, CardContent, Typography as MuiTypography, Fade, Switch, FormControlLabel, useTheme, Snackbar, Alert, CircularProgress, Backdrop } from '@mui/material';
import LoginIcon from '@mui/icons-material/Login';
import LogoutIcon from '@mui/icons-material/Logout';
import PersonAddIcon from '@mui/icons-material/PersonAdd';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import Brightness4Icon from '@mui/icons-material/Brightness4';
import Brightness7Icon from '@mui/icons-material/Brightness7';
import Profile from './components/Profile';
import AccountBoxIcon from '@mui/icons-material/AccountBox';
import HomeChart from './components/HomeChart';
import NotificationsIcon from '@mui/icons-material/Notifications';
import Badge from '@mui/material/Badge';
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';
import IconButton from '@mui/material/IconButton';
import Help from './components/Help';
import HelpOutlineIcon from '@mui/icons-material/HelpOutline';
import HomeIcon from '@mui/icons-material/Home';

function Home() {
  const user = React.useMemo(() => {
    const u = localStorage.getItem('user');
    return u ? JSON.parse(u) : null;
  }, []);
  // Eğer giriş yapmış kullanıcı admin ise otomatik admin paneline yönlendir
  if (user && user.rol === 'ADMIN') return <Navigate to="/admin" replace />;
  if (user && user.rol === 'OGRETMEN') return <Navigate to="/ogretmen" replace />;
  if (user && user.rol === 'OGRENCI') return <Navigate to="/ogrenci-dashboard" replace />;
  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center', minHeight: { xs: '50vh', sm: '60vh' }, px: 1 }}>
      <Card sx={{ minWidth: 280, maxWidth: 400, width: '100%', boxShadow: 4, borderRadius: 3, mb: 4 }}>
        <CardContent>
          <MuiTypography variant="h5" component="div" gutterBottom sx={{ fontSize: { xs: 18, sm: 24 } }}>
            Hoş geldin!
          </MuiTypography>
          <MuiTypography variant="body1" color="text.secondary" sx={{ fontSize: { xs: 14, sm: 16 } }}>
            XYZ Üniversitesi Öğrenci Kayıt Sistemi'ne hoş geldiniz.
          </MuiTypography>
        </CardContent>
      </Card>
      <Box sx={{ width: '100%', maxWidth: 500 }}>
        <HomeChart />
      </Box>
    </Box>
  );
}

function AdminPage() {
  return <Typography variant="h4" sx={{ mt: 8, textAlign: 'center' }}>Admin Paneli (Sadece Admin)</Typography>;
}
function OgretmenPage() {
  return <Typography variant="h4" sx={{ mt: 8, textAlign: 'center' }}>Öğretmen Paneli (Sadece Öğretmen)</Typography>;
}

function ProtectedRoute({ children, allowedRoles }) {
  const user = React.useMemo(() => {
    const u = localStorage.getItem('user');
    return u ? JSON.parse(u) : null;
  }, []);
  if (!user) {
    return <Navigate to="/login" replace />;
  }
  if (!allowedRoles.includes(user.rol)) {
    return <Navigate to="/login" replace />;
  }
  return children;
}

function AppBarMenu({ mode, onToggleMode, showSnackbar, notifications, setNotifications }) {
  const navigate = useNavigate();
  const [user, setUser] = React.useState(() => {
    const u = localStorage.getItem('user');
    return u ? JSON.parse(u) : null;
  });

  React.useEffect(() => {
    const handleStorage = () => {
      const u = localStorage.getItem('user');
      setUser(u ? JSON.parse(u) : null);
    };
    window.addEventListener('storage', handleStorage);
    return () => window.removeEventListener('storage', handleStorage);
  }, []);

  const handleLogout = () => {
    localStorage.removeItem('user');
    setUser(null);
    navigate('/login');
    if (showSnackbar) showSnackbar('Çıkış yapıldı.', 'success');
  };

  const theme = useTheme();
  const [anchorEl, setAnchorEl] = React.useState(null);
  const unreadCount = notifications ? notifications.filter(n => !n.read).length : 0;
  const handleNotifClick = (event) => setAnchorEl(event.currentTarget);
  const handleNotifClose = () => setAnchorEl(null);
  const handleNotifRead = () => {
    setNotifications((prev) => prev.map(n => ({ ...n, read: true })));
    handleNotifClose();
  };

  // Kullanıcı menüsü
  const [userMenuAnchor, setUserMenuAnchor] = React.useState(null);
  const handleUserMenuOpen = (event) => setUserMenuAnchor(event.currentTarget);
  const handleUserMenuClose = () => setUserMenuAnchor(null);
  const goPanel = () => {
    handleUserMenuClose();
    if (!user) return;
    if (user.rol === 'ADMIN') navigate('/admin');
    else if (user.rol === 'OGRETMEN') navigate('/ogretmen');
    else if (user.rol === 'OGRENCI') navigate('/ogrenci-dashboard');
  };
  const goProfile = () => {
    handleUserMenuClose();
    navigate('/profile');
  };
  const handleLogoutMenu = () => {
    handleUserMenuClose();
    handleLogout();
  };

  return (
    <AppBar position="static" sx={{ bgcolor: 'primary.main', boxShadow: 3 }}>
      <Container maxWidth="lg" disableGutters sx={{ px: { xs: 1, sm: 2 } }}>
        <Toolbar sx={{ minHeight: { xs: 56, sm: 64 } }}>
          <IconButton color="inherit" onClick={() => navigate('/')} sx={{ mr: 1 }}>
            <HomeIcon />
          </IconButton>
          <img src={logo} alt="Logo" style={{ height: 32, marginRight: 8, maxWidth: 32 }} />
          <Typography variant="h6" component="div" sx={{ flexGrow: 1, fontWeight: 700, letterSpacing: 1, fontSize: { xs: 16, sm: 20 } }}>
            XYZ Üniversitesi Öğrenci Kayıt Sistemi
          </Typography>
          {/* Kullanıcı menüsü */}
          {user ? (
            <>
              <IconButton color="inherit" onClick={handleUserMenuOpen} sx={{ ml: 1 }}>
                <AccountCircleIcon sx={{ width: 32, height: 32, color: '#888' }} />
              </IconButton>
              <Menu anchorEl={userMenuAnchor} open={Boolean(userMenuAnchor)} onClose={handleUserMenuClose}>
                <MenuItem onClick={goPanel}><HomeIcon sx={{ mr: 1 }} />Panel</MenuItem>
                <MenuItem onClick={goProfile}><AccountBoxIcon sx={{ mr: 1 }} />Profil</MenuItem>
                <MenuItem onClick={handleLogoutMenu}><LogoutIcon sx={{ mr: 1 }} />Çıkış Yap</MenuItem>
              </Menu>
            </>
          ) : (
            <>
              <Button color="inherit" href="/login" startIcon={<LoginIcon />} sx={{ fontSize: { xs: 12, sm: 14 }, px: { xs: 1, sm: 2 } }}>Giriş Yap</Button>
              <Button color="inherit" href="/register" startIcon={<PersonAddIcon />} sx={{ fontSize: { xs: 12, sm: 14 }, px: { xs: 1, sm: 2 } }}>Kayıt Ol</Button>
            </>
          )}
          <IconButton color="inherit" onClick={() => navigate('/help')} sx={{ mr: 1 }}>
            <HelpOutlineIcon />
          </IconButton>
          <IconButton color="inherit" onClick={handleNotifClick} sx={{ mr: 1 }}>
            <Badge badgeContent={unreadCount} color="error">
              <NotificationsIcon />
            </Badge>
          </IconButton>
          <Menu anchorEl={anchorEl} open={Boolean(anchorEl)} onClose={handleNotifClose} onClick={handleNotifRead}>
            {notifications && notifications.length > 0 ? notifications.slice(0, 5).map((n) => (
              <MenuItem key={n.id} sx={{ fontWeight: n.read ? 400 : 700 }}>{n.text}</MenuItem>
            )) : <MenuItem>Bildirim Yok</MenuItem>}
          </Menu>
          <FormControlLabel
            control={
              <Switch
                checked={mode === 'dark'}
                onChange={onToggleMode}
                color="default"
                icon={<Brightness7Icon />}
                checkedIcon={<Brightness4Icon />}
              />
            }
            label={mode === 'dark' ? 'Koyu' : 'Açık'}
            sx={{ mr: 2 }}
          />
        </Toolbar>
      </Container>
    </AppBar>
  );
}

function Footer() {
  return (
    <Box component="footer" sx={{
      width: '100%',
      bgcolor: 'primary.main',
      color: 'white',
      textAlign: 'center',
      py: 2,
      mt: 4,
      fontSize: { xs: 12, sm: 14 },
      boxShadow: 3,
    }}>
      © {new Date().getFullYear()} XYZ Üniversitesi Öğrenci Kayıt Sistemi
    </Box>
  );
}

function App({ mode, setMode }) {
  const location = useLocation();
  const [snackbar, setSnackbar] = React.useState({ open: false, message: '', severity: 'info' });
  const [notifications, setNotifications] = React.useState([
    { id: 1, text: 'Hoş geldiniz!', read: false },
    { id: 2, text: 'Profilinizi güncelleyebilirsiniz.', read: false },
  ]);
  const showSnackbar = (message, severity = 'info') => {
    setSnackbar({ open: true, message, severity });
    setNotifications((prev) => [
      { id: Date.now(), text: message, read: false },
      ...prev,
    ]);
  };
  const handleSnackbarClose = () => setSnackbar({ ...snackbar, open: false });
  // Loading state
  const [loading, setLoading] = React.useState(false);
  React.useEffect(() => {
    setLoading(true);
    const timeout = setTimeout(() => setLoading(false), 400);
    return () => clearTimeout(timeout);
  }, [location.pathname]);
  return (
    <Box sx={{ flexGrow: 1, minHeight: '100vh', background: mode === 'light' ? 'linear-gradient(135deg, #f5f6fa 0%, #e3eafc 100%)' : 'linear-gradient(135deg, #181a1b 0%, #23272a 100%)', px: { xs: 0, sm: 2 }, display: 'flex', flexDirection: 'column' }}>
      <AppBarMenu mode={mode} onToggleMode={() => setMode(mode === 'light' ? 'dark' : 'light')} showSnackbar={showSnackbar} notifications={notifications} setNotifications={setNotifications} />
      <Backdrop open={loading} sx={{ color: '#fff', zIndex: (theme) => theme.zIndex.drawer + 1 }}>
        <CircularProgress color="inherit" />
      </Backdrop>
      <Fade in={!loading} timeout={500} key={location.pathname}>
        <Box sx={{ flexGrow: 1 }}>
          <Routes location={location}>
            <Route path="/" element={<Home />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route path="/admin" element={
              <ProtectedRoute allowedRoles={['ADMIN']}>
                <AdminPanel />
              </ProtectedRoute>
            } />
            <Route path="/ogretmen" element={
              <ProtectedRoute allowedRoles={['OGRETMEN']}>
                <OgretmenPanel />
              </ProtectedRoute>
            } />
            <Route path="/ogrenciler" element={
              <ProtectedRoute allowedRoles={['ADMIN', 'OGRETMEN']}>
                <OgrenciPanel />
              </ProtectedRoute>
            } />
            <Route path="/notlar" element={
              <ProtectedRoute allowedRoles={['ADMIN', 'OGRETMEN']}>
                <NotPanel />
              </ProtectedRoute>
            } />
            <Route path="/ogrenci-dashboard" element={
              <ProtectedRoute allowedRoles={['OGRENCI']}>
                <OgrenciDashboard />
              </ProtectedRoute>
            } />
            <Route path="/profile" element={<Profile />} />
            <Route path="/help" element={<Help />} />
          </Routes>
        </Box>
      </Fade>
      <Snackbar open={snackbar.open} autoHideDuration={3000} onClose={handleSnackbarClose} anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}>
        <Alert onClose={handleSnackbarClose} severity={snackbar.severity} sx={{ width: '100%' }}>
          {snackbar.message}
        </Alert>
      </Snackbar>
      <Footer />
    </Box>
  );
}

export default App;
