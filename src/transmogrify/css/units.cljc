(ns transmogrify.css.units
  #?@(:cljs [(:require [cljs.reader :refer [read-string]])])
  (:require [transmogrify.specs.css-spec]
            [clojure.spec.alpha :as spec]
            [clojure.spec.test.alpha :as stest]))

(def
  ^{:private true
    :doc     "Regular expression for matching a CSS unit. The magnitude
             and unit are captured."}
  css-unit-regex
  ;; TODO make sure all units are here
  #"([+-]?\d+(?:\.?\d+)?)(p[xtc]|in|[cm]m|%|r?em|ex|ch|v(?:[wh]|m(?:in|ax))|deg|g?rad|turn|m?s|k?Hz|dp(?:i|cm|px))")

(defn- read-unit
  "Read a unit from the string `s`."
  [s]
  (when-let [[_ magnitude unit] (re-matches css-unit-regex s)]
    (let [unit (keyword unit)
          magnitude (if magnitude (read-string magnitude) 0)]
      {:unit unit :magnitude magnitude})))

(def
  ^{:private true}
  absolute-conversions
  {;; Absolute units
   :cm {:cm 1
        :mm 10
        :Q  40
        :in 0.39
        :pc 2.36220473
        :pt 28.3464567
        :px 37.795275591}
   :mm {:mm 1
        :cm 0.1
        :Q  4
        :in 3.9
        :pc 23.6220473
        :pt 2.83464567
        :px 3.7795275591}
   :Q  {:Q  1
        :cm 0.025
        :mm 0.25
        :in 15.6
        :pc 94.4881892
        :pt 1133.858268
        :px 1511.81102364}
   :in {:in 1
        :cm 2.54
        :mm 25.4
        :Q  101.6
        :pc 6
        :pt 72
        :px 96}
   :pc {:pc 1
        :cm 0.4233333333
        :mm 4.23333333
        :Q  16.9333333333
        :in 0.1666666667
        :pt 12
        :px 16}
   :pt {:pt 1
        :cm 0.03527777778
        :mm 0.3527777778
        :Q  1.4111111111
        :in 0.01388888889
        :pc 0.0833333333
        :px 1.3333333333}
   :px {:px 1
        :cm 0.02645833333
        :mm 0.2645833333
        :Q  1.0583333333
        :in 0.01041666667
        :pc 0.0625
        :pt 0.75}})

(def
  ^{:private true}
  relative-conversions
  {;; Relative untis
   :em   {:em 1}
   :ex   {:ex 1}
   :cap  {:cap 1}
   :ch   {:ch 1}
   :rem  {:rem 1}
   :ic   {:ic 1}
   :lh   {:lh 1}
   :rlh  {:rlh 1}
   :vh   {:vh 1}
   :vw   {:vw 1}
   :vi   {:vi 1}
   :vb   {:vb 1}
   :vmin {:vmin 1}
   :vmax {:vmax 1}})

(def
  ^{:private true}
  angular-conversions
  {;; Angular units
   :deg  {:deg  1
          :grad 1.111111111
          :rad  0.0174532925
          :turn 0.002777778}
   :grad {:grad 1
          :rad  63.661977237
          :turn 0.0025}
   :rad  {:rad  1
          :turn 0.159154943}
   :turn {:turn 1}})

(def
  ^{:private true}
  time-conversions
  {;; Time units
   :s  {:ms 1000
        :s  1}
   :ms {:ms 1
        :s  0.001}})

(def
  ^{:private true}
  frequency-conversions
  {;; Frequency units
   :Hz  {:Hz  1
         :kHz 0.001}
   :kHz {:kHz 1
         :Hz  1000}})

(def
  ^{:private true}
  resolution-conversions
  {;; Resolution units
   :dpi  {:dpi  1
          :dpcm 2.54
          :dppx 0.01041666667}
   :dpcm {:dpcm 1
          :dpi  0.39
          :dppx 37.795275591}
   :dppx {:dppx 1
          :dpi  96
          :dpcm 243.84}})

(def
  ^{:private true
    :doc     "Map associating CSS unit types to their conversion values.
              Possible rounding errors, off by a few decimals"}
  conversions
  ;; FIXME some values are off by a few significant decimal places
  ;; FIXME the values are off because you only need them in one not the other
  (merge
    absolute-conversions
    relative-conversions
    angular-conversions
    time-conversions
    frequency-conversions
    resolution-conversions
    ;; Percentage Unit - it is its own class
    {(keyword "%") {(keyword "%") 1}}))

(defn- convertable?
  "True if unit is a key of convertable-units, false otherwise."
  [unit]
  (contains? conversions unit))

(defn- force-int [value unit]
  (if (or (contains? angular-conversions unit)
          (contains? time-conversions unit)
          (contains? frequency-conversions unit)
          (contains? resolution-conversions unit))
    (if (= (or :rad :turn) unit)
      (Math/abs value)
      (Math/abs (int value)))
    value))

(defn- convert
  "Convert between unit values contained in the conversions map.
   Any units that cannot be will alert the user depending on the
   reason as to why it cannot be converted.

   Default: return initial input?"
  [{:keys [magnitude unit] :as input} to]
  (if (every? convertable? [unit to])
    (let [weight1 (get-in conversions [unit to])
          weight2 (get-in conversions [to unit])]
      (cond
        ;; FIXME do I force int conversion?
        weight1 {:magnitude (force-int (* weight1 magnitude) to) :unit to}
        weight2 {:magnitude (force-int (/ magnitude weight2) to) :unit to}
        :else (throw
                (ex-info
                  (str "Cannot convert between " (name unit) " and " (name to) ".")
                  {}))))
    (let [x (first (drop-while convertable? [unit to]))]
      (throw
        (ex-info
          (str "Inconvertible unit " (name x) " does not exist.")
          {})))))

;; TODO spec generator
(spec/fdef
  convert
  :args (spec/cat :input :transmogrify.specs.css-spec/units :to keyword?)
  :ret :transmogrify.specs.css-spec/units)

(defn unit
  "conversion function?"
  ([un]
   (cond
     (string? un) (unit (read-unit un))
     (map? un) un))
  ([un to]
   (cond
     (string? un) (unit (read-unit un) to)
     (map? un) (convert un to))))

;; FIXME string needs to be a regex
;; TODO spec generator
(spec/fdef
  unit
  :args (spec/alt :single-arg (spec/or :map :transmogrify.specs.css-spec/units :string string? #_(spec/and string? #(re-matches css-unit-regex %)))
                  :two-arg (spec/cat :unit-in (spec/or :map :transmogrify.specs.css-spec/units :string string?)
                                     :unit-out keyword?))
  :ret :transmogrify.specs.css-spec/units)

(comment
  (convert {:magnitude 96 :unit :dpi} :dppx)
  (convert {:magnitude 0 :unit :grad} :deg)
  (convert {:magnitude 0 :unit :deg} :rad)
  (convert {:magnitude 1.0 :unit :deg} :turn)
  (convert {:magnitude 0.625 :unit :grad} :rad)
  (convert {:magnitude 0 :unit :Hz} :kHz)
  (convert {:magnitude 0 :unit :s} :ms)
  (read-unit "-10px")
  (read-unit "20%")
  (unit "10px" :pt)
  (unit "10px")
  (unit {:magnitude 10 :unit :px})
  (unit "10px" :Q)
  (unit {:magnitude 10 :unit :px} :pt)


  (stest/instrument)
  (stest/abbrev-result (first (stest/check))))