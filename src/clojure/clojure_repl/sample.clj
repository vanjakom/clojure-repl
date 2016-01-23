(ns clojure-repl.sample)

(defn sample-api-handler [request]
	{:body {:status :ok :info "api route"}})


; example of api handler borrowed from photodb-server
(def api-handler
	(compojure.core/routes
		(compojure.core/POST "/api/photo/metadata" _ api-photo-metadata)
		(compojure.core/POST "/api/photo/update" _ api-photo-update)
		(compojure.core/POST "/api/tag/list" _ api-tag-list)
		(compojure.core/POST "/api/tag/explore" _ api-tag-explore)
		(compojure.core/POST "/api/tag/metadata" _ api-tag-metadata)
		(compojure.core/POST "/api/tag/update" _ api-tag-update)))

(defn sample-render-handler [request]
	{:body "this should renwder image"})


