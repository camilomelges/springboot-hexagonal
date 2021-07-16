DROP TABLE IF EXISTS bankslips;

CREATE TABLE bankslips
(
    id    INT AUTO_INCREMENT PRIMARY KEY,
    value DECIMAL(11, 2) NOT NULL,
    paid  BOOLEAN        NOT NULL
);