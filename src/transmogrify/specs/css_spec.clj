(ns transmogrify.specs.css-spec
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [spec-tools
             [core :as st-c]
             [data-spec :as st-ds]
             [spec :as spec]]))

(s/def ::css-wide-keywords (st-ds/spec ::css-wide-keywords (s/spec #{:initial :inherit :unset :revert})))
;;;;;; UNITS ;;;;;;;;;
;; https://drafts.csswg.org/css-values-3/
(comment
  (def length-units
    #{:in :cm :pc :mm :pt :px (keyword "%")})

  (def angular-units
    #{:deg :grad :rad :turn})

  (def time-units
    #{:s :ms})

  (def frequency-units
    #{:Hz :kHz})

  (def resolution-units
    #{:dpi :dpcm :dppx}))

(s/def ::percentage
  (s/or
    :string (s/and spec/string? #(re-matches #"^(\d+|\d+[.]\d+)%?$" %))
    #_(:vector (s/cat :magnitude spec/double? :unit #{:%}))
    :map {:magnitude spec/double? :unit #{:%}}))

;;; DISTANCES
;;; RELATIVE
(s/def ::em (st-ds/spec ::em {:magnitude spec/double? :unit (s/spec #{:em})}))
(s/def ::ex (st-ds/spec ::ex {:magnitude spec/double? :unit (s/spec #{:ex})}))
(s/def ::ch (st-ds/spec ::ch {:magnitude spec/double? :unit (s/spec #{:ch})}))
(s/def ::rem (st-ds/spec ::rem {:magnitude spec/double? :unit (s/spec #{:rem})}))
(s/def ::vh (st-ds/spec ::vh {:magnitude spec/double? :unit (s/spec #{:vh})}))
(s/def ::vw (st-ds/spec ::vw {:magnitude spec/double? :unit (s/spec #{:vw})}))
(s/def ::vmin (st-ds/spec ::vmin {:magnitude spec/double? :unit (s/spec #{:vmin})}))
(s/def ::vmax (st-ds/spec ::vmax {:magnitude spec/double? :unit (s/spec #{:vmax})}))

;;; ABSOLUTE
(s/def ::cm (st-ds/spec ::cm {:magnitude spec/double? :unit (s/spec #{:cm})}))
(s/def ::mm (st-ds/spec ::mm {:magnitude spec/double? :unit (s/spec #{:mm})}))
(s/def ::q (st-ds/spec ::q {:magnitude spec/double? :unit (s/spec #{:q})}))
(s/def ::in (st-ds/spec ::in {:magnitude spec/double? :unit (s/spec #{:in})}))
(s/def ::pc (st-ds/spec ::pc {:magnitude spec/double? :unit (s/spec #{:pc})}))
(s/def ::pt (st-ds/spec ::pt {:magnitude spec/double? :unit (s/spec #{:pt})}))
(s/def ::px (st-ds/spec ::px {:magnitude spec/double? :unit (s/spec #{:px})}))

;;; ANGULAR
(s/def ::deg (st-ds/spec ::deg {:magnitude (s/int-in 0 361) :unit (s/spec #{:deg})}))
(s/def ::grad (st-ds/spec ::grad {:magnitude (s/int-in 0 401) :unit (s/spec #{:grad})}))
;; FIXME spec/double? -> 0-2pi
;; FIXME spec/double? issue with whole ints
(s/def ::rad (st-ds/spec ::rad {:magnitude spec/double? :unit (s/spec #{:rad})}))
;; FIXME the range is a bit strange
(s/def ::turn (st-ds/spec ::turn {:magnitude (s/and spec/number? #(< -1 % 1.01)) :unit (s/spec #{:turn})}))

;;; DURATION
(s/def ::s (st-ds/spec ::s {:magnitude spec/double? :unit (s/spec #{:s})}))
(s/def ::ms (st-ds/spec ::ms {:magnitude spec/double? :unit (s/spec #{:ms})}))

;;; FREQUENCY
(s/def ::hz (st-ds/spec ::hz {:magnitude spec/pos-int? :unit (s/spec #{:hz})}))
(s/def ::khz (st-ds/spec ::khz {:magnitude spec/pos-int? :unit (s/spec #{:khz})}))

;;; RESOLUTION
(s/def ::dpi (st-ds/spec ::dpi {:magnitude spec/pos-int? :unit (s/spec #{:dpi})}))
(s/def ::dpcm (st-ds/spec ::dpcm {:magnitude spec/pos-int? :unit (s/spec #{:dpcm})}))
(s/def ::dppx (st-ds/spec ::dppx {:magnitude spec/pos-int? :unit (s/spec #{:dppx})}))

;;; UNIT GROUPS
;; FIXME distance vs length naming
(s/def ::relative-distance-units
  (s/alt :em ::em :ex ::ex :ch ::ch :rem ::rem :vh ::vh :vw ::vw :vmin ::vmin :vmax ::vmax))
(s/def ::absolute-distance-units (s/alt :cm ::cm :mm ::mm :q ::q :in ::in :pc ::pc :pt ::pt :px ::px))
(s/def ::distance-units (s/alt :relative ::relative-distance-units :absolute ::absolute-distance-units))
(s/def ::duration-units (s/alt :s ::s :ms ::ms))
(s/def ::angular-units (s/alt :deg ::deg :grad ::grad :rad ::rad :turn ::turn))
(s/def ::frequency-units (s/alt :hz ::hz :khz ::khz))
(s/def ::resolution-units (s/alt :dpi ::dpi :dpcm ::dpcm :dppx ::dppx))

(s/form ::angular-units)
(s/exercise ::angular-units)
(s/explain-data ::turn {:magnitude 0 :unit :turn})

(s/def ::named spec/string?)
(s/def ::generic
  (st-ds/spec
    ::generic
    (s/spec #{:serif :sans-serif :cursive :fantasy :monospace :system-ui :emoji :math :fangsong})))

(s/def ::font-family
  (st-ds/spec
    ::font-family
    [(s/or :named ::named :generic ::generic :global ::css-wide-keywords)]))

(s/form ::font-family)
(s/exercise ::font-family)
(s/conform ::font-family [:serif :cursive "helvetica"])

(defn multiple-of-100? [n] (zero? (mod n 100)))
;;FIXME possibly going to cause naming conflict later
;;TODO number keywords https://drafts.csswg.org/css-fonts-4/#valdef-font-weight-number
(s/def ::weight-number (st-ds/spec ::number (s/and (s/int-in 0 1000) multiple-of-100?)))
(s/def ::weight-value (st-ds/spec ::value (s/spec #{:normal :bold :bolder :lighter})))

(s/def ::font-weight
  (st-ds/spec
    ::font-weight
    (s/or
      :value ::weight-value
      :number ::weight-number)
    {:default :normal}))

(s/form ::font-weight)
(s/exercise ::font-weight)
(s/conform ::font-weight 100)

(s/def ::stretch-value
  (st-ds/spec
    ::stretch-value
    (s/spec
      #{:normal :ultra-condensed :extra-condensed :condensed
        :semi-condensed :semi-expanded :expanded :extra-expanded :ultra-expanded})))

(s/def ::font-stretch
  (st-ds/spec
    ::font-stretch
    (s/or
      :value ::stretch-value
      :percentage ::percentage)))

(s/form ::font-stretch)
(s/exercise ::font-stretch)
(s/conform ::font-stretch "100%")

(s/def ::font-style
  (st-ds/spec
    ::font-style
    {}))


(s/def ::properties
  (st-ds/spec
    ::properties
    {(st-ds/opt :font-family)  ::font-family
     (st-ds/opt :font-weight)  ::font-weight
     (st-ds/opt :font-stretch) ::font-stretch}))

(s/form ::properties)
(s/exercise ::properties)
(st-c/conform
  ::properties
  {:font-family ["helvetica" :serif]
   :font-weight :normal
   :font-stretch :normal})