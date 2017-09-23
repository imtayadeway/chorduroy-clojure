(ns chorduroy-clojure.views
  (:use [hiccup core page form]))

(def strings ["Sixth" "Fifth" "Fourth" "Third" "Second" "First"])
(def pitches ["A" "A#/Bb" "B" "C" "C#/Db" "D" "D#/Eb" "E" "F" "F#/Gb" "G" "G#/Ab"])

(defn index-page
  []
  (html5
   [:head
    [:title "Chorduroy"]]
   [:body
    (form-to [:post "/results"]
             (for [string strings]
               (list (label {:for (str string "_String")} (str string "_String") (str string " String"))
                     (drop-down {:id string :name (clojure.string/lower-case string)} string pitches)))
             (submit-button {:name "commit" :value "Submit" :data-disable-with "Submit" :type "Submit"} "Submit"))]))
