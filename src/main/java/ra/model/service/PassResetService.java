package ra.model.service;

import ra.model.entity.PasswordResetToken;

public interface PassResetService {
    PasswordResetToken saveOrUpdate(PasswordResetToken passwordResetToken);
    PasswordResetToken getLastTokenByUserId(int userId);
}
