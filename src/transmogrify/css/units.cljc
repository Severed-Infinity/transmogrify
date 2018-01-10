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
  ;; TODO make sure all units are here
  #"([+-]?\d+(?:\.?\d+)?)(p[xtc]|in|[cm]m|%|r?em|ex|ch|v(?:[wh]|m(?:in|ax))|deg|g?rad|turn|m?s|k?Hz|dp(?:i|cm|px))")

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

(defn abs [n] #?(:clj  ((.abs Math) n)
                 :cljs (js/Math.abs n)))

(def
  ^{:private true}
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

(def pi #?(:clj  Math/PI
           :cljs js/Math.PI))

(def
  ^{:private true}
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
  ^{:private true}
  time-conversions
  {;; Time units
   :s  {:ms (fn [n] (* n 1000))
        :s  (fn [n] (* n 1))}
   :ms {:ms (fn [n] (* n 1))
        :s  (fn [n] (* n 0.001))}})

(def
  ^{:private true}
  frequency-conversions
  {;; Frequency units
   :Hz  {:Hz  (fn [n] (* n 1))
         :kHz (fn [n] (* n 0.001))}
   :kHz {:kHz (fn [n] (* n 1))
         :Hz  (fn [n] (* n 1000))}})

(def
  ^{:private true}
  resolution-conversions
  {;; Resolution units
   :dpi  {:dpi  (fn [n] (* n 1))
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
  "True if unit is a key of convertable-units, false otherwise."
  [unit]
  (contains? conversions unit))

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
             ;; FIXME do I force int conversion?
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

   Default: return initial input?

   Relative units will return the initial unit unchanged regardless of its to unit
   as all relative units lack a conversion weight"
  [{:keys [magnitude unit] :as input} to]
  (if (every? convertible? [unit])
    (let [conversion-weight (get-in conversions [unit to])]
      (cond
        ;; defensive check against relative units due them not having a conversion
        (contains? relative-conversions unit) input
        :else {:magnitude (conversion-weight magnitude) :unit to}))
    (let [x (first (drop-while convertible? [unit to]))]
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
  (convert {:magnitude 10 :unit :rad} :deg)
  (convert2 {:magnitude 1, :unit :Hz} :kHz)
  (convert2 {:magnitude 10 :unit :pt} :pc)
  (s/valid? :transmogrify.specs.css-spec/duration-units (convert2 {:magnitude -4, :unit :ms} :s))
  (read-unit "-10px")
  (read-unit "20%")
  (unit "10px" :pt)
  (unit "10px")
  (unit {:magnitude 10 :unit :px})
  (unit "10px" :Q)
  (unit {:magnitude 10 :unit :px} :pt)


  (stest/instrument)
  (stest/abbrev-result (first (stest/check))))