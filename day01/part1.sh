#!/usr/bin/env bb -i

(->> 
   *input* 
   (map #(Integer/parseInt %))
   (partition 2 1)
   (filter (fn [[a, b]] (< a b)))
   count
)
