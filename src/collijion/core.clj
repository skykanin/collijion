(ns collijion.core
  (:require [cljfx.api :as fx]))

(def renderer
  (fx/create-renderer))

(defn root [{:keys [x]}]
  {:fx/type :stage
   :showing true
   :title "some example"
   :width 500
   :height 500
   :scene {:fx/type :scene
           :root {:fx/type :group
                  :children [{:fx/type :circle
                              :center-x x
                              :center-y 200
                              :radius 50
                              :fill :green}]}}})
                                         
(defn -main [& args]
  (loop [x 50]
    (if (= x 450)
     (renderer {:fx/type root
                :x x})
     (do
       (renderer {:fx/type root
                  :x x})
       (Thread/sleep 5)
       (recur (inc x))))))
