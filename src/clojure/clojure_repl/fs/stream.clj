(ns clojure-repl.fs.stream)

(defn byte-array-to-stream [byte-array]
	(new java.io.ByteArrayInputStream byte-array))