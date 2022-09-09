(ns livescore-scrapper.db.core-test
  (:require
    [clojure.test :refer :all]
    [java-time.pre-java8]
    [livescore-scrapper.config :refer [env]]
    [livescore-scrapper.db.core :refer [*db*] :as db]
    [luminus-migrations.core :as migrations]
    [mount.core :as mount]
    [next.jdbc :as jdbc]))

(use-fixtures
  :once
  (fn [f]
    (mount/start
     #'livescore-scrapper.config/env
     #'livescore-scrapper.db.core/*db*)
    (migrations/migrate ["migrate"] (select-keys env [:database-url]))
    (f)))

(deftest test-users
  (jdbc/with-transaction [t-conn *db* {:rollback-only true}]
    (is (= 1 (db/create-user!
              t-conn
              {:id         "1"
               :first_name "Sam"
               :last_name  "Smith"
               :email      "sam.smith@example.com"
               :pass       "pass"}
              {})))
    (is (= {:id         "1"
            :first_name "Sam"
            :last_name  "Smith"
            :email      "sam.smith@example.com"
            :pass       "pass"
            :admin      nil
            :last_login nil
            :is_active  nil}
           (db/get-user t-conn {:id "1"} {})))))
