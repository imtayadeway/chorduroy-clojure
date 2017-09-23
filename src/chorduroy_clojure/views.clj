(ns chorduroy-clojure.views
  (:use [hiccup core page]))

(def strings ["6th" "5th" "4th" "3rd" "2nd" "1st"])
(def pitches ["A" "A#/Bb" "B" "C" "C#/Db" "D" "D#/Eb" "E" "F" "F#/Gb" "G" "G#/Ab"])

(defn index-page
  []
  (html5
   [:head
    [:title "Chorduroy"]]
   [:body
    [:form {:action "/results" :method "get"}
     (for [string strings]
       (list [:label {:for (str string "_String")} (str string " String")]
             [:select {:id string :name string}
              (for [pitch pitches]
                [:option {:value pitch} pitch])]))
     [:input {:name "commit" :value "Submit" :data-disable-with "Submit" :type "Submit"}]]]))
