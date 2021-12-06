#!/usr/bin/env bb -i

(def days (vec (take 9 (repeat 0))))

(defn create-day-counts [col] (reduce #(update %1 %2 inc) days col))

(defn day [[zero & rest]]
  (let [new-items (conj (vec rest) zero)]
   (update new-items 6 #(+ zero %))))

(->> (first *input*)
     ( #(str/split % #",") )
     (map #(Integer/parseInt %))
     (create-day-counts) 
     (iterate day)
     (take 81) 
     (last)
     (reduce +))
