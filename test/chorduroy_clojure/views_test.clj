(ns chorduroy-clojure.views-test
  (:require [clojure.test :refer :all]
            [chorduroy-clojure.views :refer :all]))

(deftest position-to-chart-test
  (is (= "--0--\n--0--\n--1--\n--2--\n--2--\n--0--" (position-to-chart [0 2 2 1 0 0])))
  (is (= "--3--\n--0--\n--0--\n--0--\n--2--\n--3--" (position-to-chart [3 2 0 0 0 3]))))
