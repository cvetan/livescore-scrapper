(ns livescore-scrapper.routes.services
  (:require
    [reitit.swagger :as swagger]
    [reitit.swagger-ui :as swagger-ui]
    [reitit.ring.coercion :as coercion]
    [reitit.coercion.spec :as spec-coercion]
    [reitit.ring.middleware.muuntaja :as muuntaja]
    [reitit.ring.middleware.multipart :as multipart]
    [reitit.ring.middleware.parameters :as parameters]
    [livescore-scrapper.middleware.formats :as formats]
    [ring.util.http-response :refer :all]
    [livescore-scrapper.util.responses :as responses]
    [livescore-scrapper.service.sports :as sports-service]
    [livescore-scrapper.service.competitions :as competitions]
    [clojure.java.io :as io]))

(defn service-routes []
  ["/api"
   {:coercion spec-coercion/coercion
    :muuntaja formats/instance
    :swagger {:id ::api}
    :middleware [;; query-params & form-params
                 parameters/parameters-middleware
                 ;; content-negotiation
                 muuntaja/format-negotiate-middleware
                 ;; encoding response body
                 muuntaja/format-response-middleware
                 ;; exception handling
                 coercion/coerce-exceptions-middleware
                 ;; decoding request body
                 muuntaja/format-request-middleware
                 ;; coercing response bodys
                 coercion/coerce-response-middleware
                 ;; coercing request parameters
                 coercion/coerce-request-middleware
                 ;; multipart
                 multipart/multipart-middleware]}

   ;; swagger documentation
   ["" {:no-doc true
        :swagger {:info {:title "Livescore scrapper API"
                         :description "This API will be used as proxy to flashscore.com website."}}}

    ["/swagger.json"
     {:get (swagger/create-swagger-handler)}]

    ["/api-docs/*"
     {:get (swagger-ui/create-swagger-ui-handler
             {:url "/api/swagger.json"
              :config {:validator-url nil}})}]]

   ["/sports"
    {:swagger {:tags ["Sports API"]}}

    [""
     {:get {:summary "This request returns list of sports"
            :swagger {:produces ["application/json"]}
            :responses {200 {:description "List of sports"
                             :body [{:id int?
                                     :name string?
                                     :url string?
                                     :enabled boolean?
                                     :createdAt string?
                                     :updatedAt string?}]}}
            :handler (fn [_]
                       (sports-service/get-sports-list))}
      :post {:summary "This request creates new sport"
             :swagger {:consumes ["application/json"]
                       :produces ["application/json"]}
             :parameters {:body {:name string?
                                 :url string?
                                 :enabled boolean?}}
             :responses {201 {:description "Newly created sport data"
                              :body {:id int?
                                     :name string?
                                     :url string?
                                     :enabled boolean?
                                     :createdAt string?
                                     :updatedAt string?}}
                         400 {:description "Invalid create sport request"
                              :body {:status int?
                                     :errors [string?]
                                     :message string?}}}
             :handler (fn [{{{:keys [name url enabled] :as sport-request} :body} :parameters}]
                        (sports-service/save-sport sport-request))}}

    ]

    ["/:id"
     {:get {:summary "This request returns single sport by ID"
            :swagger {:produces ["application/json"]}
            :parameters {:path {:id int?}}
            :responses {200 {:description "Sport data"
                             :body {:id int?
                                     :name string?
                                     :url string?
                                     :enabled boolean?
                                     :createdAt string?
                                     :updatedAt string?}}
                        404 {:description "Sport not found"
                             :body {:message string?}}}
            :handler (fn [{{{:keys [id]} :path} :parameters}]
                       (sports-service/get-sport id))}

      :put {:summary "This request updates sport"
            :swagger {:produces ["application/json"]
                      :consumes ["application/json"]}
            :parameters {:body {:name string?
                                 :url string?
                                 :enabled boolean?}}
            :responses {204 {:description "Sport updated successfully."}
                        400 {:description "Bad request"
                             :body {:status int?
                                    :errors [string?]
                                    :message string?}}
                        404 {:description "Sport not found"
                             :body {:status int?
                                    :message string?
                                    }}}
            :handler (fn [_]
                       (sports-service/update-sport 1 {}))}

      }]]


   ["/competitions"
    {:swagger {:tags ["Competitions API"]}}
    [""
     {:get {:summary "This request returns list of competitions"
            :swagger {:produces ["application/json"]}
            :responses {200 {:description "List of competitions"
                             :body [{:id int?
                                     :sport string?
                                     :name string?
                                     :url string?
                                     :country string?
                                     :enabled boolean?
                                     :createdAt string?
                                     :updatedAt string?}]}}
            :handler (fn [_]
                       (competitions/get-competitions-list))}
      :post {:summary "This request creates new competition"
             :swagger {:consumes ["application/json"]
                       :produces ["application/json"]}
             :parameters {:body {:sport_id int?
                                 :name string?
                                 :url string?
                                 :country string?
                                 :enabled boolean?}}
             :responses {201 {:description "Newly created competition data"
                              :body {:id int?
                                     :sport string?
                                     :name string?
                                     :url string?
                                     :country string?
                                     :enabled boolean?
                                     :createdAt string?
                                     :updatedAt string?}}
                         400 {:description "Invalid create competition request"
                              :body {:status int?
                                     :errors [string?]
                                     :message string?}}}
             :handler (fn [{{{:keys [sport_id name url country enabled] :as sport-request} :body} :parameters}]
                        (sports-service/save-sport sport-request))}}]

    ["/{id}"
     {:get {:summary "This request returns single competition by ID"
            :swagger {:produces ["application/json"]}
            :responses {200 {:description "Competition data"
                             :body {:id int?
                                     :sport string?
                                     :name string?
                                     :url string?
                                     :country string?
                                     :enabled boolean?
                                     :createdAt string?
                                     :updatedAt string?}}
                        404 {:description "Competition not found"
                             :body {:status int?
                                    :message string?}}}
            :handler (fn [_]
                       (competitions/get-competition 1))}

      :put {:summary "This request updates competition"
            :swagger {:produces ["application/json"]
                      :consumes ["application/json"]}
            :parameters {:body {:sport_id int?
                                 :name string?
                                 :url string?
                                 :country string?
                                 :enabled boolean?}}
            :responses {204 {:description "Competition updated successfully."}
                        400 {:description "Bad request"
                             :body {:status int?
                                    :errors [string?]
                                    :message string?}}
                        404 {:description "Competition not found"
                             :body {:status int?
                                    :message string?
                                    }}}
            :handler (fn [_]
                       (competitions/update-competition 1 {}))}

      }]

    ["/{id}/standings"
     {:get {:summary "This request fetches standings for competition"
            :swagger {:produces ["application/json"]}
            :parameters {:path {:id int?}}
            :responses {200 {:description "Competition standings"
                             :body [{:position int?
                                     :team string?
                                     :mp int?
                                     :w int?
                                     :d int?
                                     :l int?
                                     :pts int?}]}
                        404 {:description "Competition not found"
                             :body {:status int?
                                    :message string?}}}
            :handler (fn [_]
                       (competitions/get-standings 1))}}]

    ["/{id}/results"
     {:get {:summary "This request fetches results for competition"
            :swagger {:produces ["application/json"]}
            :parameters {:path {:id int?}}
            :responses {200 {:description "Competition results"
                             :body [{:time string?
                                     :match string?
                                     :result string?}]}
                        404 {:description "Competition not found"
                             :body {:status int?
                                    :message string?}}}
            :handler (fn [_]
                       (competitions/get-results 1))}}]]
   ])
