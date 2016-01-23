(ns clojure-repl.playground.core-async)

(use 'clojure.core.async)

(alter-var-root #'*out* (constantly *out*))

(def channel (chan))

(thread (println (.getName (Thread/currentThread))))

(thread (while true (let [value (<!! channel)] (println "value: " value))))

(go (>! channel "2"))

