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

(def
  ^{:private true
    :doc     "Map associating CSS unit types to their conversion values."}
  conversions
  {;; Absolute units
   :cm           {:cm 1
                  :mm 10
                  :q  40
                  :in 0.39
                  :pc 2.36220473
                  :pt 28.3464567
                  :px 37.795275591}
   :mm           {:mm 1
                  :cm 0.1
                  :q  4
                  :in 3.9
                  :pc 23.6220473
                  :pt 2.83464567
                  :px 3.7795275591}
   :q            {:q  1
                  :cm 0.025
                  :mm 0.25
                  :in 15.6
                  :pc 94.4881892
                  :pt 1133.858268
                  :px 1511.81102364}
   :in           {:in 1
                  :cm 2.54
                  :mm 25.4
                  :q  101.6
                  :pc 6
                  :pt 72
                  :px 96}
   :pc           {:pc 1
                  :cm 0.4233333333
                  :mm 4.23333333
                  :q  16.9333333333
                  :in 0.1666666667
                  :pt 12
                  :px 16}
   :pt           {:pt 1
                  :cm 0.03527777778
                  :mm 0.3527777778
                  :q 1.4111111111
                  :in 0.01388888889
                  :pc 0.0833333333
                  :px 1.3333333333}
   :px           {:px 1
                  :cm 0.02645833333
                  :mm 0.2645833333
                  :q 1.0583333333
                  :in 0.01041666667
                  :pc 0.0625
                  :pt 0.75}

   ;; Percentage Unit - it is its own class
   (keyword "%") {(keyword "%") 1}

   ;; Relative untis
   :em           {:em 1}
   :ex           {:ex 1}
   :cap          {:cap 1}
   :ch           {:ch 1}
   :rem          {:rem 1}
   :ic           {:ic 1}
   :lh           {:lh 1}
   :rlh          {:rlh 1}
   :vh           {:vh 1}
   :vw           {:vw 1}
   :vi           {:vi 1}
   :vb           {:vb 1}
   :vmin         {:vmin 1}
   :vmax         {:vmax 1}

   ;; Angular units
   :deg          {:deg  1
                  :grad 1.111111111
                  :rad  0.0174532925
                  :turn 0.002777778}
   :grad         {:grad 1
                  :rad  63.661977237
                  :turn 0.0025}
   :rad          {:rad  1
                  :turn 0.159154943}
   :turn         {:turn 1}

   ;; Time units
   :s            {:ms 1000
                  :s  1}
   :ms           {:ms 1
                  :s 0.001}

   ;; Frequency units
   :Hz           {:Hz  1
                  :kHz 0.001}
   :kHz          {:kHz 1
                  :Hz 1000}

   ;; Resolution units
   :dpi          {:dpi 1}
   :dpcm         {:dpcm 1}
   :dppx         {:dppx 1}})


(defn convert
  "convert between unit values"
  ;; FIXME I don't like this form of map destructuring;feels repetitive
  [{magnitude :magnitude from :unit} to]
  ;; FIXME this is just temporary
  conversions
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
  [data]
  data)


(css [:body :h1 {:font-size (unit "10px" :em)}])
(read-unit "-10px")
(read-unit "20%")
(unit "10px" :%)
(unit {:magnitude 10 :unit :px})
(unit "10px" :em)
(unit {:magnitude 10 :unit :px} :em)