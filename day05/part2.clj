#!/usr/bin/env bb -i

(defn read-points [s] (->>
   s
   ( #(str/split % #" -> "))
   (map #(str/split % #","))
   (map (fn[[x y]] [(Integer/parseInt x) (Integer/parseInt y)]))))

(defn points-between [[start, end]]
   (let [
     xinc (compare (first end) (first start))
     yinc (compare (second end) (second start))]
   (->>
     (iterate (fn [[x y]] [(+ xinc x) (+ y yinc)]) start)
     (take-while #(not= % end))
     ( #(concat % [end]) ))))

(->> *input*
  (map read-points)
  (mapcat points-between)
  (frequencies)
  (filter #(> (second %) 1))
   (count))
