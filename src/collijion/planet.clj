(ns collijion.planet
  (:import [clojure.lang PersistentVector]))

#_(def G 6.67264E-11)
(def G 7E-5)

(defrecord Planet [m r x y ^PersistentVector v])

(defn add
  "Adds together an arbitrary amount of vectors"
  [& colls]
  (reduce (partial mapv +) colls))

(defn sub
  "Subtracts an arbitrary amount of vectors"
  [& colls]
  (reduce (partial mapv -) colls))

(defn mult
  "Multiplies a vector by a number"
  [v n]
  (mapv #(* % n) v))

(defn div
  "Divides a vector by a number"
  [v n]
  (when-not (= n 0)
   (mapv #(/ % n) v)))

(defn distance
  "Calculates the Euclidean distance between
  two points"
  [p1 p2]
  (Math/sqrt
   (+ (Math/pow (- (:x p1) (:x p2)) 2)
      (Math/pow (- (:y p1) (:y p2)) 2))))

(defn magnitude
  "Calculates the magnitude of a vector"
  [[x y]]
  (Math/sqrt (+ (Math/pow x 2) (Math/pow y 2))))

(defn unit
  "Calculates the unit vector of a vector"
  [v]
  (let [m (magnitude v)]
    (if (= m 0)
      [0 0]
      (div v m))))

(defn grav-vec
  "Calculates the attraction force between two
  bodies using Newton's law of universal gravitation.
  Force applied on object 2 exerted by object 1."
  [{x1 :x y1 :y :as p1} {x2 :x y2 :y :as p2}]
  (let [dist (distance p2 p1)
        unit (div (sub [x2 y2] [x1 y1]) dist)]
    (mult
     unit
     (* (- G)
       (/ (* (:m p1) (:m p2))
          (Math/pow dist 2))))))

(defn gen-f-vec
  "Calculate all force vectors for all objects in
  a vector"
  [planets]
  (partition (dec (count planets))
   (for [p1 planets p2 planets
         :when (not= p1 p2)]
     (grav-vec p2 p1)))) ;check this application

(defn apply-forces
  "Apply all the force vectors to the planets"
  [planets forces]
  (let [sum-forces (fn [old fl] (reduce add old fl))
        zip (fn [planet force-list]
              (update planet :v sum-forces force-list))]
   (map zip planets forces)))

(defn apply-speed
  "Applies the new velocity to the planets"
  [planets]
  (let [move (fn [{[vx vy] :v :as p}]
               (-> p
                (update :x #(+ % vx))
                (update :y #(+ % vy))))]
    (map move planets)))

(defn update-planets
  "Update all planets velocity vectors
  and coordinates"
  [planets]
  (apply-speed (apply-forces planets (gen-f-vec planets))))
