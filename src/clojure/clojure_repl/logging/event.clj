(ns clojure-repl.logging.event)

(require 'clojure-repl.time.timestamp)

(defonce logger (org.slf4j.LoggerFactory/getLogger "event-logger"))

(defn report [event-name event-data]
	(let [timestamp (clojure-repl.time.timestamp/timestamp)]
		(.info 
			logger 
			(str {
				:timestamp timestamp 
				:date (clojure-repl.time.timestamp/to-date timestamp)
				:event event-name
				:data event-data}))))
