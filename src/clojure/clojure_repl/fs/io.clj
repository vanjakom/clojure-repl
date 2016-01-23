(ns clojure-repl.fs.io)

(defn read-file-to-byte-array [path]
	(org.apache.commons.io.IOUtils/toByteArray
		(new java.io.FileInputStream (new java.io.File path))))

(defn write-file-from-byte-array [byte-array path]
	(let [	output-stream (new java.io.FileOutputStream path)]
		(.write output-stream byte-array)
		(.close output-stream)))

(defn exists [path]
	(let [ file (new java.io.File path)]
		(.exists file)))