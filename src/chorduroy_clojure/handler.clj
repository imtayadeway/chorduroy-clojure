(ns chorduroy-clojure.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [chorduroy-clojure.views :as views]
            [chorduroy-clojure.generator :as generator])
  (:gen-class))

(defroutes app-routes
  (GET "/" [] (views/index-page))
  (POST "/results"
        {{:strs [sixth fifth fourth third second first]} :form-params}
        (views/results-page (generator/generate [sixth fifth fourth third second first])))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes
                 (assoc-in site-defaults [:security :anti-forgery] false)))

(defn -main
  []
  (let [port (or (Integer. (System/getenv "PORT")) 8080)]
    (run-jetty app {:port port})))
