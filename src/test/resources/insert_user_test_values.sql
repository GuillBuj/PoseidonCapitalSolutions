use demotest;

INSERT INTO Users (username, password, fullname, role) VALUES 
  ('user', '$2a$10$ABCDEFGH1234567890', 'Trader One', 'USER'), -- Password123!
  ('admin', '$2a$10$ZYXWVUTS9876543210', 'Trader Two', 'ADMIN');  -- Admin456!