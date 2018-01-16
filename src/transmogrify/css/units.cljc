(ns transmogrify.css.units
  #?@(:cljs [(:require [cljs.reader :refer [read-string]])])
  (:require [transmogrify.specs.css-spec]
            [clojure.spec.alpha :as spec]
            [clojure.spec.test.alpha :as stest]
            [clojure.spec.alpha :as s]))

(def
  ^{:private true
    :doc     "Regular expression for matching a CSS unit. The magnitude
             and unit are captured."}
  css-unit-regex
  #"([+-]?\d+(?:\.?\d+)?)(p[xtc]|in|Q|[cm]m|%|r?em|ex|ch|v(?:[whib]|m(?:in|ax))|cap|ic|r?lh|deg|g?rad|turn|m?s|k?Hz|dp(?:i|cm|px))")

(defn- read-unit
  "Read a unit from the string `s`."
  [s]
  (when-let [[_ magnitude unit] (re-matches css-unit-regex s)]
    (let [unit (keyword unit)
          magnitude (if magnitude (read-string magnitude) 0)]
      {:unit unit :magnitude magnitude})))

(def absolute-conversions
  ^{:private true
    :doc     "Conversion rates (functions) for the css absolute units"}
  {:cm {:cm (fn [n] (* n 1))
        :mm (fn [n] (* n 10))
        :Q  (fn [n] (* n 40))
        :in (fn [n] (* n 0.39))
        :pc (fn [n] (* n 2.36220473))
        :pt (fn [n] (* n 28.3464567))
        :px (fn [n] (* n 37.795275591))}

   :mm {:mm (fn [n] (* n 1))
        :cm (fn [n] (* n 0.1))
        :Q  (fn [n] (* n 4))
        :in (fn [n] (* n 3.9))
        :pc (fn [n] (* n 23.6220473))
        :pt (fn [n] (* n 2.83464567))
        :px (fn [n] (* n 3.7795275591))}

   :Q  {:Q  (fn [n] (* n 1))
        :cm (fn [n] (* n 0.025))
        :mm (fn [n] (* n 0.25))
        :in (fn [n] (* n 15.6))
        :pc (fn [n] (* n 94.4881892))
        :pt (fn [n] (* n 1133.858268))
        :px (fn [n] (* n 1511.81102364))}

   :in {:in (fn [n] (* n 1))
        :cm (fn [n] (* n 2.54))
        :mm (fn [n] (* n 25.4))
        :Q  (fn [n] (* n 101.6))
        :pc (fn [n] (* n 6))
        :pt (fn [n] (* n 72))
        :px (fn [n] (* n 96))}

   :pc {:pc (fn [n] (* n 1))
        :cm (fn [n] (* n 0.4233333333))
        :mm (fn [n] (* n 4.23333333))
        :Q  (fn [n] (* n 16.9333333333))
        :in (fn [n] (* n 0.1666666667))
        :pt (fn [n] (* n 12))
        :px (fn [n] (* n 16))}

   :pt {:pt (fn [n] (* n 1))
        :cm (fn [n] (* n 0.03527777778))
        :mm (fn [n] (* n 0.3527777778))
        :Q  (fn [n] (* n 1.4111111111))
        :in (fn [n] (* n 0.01388888889))
        :pc (fn [n] (/ n 12))
        :px (fn [n] (* n 1.3333333333))}

   :px {:px (fn [n] (* n 1))
        :cm (fn [n] (* n 0.02645833333))
        :mm (fn [n] (* n 0.2645833333))
        :Q  (fn [n] (* n 1.0583333333))
        :in (fn [n] (* n 0.01041666667))
        :pc (fn [n] (* n 0.0625))
        :pt (fn [n] (* n 0.75))}})

(defn abs
  "helper function for calling the maths absolute function"
  [n]
  #?(:clj ((.abs Math) n)
     :cljs (js/Math.abs n)))

(def
  ^{:private true
    :doc     "Conversion rates (functions) for the css relative units,
              relative units should have no conversions except to themselves"}
  relative-conversions
  {:em   {:em (fn [n] (abs (* n 1)))}
   :ex   {:ex (fn [n] (abs (* n 1)))}
   :cap  {:cap (fn [n] (abs (* n 1)))}
   :ch   {:ch (fn [n] (abs (* n 1)))}
   :rem  {:rem (fn [n] (abs (* n 1)))}
   :ic   {:ic (fn [n] (abs (* n 1)))}
   :lh   {:lh (fn [n] (abs (* n 1)))}
   :rlh  {:rlh (fn [n] (abs (* n 1)))}
   :vh   {:vh (fn [n] (abs (* n 1)))}
   :vw   {:vw (fn [n] (abs (* n 1)))}
   :vi   {:vi (fn [n] (abs (* n 1)))}
   :vb   {:vb (fn [n] (abs (* n 1)))}
   :vmin {:vmin (fn [n] (abs (* n 1)))}
   :vmax {:vmax (fn [n] (abs (* n 1)))}})

(def
  ^{:doc "Helper var for calling Math PI."}
  pi #?(:clj  Math/PI
        :cljs js/Math.PI))

(def
  ^{:private true
    :doc     "Conversion rates (functions) for the css angular units"}
  angular-conversions
  ;; pass all values through degree as its SI of this unit
  {:deg  {:deg  (fn [n] (mod (* n 1) 360))
          :grad (fn [n] (mod (* n 1.1111111111111) 400))
          :rad  (fn [n] (mod (* n 0.017453292519943) (* 2 pi)))
          :turn (fn [n] (mod (* n 0.0027777777777778) 1))}

   :grad {:grad (fn [n] (mod (* n 1) 400))
          :deg  (fn [n] (mod (* n 0.9) 360))
          :rad  (fn [n] (mod (* n 0.015707963267949) (* 2 pi)))
          :turn (fn [n] (mod (* n 0.0025) 1))}

   :rad  {:rad  (fn [n] (mod (* n 1) (* 2 pi)))
          :deg  (fn [n] (mod (* n 57.295779513082) 360))
          :grad (fn [n] (mod (* n 63.661977236758) 400))
          :turn (fn [n] (mod (* n 0.1591549430919) 1))}

   :turn {:turn (fn [n] (mod (* n 1) 1))
          :deg  (fn [n] (mod (* n 360) 360))
          :grad (fn [n] (mod (* n 400) 400))
          :rad  (fn [n] (mod (* n 6.2831853071796) (* 2 pi)))}})

(def
  ^{:private true
    :doc     "Conversion rates (functions) for the css time units"}
  time-conversions
  {:s  {:ms (fn [n] (* n 1000))
        :s  (fn [n] (* n 1))}
   :ms {:ms (fn [n] (* n 1))
        :s  (fn [n] (* n 0.001))}})

(def
  ^{:private true
    :doc     "Conversion rates (functions) for the css frequency units"}
  frequency-conversions
  {:Hz  {:Hz  (fn [n] (* n 1))
         :kHz (fn [n] (* n 0.001))}
   :kHz {:kHz (fn [n] (* n 1))
         :Hz  (fn [n] (* n 1000))}})

(def
  ^{:private true
    :doc     "Conversion rates (functions) for the css resolution units"}
  resolution-conversions
  {:dpi  {:dpi  (fn [n] (* n 1))
          :dpcm (fn [n] (* n 2.54))
          :dppx (fn [n] (* n 0.01041666667))}
   :dpcm {:dpcm (fn [n] (* n 1))
          :dpi  (fn [n] (* n 0.39))
          :dppx (fn [n] (* n 37.795275591))}
   :dppx {:dppx (fn [n] (* n 1))
          :dpi  (fn [n] (* n 96))
          :dpcm (fn [n] (* n 243.84))}})

(def
  ^{:private true
    :doc     "Map associating CSS unit types to their conversion values.
              Possible rounding errors, off by a few decimals"}
  conversions
  ;; FIXME some values are off by a few significant decimal places
  ;; FIXME the values are off because you only need them in one not the other
  ;; FIXME some conversion values might be wrong, will need to check
  (merge
    absolute-conversions
    relative-conversions
    angular-conversions
    time-conversions
    frequency-conversions
    resolution-conversions
    ;; Percentage Unit - it is its own class
    {(keyword "%") {(keyword "%") (fn [n] (* n 1))}}))

(defn- convertible?
  "True if unit is a key of convertible-units, false otherwise."
  [unit]
  (contains? conversions unit))

(defn- can-convert?
  "True if the unit can be convert to its to (target), false otherwise."
  [unit to-unit]
  (not (nil? (get-in conversions [unit to-unit]))))

(comment
  #_(defn- force-int [value unit]
      (if (or (contains? angular-conversions unit)
              (contains? time-conversions unit)
              (contains? frequency-conversions unit)
              (contains? resolution-conversions unit))
        (if (= (or :rad :turn) unit)
          (Math/abs value)
          (Math/abs (int value)))
        value))

  #_(defn- convert
      "Convert between unit values contained in the conversions map.
       Any units that cannot be will alert the user depending on the
       reason as to why it cannot be converted.

       Default: return initial input?"
      [{:keys [magnitude unit] :as input} to
       (if (every? convertible? [unit to])
         (let [weight1 (get-in conversions [unit to])
               weight2 (get-in conversions [to unit])]
           (cond
             weight1 {:magnitude (force-int (* magnitude weight1) to) :unit to}
             weight2 {:magnitude (force-int (/ magnitude weight2) to) :unit to}
             :else (throw
                     (ex-info
                       (str "Cannot convert between " (name unit) " and " (name to) ".")
                       {}))))
         (let [x (first (drop-while convertible? [unit to]))]
           (throw
             (ex-info
               (str "Inconvertible unit " (name x) " does not exist.")
               {}))))]))

