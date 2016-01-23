(ns clojure-repl.image.exif)

(require '[clojure-repl.data.transform :as data-tranform])

(require 'clj-time.core)
(require 'clj-time.format)
(require 'clj-time.coerce)

(declare exif-extraction-template)
(declare collect-all-tags)

(defn extract-photo-exif [original-photo-bytes]
	(let [	input-stream (new java.io.ByteArrayInputStream original-photo-bytes)
			image-metadata (com.drew.imaging.ImageMetadataReader/readMetadata input-stream)
			all-tags (collect-all-tags image-metadata)]
		; if needed flatten other directories
		(:exif (data-tranform/extract-from-map-by-template-map all-tags exif-extraction-template))))

(def ^:private exif-formatter (clj-time.format/formatter "yyyy:MM:dd HH:mm:ss"))

(defn exif-date-to-timestamp [exif-date]
	(clj-time.coerce/to-long (clj-time.format/parse exif-formatter exif-date)))

(def ^:private exif-extraction-template {
	:ExifIFD0Directory 
		(list :exif {
			(keyword "0x0112")
			(fn [value] {:orientation value})

			(keyword "0x010f")
			(fn [value] {:camera-make value})

			(keyword "0x0110")
			(fn [value] {:camera-model value}) 

			(keyword "0x0132")
			(fn [value] {:date value})})})

(defn- collect-all-tags [image-metadata]
	(apply
		conj
		(filter 
			(fn [collected-directory]
				(> (count collected-directory) 0))
			(map 
				(fn [directory]
					{
						(keyword (str (.getSimpleName (.getClass directory))))
						(apply 
							conj 
							(map
								(fn [tag]
									{
										(keyword (str (.getTagTypeHex tag)))
										(.getObject directory (.getTagType tag))})
								(.getTags directory) 
								))})					
				(.getDirectories image-metadata)))))

