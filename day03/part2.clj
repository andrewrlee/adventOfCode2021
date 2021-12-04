#!/usr/bin/env bb -i

(defn read-binary-to-digits [s] (map #(Character/digit % 10) (seq (char-array s))))

(defn read-as-int [col] (Integer/parseInt (str/join col) 2)) 

(def oxygen       [> 1])
(def co2-scrubber [< 0])

(defn choose [[pred default-on-tie] { zeros 0, ones 1 :as zeros-and-ones}]  
  (cond 
    (= (count zeros)   (count ones)) (zeros-and-ones default-on-tie)
    (pred              (count zeros) (count ones)) zeros
    ((complement pred) (count zeros) (count ones)) ones))

(defn narrow-by-criteria [mode items pos]
  (->> items
       (group-by #(nth % pos))
       (choose mode)))  

(defn find
  ([mode items] (find mode 0 items)) 
  ([mode pos items] 
    (cond
      (= 1 (count items)) (read-as-int (first items))
      :else (find mode (inc pos) (narrow-by-criteria mode items pos)))))

(->> 
  *input*
  (map #(read-binary-to-digits %)) 
  ( #(* (find oxygen %) (find co2-scrubber %)) )
