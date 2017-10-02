(ns chorduroy-clojure.generator-test
  (:require [clojure.test :refer :all]
            [chorduroy-clojure.core :refer :all]
            [chorduroy-clojure.generator :refer :all]
            [chorduroy-clojure.filterer :refer [frets-from]]))

(def standard-tuning ["E" "A" "D" "G" "B" "E"])
(def open-g-tuning ["D" "G" "D" "G" "B" "D"])

(deftest positions-for-chord-test
  (let [standard-e-chords (positions-for-chord {:root "E" :tonality "Major"} standard-tuning)
        standard-g-chords (positions-for-chord {:root "G" :tonality "Major"} standard-tuning)
        open-d-chords (positions-for-chord {:root "D" :tonality "Major"} open-g-tuning)
        open-g-chords (positions-for-chord {:root "G" :tonality "Major"} open-g-tuning)
        open-f-sharp-chords (positions-for-chord {:root "F#/Gb" :tonality "Major"} open-g-tuning)]
    (is (some #{[0 2 2 1 0 0]} (map frets-from standard-e-chords)))
    (is (some #{[3 2 0 0 0 3]} (map frets-from standard-g-chords)))
    (is (some #{[3 2 0 0 3 3]} (map frets-from standard-g-chords)))
    (is (some #{[0 2 0 2 3 0]} (map frets-from open-d-chords)))
    (is (some #{[nil 0 0 0 0 0]} (map frets-from open-g-chords)))
    (is (some #{[11 11 11 11 11 11]} (map frets-from open-f-sharp-chords)))
    (is (not-any? #{[0 2 2 1 0 0]} (map frets-from standard-g-chords)))
    (is (not-any? #{[nil 0 0 0 0 0]} (map frets-from standard-g-chords)))))

(deftest generate-test
  (let [results (generate standard-tuning)
        chords (map :name results)
        expected (map name-for-chord the-major-and-minor-chords)]
    (is (= (set expected) (set chords)))))
