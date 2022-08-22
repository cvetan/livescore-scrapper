CREATE TABLE competitions
(
    id INT auto_increment PRIMARY KEY,
    sport_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    url VARCHAR(255) NOT NULL,
    country varchar(255) not null,
    enabled BOOLEAN DEFAULT FALSE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT competitions_sports_fk FOREIGN KEY (sport_id) REFERENCES sports (id)
);

