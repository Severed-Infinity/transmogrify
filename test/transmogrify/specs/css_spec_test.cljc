(ns transmogrify.specs.css-spec-test
  (:require [transmogrify.specs.css-spec :as css-spec]
            [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as s-gen]
            [clojure.test :as test]
            [clojure.test.check :as tc]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.clojure-test :refer [defspec]]))

(def number-of-tests 1000)

(def pos-double-gen (gen/fmap (fn [n] (if (pos? n) n 0.1)) gen/double))
(def number-gen (gen/one-of [gen/int gen/double]))

;; TODO css spec units property tests
;; FIXME I am an idiot, I should be testing with s/valid? and not s/conform
;; FIXME a lot of cases that use tuple can be of variable length with some values missing
;;   issue would then become keeping order consistency

(comment
  ;spec stub
  (defspec css-spec--generative-test
           number-of-tests
           (prop/for-all
             [input (gen/return true)]
             (true? input))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;  UNITS ;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defspec css-spec-magnitude-key-generative-test
         number-of-tests
         (prop/for-all
           [d pos-double-gen]
           (s/valid? ::css-spec/magnitude d)))

(defspec css-spec-unit-key-generative-test
         number-of-tests
         (prop/for-all
           [per-sym (gen/return :%)]
           (s/valid? ::css-spec/unit per-sym)))

(def percentage-as-a-string
  (gen/fmap
    (fn [[n1 n2]] (str n1 "." n2 "%"))
    (gen/tuple gen/pos-int gen/pos-int)))

(defspec css-spec-percentage-unit-string-generative-test
         number-of-tests
         (prop/for-all
           [per-str percentage-as-a-string]
           (s/valid? ::css-spec/percentage per-str)))

;; FIXME needs to be looked at, expects an int to pass code coverage
(defspec css-spec-percentage-unit-map-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/hash-map :magnitude pos-double-gen
                                :unit (gen/return :%))]
           (s/valid? ::css-spec/percentage input)))

;;;;;;;;;;  ABSOLUTE ;;;;;;;;;;;;;;;

(defspec css-spec-unit-cm-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/hash-map :magnitude number-gen
                                :unit (gen/return :cm))]
           (s/valid? ::css-spec/cm input)))

(defspec css-spec-unit-mm-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/hash-map :magnitude number-gen
                                :unit (gen/return :mm))]
           (s/valid? ::css-spec/mm input)))

(defspec css-spec-unit-q-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/hash-map :magnitude number-gen
                                :unit (gen/return :Q))]
           (s/valid? ::css-spec/q input)))

(defspec css-spec-unit-in-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/hash-map :magnitude number-gen
                                :unit (gen/return :in))]
           (s/valid? ::css-spec/in input)))

(defspec css-spec-unit-pc-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/hash-map :magnitude number-gen
                                :unit (gen/return :pc))]
           (s/valid? ::css-spec/pc input)))

(defspec css-spec-unit-pt-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/hash-map :magnitude number-gen
                                :unit (gen/return :pt))]
           (s/valid? ::css-spec/pt input)))

(defspec css-spec-unit-px-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/hash-map :magnitude number-gen
                                :unit (gen/return :px))]
           (s/valid? ::css-spec/px input)))

