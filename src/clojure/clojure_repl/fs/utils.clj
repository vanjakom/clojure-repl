(ns clojure-repl.fs.utils)

; notes
; path doesn't finish with /, example
; /tmp
; /tmp/file1

; lists given path and returns full subpaths
(defn list-path [path]
	(let [	file (new java.io.File path)]
		(let [	files (.list file)]
			(map 
				(fn [file] (.replace (str path "/" file) "//" "/" )) 
				files))))

(defn make-path [root-path filename]
	(str root-path "/" filename))

; returns name of path
(defn filename [path]
	(let [	last-index-of-separator (.lastIndexOf path "/")]
		(.substring path (inc last-index-of-separator))))

(defn filename-without-extension [path]
	(let [	full-name (filename path)
			last-index-of-dot (.lastIndexOf full-name ".")]
		(.substring full-name 0 last-index-of-dot)))

(defn extension [path]
	(let [	full-name (filename path)
			last-index-of-dot (.lastIndexOf full-name ".")]
		(.substring full-name (inc last-index-of-dot))))

; filter paths by extensions
; extensions - list of allowed extensions
(defn filter-by-extension [paths allowed-extensions]
	(let [	extensions (set allowed-extensions)]
		(filter 
			(fn [path] 
				(let [	last-index-of-dot (.lastIndexOf path ".")
						extension (.substring path (inc last-index-of-dot))]
					;(println extension)
					;(println last-index-of-dot)
					(not (nil? (get extensions extension)))))
			paths)))

(defn filter-images-only [paths]
	(filter-by-extension paths '("JPG" "jpg")))