(defn- convert
  "Convert between unit values contained in the conversions map.
   Any units that cannot be will alert the user depending on the
   reason as to why it cannot be converted.

   Default: return initial input.

   Relative units will return the initial unit unchanged regardless of its to unit
   as all relative units lack a conversion weight"
  [{:keys [magnitude unit] :as input} to-unit]
  (if (every? convertible? [unit])
    (let [conversion-weight (get-in conversions [unit to-unit])]
      (cond
        (can-convert? unit to-unit) {:magnitude (conversion-weight magnitude) :unit to-unit}
        :default input)
      #_(cond
          ;; defensive check against relative units due them not having a conversion
          (contains? relative-conversions unit) input
          :else {:magnitude (conversion-weight magnitude) :unit to}))
    (let [x (first (drop-while convertible? [unit to-unit]))]
      (throw
        (ex-info
          (str "Inconvertible unit " (name x) " does not exist.")
          {})))))

;; TODO spec generator
(spec/fdef
  convert
  :args (spec/cat :input :transmogrify.specs.css-spec/units
                  :to :transmogrify.specs.css-spec/unit-keywords)
  :ret :transmogrify.specs.css-spec/units
  :fn (fn [{:keys [args ret]}]
        (let [input (spec/unform :transmogrify.specs.css-spec/units (:input args))
              output (spec/unform :transmogrify.specs.css-spec/units ret)]
          #_(println "in" input (:to args) "\n" "out" output)
          (and (can-convert? (:unit input) (:to args))
               (or (= (:to args) (:unit output))
                   (= input output))))))

