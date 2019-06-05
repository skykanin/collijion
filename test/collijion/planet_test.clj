(ns collijion.planet-test
  (:require [clojure.test :refer :all]
            [collijion.planet :as p])
  (:import [collijion.planet Planet]))

(deftest basic-arithmetic
  (testing "Vector addition"
    (is (= (p/add [20 20] [15 15]) [35 35]))
    (is (= (p/add [-20 5] [10 20]) [-10 25]))
    (is (= (p/add [15 10] [0 -30]) [15 -20])))

  (testing "Vector subtraction"
    (is (= (p/sub [5 5] [5 5]) [0 0]))
    (is (= (p/sub [-20 -20] [-5 -5]) [-15 -15]))
    (is (= (p/sub [-10 -15] [4 4]) [-14 -19])))

  (testing "Vector negation"
    (is (= (p/neg [50 50]) [-50 -50]))
    (is (= (p/neg [-50 -50]) [50 50])))

  (testing "Vector multiplication"
    (is (= (p/mult [20 20] 5) [100 100]))
    (is (= (p/mult [10 10] -5) [-50 -50]))
    (is (= (p/mult [-5 -5] -5) [25 25])))

  (testing "Vector division"
    (is (= (p/div [50 50] 5) [10 10]))
    (is (= (p/div [25 25] -5) [-5 -5]))
    (is (= (p/div [30 30] 0) nil))))

(deftest vector-arithmetic
  (let [a [15 15]
        b [-10 -10]
        c [0 0]
        d [10 10]
        e [-5 -5]]
    (testing "Vector magnitude"
      (is (= (p/magnitude c) 0.0))
      (is (= (p/magnitude d)
             (Math/sqrt (apply * (conj d 2)))))
      (is (= (p/magnitude e)
             (Math/sqrt (apply * (conj e 2))))))
    (testing "Unit vector"
      (is (= (p/unit a)
             (p/div a (p/magnitude a))))
      (is (= (p/unit b) (p/div b (p/magnitude b))))
      (is (= (p/unit c) [0 0])))))

(defn check-inner
  "Checks the length of the inner vectors"
  [l n]
  (apply (partial = n) (map count (p/gen-f-vec l))))

(deftest newtonian-physics
  (let [a (Planet. 1E9 50 500 500 [0 0])
        b (Planet. 10000 5 0 100 [50 0])
        c (Planet. 75000 10 200 350 [30 -30])]
    (testing "Gravitational force between two bodies"
      (is (= (p/grav-vec a b) [-0.001270843037229742 -0.0010166744297837936])))
    (testing "List of force vector generation"
      (is (and (= (count (p/gen-f-vec [a b])) 2)
               (check-inner [a b] 1)))
      (is (and (= (count (p/gen-f-vec [a b c])) 3)
               (check-inner [a b c] 2))))))
