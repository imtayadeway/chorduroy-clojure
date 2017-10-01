(ns chorduroy-clojure.filterer)
(require '[chorduroy-clojure.core :refer :all])

(defn frets-from
  [position]
  (map :fret position))

(defn playable?
  [position]
  (let [frets (frets-from position)
        clusters (partition-by nil? frets)
        fretted (remove #(or (nil? %) (zero? %)) frets)
        max (if (empty? fretted) 0 (apply max fretted))
        min (if (empty? fretted) 0 (apply min fretted))
        reach (- max min)]
    (and (< (count clusters) 3)
         (< reach 4))))

(defn get-position-notes
  [position]
  (->> position
       (remove #(nil? (:fret %)))
       (map #(walk-scale (:open %) (:fret %)))
       set))

(defn sufficient?
  [chord position]
  (let [chord-notes (harmonize chord)
        position-notes (get-position-notes position)]
    (every? position-notes chord-notes)))


(defn root-position?
  [chord position]
  (let [{root :root} chord
        base (->> position
                  (remove #(nil? (:fret %)))
                  first
                  (#(walk-scale (:open %) (:fret %))))]
    (= root base)))

(defn eligible?
  [chord position]
  (and (playable? position)
       (sufficient? chord position)
       (root-position? chord position)))
