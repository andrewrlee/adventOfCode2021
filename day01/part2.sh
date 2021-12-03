#!/usr/bin/env bb -i

(->>
   *input*
   (map #(Integer/parseInt %))
   (partition 3 1) 
   (map #(reduce + %))
   (partition 2 1) 
   (filter (fn [[a, b]] (< a b)))
   count
)