;;;;;;;;;;  RELATIVE ;;;;;;;;;;;;;;;;
(defspec css-spec-unit-em-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-unit-ex-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-unit-ch-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-unit-rem-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-unit-vh-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-unit-vw-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-unit-vmin-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-unit-vmax-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-unit-cap-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-unit-ic-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-unit-lh-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-unit-rlh-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-unit-vi-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-unit-vb-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

;;;;;;;;;;  ANGULAR ;;;;;;;;;;;;;;;;;
(defspec css-spec-unit-deg-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-unit-grad-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-unit-rad-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-unit-turn-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

;;;;;;;;;;  DURATION ;;;;;;;;;;;;;;;;
(defspec css-spec-unit-s-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-unit-ms-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

;;;;;;;;;;  FREQUENCY ;;;;;;;;;;;;;;;
(defspec css-spec-unit-hz-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-unit-khz-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

;;;;;;;;;;  RESOLUTION ;;;;;;;;;;;;;;
(defspec css-spec-styleset-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;  FONT ;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defspec css-spec-font-family-generative-test
         number-of-tests
         (prop/for-all
           [inputs (gen/one-of [(gen/tuple gen/string)
                                (gen/tuple (s/gen ::css-spec/generic))
                                (gen/tuple (s/gen ::css-spec/css-wide-keywords))
                                (gen/tuple gen/string (s/gen ::css-spec/generic))
                                (gen/tuple gen/string (s/gen ::css-spec/css-wide-keywords))
                                (gen/tuple (s/gen ::css-spec/css-wide-keywords) (s/gen ::css-spec/generic))
                                (gen/tuple gen/string (s/gen ::css-spec/generic) (s/gen ::css-spec/css-wide-keywords))])]
           (s/valid? ::css-spec/font-family inputs)))

(def multiple-of-100-less-then-1000
  (gen/fmap
    (fn [n] (* n 100))
    (gen/large-integer* {:min 1 :max 9})))

(defspec css-spec-weight-number-generative-test
         number-of-tests
         (prop/for-all
           [n multiple-of-100-less-then-1000]
           (s/valid? ::css-spec/weight-number n)))

(defspec css-spec-weight-value-generative-test
         number-of-tests
         (prop/for-all
           [val (gen/one-of [(gen/return :normal) (gen/return :bold) (gen/return :bolder) (gen/return :lighter)])]
           (s/valid? ::css-spec/weight-value val)))

(defspec css-spec-font-weight-generative-test
         number-of-tests
         (prop/for-all
           [inputs (gen/one-of [(s/gen ::css-spec/weight-number) (s/gen ::css-spec/weight-value)])]
           (s/valid? ::css-spec/font-weight inputs)))

(defspec css-spec-font-stretch-generative-test
         number-of-tests
         (prop/for-all
           [inputs (gen/one-of [(s/gen ::css-spec/stretch-value)
                                (s/gen ::css-spec/css-wide-keywords)
                                (s/gen ::css-spec/percentage)])]
           (s/valid? ::css-spec/font-stretch inputs)))

(defspec css-spec-font-style-generative-test
         number-of-tests
         (prop/for-all
           [inputs (gen/one-of [(gen/return :normal) (gen/return :italic) (gen/return :oblique)
                                (gen/tuple (gen/return :oblique) (s/gen ::css-spec/angular-units))])]
           (s/valid? ::css-spec/font-style inputs)))

(defspec css-spec-font-size-generative-test
         number-of-tests
         (prop/for-all
           [inputs (gen/one-of [(s/gen ::css-spec/absolute-size)
                                (s/gen ::css-spec/relative-size)
                                (s/gen ::css-spec/distance-units)
                                (s/gen ::css-spec/percentage)])]
           (s/valid? ::css-spec/font-size inputs)))

(defspec css-spec-font-min-size-generative-test
         number-of-tests
         (prop/for-all
           [inputs (gen/one-of [(s/gen ::css-spec/absolute-size)
                                (s/gen ::css-spec/relative-size)
                                (s/gen ::css-spec/distance-units)
                                (s/gen ::css-spec/percentage)])]
           (s/valid? ::css-spec/font-min-size inputs)))

(defspec css-spec-font-max-size-generative-test
         number-of-tests
         (prop/for-all
           [inputs (gen/one-of [(s/gen ::css-spec/absolute-size)
                                (s/gen ::css-spec/relative-size)
                                (s/gen ::css-spec/distance-units)
                                (s/gen ::css-spec/percentage)
                                (gen/return :infinity)])]
           (s/valid? ::css-spec/font-max-size inputs)))

(defspec css-spec-font-size-adjust-generative-test
         number-of-tests
         (prop/for-all
           [inputs (gen/one-of [(gen/return :none) gen/large-integer gen/double])]
           (s/valid? ::css-spec/font-size-adjust inputs)))

(defspec css-spec-font-tuple-generative-test
         ;;fixme need to add the rest of the properties
         number-of-tests
         (prop/for-all
           [input (gen/tuple (s/gen ::css-spec/font-style)
                             #_(s/gen ::css-spec/font-variant)
                             (s/gen ::css-spec/font-weight)
                             (s/gen ::css-spec/font-stretch)
                             (s/gen ::css-spec/font-size)
                             #_(s/gen ::css-spec/line-height)
                             (s/gen ::css-spec/font-family))]
           (s/valid? ::css-spec/font input)))

(defspec css-spec-font-system-fonts-generative-test
         number-of-tests
         (prop/for-all
           [inputs (gen/one-of [(gen/return :caption)
                                (gen/return :icon)
                                (gen/return :menu)
                                (gen/return :message-box)
                                (gen/return :small-caption)
                                (gen/return :status-bar)])]
           (s/valid? ::css-spec/font inputs)))

(defspec css-spec-font-generative-test
         ;;fixme need to add the rest of the properties
         number-of-tests
         (prop/for-all
           [input (gen/one-of
                    [(gen/tuple (s/gen ::css-spec/font-style)
                                #_(s/gen ::css-spec/font-variant)
                                (s/gen ::css-spec/font-weight)
                                (s/gen ::css-spec/font-stretch)
                                (s/gen ::css-spec/font-size)
                                #_(s/gen ::css-spec/line-height)
                                (s/gen ::css-spec/font-family))
                     (gen/one-of [(gen/return :caption)
                                  (gen/return :icon)
                                  (gen/return :menu)
                                  (gen/return :message-box)
                                  (gen/return :small-caption)
                                  (gen/return :status-bar)])])]
           (s/valid? ::css-spec/font input)))

(defspec css-spec-font-synthesis-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/one-of
                    [(gen/return :none)
                     (gen/tuple (gen/return :weight)
                                (gen/return :small-caps)
                                (gen/return :style))])]
           (s/valid? ::css-spec/font-synthesis input)))

(defspec css-spec-feature-tag-value-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-font-feature-settings-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-font-kerning-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-font-language-override-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-font-variant-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-font-variant-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-font-feature-tag-value-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-stylistic-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-styleset-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-character-variant-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-swash-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-ornaments-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-annotations-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-font-variant-alternatives-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-font-variant-caps-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-east-asian-variant-values-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-east-asian-width-values-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-font-variant-east-asian-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-common-lig-values-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-discretionary-lig-values-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-historical-lig-values-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-contextual-alt-values-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-font-variant-ligatures-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-numeric-figures-values-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-numeric-spacing-values-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-numeric-fraction-values-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-numeric-other-values-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-font-variant-numeric-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-font-variant-position-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;  COLOUR ;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defspec css-spec-color-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-opacity-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;  BACKGROUND ;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defspec css-spec-background-color-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;  BORDER ;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defspec css-spec-border-top-width-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;  BOX ;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defspec css-spec-margin-top-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;  FLEXIBLE BOX ;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defspec css-spec-align-content-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;  GRID LAYOUT ;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defspec css-spec-grid-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;  TEXT ;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defspec css-spec-word-spacing-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;  TEXT DECORATION ;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defspec css-spec-text-decoration-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;  WRITING MODES ;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defspec css-spec-direction-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;  TABLES ;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defspec css-spec-border-collapse-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;  LISTS & COUNTERS ;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defspec css-spec-counter-increment-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;  ANIMATION ;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defspec css-spec-animation-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;  TRANSFORM ;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defspec css-spec-backface-visibility-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;  TRANSITIONS ;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defspec css-spec-transition-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;  BASIC USER INTERFACE ;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defspec css-spec-box-sizing-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;  MULTI-COLUMN LAYOUT ;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defspec css-spec-break-after-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;  PAGE MEDIA ;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defspec css-spec-windows-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;  FILTER EFFECTS ;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defspec css-spec-filter-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;  IMAGE VALUES & REPLACED CONTENT ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defspec css-spec-image-orientation-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;  MASKING ;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defspec css-spec-mask-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

(defspec css-spec-mask-type-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;  SPEECH ;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defspec css-spec-azimuth-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;  MARQUEE ;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defspec css-spec-marquee-direction-generative-test
         number-of-tests
         (prop/for-all
           [input (gen/return true)]
           (true? input)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;  PROPERTIES ;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defspec z-css-spec-properties-map-generative-test
         number-of-tests
         (prop/for-all
           [f-family (s/gen ::css-spec/font-family)
            f-weight (s/gen ::css-spec/font-weight)
            f-stretch (s/gen ::css-spec/font-stretch)
            f-style (s/gen ::css-spec/font-style)
            f-size (s/gen ::css-spec/font-size)
            f-min-size (s/gen ::css-spec/font-min-size)
            f-max-size (s/gen ::css-spec/font-max-size)
            f-font (s/gen ::css-spec/font)
            f-synthesis (s/gen ::css-spec/font-synthesis)]
           (s/valid?
             ::css-spec/properties
             {:font-family    f-family
              :font-weight    f-weight
              :font-stretch   f-stretch
              :font-style     f-style
              :font-size      f-size
              :font-min-size  f-min-size
              :font-max-size  f-max-size
              :font           f-font
              :font-synthesis f-synthesis})))

;; FIXME should be moved to the units test file
(test/deftest css-spec-unit-tests
  (test/testing "css unit specs"
    (test/testing "-> percentage units"
      (test/is (s/valid? ::css-spec/magnitude 10.5) "only doubles allowed")
      (test/is (not (s/valid? ::css-spec/magnitude 50)) "anything but a double should be invalid")
      (test/is (s/valid? ::css-spec/unit :%) "only the percentage sign (:%) as a keyword allowed")
      (test/is (not (s/valid? ::css-spec/unit :percentage)) "anything but :% should be invalid")

      (test/is (not (s/valid? ::css-spec/percentage "100")) "should be invalid for not having % at the end")
      (test/is (s/valid? ::css-spec/percentage "50.5%")
               "should be valid so long as its a double followed by % is at the end")
      (test/is (not (s/valid? ::css-spec/percentage "invalid%"))
               "even though % is at the end it should be invalid for not having a number before it")
      (test/is (s/valid? ::css-spec/percentage {:magnitude 16.0 :unit :%})
               "map format should be valid")
      (test/is (not (s/valid? ::css-spec/percentage {:magnitude 16 :unit :%})) "ints should be invalid")
      (test/is (not (s/valid? ::css-spec/percentage {::css-spec/magnitude 75.76 ::css-spec/unit :%}))
               "namespaced map format should be invalid")
      (test/is (not (s/valid? ::css-spec/percentage [16.0 :%])) "anything else should be invalid"))

    (test/testing "-> distance"
      (test/testing "relative units"
        (test/is (s/valid? ::css-spec/em {:magnitude 16.0 :unit :em}))
        (test/is (s/valid? ::css-spec/ex {:magnitude 16.0 :unit :ex}))
        (test/is (s/valid? ::css-spec/ch {:magnitude 16.0 :unit :ch}))
        (test/is (s/valid? ::css-spec/rem {:magnitude 16.0 :unit :rem}))
        (test/is (s/valid? ::css-spec/vh {:magnitude 16.0 :unit :vh}))
        (test/is (s/valid? ::css-spec/vw {:magnitude 16.0 :unit :vw}))
        (test/is (s/valid? ::css-spec/vmin {:magnitude 16.0 :unit :vmin}))
        (test/is (s/valid? ::css-spec/vmax {:magnitude 16.0 :unit :vmax})))

      (test/testing "absolute units"
        (test/is (s/valid? ::css-spec/cm {:magnitude 16.0 :unit :cm}))
        (test/is (s/valid? ::css-spec/mm {:magnitude 16.0 :unit :mm}))
        (test/is (s/valid? ::css-spec/q {:magnitude 16.0 :unit :Q}))
        (test/is (s/valid? ::css-spec/in {:magnitude 16.0 :unit :in}))
        (test/is (s/valid? ::css-spec/pc {:magnitude 16.0 :unit :pc}))
        (test/is (s/valid? ::css-spec/pt {:magnitude 16.0 :unit :pt}))
        (test/is (s/valid? ::css-spec/px {:magnitude 16.0 :unit :px}))))

    (test/testing "-> angular units"
      (test/is (s/valid? ::css-spec/deg {:magnitude 240 :unit :deg})
               "magnitude should be between 0 and 360 (inclusive)")
      (test/is (not (s/valid? ::css-spec/deg {:magnitude -14 :unit :deg}))
               "anything outside of 0 and 360 should be invalid")
      (test/is (s/valid? ::css-spec/grad {:magnitude 126 :unit :grad})
               "magnitude should be between 0 and 400 (inclusive)")
      (test/is (not (s/valid? ::css-spec/grad {:magnitude 644 :unit :grad}))
               "anything outside of 0 and 300 should be invalid")
      (test/is (s/valid? ::css-spec/rad {:magnitude 6.2 :unit :rad})
               "should be between 0 and 2pi")
      (test/is (s/valid? ::css-spec/rad {:magnitude 3 :unit :rad})
               "should be between 0 and 2pi")
      (test/is (not (s/valid? ::css-spec/rad {:magnitude 65.89 :unit :rad}))
               "anything outside of  0 and 2pi(inclusive) should be invalid")
      (test/is (s/valid? ::css-spec/turn {:magnitude 0.2 :unit :turn})
               "should be between 0 and 1 (inclusive)")
      (test/is (not (s/valid? ::css-spec/turn {:magnitude -0.3 :unit :turn}))
               "anything outside of  0 and 2pi should be invalid"))

    (test/testing "-> duration units"
      (test/is (s/valid? ::css-spec/s {:magnitude 78 :unit :s}))
      (test/is (s/valid? ::css-spec/ms {:magnitude 2301 :unit :ms})))

    (test/testing "-> frequency units"
      (test/is (s/valid? ::css-spec/hz {:magnitude 120 :unit :Hz}))
      (test/is (s/valid? ::css-spec/khz {:magnitude 10 :unit :kHz})))

    (test/testing "-> resolution units"
      (test/is (s/valid? ::css-spec/dpi {:magnitude 72 :unit :dpi}))
      (test/is (not (s/valid? ::css-spec/dpi {:magnitude -2 :unit :dpi})) "negative magnitude should be invalid")
      (test/is (s/valid? ::css-spec/dpcm {:magnitude 12 :unit :dpcm}))
      (test/is (not (s/valid? ::css-spec/dpcm {:magnitude -43 :unit :dpcm})) "negative magnitude should be invalid")
      (test/is (s/valid? ::css-spec/dppx {:magnitude 89 :unit :dppx}))
      (test/is (not (s/valid? ::css-spec/dppx {:magnitude -23 :unit :dppx})) "negative magnitude should be invalid")))

  (test/testing "-> unit groups"))

(test/deftest css-spec-property-tests
  (test/testing "css font specs"
    (test/testing "-> font family spec"
      ;; TODO Sub specs
      ;; FIXME proper output comparison
      (test/is (s/valid? ::css-spec/font-family ["helvetica"]) "coll of a single font as a string")
      (test/is (s/valid? ::css-spec/font-family [:serif]) "coll of a single font from generics")
      (test/is (s/valid? ::css-spec/font-family ["helvetica" :serif]) "coll of multiple mixed font options")
      (test/is (not (s/valid? ::css-spec/font-family "helvetica")) "string font should be invalid")
      (test/is (not (s/valid? ::css-spec/font-family :serif)) "keyword generic font should be invalid"))

    (test/testing "-> font weight spec"
      #_(test/testing "-> testing css-spec/multiple-of-100?"
          (test/is (true? (css-spec/multiple-of-100? 800)) "positive 100's, should be true")
          (test/is (true? (css-spec/multiple-of-100? 300)) "positive 100's, should be true")
          (test/is (true? (css-spec/multiple-of-100? -200)) "negative 100's should be true")
          (test/is (false? (css-spec/multiple-of-100? 90)) "not a multiple of 100, should be false")
          (test/is (false? (css-spec/multiple-of-100? 1110)) "not a multiple of 100, should be false")
          (test/is (false? (css-spec/multiple-of-100? -330))) "not a multiple of 100, should be false")

      (test/testing "-> testing weight number"
        (test/is (s/valid? ::css-spec/weight-number 400))
        (test/is (s/valid? ::css-spec/weight-number 300))
        (test/is (not (s/valid? ::css-spec/weight-number -100)))
        (test/is (not (s/valid? ::css-spec/weight-number 1200)))
        (test/is (not (s/valid? ::css-spec/weight-number 50)))))

    (test/testing "-> properties map of just font specs"
      (test/is
        (s/valid?
          ::css-spec/properties
          {:font-family  ["helvetica" :serif]
           :font-weight  :normal
           :font-stretch :normal
           :font-style   :normal
           :font-size    {:magnitude 16.0 :unit :px}})))))
