(ns chorduroy-clojure.generator)
(require '[chorduroy-clojure.core :refer :all])
(require '[chorduroy-clojure.filterer :as filterer])

(defn frets-in-harmony
  [chord open]
  (->> (range 12)
       (filter #(in-harmony? (walk-scale open %) chord))
       (map #(hash-map :open open :fret %))))

(defn positions-for-chord
  [chord tuning]
  (for [sixth (conj (frets-in-harmony chord (get tuning 0)) {:open (get tuning 0) :fret nil})
        fifth (conj (frets-in-harmony chord (get tuning 1)) {:open (get tuning 1) :fret nil})
        fourth (frets-in-harmony chord (get tuning 2))
        third (frets-in-harmony chord (get tuning 3))
        second (frets-in-harmony chord (get tuning 4))
        first (frets-in-harmony chord (get tuning 5))]
    [sixth fifth fourth third second first]))

(defn- generate-row
  [tuning chord]
  (let [name (name-for-chord chord)
        positions (filter (partial filterer/eligible? chord) (positions-for-chord chord tuning))]
    {:name name :positions positions}))

(defn generate
  [tuning]
  (map (partial generate-row tuning) the-major-and-minor-chords))
