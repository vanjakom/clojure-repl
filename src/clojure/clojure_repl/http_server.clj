(ns clojure-repl.http-server)

(require 'ring.adapter.jetty)
(require 'ring.middleware.params)
(require 'ring.middleware.resource)
(require 'ring.middleware.file)
(require 'ring.middleware.json)
(require 'ring.middleware.keyword-params)

(require 'compojure.core)

(defn api-response-ok [body]
	{
		:status 200
		:headers {"Content-Type" "application/json"}
		:body body })

(defn api-response-fail [& [body]]
	(if body
		{
			:status 500
			:headers {"Content-Type" "application/json"}
			:body body}
		{
			:status 500}))

(defn render-response-ok [content-type body]
	{
		:status 200
		:header {"Content-Type" content-type}
		:body body})

(defn render-response-fail []
	{:status 500})

; application routes
; / -> GET, static file, index.html
; /web/ -> GET, static file
; /api/ -> POST, json -> json
; /render/ -> ANY, byte[]

; send handler as var to enable change of handler in runtime
(defn start-server [port api-handler render-handler]
	(ring.adapter.jetty/run-jetty 
		(compojure.core/routes 
			(compojure.core/GET
				"/"
				_
				(fn [request]
					(let [new-request (assoc request :path-info "/web/index.html")]
						(ring.middleware.resource/resource-request new-request ""))))
			(compojure.core/GET
				"/status"
				_
				(fn [request]
					{:status 200 :body "Status: OK\n"}))
			(compojure.core/GET
				"/web/*"
				_
				(fn [request]
					(ring.middleware.resource/resource-request request "")))
			(compojure.core/POST
				"/api/*"
				_
				(ring.middleware.json/wrap-json-response 
					(ring.middleware.json/wrap-json-params 
						(ring.middleware.keyword-params/wrap-keyword-params api-handler))))
			(compojure.core/ANY
				"/render/*"
				_
				(ring.middleware.params/wrap-params 
					(ring.middleware.keyword-params/wrap-keyword-params render-handler))))
		{:port port :join? false}))

(defn stop-server [server-handle]
	(.stop server-handle))

