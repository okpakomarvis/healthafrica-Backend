-- Fix seed user passwords: previous hash did not match plaintext "password"
UPDATE app_user
SET password = '$2a$10$C5SN2U3/AdaWWM3qSFCg/uAwM4e5oO.zJVsbQC62jcHimIV90wCvy'
WHERE username IN ('chw1', 'clinician1', 'manager1', 'admin1');
