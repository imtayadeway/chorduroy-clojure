(ns chorduroy-clojure.core)

(def the-chromatic-scale
  ["A" "A#/Bb" "B" "C" "C#/Db" "D" "D#/Eb" "E" "F" "F#/Gb" "G" "G#/Ab"])

(defn walk-scale
  [start degrees]
  (let [start-index (.indexOf the-chromatic-scale start)
        sum (+ start-index degrees)
        index (if (< sum 12) sum (- sum 12))]
    (get the-chromatic-scale index)))

(defn harmonize
  [chord]
  (let [{:keys [root tonality]} chord
        third-degrees (case tonality "Minor" 3 "Major" 4)
        third (walk-scale root third-degrees)
        fifth (walk-scale root 7)]
    #{root third fifth}))
