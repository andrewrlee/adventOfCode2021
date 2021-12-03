#!/usr/bin/env bb

(defn exec [part]
  (let [
      day    (first *command-line-args*)
      folder (str "./" day)]
    (:out 
      (shell/sh 
        "bash" "-c" (str "cat " folder "/input.txt | " folder "/" part ".sh")))))

(print "Part 1: " (exec "part1"))
(print "Part 2: " (exec "part2"))
