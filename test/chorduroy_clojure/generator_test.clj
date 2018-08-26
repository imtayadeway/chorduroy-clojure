(ns chorduroy-clojure.generator-test
  (:require [clojure.test :refer :all]
            [chorduroy-clojure.core :refer :all]
            [chorduroy-clojure.generator :refer :all]
            [chorduroy-clojure.filterer :refer [frets-from]]))

(def standard-tuning ["E" "A" "D" "G" "B" "E"])
(def open-g-tuning ["D" "G" "D" "G" "B" "D"])

(deftest generate-test
  (let [results (generate standard-tuning)
        chords (keys results)
        expected (map name-for-chord the-diatonic-chords)]
    (is (= (set expected) (set chords)))))
