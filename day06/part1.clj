#!/usr/bin/env bb -i

(defn grow [n] (if (= n 0) [6 8] [(dec n)])) 


  
(count (last (take 81 (iterate #(mapcat grow %) [3 4 3 1 2]))))

(->> (first *input*)
     ( #(str/split % #",") )
     (map #(Integer/parseInt %)) 
     (iterate #(mapcat grow %))
     (take 81) 
     (last)
     (count)    
)
