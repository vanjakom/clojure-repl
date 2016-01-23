(ns clojure-repl.time.timestamp)

(require 'clj-time.core)
(require 'clj-time.format)
(require 'clj-time.coerce)

(def ^:private formatter (clj-time.format/formatter "yyyy:MM:dd HH:mm:ss"))

(defn timestamp [] (System/currentTimeMillis))

; always use millis timestamp
(defn normalize [timestamp]
	(if (< timestamp 1000000000000)
		(* timestamp 1000)
		timestamp))

(defn to-date [timestamp]
	(clj-time.format/unparse formatter (clj-time.coerce/from-long (normalize timestamp))))

(defn from-date [date]
	(clj-time.coerce/to-long (clj-time.format/parse formatter date)))

