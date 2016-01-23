(ns clojure-repl.metrics)

(defonce metrics-registry
	(new com.codahale.metrics.MetricRegistry))

(def server-port 9000)

(defonce server-handle (ref nil))

(defn counter [name]
	(.counter metrics-registry name))

(defn counter-inc [name]
	(.inc (counter name)))

(defn stop-server []
	(if (some? server-handle)
		(do
			(.stop server-handle)
			(dosync (ref-set server-handle nil)))))

(defn- start-server-internal []
	(let [server (new org.eclipse.jetty.server.Server server-port)]
		(let [servlet-context-handler (new org.eclipse.jetty.servlet.ServletContextHandler server "/metrics", false, false)]
			(.setAttribute 
				(.getServletContext servlet-context-handler) 
				com.codahale.metrics.servlets.MetricsServlet/METRICS_REGISTRY 
				metrics-registry)
			(.addServlet servlet-context-handler com.codahale.metrics.servlets.MetricsServlet "/registry")
			(.addServlet servlet-context-handler com.codahale.metrics.servlets.ThreadDumpServlet "/threads")

			(.start server)
			server)))

(defn start-server []
	(if (some? @server-handle)
		(stop-server))
	(let [new-server-handle (start-server-internal)]
		(dosync (ref-set server-handle new-server-handle))))