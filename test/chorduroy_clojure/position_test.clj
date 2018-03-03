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
