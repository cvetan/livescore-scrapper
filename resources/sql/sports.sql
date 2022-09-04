-- :name create-sport! :! :1
INSERT INTO sports
(name, url, enabled)
VALUES (:name, :url, :enabled)
RETURNING id, name, url, enabled, created_at, updated_at;

-- :name fetch-sports :? :*
SELECT *
FROM sports
ORDER BY name;


-- :name fetch-sport-by-id :? :1
SELECT *
FROM sports
WHERE id = :id;


-- :name sport-exists-by-id :? :1
SELECT COUNT(*) AS `exists`
FROM sports
where id = :id;


-- :name delete-sport-by-id :! :1
DELETE FROM sports
WHERE id = :id;