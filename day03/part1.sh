#!/usr/bin/env bb -i

initial-state (fn [n] (take n (repeatedly (constantly 0))))
            

(let 
           [read-bin  (fn [s] (map #(Character/digit % 10) (seq (char-array s))))
            op-map { 0 dec 1 inc }
            next-mapping (fn [next] (map #(op-map %) (read-bin next)))
            initial-state [0 0 0 0 0 0 0 0 0 0 0 0]
            apply-next-mapping (fn [state, next] (map (fn [a b] (a b)) (next-mapping next) state))
            readValue (fn [input pred]  (Integer/parseInt (str/join (map #(if (pred %) 1 0) input)) 2))
            intermediate-result (reduce #(apply-next-mapping %1 %2) initial-state *input*)
            most-common (readValue intermediate-result pos-int?)
            least-common (readValue intermediate-result neg-int?)  ]
              (* most-common least-common)
        )

