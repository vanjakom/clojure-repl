(ns clojure-repl.syntax)

(defn todo [comment]
	(println "todo: " comment))

(defmacro def- [item value]
  `(def ^{:private true} ~item ~value))

(defmacro defonce- [item value]
  `(defonce ^{:private true} ~item ~value))

(defn not-nil? [object] (not (nil? object)))