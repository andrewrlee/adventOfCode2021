#!/usr/bin/env bb -i

(defn distance [i crab] (Math/abs (- i crab)))

(defn distances [i crabs] (map (fn [[crab n]] (* n (distance i crab))) crabs))

(defn total-fuel [i crabs] (apply + (distances i crabs)))

(->> (first *input*)
     ( #(str/split % #",") )
     (map #(Integer/parseInt %))
     (#(let [crabs         (frequencies %)
             high          (apply max %)
             all-fuel      (map #(total-fuel % crabs) (range high))]
         (apply min all-fuel)
     )))
