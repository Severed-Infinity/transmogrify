(ns transmogrify.specs.css-spec-exercise
  (:require [clojure.test :refer :all]
            [transmogrify.specs.css-spec :as css-spec]
            [clojure.spec.alpha :as s]))


;; FIXME s/form is not necessary
#_(s/form ::css-spec/css-wide-keywords)
(s/exercise ::css-spec/css-wide-keywords)

;;; UNITS ;;;;;;
;;; PERCENTAGE
#_(s/form ::css-spec/magnitude)
(s/exercise ::css-spec/magnitude)
#_(s/form ::css-spec/unit)
(s/exercise ::css-spec/unit)

;; Removing conform and explain-data causes a massive code coverage drop
;; Exercise doesn't work 100% of the time, must affect code coverage
#_(s/form ::css-spec/percentage)
;(s/exercise ::css-spec/percentage)
;(s/conform ::css-spec/percentage "100%")
;(s/conform ::css-spec/percentage "-65.45%")
;(s/conform ::css-spec/percentage "65.45")
;(s/conform ::css-spec/percentage "15")
;(s/conform ::css-spec/percentage 15)
;;;; FIXME map format is lacking code coverage
;(s/conform ::css-spec/percentage {::magnitude 16.0 ::unit :%})
;(s/conform ::css-spec/percentage {::magnitude 62 ::unit :%})
;(s/conform ::css-spec/percentage {:magnitude 16.0 :unit :%})
;(s/conform ::css-spec/percentage {::magnitude 16.0 :unit :%})
;(s/conform ::css-spec/percentage {:magnitude 16.0 ::unit :%})
;(s/conform ::css-spec/percentage {::magnitude 16.0})
;(s/conform ::css-spec/percentage {::unit :%})
;(s/conform ::css-spec/percentage [16.0 :%])
;(s/conform ::css-spec/percentage {:magnitude -73 :unit :%})
;;; So it's the explain data that has the code coverage
;(s/explain-data ::css-spec/percentage "-65.45%")
;(s/explain-data ::css-spec/percentage {::magnitude 16.0 ::unit :%}) ;- namespaced keys
;(s/explain-data ::css-spec/percentage {::magnitude 62 ::unit :%}) ;- namespaced keys
;(s/explain-data ::css-spec/percentage {:magnitude 16.0 :unit :%})
;(s/explain-data ::css-spec/percentage {::magnitude 16.0 :unit :%}) ;- namespaced keys
;(s/explain-data ::css-spec/percentage {:magnitude 16.0 ::unit :%}) ;- provides most coverage
;(s/explain-data ::css-spec/percentage {::magnitude 16.0}) ;- namespaced key
;(s/explain-data ::css-spec/percentage {::unit :%}) ;- namespaced key
;(s/explain-data ::css-spec/percentage [16.0 :%])
;(s/explain-data ::css-spec/percentage [:magnitude 16.0 :unit :%])
(s/explain-data ::css-spec/percentage {:magnitude 16 :unit :%}) ;- provides full coverage
;(s/explain-data ::css-spec/percentage {:magnitude 16.0}) ;- full coverage
;(s/explain-data ::css-spec/percentage {:unit :%})

;;;; DISTANCE
;;;; RELATIVE
#_(s/form ::css-spec/em)
(s/exercise ::css-spec/em)
#_(s/form ::css-spec/ex)
(s/exercise ::css-spec/ex)
#_(s/form ::css-spec/ch)
(s/exercise ::css-spec/ch)
#_(s/form ::css-spec/rem)
(s/exercise ::css-spec/rem)
#_(s/form ::css-spec/vh)
(s/exercise ::css-spec/vh)
#_(s/form ::css-spec/vw)
(s/exercise ::css-spec/vw)
#_(s/form ::css-spec/vmin)
(s/exercise ::css-spec/vmin)
#_(s/form ::css-spec/vmax)
(s/exercise ::css-spec/vmax)

;;;; ABSOLUTE
(s/form ::css-spec/cm)
(s/exercise ::css-spec/cm)
(s/form ::css-spec/mm)
(s/exercise ::css-spec/mm)
(s/form ::css-spec/q)
(s/exercise ::css-spec/q)
(s/form ::css-spec/in)
(s/exercise ::css-spec/in)
(s/form ::css-spec/pc)
(s/exercise ::css-spec/pc)
(s/form ::css-spec/pt)
(s/exercise ::css-spec/pt)
(s/form ::css-spec/px)
(s/exercise ::css-spec/px)

;;;; ANGULAR
(s/form ::css-spec/deg)
(s/exercise ::css-spec/deg)
(s/conform ::css-spec/deg {:magnitude 15 :unit :deg})

(s/form ::css-spec/grad)
(s/exercise ::css-spec/grad)
(s/conform ::css-spec/grad {:magnitude 300 :unit :grad})

(s/form ::css-spec/rad)
(s/exercise ::css-spec/rad)
(s/conform ::css-spec/rad {:magnitude 5.4 :unit :rad})

(s/form ::css-spec/turn)
(s/exercise ::css-spec/turn)
(s/conform ::css-spec/turn {:magnitude 0.45 :unit :turn})

;;;; DURATION
(s/form ::css-spec/s)
(s/exercise ::css-spec/s)
(s/form ::css-spec/ms)
(s/exercise ::css-spec/ms)

;;;; FREQUENCY
(s/form ::css-spec/hz)
(s/exercise ::css-spec/hz)
(s/form ::css-spec/khz)
(s/exercise ::css-spec/khz)

