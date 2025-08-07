CREATE TABLE card_transactions (
                                   id INT AUTO_INCREMENT PRIMARY KEY,
                                   transaction_date DATE NOT NULL,
                                   card_type VARCHAR(20),
                                   used_card VARCHAR(50),
                                   merchant_name VARCHAR(100),
                                   approval_number VARCHAR(30),
                                   amount DECIMAL(15, 2),
                                   purchase_type VARCHAR(20),
                                   usage_type VARCHAR(20),
                                   currency VARCHAR(10),
                                   payment_date DATE
) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
