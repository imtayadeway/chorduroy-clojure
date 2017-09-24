(ns chorduroy-clojure.generator-test
  (:require [clojure.test :refer :all]
            [chorduroy-clojure.generator :refer :all]))

(deftest name-for-chord-test
  (is (= "A Major" (name-for-chord {:pitch "A" :tonality "Major"})))
  (is (= "A Minor" (name-for-chord {:pitch "A" :tonality "Minor"}))))

(deftest positions-for-chord-test
  (let [e-chords (positions-for-chord {:pitch "E" :tonality "Major"} standard-tuning)
        g-chords (positions-for-chord {:pitch "G" :tonality "Minor"} standard-tuning)]
    (is (some #{[0 2 2 1 0 0]} e-chords))
    (is (some #{[3 2 0 0 0 3]} g-chords))))

(deftest generate-test
  (let [results (generate standard-tuning)
        chords (map :name results)
        expected (map name-for-chord the-major-and-minor-chords)]
    (is (= (set expected) (set chords)))))
