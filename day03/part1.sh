#!/usr/bin/env bb -i

(defn read-binary-to-digits [s]
  " '1010' -> [1 0 1 0] "
  (map #(Character/digit % 10) (seq (char-array s))))

(defn read-as-int [ns pred]
  " [3 1 3 6] #(= n 3) -> 1010 -> 1111110010 "
  (Integer/parseInt (str/join (map #(if (pred %) 1 0) ns)) 2)) 

(defn to-adjusters [ns] 
  " [0 1 0 1] -> [dec inc dec inc]" 
  (let [op-map { 0 dec 1 inc }] (map #(op-map %) (read-binary-to-digits ns))))            

(def input (seq *input*))

(def initial-state (take (count (first input)) (repeatedly (constantly 0))))	

(let [
       adjust               (fn [state, next] (map #(%1 %2) (to-adjusters next) state))
       intermediate-result  (reduce adjust initial-state input)
       most-common          (read-as-int intermediate-result pos-int?)
       least-common         (read-as-int intermediate-result neg-int?)]
         (* most-common least-common))

