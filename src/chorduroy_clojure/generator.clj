(ns chorduroy-clojure.generator)
(require '[chorduroy-clojure.core :refer :all])
(require '[chorduroy-clojure.filterer :as filterer])
(require '[chorduroy-clojure.position :as position])

(defn generate
  [tuning]
  (let [chords (pmap (fn [position] (if-let [chord (identify position tuning)]
                                      (if (filterer/eligible? chord (map #(assoc {} :open %1 :fret %2) tuning position))
                                        [(name-for-chord chord) position]))) position/playable)]
    (reduce (fn [acc [name position]]
              (if (nil? name)
                acc
                (assoc acc name (conj (get acc name []) position))))
            {}
            chords)))
