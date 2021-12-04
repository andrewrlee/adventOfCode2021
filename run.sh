#!/usr/bin/env bb

(defn exec [part]
  (let [
      day    (first *command-line-args*)
      folder (str "./" day)
      {out :out, err :err}   (select-keys 
               (shell/sh "bash" "-c" (str "cat " folder "/input.txt | " folder "/" part ".sh")) 
               [:out :err])]
      (str (str/trim out) (str/trim err))
   ))

(println "Part 1: " (exec "part1"))
(println "Part 2: " (exec "part2"))
