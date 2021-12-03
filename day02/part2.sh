#!/usr/bin/env bb -i

(require '[clojure.string :as str])

(def directions { 
   "forward" (fn [mag state] (merge-with + state { :hp mag :depth (* mag (:aim state)) })) 
   "down"    (fn [mag state] (update-in state [:aim] + mag)) 
   "up"      (fn [mag state] (update-in state [:aim] - mag)) 
}) 

(->> 
   *input*
   (map #(str/split % #"\s+"))	
   (map (fn [[dir mag]] (partial (directions dir) (Integer/parseInt mag))))
   (reduce (fn [acc, f] (f acc) ) { :hp 0 :depth: 0 :aim 0 })
   ( #(* (:depth %) (:hp %)) )
)
