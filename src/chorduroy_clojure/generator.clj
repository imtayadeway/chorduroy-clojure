(ns chorduroy-clojure.generator)

(def the-major-and-minor-chords
  (set (for [pitch ["A" "A#/Bb" "B" "C" "C#/Db" "D" "D#/Eb" "E" "F" "F#/Gb" "G" "G#/Ab"]
             tonality ["Major" "Minor"]]
         {:pitch pitch :tonality tonality} )))

(defn name-for-chord
  [chord]
  (let [{:keys [pitch tonality]} chord]
    (str pitch " " tonality)))

(defn positions-for-chord
  [chord tuning]
  (case (:pitch chord)
    "E" [[0 2 2 1 0 0]]
    "G" (if (= "E" (get tuning 0))
          [[3 2 0 0 0 3]]
          [[nil 0 0 0 0 0]])
    "D" [[0 2 0 2 3 0]]))

(defn generate
  [tuning]
  (map (fn [chord] {:name (name-for-chord chord) :positions (positions-for-chord chord tuning)})
       the-major-and-minor-chords))
