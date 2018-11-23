(ns chorduroy-clojure.position-test
  (:require [clojure.test :refer :all]
            [chorduroy-clojure.position :refer :all]))

(deftest playable?-test
  (is (playable? [0 2 2 1 0 0]))
  (is (not (playable? [0 2 nil 1 0 0])))
  (is (playable? [nil nil 0 2 3 2]))
  (is (not (playable? [1 2 3 4 5 6])))
  (is (not (playable? [1 2 3 1 2 3])))
  (is (playable? [nil nil nil nil nil nil]))
  (is (playable? [0 7 6 7 7 0]))
  (is (playable? [1 3 3 2 1 1])))

(deftest to-chart-test
  (let [e-chord [0 2 2 1 0 0]
        g-chord [3 2 0 0 0 3]
        d-chord [nil nil 0 2 3 2]]
    (is (= "--0--\n--0--\n--1--\n--2--\n--2--\n--0--" (to-chart e-chord)))
    (is (= "--3--\n--0--\n--0--\n--0--\n--2--\n--3--" (to-chart g-chord)))
    (is (= "--2--\n--3--\n--2--\n--0--\n--x--\n--x--" (to-chart d-chord)))))

(deftest sort-keyfn-test
  (is (= [[0 2 2 1 0 0]
          [3 2 0 0 0 3]
          [nil 0 2 2 2 0]
          [nil nil 0 2 3 2]]
         (sort-by sort-keyfn
                  [[nil 0 2 2 2 0]
                   [3 2 0 0 0 3]
                   [nil nil 0 2 3 2]
                   [0 2 2 1 0 0]]))))
