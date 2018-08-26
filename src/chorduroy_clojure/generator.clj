(ns chorduroy-clojure.generator)
(require '[chorduroy-clojure.core :refer :all])
(require '[chorduroy-clojure.filterer :as filterer])

(defn generate
  [tuning]
  (reduce (fn [result position]
            (if-let [chord (identify position tuning)]
              (if (filterer/eligible? chord (map #(assoc {} :open %1 :fret %2) tuning position))
                (let [name (name-for-chord chord)]
                  (assoc result name (conj (get result name []) position)))
                result)
              result))
          {}
          all-playable-positions))
