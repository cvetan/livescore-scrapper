CREATE TABLE blacklisted_items
(
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type ENUM('sport', 'competitions') NOT NULL
);

