#!/usr/bin/env bb -i

(require '[clojure.string :as str])

(def directions { 
   "forward" [0 +] 
   "down"    [1 +] 
   "up"      [1 -] 
}) 

(->> 
   *input*
   (map #(str/split % #"\s+"))	
   (map (fn [[dir mag]] (conj (directions dir) (Integer/parseInt mag))))
   (reduce #(apply update %1 %2) [0 0])
   (apply *)
)
