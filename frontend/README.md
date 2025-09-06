# React + Vite

Bu frontend, Vite ile oluşturulmuş modern bir React uygulamasıdır.

## Geliştirme
```bash
npm install
npm run dev
```

## Production Build
```bash
npm run build
```

## Docker ile Çalıştırma
```bash
docker build -t ogrenci-kayit-frontend .
docker run -p 3000:80 ogrenci-kayit-frontend
```

## Docker Compose ile Tüm Proje
Proje kök dizininde:
```bash
docker-compose up --build
```

Uygulama: [http://localhost:3000](http://localhost:3000)
