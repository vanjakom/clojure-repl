(ns clojure-repl.misc.utils)

; when you don't know where to put something :)

(defn md5sum-byte-array [byte-array]
	(.toString 
		(new java.math.BigInteger 
			(.digest (java.security.MessageDigest/getInstance "MD5") byte-array))
		16))