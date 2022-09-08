-- :name fetch-competitions :? :*
SELECT c.id,
       c.name,
       s.name AS sport,
       c.url,
       c.country,
       c.enabled,
       c.created_at AS createdAt,
       c.updated_at AS updatedAt
FROM competitions c
    JOIN sports s ON c.sport_id = s.id
ORDER BY c.name,
         c.country;


-- :name competition-exists-by-id :? :1
SELECT COUNT(*) AS `exists`
FROM competitions
where id = :id;


-- :name delete-competition :? :1
DELETE FROM competitions
WHERE id = :id;


-- :name fetch-competition-by-id ?1 :1
SELECT c.id,
       s.name AS sport,
       c.name,
       c.url,
       c.country,
       c.enabled,
       c.created_at AS createdAt,
       c.updated_at AS updatedAt
FROM competitions c
    JOIN sports s ON c.sport_id = s.id
WHERE c.id = :id;


-- :name enable-competition ?1 :1
UPDATE competitions
SET enabled = 1
WHERE id = :id;


-- :name disable-competition ?1 :1
UPDATE competitions
SET enabled = 0
WHERE id = :id;


-- :name save-competition :! :1
INSERT INTO competitions
(sport_id, name, url, country, enabled)
VALUES (:sport_id, :name, :url, :country, :enabled)
RETURNING id;


-- :name update-competition :! :1
UPDATE competitions
SET sport_id = :sport_id,
    name = :name,
    url = :url,
    country = :country,
    enabled = :enabled
WHERE id = :id;


-- :name import-competitions :! :*
INSERT IGNORE INTO competitions(sport_id, name, url, country)
VALUES :tuple*:competitions;