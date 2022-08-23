(ns livescore-scrapper.util.responses)

:gen-class

(defn created
  "This function returns 201 created response"
  [body]
  {:status 201
   :body body})

(defn not-found
  "This function returns 404 Not Found response"
  [message]
  {:status 404
   :body {:message message}})

(defn bad-request
  "This function returns 400 Bad Request response with errors and/or message"
  ([errors]
   {:status 400
    :body {:errors errors}})
  ([errors message]
   {:status 400
    :body {:errors errors
           :message message}}))

(defn internal-server-error
  "This function returns 500 Internal Server Error response"
  []
  {:status 500
   :body {:message "Internal server error"}})