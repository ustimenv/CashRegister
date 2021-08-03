-- Insert the items specified in the pdf

INSERT INTO sellable_item(code, full_name, default_price) VALUES('VOUCHER', 'Gift Card', 5);

INSERT INTO sellable_item(code, full_name, default_price) VALUES('TSHIRT', 'Summer T-Shirt', 20);

INSERT INTO sellable_item(code, full_name, default_price) VALUES('PANTS', 'Summer Pants', 7.5);

-- username='V', password='pass'
INSERT INTO cashier(username, first_name, last_name, password) VALUES('V', 'fn', 'ln', '{bcrypt}$2a$10$97rrLaLN22FhhIWA9uvNw.zyV9kuTkTt5BK9c4Pqdb8Zwo74NELZ2')