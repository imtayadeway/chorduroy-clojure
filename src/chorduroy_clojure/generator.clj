(ns chorduroy-clojure.generator)

(def standard-tuning ["E" "A" "D" "G" "B" "E"])
(def the-major-and-minor-chords
  (set (for [pitch ["A" "A#/Bb" "B" "C" "C#/Db" "D" "D#/Eb" "E" "F" "F#/Gb" "G" "G#/Ab"]
             harmony ["major" "minor"]]
         (str pitch " " harmony))))

(defn generate-for-chord
  [chord tuning]
  [[0 2 2 1 0 0] [3 2 0 0 0 3]])

(defn generate
  [strings]
  (map (fn [chord] {:name chord :charts ["--0--\n--0--\n--0--\n--0--\n--0--\n--0--"]})
       the-major-and-minor-chords))
