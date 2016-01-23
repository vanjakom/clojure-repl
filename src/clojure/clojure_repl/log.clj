(ns clojure-repl.log)

; not itendend for including and usage, more like history 

(require 'compojure.core)
(require 'ring.middleware.params)
(require 'ring.middleware.resource)
(require 'ring.middleware.file)
(require 'ring.middleware.keyword-params)

(require 'ring.middleware.json)
(require 'compojure.core)

(def handler (ring.middleware.json/wrap-json-params (ring.middleware.keyword-params/wrap-keyword-params (fn [request] (clojure.pprint/pprint request) {:body "ok"}))))
(def body (new java.io.ByteArrayInputStream (.getBytes "{\"tags\":\"about\",\"name\":\"patike velicina\",\"body\":\"45\"}")))
(handler {:request-method :post :uri "/api/get" :headers  {"content-type" "application/json"} :body body})

(def routing 
	(ring.middleware.json/wrap-json-response (compojure.core/routes
		(compojure.core/GET 
			"/api/*" 
			_ 
			(fn [request] 
				(compojure.core/routes 
					(compojure.core/GET
						"/api/get"
						_
						{:body {:status "ok"}})))))))
(def routing1 
	(compojure.core/routes
		(compojure.core/GET 
			"/api/*" 
			_ 
			(fn [request] 
				(ring.middleware.json/wrap-json-response (compojure.core/routes 
					(compojure.core/GET
						"/api/get"
						_
						{:body {:status "ok"}})))))))

(routing {:request-method :get :uri "/api/get"})
(routing1 {:request-method :get :uri "/api/get"})

(def api-handler
	(compojure.core/routes
		(compojure.core/POST "/api/note/add" _ {:body {:status "ok"}})))

(defn handler [api-handler render-handler]
	(compojure.core/routes 
			(compojure.core/GET
				"/"
				_
				(fn [request]
					(let [new-request (assoc request :path-info "/web/index.html")]
						(ring.middleware.resource/resource-request new-request ""))))
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
						api-handler {:keywords? true})))
			(compojure.core/ANY
				"/render/*"
				_
				render-handler)))
(def handler-fn (handler api-handler api-handler))
(handler-fn {:request-method :post :uri "/api/note/add" :body "{}"})


 (clojure-repl.http-server/start-server 9999 clojure-repl.sample/sample-api-handler clojure-repl.sample/sample-render-handler)

(def simple-handler 
	(compojure.core/routes 
		(compojure.core/GET 
			"/" 
			_ 
			(fn [request] (println "handling /"))) 
		(compojure.core/GET 
			"/test" 
			_ 
			(ring.middleware.params/wrap-params 
				(fn [request] (clojure.pprint/pprint request) 
					(println "handling /test, user: " (get (get request :params) "user")))))))

(def simple-handler 
	(compojure.core/routes 
		(compojure.core/GET 
			"/" 
			_ 
			(fn [request] {:body "Welcome to app"}))
		(compojure.core/GET
			"/web/*"
			_
			(fn [request]
				(let [new-request (assoc request :path-info (.replace (:uri request) "/web" ""))]
					(clojure.pprint/pprint new-request)
					(
						(ring.middleware.resource/wrap-resource
							(fn [request]
								(println "trying with file handler")
								(ring.middleware.file/file-request new-request "src/web/")) 
							"web/")
						new-request))))
		(compojure.core/GET 
			"/test" 
			_ 
			(ring.middleware.params/wrap-params 
				(fn [request] (clojure.pprint/pprint request) 
					(println "handling /test, user: " (get (get request :params) "user")))))))

(require 'clojure.pprint)

; returns server handle


; (static-web-route-builder "/web/" "web/")
(defn static-web-route-builder [root-uri-part root-path-resource]
	(compojure.core/GET
		(str root-uri-part "*")
		_
		(fn [request]
			(let [new-request (assoc request :path-info (.replace (:uri request) root-uri-part "/"))]
				(clojure.pprint/pprint new-request)
				(ring.middleware.resource/resource-request new-request root-path-resource)))))

(defn root-page-route-builder )

(def simple-handler 
	(compojure.core/routes 
		(compojure.core/GET 
			"/" 
			_ 
			(fn [request] {:body "Welcome to app"}))
		(static-web-route-builder "/web/" "web/")
		(compojure.core/GET 
			"/test" 
			_ 
			(ring.middleware.params/wrap-params 
				(fn [request] (clojure.pprint/pprint request) 
					(println "handling /test, user: " (get (get request :params) "user")))))))




