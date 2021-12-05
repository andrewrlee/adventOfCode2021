#!/usr/bin/env bb -i

(def input *input*)

(defn transpose [matrix] (apply map list matrix))

(defn read-numbers [s re] (map #(Integer/parseInt %) (filter (complement empty?) (str/split s re))) )

(def numbers (read-numbers (first input) #","))

(defn read-grid [string-rows] 
  (->> string-rows
       (map #(read-numbers % #"\s+"))
       ( #(identity { :rows % :columns (transpose %)}))))

(def grids 
  (->> input
     (rest)
     (partition-by str/blank?)
     (filter #(not= (count %) 1))
     (map read-grid)))


(defn is-strip-complete? [numbers strip] (every? #(contains? (set numbers) %) strip))

(defn find-match [numbers strips] (some #(is-strip-complete? numbers %) strips))

(defn is-bingo? [numbers grid] (or (find-match numbers (:rows grid)) (find-match numbers (:columns grid))))

(defn play
  ([] 
    (play [] numbers nil))
  ([called next-numbers result] 
    (cond 
      (some? (:grid result)) result
      :else 
        (let 
          [this-number    (first next-numbers)
           called-numbers (conj called this-number)
           winning-grid   (first (filter #(is-bingo? called-numbers %) grids))
           winner         { :grid winning-grid, :called-numbers called-numbers :last-number this-number }]
            (play called-numbers (rest next-numbers) winner)))))

(defn unmarked-numbers [grid called-numbers] 
   (clojure.set/difference (set (flatten (:rows grid))) (set called-numbers)))

(let [{grid :grid last-number :last-number called-numbers :called-numbers} (play)]
    (* last-number (apply + (unmarked-numbers grid called-numbers)))
)

