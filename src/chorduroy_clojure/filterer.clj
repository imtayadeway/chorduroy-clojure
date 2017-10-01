(ns chorduroy-clojure.filterer)
(require '[chorduroy-clojure.core :refer :all])

(defn frets-from
  [position]
  (map :fret position))

(defn- well-clustered?
  [frets]
  (let [clusters (partition-by nil? frets)]
    (< (count clusters) 3)))

(defn- reachable?
  [frets]
  (let [fretted (remove #(or (nil? %) (zero? %)) frets)
        max (if (empty? fretted) 0 (apply max fretted))
        min (if (empty? fretted) 0 (apply min fretted))
        fingers (count fretted)
        reach (- max min)]
    (and (< reach 4)
         (< fingers 5))))

(defn playable?
  [position]
  (let [frets (frets-from position)]
    (and (well-clustered? frets)
         (reachable? frets))))

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
