#!/usr/bin/env bb -i

(defn has-length       [n]     (fn [s] (= (count s) n)))
(defn contains         [col]   (fn [s] (= (count col) (count (clojure.set/intersection (set s) col)))))
(defn does-not-contain [col]   (fn [s] (not ((contains col) s))))
(defn has-in-common    [n col] (fn [s] (= n (count (clojure.set/intersection col (set s))))))

(defn find-in [language pred] (first (filter pred language)))

(defn determine-numbers [language]
  (let [
        one   (find-in language (has-length 2))
        four  (find-in language (has-length 4))
        seven (find-in language (has-length 3))
        eight (find-in language (has-length 7))
        
        six-char-long (filter (has-length 6) language)
        six   (find-in six-char-long (does-not-contain one))
        zero  (find-in six-char-long #(and ((contains one) %) ((does-not-contain four) %)))
        nine  (find-in six-char-long (contains four))

        five-char-long (filter (has-length 5) language)
        three (find-in five-char-long (contains one))
        two   (find-in five-char-long #(and ((does-not-contain one) %) ((has-in-common 4 six) %)))
        five  (find-in five-char-long (has-in-common 5 six))]
    { zero 0 one 1 two 2 three 3 four 4 five 5 six 6 seven 7 eight 8 nine 9}))

(defn decode [[language input]]
  (let [codex (determine-numbers (map set language))
        code  (map set input)]  
    (->> code (map #(codex %)) (apply str) ( #(Integer/parseInt %) ))))  

(defn process-line [line]
  (->> 
     line
     ( #(str/split % #" \| ") )
     (map #(str/split % #"\s+"))
     (decode)))

(->> *input* (map process-line) (reduce +))
