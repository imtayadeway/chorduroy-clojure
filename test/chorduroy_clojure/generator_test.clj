(ns chorduroy-clojure.generator-test
  (:require [clojure.test :refer :all]
            [chorduroy-clojure.generator :refer :all]))

(deftest generate-test
  (let [results (generate standard-tuning)
        chords (map :name results)]
    (is (= the-major-and-minor-chords (set chords)))))

(deftest generate-for-chord-test
  (let [e-chords (generate-for-chord {:pitch "E" :tonality "Major"} standard-tuning)
        g-chords (generate-for-chord {:pitch "G" :tonality "Minor"} standard-tuning)]
    (is (some #{[0 2 2 1 0 0]} e-chords))
    (is (some #{[3 2 0 0 0 3]} g-chords))))
