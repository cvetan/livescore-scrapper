-- :name create-sport! :! :1
INSERT INTO sports
(name, url, enabled)
VALUES (:name, :url, :enabled)
RETURNING id, name, url, created_at, updated_at;

-- :name fetch-sports :? :*
SELECT *
FROM sports
ORDER BY name;


-- :name fetch-sport-by-id :? :1
SELECT *
FROM sports
WHERE id = :id;