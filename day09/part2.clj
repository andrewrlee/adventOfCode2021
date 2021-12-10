#!/usr/bin/env bb -i

(defn to-grid [idx line]
  (->> 
     line 
     (re-seq #"[\d]")
     (map-indexed #(identity [[idx %] (Integer/parseInt %2)]))))

(defn get-surrounding-points [grid [x y]] 
  (let [deltas             [[0 1] [1 0] [0 -1] [-1 0]]
        surrounding-points (map (fn [[x2 y2]] [(+ x x2) (+ y y2)]) deltas)] 
    (remove (fn [[point value]] (or (nil? value) (= value 9))) (map #(identity [% (grid %)]) surrounding-points)))) 

(defn find-basin [grid seen [point value]]
  (let [surrounding-points  (get-surrounding-points grid point)
        to-process          (filter (fn [[point]] (not (contains? seen point))) surrounding-points)
        new-seen            (clojure.set/union #{point} seen (set (map first surrounding-points)))]

    (println point to-process "<<")
    (cond 
      (empty? to-process) [[point] [[new-seen]]]
      :else (println (mapcat #(find-basin grid new-seen %) to-process)))))

(defn add-to-basin [grid]
  (fn [{:keys [seen basins]} point]
    (if (contains? seen point) 
      {:seen #{} :basins [] }
      (let [[new-basin new-seen] (find-basin grid seen point)]
        (println point "###########" basins  " ! " seen )
        {:seen #{} :basins [] }
      ))))

(->> *input*
     (map-indexed to-grid)
     (mapcat identity)
     (#(let [grid (into (sorted-map) %)]
         (->> grid
              (filter (fn [[_ value]] (not= 9 value)))
              (reduce (add-to-basin grid) {:seen #{} :basins [] })))))
