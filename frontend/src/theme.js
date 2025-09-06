import { createTheme } from '@mui/material/styles';

const getTheme = (mode = 'light') => createTheme({
  palette: {
    mode,
    primary: {
      main: '#1a237e',
    },
    secondary: {
      main: '#fbc02d',
    },
    background: {
      default: mode === 'light' ? '#f5f6fa' : '#181a1b',
      paper: mode === 'light' ? '#fff' : '#23272a',
    },
    error: {
      main: '#e53935',
    },
    text: {
      primary: mode === 'light' ? '#263238' : '#fff',
    },
  },
  typography: {
    fontFamily: 'Roboto, Arial, sans-serif',
    h6: {
      fontWeight: 700,
      letterSpacing: 1,
    },
  },
});

export default getTheme; 