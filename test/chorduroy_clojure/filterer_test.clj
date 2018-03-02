(ns chorduroy-clojure.filterer-test
  (:require [clojure.test :refer :all]
            [chorduroy-clojure.filterer :refer :all]))

(defn build-standard-position
  [frets]
  (let [[sixth fifth fourth third second first] frets]
    [{:open "E" :fret sixth}
     {:open "A" :fret fifth}
     {:open "D" :fret fourth}
     {:open "G" :fret third}
     {:open "B" :fret second}
     {:open "E" :fret first}]))

(def standard-e-position (build-standard-position [0 2 2 1 0 0]))
(def standard-d-position (build-standard-position [nil nil 0 2 3 2]))
(def power-e-position (build-standard-position [0 2 2 nil nil nil]))

(deftest get-position-notes-test
  (let [e-notes (get-position-notes standard-e-position)
        d-notes (get-position-notes standard-d-position)]
    (is (= #{"E" "G#/Ab" "B"} e-notes))
    (is (= #{"D" "F#/Gb" "A"} d-notes))))

(deftest sufficient?-test
  (let [e-chord {:root "E" :tonality "Major"}
        sufficient-position standard-e-position
        insufficient-position power-e-position]
    (is (sufficient? e-chord sufficient-position))
    (is (not (sufficient? e-chord insufficient-position)))))

(deftest root-position?-test
  (let [e-chord {:root "E" :tonality "Major"}
        root-position standard-e-position
        second-inversion (build-standard-position [nil 2 2 1 0 0])]
    (is (root-position? e-chord root-position))
    (is (not (root-position? e-chord second-inversion)))))
