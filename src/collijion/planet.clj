(ns collijion.planet
  (:import [clojure.lang PersistentVector]))

(def G 6.674E-11)

(defrecord Planet [m r x y ^PersistentVector v])

(defn add
  "Adds a vector to another"
  [a b]
  (mapv + a b))

(defn sub
  "Subtracts a vector from another"
  [a b]
  (mapv - a b))

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
  [p1 p2]
  (let [dist (distance p2 p1)]
    (mult
     (div (sub (:v p2) (:v p1)) dist)
     (* (- G)
       (/ (* (:m p1) (:m p2))
          (Math/pow dist 2))))))
