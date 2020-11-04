(ns chorduroy-clojure.position)

(defn- fretted?
  [fret]
  (and (not (nil? fret))
       (not (zero? fret))))

(defn- min-max-fret
  [position]
  (let [fretted (filter fretted? position)]
    (if (empty? fretted)
      [0 0]
      [(apply min fretted) (apply max fretted)])))

(defn- count-fingers
  [position]
  (let [fretted (filter fretted? position)
        [min max] (min-max-fret position)]
    (if (some #{0} position)
      (count fretted)
      (inc (count (remove #(= min %) fretted))))))

(defn- reachable?
  [position]
  (let [[min max] (min-max-fret position)
        reach (- max min)]
    (and (< reach 4)
         (< (count-fingers position) 5))))

(defn playable?
  [position]
  (reachable? position))

(defn- generate-playable []
  (set (filter playable? (for [sixth (cons nil (range 15))
                               fifth (cons nil (range 15))
                               fourth (cons nil (range 15))
                               third (cons nil (range 15))
                               second (cons nil (range 15))
                               first (cons nil (range 15))]
                           [sixth fifth fourth third second first]))))

(def db "db/playable.txt")

(defn dump-playable []
  (spit
   db
   (with-out-str (binding [*print-dup* true] (pr (generate-playable))))))

(defn load-playable []
  (with-in-str (slurp db) (read)))

(def playable (load-playable))

(defn to-chart
  [position]
  (->> position
       (map #(str "--" (or % "x") "--"))
       reverse
       (interpose "\n")
       (apply str)))

(defn sort-keyfn
  [position]
  (let [muted-score 16] ;; muted scores high
    (reduce #(+ %2 (or %1 muted-score) 0 position))))
