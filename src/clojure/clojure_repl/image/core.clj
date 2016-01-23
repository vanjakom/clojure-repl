(ns clojure-repl.image.core)

; Java2D is used for transformations / in mememory image representation
; BufferedImage

; todo
; .createGraphics and .dispose are often used, create with-graphics pattern

(defn ^java.awt.image.BufferedImage create-image-from-bytes [original-photo-bytes]
	(let [	input-stream (new java.io.ByteArrayInputStream original-photo-bytes)	
			image-input-stream (javax.imageio.ImageIO/createImageInputStream input-stream)]
		(javax.imageio.ImageIO/read image-input-stream)))

(defn ^java.awt.image.BufferedImage create-empty-image [width height]
	(new java.awt.image.BufferedImage 
		width 
		height
		java.awt.image.BufferedImage/TYPE_INT_RGB))

(defn write-image-to-stream 
	"Writes given image to stream and closes it"
	[^java.awt.image.BufferedImage buffered-image output-stream]
	(let [	image-writer (.next (javax.imageio.ImageIO/getImageWritersByFormatName "jpeg"))
			image-output-stream (javax.imageio.ImageIO/createImageOutputStream output-stream)]
		; todo change quality 
		(.setOutput image-writer image-output-stream)
		(.write image-writer buffered-image)
		(.close image-output-stream)))

(defn write-image-to-path [^java.awt.image.BufferedImage buffered-image path-to-write]
	(write-image-to-stream buffered-image (new java.io.FileOutputStream path-to-write)))

(defn write-image-to-bytes [^java.awt.image.BufferedImage buffered-image]
	(let [	byte-output-stream (new java.io.ByteArrayOutputStream)]
		(write-image-to-stream buffered-image byte-output-stream)
		(.toByteArray byte-output-stream)))

(defn transform-image 
	"Applies given affine tranform to image and renders result to new image
	with given new-width and new-height"
	[^java.awt.image.BufferedImage image affine-tranform new-width new-height]

	(let [	new-image (create-empty-image new-width new-height)
			graphics (.createGraphics new-image)]
		(.drawImage graphics image affine-tranform nil)
		(.dispose graphics)
		new-image))

; Note: rotate-image-X could be single function if width / height calculation
; is automatized

(defn rotate-image-90 
	"Rotates image clockwise, rotation is given in degrees"
	[^java.awt.image.BufferedImage image]
	(let [	radian-rotation (java.lang.Math/toRadians 90)
			width (.getWidth image)
			height (.getHeight image)
			affine-tranform (new java.awt.geom.AffineTransform)]
		(.translate affine-tranform height 0)
		(.rotate affine-tranform radian-rotation)
		(transform-image image affine-tranform height width)))

(defn rotate-image-180 
	"Rotates image clockwise, rotation is given in degrees"
	[^java.awt.image.BufferedImage image]
	(let [	radian-rotation (java.lang.Math/toRadians 180)
			width (.getWidth image)
			height (.getHeight image)
			affine-tranform (new java.awt.geom.AffineTransform)]
		(.translate affine-tranform width height)
		(.rotate affine-tranform radian-rotation)
		(transform-image image affine-tranform width height)))

(defn rotate-image-270 
	"Rotates image clockwise, rotation is given in degrees"
	[^java.awt.image.BufferedImage image]
	(let [	radian-rotation (java.lang.Math/toRadians 270)
			width (.getWidth image)
			height (.getHeight image)
			affine-tranform (new java.awt.geom.AffineTransform)]
		(.translate affine-tranform 0 width)
		(.rotate affine-tranform radian-rotation)
		(transform-image image affine-tranform height width)))

(defn flip-image-horizontal [^java.awt.image.BufferedImage image]
	(let [	width (.getWidth image)
			height (.getheight image)
			affine-tranform (new java.awt.geom.AffineTransform)]
		(.translate affine-tranform width 0)
		(.scale affine-tranform -1.0 1.0)
		(transform-image image affine-tranform width height)))

(defn flip-image-vertical [^java.awt.image.BufferedImage image]
	(let [	width (.getWidth image)
			height (.getheight image)
			affine-tranform (new java.awt.geom.AffineTransform)]
		(.translate affine-tranform 0 height)
		(.scale affine-tranform 1.0 -1.0)
		(transform-image image affine-tranform width height)))

(defn normalize-image-on-exif-rotation
	"Use exif orientation value ( 1 - 8 ) to rotate image
	https://beradrian.wordpress.com/2008/11/14/rotate-exif-images/
	https://en.wikipedia.org/wiki/Exchangeable_image_file_format "
	[^java.awt.image.BufferedImage image exif-orientation]

	(cond
		(== exif-orientation 1) image	
		(== exif-orientation 2) (flip-image-horizontal image)
		(== exif-orientation 3) (rotate-image-180 image)
		(== exif-orientation 4) (flip-image-vertical image)
		(== exif-orientation 5) (flip-image-horizontal (rotate-image-90 image))
		(== exif-orientation 6) (rotate-image-90 image)
		(== exif-orientation 7) (flip-image-horizontal (rotate-image-270 image))
		(== exif-orientation 8) (rotate-image-270 image)))

(defn create-thumbnail 
	"Creates thumbnail that will fit into max-dimension x max-dimension square"
	[^java.awt.image.BufferedImage image max-dimension]
	(let [	width (.getWidth image)
			height (.getHeight image)
			factor (min (/ max-dimension width) (/ max-dimension height))
			new-width (int (* width factor))
			new-height (int (* height factor))
			empty-image (create-empty-image new-width new-height)
			graphics (.getGraphics empty-image)]
		(.drawImage graphics image 0 0 new-width new-height nil)
		(.dispose graphics)
		empty-image))

(defn create-thumbnail-square [^java.awt.image.BufferedImage image size]
	(let [	empty-image (create-empty-image size size)
			graphics (.getGraphics empty-image)
			width (.getWidth image)
			height (.getHeight image)
			min-dimension (min width height)
			offset-x (int (/ (- width min-dimension) 2))
			offset-y (int (/ (- height min-dimension) 2))]
		(.drawImage 
			graphics 
			image 
			0 0 size size 
			offset-x offset-y min-dimension min-dimension 
			nil)
		(.dispose graphics)
		empty-image))


