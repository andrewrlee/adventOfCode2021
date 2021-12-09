#!/usr/bin/env bb -i

(defn has-length [n] (fn [s] (= (count s) n)))

(defn contains         [col]   (fn [s] (= (count col) (count (clojure.set/intersection (set s) col)))))
(defn does-not-contain [col]   (fn [s] (not= (count col) (count (clojure.set/intersection (set s) col)))))
(defn has-in-common    [n col] (fn [s] (= n (count (clojure.set/intersection col (set s))))))

(defn find-with [language pred] (first (filter pred language)))

(defn determine-numbers [language]
  (let [
        one   (find-with language (has-length 2))
        four  (find-with language (has-length 4))
        seven (find-with language (has-length 3))
        eight (find-with language (has-length 7))
        
        six-char-length (filter (has-length 6) language)
        six   (find-with six-char-length (does-not-contain one))
        zero  (find-with six-char-length (fn [s] (and ((contains one) s) ((does-not-contain four) s))))
        nine  (find-with six-char-length (contains four))

        five-char-length (filter (has-length 5) language)
        three (find-with five-char-length (contains one))
        two   (find-with five-char-length (fn [s] (and (not ((contains one)s)) ((has-in-common 4 six) s))))
        five  (find-with five-char-length (has-in-common 5 six))
        ]
    { zero 0 one 1 two 2 three 3 four 4 five 5 six 6 seven 7 eight 8 nine 9}))

(defn decode [[language input]]
  (let [codex (determine-numbers (map set language))
        code  (map set input)]  
    (apply str (map #(codex %) code))))

(defn process-line [line]
  (->> 
     line 
     ( #(str/split % #" \| ") )
     (map #(str/split % #"\s+"))
     (decode)
     (#(Integer/parseInt %))))

(->> *input*(map process-line) (reduce +))
