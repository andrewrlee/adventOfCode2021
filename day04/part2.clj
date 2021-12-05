#!/usr/bin/env bb -i

(def input *input*)

(defn transpose [matrix] (apply map list matrix))

(defn read-numbers [s re] (map #(Integer/parseInt %) (filter (complement empty?) (str/split s re))) )

(def numbers (read-numbers (first input) #","))

(defn read-grid [string-rows] 
  (->> string-rows
       (map #(read-numbers % #"\s+"))))

(def grids 
  (->> input
     (rest)
     (partition-by str/blank?)
     (filter #(not= (count %) 1))
     (map read-grid)))

(defn find-match [numbers rows] 
  (letfn [(is-complete? [numbers row] (every? #(contains? (set numbers) %) row))]
    (some #(is-complete? numbers %) rows)))

(defn is-bingo? [numbers grid] 
  (if (or (find-match numbers grid) (find-match numbers (transpose grid))) :winners :still-in-play))

(defn unmarked-numbers [grid called-numbers]
   (clojure.set/difference (set (flatten grid)) (set called-numbers)))

(defn bingo-score [grid called-numbers this-number] 
  (let [unmarked-numbers (clojure.set/difference (set (flatten grid)) (set called-numbers))] 
    (* this-number (apply + unmarked-numbers))))
  
(defn play
  ([] (play grids [] numbers []))
  ([remaining-grids called [this-number & remaining-numbers] results]
   (cond
     (empty? remaining-grids) results
     :else 
     (let [called-numbers                   (conj called this-number)
           {:keys [winners still-in-play]}  (group-by #(is-bingo? called-numbers %) remaining-grids)
           these-winners                    (map #(bingo-score % called-numbers this-number) winners)]
       (play still-in-play called-numbers remaining-numbers (concat results these-winners))))))

(last (play))

