#!/usr/bin/env bb -i

(defn to-grid [idx line]
  (->> 
     line 
     (re-seq #"[\d]")
     (map-indexed #(identity [[idx %] (Integer/parseInt %2)]))))

(defn get-surrounding-values [grid [x y]] 
  (let [deltas             [[0 1] [1 0] [0 -1] [-1 0]]
        surrounding-points (map (fn [[x2 y2]] [(+ x x2) (+ y y2)]) deltas)] 
    (remove nil? (map grid surrounding-points)))) 

(defn is-low-point? [grid [point value]] 
  (let [surrounding-values (get-surrounding-values grid point)]
    (comment println value " ! " surrounding-values " ! " (empty? (filter #(>= value %) surrounding-values)))
    (empty? (filter #(>= value %) surrounding-values))))

(->> *input*
     (map-indexed to-grid)
     (mapcat identity)
     (#(let [grid (into (sorted-map) %)]
         (->> grid
              (filter #(is-low-point? grid %))
              (map second)
              (map inc)
              (reduce +)))))
