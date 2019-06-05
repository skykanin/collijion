(ns collijion.core
  (:require [cljfx.api :as fx]
            [collijion.planet])
  (:import [collijion.planet Planet]))

(def renderer
  (fx/create-renderer))

(def timestep 50)

(defn root [{:keys [planets]}]
  {:fx/type :stage
   :showing true
   :title "gravity"
   :width 800
   :height 800
   :scene {:fx/type :scene
           :root {:fx/type :group
                  :children (into []
                                  (flatten
                                   (for [{:keys [m r x y v]} planets]
                                     [{:fx/type :circle
                                       :center-x x
                                       :center-y y
                                       :radius r
                                       :fill (if (> m 1000) :black :brown)}
                                      {:fx/type :line
                                       :start-x x
                                       :start-y y
                                       :end-x (+ x (* (first v) r))
                                       :end-y (+ y (* (last v) r))
                                       :stroke :red
                                       :stroke-width 1.5}])))}}})

(defn -main [& args]
  "Entrypoint for the program"
  (loop [planets [(Planet. 6E13 30 400 400 [0 0])
                  (Planet. 1500 5 700 50 [-5 10])
                  (Planet. 1000 5 100 750 [2.5 -5])
                  (Planet. 500 10 700 400 [-5 -3])]]
    (renderer {:fx/type root :planets planets})
    (Thread/sleep timestep)
    (recur (collijion.planet/update-planets planets))))
