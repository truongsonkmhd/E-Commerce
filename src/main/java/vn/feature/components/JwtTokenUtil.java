package vn.feature.components;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import vn.feature.model.Token;
import vn.feature.repository.TokenRepository;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    private final TokenRepository tokenRepository;

    @Value("${jwt.expiration}")
    private int expiration; // lưu trong biến môi trường ,Thời gian sống của token,

    @Value("${jwt.secretKey}")
    private String secretKey;

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    // generate token using jwt utility class and return token as string
    public String generateToken(Map<String, Object> extractClaims, UserDetails userDetails) {
        try {
            return Jwts
                    .builder()
                    .setClaims(extractClaims)// Thêm claims vào token
                    .setSubject(userDetails.getUsername())// Đặt subject là username
                    .setIssuedAt(new Date(System.currentTimeMillis())) // Thời gian phát hành token
                    .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Thời gian hết hạn
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)// Ký token với HMAC SHA256
                    .compact();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    // decode and get the key
    private Key getSignInKey() {
        // decode SECRET_KEY
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token) { // Giải mã JWT
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    // => muc dich : Giải mã token và lấy toàn bộ claims (dữ liệu được mã hóa trong JWT)

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    //=> Cho phép lấy bất kỳ thông tin nào từ token thông qua claimsResolver.


    // if token is valid by checking if token is expired for current user
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String phoneNumber = extractPhoneNumber(token);
        Token existingToken = tokenRepository.findByToken(token);
        if (existingToken == null
                || existingToken.isRevoked()
                || !existingToken.getUser().isActive()
        ) {
            return false;
        }
        return phoneNumber.equals(userDetails.getUsername()) && !isTokenExpirated(token);
    }


    // extract user from token
    public String extractPhoneNumber(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // if token expirated
    public boolean isTokenExpirated(String token) {
        return extractExpiration(token).before(new Date());
    }

    // get expiration data from token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}
