(ns clojure-repl.main
	(:gen-class))

;(require 'clojure-repl.http-server)

; lein uberjar
; java -jar target/clojure-repl-0.1.0-SNAPSHOT-standalone.jar
(defn -main []
	(println "starting clojure-repl main function")
	;(println "starting http server on 8122 with simple handler")
	;(clojure-repl.http-server/start-server 8122 clojure-repl.http-server/simple-handler)
	;(println "simple handler http server alive")
	)