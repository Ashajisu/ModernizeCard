CREATE TABLE users (
                       empno BIGINT AUTO_INCREMENT PRIMARY KEY,
                       usertype VARCHAR(255),
                       name VARCHAR(255),
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(100) NOT NULL,
                       deptcode BIGINT,
                       position VARCHAR(50)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE card_transactions (
                                   id INT AUTO_INCREMENT PRIMARY KEY,
                                   transaction_date DATETIME NOT NULL,
                                   card_type VARCHAR(20),
                                   used_card VARCHAR(50),
                                   merchant_name VARCHAR(100),
                                   approval_number VARCHAR(30),
                                   amount BIGINT,
                                   purchase_type VARCHAR(20),
                                   usage_type VARCHAR(20),
                                   currency VARCHAR(10),
                                   payment_date DATE,
                                   deleted BOOLEAN DEFAULT FALSE


) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
