(ns chorduroy-clojure.generator)
(require '[chorduroy-clojure.core :refer :all])

(defn generate
  [tuning]
  (reduce (fn [result position]
            (if-let [chord (identify position tuning)]
              (let [name (name-for-chord chord)]
                (assoc result name (conj (get result name []) position)))

              result))
          {}
          all-playable-positions))
