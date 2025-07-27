package com.berkmermer.ogrencikayit.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Öğrenci Kayıt Sistemi</title>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; margin: 40px; background-color: #f5f5f5; }
                    .container { max-width: 800px; margin: 0 auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                    h1 { color: #2c3e50; text-align: center; }
                    .api-section { margin: 20px 0; padding: 15px; background: #ecf0f1; border-radius: 5px; }
                    .api-link { color: #3498db; text-decoration: none; font-weight: bold; }
                    .api-link:hover { text-decoration: underline; }
                    .status { color: #27ae60; font-weight: bold; }
                </style>
            </head>
            <body>
                <div class="container">
                    <h1>🎓 Öğrenci Kayıt Sistemi</h1>
                    <p class="status">✅ Sistem çalışıyor!</p>
                    
                    <div class="api-section">
                        <h3>📚 API Endpoint'leri:</h3>
                        <ul>
                            <li><a href="/api/ogrenciler" class="api-link">👥 Öğrenci Listesi</a></li>
                            <li><a href="/api/dersler" class="api-link">📖 Ders Listesi</a></li>
                            <li><a href="/api/kayitlar" class="api-link">📝 Kayıt Listesi</a></li>
                            <li><a href="/api/notlar" class="api-link">📊 Not Listesi</a></li>
                            <li><a href="/api/dashboard" class="api-link">📈 Dashboard</a></li>
                        </ul>
                    </div>
                    
                    <div class="api-section">
                        <h3>🔧 Sistem Bilgileri:</h3>
                        <ul>
                            <li><strong>Port:</strong> 8080</li>
                            <li><strong>Veritabanı:</strong> PostgreSQL</li>
                            <li><strong>Framework:</strong> Spring Boot</li>
                            <li><strong>Container:</strong> Docker</li>
                        </ul>
                    </div>
                </div>
            </body>
            </html>
            """;
    }
} 