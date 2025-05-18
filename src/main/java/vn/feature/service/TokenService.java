package vn.feature.service;
import vn.feature.model.Token;
import vn.feature.model.User;

public interface TokenService {
    Token addTokenEndRefreshToken(User user, String token, boolean isMobile);
}