;;;; RESOLUTION
(s/form ::css-spec/dpi)
(s/exercise ::css-spec/dpi)
(s/form ::css-spec/dpcm)
(s/exercise ::css-spec/dpcm)
(s/form ::css-spec/dppx)
(s/exercise ::css-spec/dppx)

;;;; UNIT GROUPS
(s/form ::css-spec/relative-distance-units)
(s/exercise ::css-spec/relative-distance-units)
(s/form ::css-spec/absolute-distance-units)
(s/exercise ::css-spec/absolute-distance-units)
(s/form ::css-spec/distance-units)
(s/exercise ::css-spec/distance-units)
(s/form ::css-spec/duration-units)
(s/exercise ::css-spec/duration-units)
(s/form ::css-spec/frequency-units)
(s/exercise ::css-spec/frequency-units)
(s/form ::css-spec/resolution-units)
(s/exercise ::css-spec/resolution-units)
(s/form ::css-spec/angular-units)
(s/exercise ::css-spec/angular-units)

;;;; FONT SPECIFIC UNITS
(s/form ::css-spec/absolute-size)
(s/exercise ::css-spec/absolute-size)
(s/form ::css-spec/relative-size)
(s/exercise ::css-spec/relative-size)

;;;; PROPERTIES
;;;; FONT
(s/form ::css-spec/named)
(s/exercise ::css-spec/named)
(s/form ::css-spec/generic)
(s/exercise ::css-spec/generic)

(s/form ::css-spec/font-family)
(s/exercise ::css-spec/font-family)
(s/conform ::css-spec/font-family [:serif :cursive "helvetica"])

(s/form ::css-spec/weight-number)
(s/exercise ::css-spec/weight-number)
(s/conform ::css-spec/weight-number 900)
(s/conform ::css-spec/weight-number 800)
(s/conform ::css-spec/weight-number 700)
(s/conform ::css-spec/weight-number 600)
(s/conform ::css-spec/weight-number 500)
(s/conform ::css-spec/weight-number 400)
(s/conform ::css-spec/weight-number 300)
(s/conform ::css-spec/weight-number 200)
(s/conform ::css-spec/weight-number 100)
(s/conform ::css-spec/weight-number 1000)
(s/conform ::css-spec/weight-number -100)
(s/conform ::css-spec/weight-number 50)
(s/conform ::css-spec/weight-number 1200)
(s/conform ::css-spec/weight-number 0)
(s/conform ::css-spec/weight-number 100.50)
(s/conform ::css-spec/weight-number 100.00)
(s/conform ::css-spec/weight-number -200.00)
(s/conform ::css-spec/weight-number "hello")

(s/form ::css-spec/weight-value)
(s/exercise ::css-spec/weight-value)

(s/form ::css-spec/font-weight)
(s/exercise ::css-spec/font-weight)
(s/conform ::css-spec/font-weight 100)

(s/form ::css-spec/stretch-value)
(s/exercise ::css-spec/stretch-value)

(s/form ::css-spec/font-stretch)
(s/exercise ::css-spec/font-stretch)
(s/conform ::css-spec/font-stretch "100%")

(s/form ::css-spec/font-style)
(s/exercise ::css-spec/font-style)
(s/conform ::css-spec/font-style [:oblique {:magnitude 10 :unit :deg}])
(s/explain-data ::css-spec/font-style [:oblique {:magnitude 10 :unit :deg}])

(s/form ::css-spec/font-size)
(s/exercise ::css-spec/font-size)
(s/conform ::css-spec/font-size {:magnitude 16.0 :unit :px})
(s/explain-data ::css-spec/font-size {:magnitude 16.0 :unit :%})

(s/form ::css-spec/font-min-size)
(s/exercise ::css-spec/font-min-size)
(s/conform ::css-spec/font-min-size :small)

(s/form ::css-spec/font-max-size)
(s/exercise ::css-spec/font-max-size)
(s/conform ::css-spec/font-max-size :infinity)

(s/form ::css-spec/font-size-adjust)
(s/exercise ::css-spec/font-size-adjust)
(s/conform ::css-spec/font-size-adjust :none)

(s/form ::css-spec/font)
(s/exercise ::css-spec/font)
(s/conform ::css-spec/font [{:magnitude 16.0 :unit :em} ["helvetica"]])
(s/conform ::css-spec/font [:normal 400 :normal {:magnitude 16.0 :unit :px} ["helvetica"]])
(s/explain-data ::css-spec/font [{:magnitude 16.0 :unit :em} ["helvetica"]])

(s/form ::css-spec/font-synthesis)
(s/exercise ::css-spec/font-synthesis)
(s/conform ::css-spec/font-synthesis [:weight])

(s/exercise ::css-spec/feature-tag-value)
(s/exercise ::css-spec/font-feature-settings)
(s/exercise ::css-spec/font-kerning)
(s/exercise ::css-spec/font-language-override)

(s/form ::css-spec/properties)
(s/exercise ::css-spec/properties)
(s/conform
  ::css-spec/properties
  {:font-family    ["helvetica" :serif]
   :font-weight    :normal
   :font-stretch   :normal
   :font-style     :normal
   :font-size      {:magnitude 16.0 :unit :px}
   :font-min-size  {:magnitude 12.0 :unit :px}
   :font-max-size  {:magnitude 20.0 :unit :px}
   :font           [:normal 400 :normal {:magnitude 16.0 :unit :px} ["helvetica"]]
   :font-synthesis :none})