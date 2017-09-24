(ns chorduroy-clojure.generator)

(def the-major-and-minor-chords
  (set (for [pitch ["A" "A#/Bb" "B" "C" "C#/Db" "D" "D#/Eb" "E" "F" "F#/Gb" "G" "G#/Ab"]
             tonality ["Major" "Minor"]]
         {:pitch pitch :tonality tonality} )))

(defn name-for-chord
  [chord]
  (let [{:keys [pitch tonality]} chord]
    (str pitch " " tonality)))

(defn in-harmony?
  [open fret chord]
  true)

(defn positions-for-chord
  [chord tuning]
  [(map (fn first-fret-in-harmony
          ([open]
           (first-fret-in-harmony open 0))
          ([open fret]
           (if (in-harmony? open fret chord)
             fret
             (recur open (inc fret))))) tuning)])

(defn generate
  [tuning]
  (map (fn [chord] {:name (name-for-chord chord) :positions (positions-for-chord chord tuning)})
       the-major-and-minor-chords))
