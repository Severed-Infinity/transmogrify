(ns transmogrify.wips.css
  (:require [clojure.string :refer [split]]))

(def
  ^{:doc     "Regular expression for matching a CSS unit. The magnitude
             and unit are captured."
    :private true}
  css-unit-regex
  ;; TODO make sure all units are here
  #"([+-]?\d+(?:\.?\d+)?)(p[xtc]|in|[cm]m|%|r?em|ex|ch|v(?:[wh]|m(?:in|ax))|deg|g?rad|turn|m?s|k?Hz|dp(?:i|cm|px))")

(defn read-unit
  "Read a `CSSUnit` object from the string `s`."
  [s]
  (when-let [[_ magnitude unit] (re-matches css-unit-regex s)]
    (let [unit (keyword unit)
          magnitude (if magnitude (read-string magnitude) 0)]
      {:unit unit :magnitude magnitude})))

(defn convert
  "convert between unit values"
  [{magnitude :magnitude from :unit} to]
  ;; FIXME this is just temporary
  {:magnitude magnitude :unit to})

(defn unit
  "conversion function?"
  ([un]
   (cond
     (string? un) (read-unit un)
     (map? un) un))
  ([un to]
   (cond
     (string? un) (unit (read-unit un) to)
     (map? un) (convert un to))))

(defn css
  "core function for clojure to css - and to files?"
  [css]
  css)



(read-unit "10px")
(unit "10px")
(unit {:magnitude 10 :unit :px})
(unit "10px" :em)
(unit {:magnitude 10 :unit :px} :em)