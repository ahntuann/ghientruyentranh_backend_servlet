package model;

import java.time.LocalDateTime;

// Lớp chứa mã xác thực và thời gian hết hạn
public class VerifyCode {
    private String authCode;
    private LocalDateTime expiredTime;

    public VerifyCode(String authCode, LocalDateTime expiredTime) {
        this.authCode = authCode;
        this.expiredTime = expiredTime;
    }

    public String getAuthCode() {
        return authCode;
    }

    public LocalDateTime getExpiredTime() {
        return expiredTime;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiredTime);
    }
}
