(ns clojure-repl.core)

(require 'clojure.stacktrace)

(defn class-for-class-name [class-name]
	(Class/forName class-name))

(defn describe-class-name [clazz]
	(if (nil? clazz)
		"nil"
		(.getSimpleName clazz)))

(defn describe-full-class-name [clazz]
	(if (nil? clazz)
		"nil"
		(.getName clazz)))

; on jdk8 we will be able to extract parameter name
; http://stackoverflow.com/questions/21455403/how-to-get-method-parameter-names-in-java-8-using-reflection
; for now parameter is just class instance
(defn describe-parameter [parameter]
	(describe-class-name parameter))

(defn describe-parameters [method]
	(clojure.string/join 
		", "
		(map 
			describe-parameter
			(.getParameterTypes method))))

(defn describe-method [method]
	(str 
		(.getName method) 
		"(" (describe-parameters method) "): " 
		(describe-class-name (.getReturnType method))))

(defn describe-superclass [clazz]
	(describe-full-class-name (.getSuperclass clazz)))

(defn describe-interfaces [clazz]
	(map 
		(fn [interface] (describe-full-class-name interface))
		(.getInterfaces clazz)))

; clazz as Class or String
(defn print-class [clazz]
	(let [
			clazz-object (if (string? clazz) 
				(class-for-class-name clazz)
				clazz)]
	(println "Class: " (describe-class-name clazz-object))
	(println "Superclass: " (describe-superclass clazz-object))
	(println "Implements: " (clojure.string/join "," (describe-interfaces clazz-object)))
	(println "Methods for class:")
	(doseq [method (.getDeclaredMethods clazz-object)]
		(println "\t" (describe-method method)))))

(defn print-last-exception []
	(clojure.stacktrace/print-stack-trace *e))


