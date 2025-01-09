package kz.odik.crm.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    private final String secretKey = "AlikhanDamirOdikRustDotaCS2OgromniiPricelDamiga4x4kapecTEAMKZKHANABLAIDOTA+KA4KIKA4ALO4KA"; // Секретный ключ для подписи токенов
    private final long validityInMilliseconds = 3600000; // Время действия токена (1 час)

    // Метод для создания токена
    public String createToken(String username, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(username); // Добавляем имя пользователя в токен
        claims.put("authorities", roles); // Добавляем роли в токен

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey) // Подписываем токен
                .compact();
    }

    // Метод для извлечения имени пользователя из токена
    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    // Метод для извлечения ролей из токена
    public List<String> getRolesFromToken(String token) {
        return (List<String>) getClaimsFromToken(token).get("authorities");
    }

    // Метод для извлечения всех данных из токена
    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Метод для проверки валидности токена
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token); // Проверяем подпись токена
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Невалидный или истекший токен
            System.out.println(e);
            return false;
        }
    }
}
