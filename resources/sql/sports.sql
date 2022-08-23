-- :name create-user! :! :n
-- :doc creates a new user record
INSERT INTO sports
(name, url, enabled)
VALUES (:name, :url, :enabled)