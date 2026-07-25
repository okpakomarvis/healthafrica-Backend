-- V11 used the pre-V7 bcrypt hash; align superadmin with seed password "password"
UPDATE app_user
SET password = '$2a$10$C5SN2U3/AdaWWM3qSFCg/uAwM4e5oO.zJVsbQC62jcHimIV90wCvy'
WHERE tenant_id = 'PLATFORM' AND username = 'superadmin';
