#!/usr/bin/env bb -i

(defn to-grid [idx line]
  (->> 
     line 
     (re-seq #"[\d]")
     (map-indexed #(identity [[idx %] (Integer/parseInt %2)]))))

(defn get-surrounding-points [grid [x y]] 
  (let [deltas             [[0 1] [1 0] [0 -1] [-1 0]]
        surrounding-points (map (fn [[x2 y2]] [(+ x x2) (+ y y2)]) deltas)] 
    (map first (remove (fn [[point value]] (or (nil? value) (= value 9))) (map #(identity [% (grid %)]) surrounding-points))))) 

(defn find-basin [grid seen point]
  (let [surrounding-points  (get-surrounding-points grid point)
        to-process          ( filter (fn [point] (not (contains? seen point))) surrounding-points)
        new-seen            (clojure.set/union #{point} seen (set surrounding-points))
        to-add              (clojure.set/difference (clojure.set/union #{point} (set to-process)) seen)]
    (cond 
      (empty? to-process) to-add
      :else (let [result (mapcat #(find-basin grid new-seen %) to-process)]
             (set (concat result (set to-add)))))))

(defn add-to-basin [grid]
  (fn [{:keys [seen basins]} point]
    (if (contains? seen point) 
      {:seen seen :basins basins }
      (let [basin (find-basin grid seen point)]
         {:seen (clojure.set/union seen basin) :basins (conj basins basin ) }
      ))))

(->> *input*
     (map-indexed to-grid)
     (mapcat identity)
     (#(let [grid (into (sorted-map) %)]
         (->> grid
              (filter (fn [[_ value]] (not= 9 value)))
              (map first)
              (reduce (add-to-basin grid) {:seen #{} :basins []})
              (:basins)
              (map count)
              (sort)
              (take-last 3)
              (apply *)
              ))))
