(ns chorduroy-clojure.generator-test
  (:require [clojure.test :refer :all]
            [chorduroy-clojure.generator :refer :all]))

(def standard-tuning ["E" "A" "D" "G" "B" "E"])
(def open-g-tuning ["D" "G" "D" "G" "B" "D"])

(deftest name-for-chord-test
  (is (= "A Major" (name-for-chord {:pitch "A" :tonality "Major"})))
  (is (= "A Minor" (name-for-chord {:pitch "A" :tonality "Minor"}))))

(deftest positions-for-chord-test
  (let [standard-e-chords (positions-for-chord {:pitch "E" :tonality "Major"} standard-tuning)
        standard-g-chords (positions-for-chord {:pitch "G" :tonality "Minor"} standard-tuning)
        open-d-chords (positions-for-chord {:pitch "D" :tonality "Major"} open-g-tuning)]
    (is (some #{[0 2 2 1 0 0]} standard-e-chords))
    (is (some #{[3 2 0 0 0 3]} standard-g-chords))
    (is (some #{[0 2 0 2 3 0]} open-d-chords))
    (is (not-any? #{[0 2 2 1 0 0]} standard-g-chords))))

(deftest generate-test
  (let [results (generate standard-tuning)
        chords (map :name results)
        expected (map name-for-chord the-major-and-minor-chords)]
    (is (= (set expected) (set chords)))))
