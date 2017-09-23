(ns chorduroy-clojure.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [chorduroy-clojure.views :as views]
            [chorduroy-clojure.generator :as generator]))

(defroutes app-routes
  (GET "/" [] (views/index-page))
  (POST "/results"
        {{:strs [sixth fifth fourth third second first] :as strings} :form-params}
        (views/results-page (generator/generate strings)))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes
                 (assoc-in site-defaults [:security :anti-forgery] false)))
