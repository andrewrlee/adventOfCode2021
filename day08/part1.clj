#!/usr/bin/env bb -i

(def included #{2 3 4 7})

(defn process-line [line]
  (->> 
     line 
     ( #(str/split % #"\|") )
     ( #(drop 1 %) )
     (first)
     (str/trim)
     ( #(str/split % #"\s+") )
     (map count)
     (filter #(contains? included %))
     ))

(->> *input*
     (map process-line)
     (flatten)
     (count)
     )
