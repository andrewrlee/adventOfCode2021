#!/usr/bin/env bb -i

(def initial-state { :hp 0 :depth: 0 :aim 0 })

(def directions { 
   "forward" (fn [mag state] (merge-with + state { :hp mag :depth (* mag (:aim state)) })) 
   "down"    (fn [mag state] (update-in state [:aim] + mag)) 
   "up"      (fn [mag state] (update-in state [:aim] - mag)) 
}) 

(->> 
   *input*
   (map #(str/split % #"\s+"))	
   (map (fn [[dir mag]] (partial (directions dir) (Integer/parseInt mag))))
   ((fn [operations] ((apply comp (reverse operations)) initial-state)))
   ((fn [{ :keys [depth hp]}] (* depth hp)))
)
