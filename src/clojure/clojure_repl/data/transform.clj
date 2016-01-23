(ns clojure-repl.data.transform)

; uses template-map as pattern for extraction from map-to-extract-from
; replaces key, value in map-to-extract-from with one given by processing value from map-to-extract-from
; if value in template is list goes inside taking first element as key and second as template map
; example:
; (extract-from-map-by-template-map  
;	{
;		:a 5 
;		:b 4 
;		:d 7 
;		:e {
;			:ea 1 
;			:ec 4}} 
;	{
;		:a (fn [x] {:key-a x}) 
;		:c (fn [x] {:key-c x}) 
;		:d (fn [x] {:key-d x}) 
;		:e (list :key-e {
;			:ec (fn [x] {:key-ec x})})})
; result:
; {
;	:key-a 5, 
;	:key-d 7, 
;	:key-e {
;		:key-ec 4}}
(defn extract-from-map-by-template-map [map-to-extract-from template-map]
	(letfn [
			(extract-one-level [value-map template]
				(apply
					conj
					(filter
						;(fn [[key value]] (not (nil? value)))
						(fn [x] (not (nil? x)))
						(map 
							(fn [[key value]]
								(if (list? value)
									{(first value) (extract-one-level (get value-map key) (nth value 1))}
									(if (not (nil? (get value-map key)))
										(value (get value-map key))
										nil
										)))
							template))))]
		(extract-one-level map-to-extract-from template-map)))