(defn unit
  "Unit conversion and formatting function, use this even when looking to convert.
   Converts a unit map or string to a unit map format, and if a to unit value is provided
   convert to that unit and returns the map


   See internal convert function for conversion."
  ([un]
   (cond
     (string? un) (unit (read-unit un))
     (map? un) un))
  ([un to-unit]
   (cond
     (string? un) (unit (read-unit un) to-unit)
     (map? un) (convert un to-unit))))

;; TODO spec generator - specifically towards string regex value
(spec/fdef
  unit
  :args (spec/alt :unary (spec/or :map :transmogrify.specs.css-spec/units
                                  :string (spec/and string? #(re-matches css-unit-regex %)))
                  :binary (spec/cat :unit-in (spec/or :map :transmogrify.specs.css-spec/units
                                                      :string (spec/and string? #(re-matches css-unit-regex %)))
                                    :unit-out :transmogrify.specs.css-spec/unit-keywords))
  :ret :transmogrify.specs.css-spec/units)


;;FIXME remove from this namespace
(comment
  (convert {:magnitude 96 :unit :dpi} :dppx)
  (convert {:magnitude 0 :unit :grad} :deg)
  (convert {:magnitude 0 :unit :deg} :rad)
  (convert {:magnitude 1.0 :unit :deg} :turn)
  (convert {:magnitude 0.625 :unit :grad} :rad)
  (convert {:magnitude 0 :unit :Hz} :kHz)
  (convert {:magnitude 0 :unit :s} :ms)
  (convert {:magnitude 10 :unit :rad} :deg)
  (convert {:magnitude 1, :unit :Hz} :kHz)
  (convert {:magnitude 10 :unit :pt} :pc)
  (convert {:magnitude 1, :unit :ms} :ms)
  (can-convert? :em :ex)
  (s/valid? :transmogrify.specs.css-spec/duration-units (convert2 {:magnitude -4, :unit :ms} :s))
  (read-unit "-10px")
  (read-unit "20%")
  (unit "10px" :pt)
  (unit "10px")
  (unit {:magnitude 10 :unit :px})
  (unit "10px" :Q)
  (unit {:magnitude 10 :unit :px} :pt)


  (stest/instrument)
  (stest/check `convert)
  (stest/summarize-results (stest/check)))