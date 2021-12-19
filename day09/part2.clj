#!/usr/bin/env bb -i

(defn to-grid [idx line]
  (->> 
     line 
     (re-seq #"[\d]")
     (map-indexed #(identity [[idx %] (Integer/parseInt %2)]))))

(defn get-surrounding-points [grid seen [x y]]
  (let [deltas             [[0 1] [1 0] [0 -1] [-1 0]]
        surrounding-points (map (fn [[x2 y2]] [(+ x x2) (+ y y2)]) deltas)]
    (->> surrounding-points
         (map (fn [point] [point (get-in grid [(first point) point] nil)]))
         (remove (fn [[point value]] (or (nil? value) (= value 9) (contains? seen point))))
         (map first))))

(defn find-basin [grid seen point]
  (let [to-process          (get-surrounding-points grid seen point)
        to-add              (clojure.set/difference (clojure.set/union #{point} (set to-process)) seen)]
    (cond 
      (empty? to-process) to-add
      :else (let [new-seen   (clojure.set/union #{point} (set to-process) seen)
                  result     (mapcat #(find-basin grid new-seen %) to-process)]
             (set (concat result (set to-add)))))))

(defn add-to-basin [grid]
  (fn [{:keys [seen basins]} point]
    (println "aaaa") 
    (if (contains? seen point) 
      {:seen seen :basins basins }
      (let [basin (find-basin grid seen point)]
         (println basin)
         {:seen (clojure.set/union seen basin) :basins (conj basins basin ) }
      ))))

(->> *input*
     (map-indexed to-grid)
     (map #(into (hash-map) %))
     ((fn [vs] { :grid (mapv #(into (hash-map) %) vs) :vs (mapcat identity vs)}))
     ((fn [{ :keys [vs grid]}]
        (->> vs
              (filter (fn [[_ v]] (not= 9 v)))
              (map first)
              (( fn[vs] (reduce (add-to-basin grid) {:seen #{} :basins []} (vec vs))))
              (:basins)
              (map count)
              (sort)
              (take-last 3)
              (apply *)              )
         )))

(comment "This hangs on the real input, given up and moved to kotlin!")