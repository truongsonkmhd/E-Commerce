package vn.feature.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.feature.exception.payload.DataNotFoundException;
import vn.feature.model.Token;
import vn.feature.model.User;
import vn.feature.repositorys.TokenRepository;
import vn.feature.repositorys.UserRepository;
import vn.feature.service.TokenService;
import vn.feature.util.MessageKeys;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    // số thiết bị nhập
    private static final int MAX_TOKENS = 3;

    @Value("${jwt.expiration}")
    private int expiration;

    @Value("${jwt.expiration-refresh-token}")
    private int expirationRefreshToken;

    @Transactional
    @Override
    public Token addTokenEndRefreshToken(User user, String token, boolean isMobile) {
        List<Token> userTokens = tokenRepository.findByUser(user);

        int tokensCount = userTokens.size();
        //  số lượng token vượt quá giới hạn
        if (tokensCount >= MAX_TOKENS) {
            boolean hasNoMobileToken = !userTokens.stream().allMatch(Token::isMobile);
            Token tokenToDelete;
            if (hasNoMobileToken) {
                tokenToDelete = userTokens.stream()
                        .filter(userToken -> !userToken.isMobile())
                        .findFirst()
                        .orElse(userTokens.get(0));
            } else {
                // chúng ta sẽ xoá token đầu tiên trong danh sách
                tokenToDelete = userTokens.get(0);
            }
            tokenRepository.delete(tokenToDelete);
        }
        Token newToken = Token.builder()
                .user(user)
                .token(token)
                .refreshToken(UUID.randomUUID().toString())
                .tokenType("Bearer")
                .expirationTime(Instant.now().plusMillis(expiration))
                .refreshExpirationTime(Instant.now().plusMillis(expirationRefreshToken))
                .revoked(false)
                .expired(false)
                .isMobile(isMobile)
                .build();

        return tokenRepository.save(newToken);
    }

    public Token verifyRefreshToken(String refreshToken) {
        Token token = tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new DataNotFoundException((MessageKeys.NOT_FOUND)));

        if (token.getExpirationTime().compareTo(Instant.now()) < 0) {
            tokenRepository.delete(token);
            throw new RuntimeException("Refresh token expired");
        }

        return token;
    }

}