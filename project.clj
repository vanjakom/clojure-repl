(defproject com.mungolab/clojure-repl "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :source-paths ["src/clojure"]
  :java-source-paths ["src/java"]
  :resource-paths ["src/resource"]

  :aot [clojure-repl.main]
  :main clojure-repl.main
  :dependencies [
  	; core
  	[org.clojure/clojure                        "1.7.0"]
  	[org.clojure/core.async                     "0.2.374"]
  	
  	; http server
  	[ring                                       "1.4.0"]
  	[compojure                                  "1.4.0"]
    [ring/ring-json                             "0.4.0"]
    [ring.middleware.jsonp                      "0.1.6"]
    [enlive                                     "1.1.5"]
    [org.eclipse.jetty/jetty-servlet            "9.3.4.v20151007"]
    [org.eclipse.jetty/jetty-server             "9.3.4.v20151007"]

    ; http client
    [clj-http                                   "1.1.2"]

    ; db
    [com.novemberain/monger                     "1.7.0-beta1"]
    
    ; data transform
    [org.clojure/data.json                      "0.2.5"]
    [clj-time                                   "0.11.0"]

    ; metrics
    [io.dropwizard.metrics/metrics-core         "3.1.2"]
    [io.dropwizard.metrics/metrics-servlets     "3.1.2"]

    ; http


    ; logging
    [org.slf4j/slf4j-log4j12                    "1.7.7"]
    [org.slf4j/slf4j-api                        "1.7.7"]

    ; apache commons
    [commons-io/commons-io                      "2.4"]

    ;image manipultion
    [net.coobird/thumbnailator                  "0.4.8"]])
