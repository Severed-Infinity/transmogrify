(ns transmogrify.specs.html_spec
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [spec-tools
             [core :as st-c]
             [data-spec :as st-ds]
             [spec :as spec]]))

;;TODO better testing

;;(comment
;;general structure for a tag/element
;;[::tag ::attr ::content]
;;example (in somewhat plain text)
;;{::tag ::a
;;         ::attr {:req [::href]
;;         :opt [::styles]]]
;;     ::content ::any-element)
;;{::tag ::p
;;        ::attr {:opt [::styles]}
;;      ::content ::any-element))

;;(comment
;;(s/def ::a #{:a})
;;(def a
;;        {::tag #{::a}
;;       ::attr {(st-ds/req :href) string?}
;;     ::content []])

;;(def a-spec (st-ds/spec ::a a)))

;;;;;;;;;;;;;;;;;;;;;
;;; spec's

;;;;;;;;;;;;;;;;;;;;;
;;; attributes

(s/def ::global-attributes
  (st-c/spec
    #{::access-key
      ::class
      ::content-editable
      ::context-menu
      ::data-*
      ::dir
      ::draggable
      ::dropzone
      ::hidden
      ::id
      ::lang
      ::spellcheck
      ::style
      ::tab-index
      ::title
      ::translate}))

(s/def ::window-event-attributes
  (st-c/spec
    #{::on-after-print
      ::on-before-print
      ::on-before-unload
      ::on-error
      ::on-hash-change
      ::on-load
      ::on-message
      ::on-offline
      ::on-online
      ::on-page-hide
      ::on-page-show
      ::on-pop-state
      ::on-resize
      ::on-storage
      ::on-unload}))

(s/def ::form-event-attributes
  (st-c/spec
    #{::on-blur
      ::on-change
      ::on-context-menu
      ::on-focus
      ::on-input
      ::on-invalid
      ::on-reset
      ::on-search
      ::on-select
      ::on-submit}))

(s/def ::keyboard-event-attributes
  (st-c/spec
    #{::on-key-down
      ::on-key-press
      ::on-key-up}))

(s/def ::mouse-event-attributes
  (st-c/spec
    #{::on-click
      ::on-dbl-click
      ::on-mouse-down
      ::on-mouse-move
      ::on-mouse-out
      ::on-mouse-over
      ::on-mouse-up
      ::on-mouse-wheel
      ::on-wheel}))

(s/def ::drag-event-attributes
  (st-c/spec
    #{::on-drag
      ::on-drag-end
      ::on-drag-enter
      ::on-drag-leave
      ::on-drag-over
      ::on-drag-start
      ::on-drop
      ::on-scroll}))

(s/def ::clipboard-event-attributes
  (st-c/spec
    #{::on-copy
      ::on-cut
      ::on-paste}))

(s/def ::media-event-attributes
  (st-c/spec
    #{::on-abort
      ::on-can-play
      ::on-can-play-through
      ::on-cue-change
      ::on-duration-change
      ::on-emptied
      ::on-ended
      ::on-error
      ::on-loaded-data
      ::on-loaded-meta-data
      ::on-load-start
      ::on-pause
      ::on-play
      ::on-playing
      ::on-progress
      ::on-rate-change
      ::on-seeked
      ::on-seeking
      ::on-stalled
      ::on-suspend
      ::on-time-update
      ::on-volume-change
      ::on-waiting}))

(s/def ::misc-event-attributes
  (st-c/spec
    #{::on-show
      ::on-toggle}))

(s/def ::global-event-attributes
  (st-c/spec
    (s/or
      ::window-events ::window-event-attributes
      ::form-events ::form-event-attributes
      ::keyboard-events ::keyboard-event-attributes
      ::mouse-events ::mouse-event-attributes
      ::drag-events ::drag-event-attributes
      ::clipboard-events ::clipboard-event-attributes
      ::media-events ::media-event-attributes
      ::misc-events ::misc-event-attributes)))


;;;;;;;;;;;;;;;;;;;;;
;;; elements


;;;;; Needs work
(defrecord element [tag attrs content])
(->element :head {} :a)

(defn- tag [tag]
  (s/and keyword? #{tag}))


#_(comment
    (defn keyword-element-spec-tuple [html-tag]
      [(keyword (name html-tag)) html-tag])

    (defn html-content [content]
      (flatten (transduce (map keyword-element-spec-tuple) conj content)))

    (html-content [::head ::body])
    #_(defmacro def-content [content]
        `(s/cat ~(html-content content)))

    #_(def-content [::head ::body])

    (defn- html-spec
      ([html-tag
        (let [keyword-html-tag (keyword (name html-tag))]
          (s/or
            :map-form (st-ds/spec html-tag {:tag (tag keyword-html-tag)})
            :vector-form (s/cat :tag (tag keyword-html-tag))))])

      ([html-tag content
        (let [keyword-html-tag (keyword (name html-tag))
              required-content (:req content)
              optional-content (:opt content)]
          (s/or
            :map (st-ds/spec html-tag {:tag     (tag keyword-html-tag)
                                       :content (s/cat)})))]))

    (defmacro def-html-spec
      ([html-tag
        `(s/def ~html-tag ~(html-spec html-tag))])
      ([html-tag content
        `(s/def ~html-tag ~(html-spec html-tag content))]))

    (def-html-spec ::e)
    (s/form ::e)
    (s/valid? ::e {:tag :e}))

;;;;;;;;;;;;;

;;(s/or :map (st-ds/spec ::a {:tag (tag :a)}) :vector (s/cat :tag (tag :a))))

(s/def ::text string?)

(s/def ::a
  (s/or
    :map-form (st-ds/spec ::a
                          {:tag     (tag :a)
                           :content (s/cat :text (s/* ::text)
                                           :phrasing (s/* ::phrasing-elements)
                                           :flow (s/* ::flow-elements))})
    :vector-form (s/cat
                   :tag (tag :a)
                   :content (s/cat :text (s/* ::text)
                                   :phrasing (s/* ::phrasing-elements)
                                   :flow (s/* ::flow-elements)))))

(s/def ::em
  (s/or
    :map-form (st-ds/spec ::em {:tag (tag :em)})
    :vector-form (s/cat :tag (tag :em))))

(s/def ::strong
  (s/or
    :map-form (st-ds/spec ::strong {:tag (tag :strong)})
    :vector-form (s/cat :tag (tag :strong))))
(s/def ::small
  (s/or
    :map-form (st-ds/spec ::small {:tag (tag :small)})
    :vector-form (s/cat :tag (tag :small))))
(s/def ::s
  (s/or
    :map-form (st-ds/spec ::s {:tag (tag :s)})
    :vector-form (s/cat :tag (tag :s))))
(s/def ::cite
  (s/or
    :map-form (st-ds/spec ::cite {:tag (tag :cite)})
    :vector-form (s/cat :tag (tag :cite))))
(s/def ::q
  (s/or
    :map-form (st-ds/spec ::q {:tag (tag :q)})
    :vector-form (s/cat :tag (tag :q))))
(s/def ::dfn
  (s/or
    :map-form (st-ds/spec ::dfn {:tag (tag :dfn)})
    :vector-form (s/cat :tag (tag :dfn))))
(s/def ::abbr
  (s/or
    :map-form (st-ds/spec ::abbr {:tag (tag :abbr)})
    :vector-form (s/cat :tag (tag :abbr))))
(s/def ::data
  (s/or
    :map-form (st-ds/spec ::data {:tag (tag :data)})
    :vector-form (s/cat :tag (tag :data))))
(s/def ::time
  (s/or
    :map-form (st-ds/spec ::time {:tag (tag :time)})
    :vector-form (s/cat :tag (tag :time))))
(s/def ::code
  (s/or
    :map-form (st-ds/spec ::code {:tag (tag :code)})
    :vector-form (s/cat :tag (tag :code))))
(s/def ::var
  (s/or
    :map-form (st-ds/spec ::var {:tag (tag :var)})
    :vector-form (s/cat :tag (tag :var))))
(s/def ::samp
  (s/or
    :map-form (st-ds/spec ::samp {:tag (tag :samp)})
    :vector-form (s/cat :tag (tag :samp))))
(s/def ::kbd
  (s/or
    :map-form (st-ds/spec ::kbd {:tag (tag :kbd)})
    :vector-form (s/cat :tag (tag :kbd))))
(s/def ::sub
  (s/or
    :map-form (st-ds/spec ::sub {:tag (tag :sub)})
    :vector-form (s/cat :tag (tag :sub))))
(s/def ::sup
  (s/or
    :map-form (st-ds/spec ::sup {:tag (tag :sup)})
    :vector-form (s/cat :tag (tag :sup))))
(s/def ::i
  (s/or
    :map-form (st-ds/spec ::i {:tag (tag :i)})
    :vector-form (s/cat :tag (tag :i))))
(s/def ::b
  (s/or
    :map-form (st-ds/spec ::b {:tag (tag :b)})
    :vector-form (s/cat :tag (tag :b))))
(s/def ::u
  (s/or
    :map-form (st-ds/spec ::u {:tag (tag :u)})
    :vector-form (s/cat :tag (tag :u))))
(s/def ::mark
  (s/or
    :map-form (st-ds/spec ::mark {:tag (tag :mark)})
    :vector-form (s/cat :tag (tag :mark))))
(s/def ::ruby
  (s/or
    :map-form (st-ds/spec ::ruby {:tag (tag :ruby)})
    :vector-form (s/cat :tag (tag :ruby))))
(s/def ::rb
  (s/or
    :map-form (st-ds/spec ::rb {:tag (tag :rb)})
    :vector-form (s/cat :tag (tag :rb))))
(s/def ::rt
  (s/or
    :map-form (st-ds/spec ::rt {:tag (tag :rt)})
    :vector-form (s/cat :tag (tag :rt))))
(s/def ::rtc
  (s/or
    :map-form (st-ds/spec ::rtc {:tag (tag :rtc)})
    :vector-form (s/cat :tag (tag :rtc))))
(s/def ::rp
  (s/or
    :map-form (st-ds/spec ::rp {:tag (tag :rp)})
    :vector-form (s/cat :tag (tag :rp))))
(s/def ::bdi
  (s/or
    :map-form (st-ds/spec ::bdi {:tag (tag :bdi)})
    :vector-form (s/cat :tag (tag :bdi))))
(s/def ::bdo
  (s/or
    :map-form (st-ds/spec ::bdo {:tag (tag :bdo)})
    :vector-form (s/cat :tag (tag :bdo))))
(s/def ::span
  (s/or
    :map-form (st-ds/spec ::span {:tag (tag :span)})
    :vector-form (s/cat :tag (tag :span))))
(s/def ::br
  (s/or
    :map-form (st-ds/spec ::br {:tag (tag :br)})
    :vector-form (s/cat :tag (tag :br))))
(s/def ::wbr
  (s/or
    :map-form (st-ds/spec ::wbr {:tag (tag :wbr)})
    :vector-form (s/cat :tag (tag :wbr))))

(s/def ::text-level-elements

  (s/or
    :a ::a
    :em ::em
    :strong ::strong
    :small ::small
    :s ::s
    :cite ::cite
    :q ::q
    :dfn ::dfn
    :abbr ::abbr
    :data ::data
    :time ::time
    :code ::code
    :var ::var
    :samp ::samp
    :kbd ::kbd
    :sub ::sub
    :sup ::sup
    :i ::i
    :b ::b
    :u ::u
    :mark ::mark
    :ruby ::ruby
    :rb ::rb
    :rt ::rt
    :rtc ::rtc
    :rp ::rp
    :bdi ::bdi
    :bdo ::bdo
    :span ::span
    :br ::br
    :wbr ::wbr
    :text ::text)
  #_{:description "text level html tags, the elements that must exists at the end of an element branch
                   a.k.a the inner most element"})

(s/def ::head
  (s/or
    :map-form (st-ds/spec ::head
                          {:tag     (tag :head)
                           :content (s/cat :title ::title
                                           :base (s/? ::base)
                                           :meta-element (s/+ ::meta-data-elements))})
    :vector-form (s/cat
                   :tag (tag :head)
                   :content (s/cat :title ::title
                                   :base (s/? ::base)
                                   :meta-element (s/+ ::meta-data-elements)))))

(s/def ::title
  (s/or
    :map-form (st-ds/spec ::title
                          {:tag     (tag :title)
                           :content ::text})
    :vector-form (s/cat
                   :tag (tag :title)
                   :content (st-c/spec ::text))))

(s/def ::base
  (s/or
    :map-form (st-ds/spec ::base {:tag (tag :base)})
    :vector-form (s/cat :tag (tag :base))))
(s/def ::link
  (s/or
    :map-form (st-ds/spec ::link {:tag (tag :link)})
    :vector-form (s/cat :tag (tag :link))))
(s/def ::meta
  (s/or
    :map-form (st-ds/spec ::meta {:tag (tag :meta)})
    :vector-form (s/cat :tag (tag :meta))))
(s/def ::style
  (s/or
    :map-form (st-ds/spec ::style {:tag (tag :style)})
    :vector-form (s/cat :tag (tag :style))))

(s/def ::meta-data-elements

  (s/alt
    :head ::head
    #_(comment :title ::title
               :base ::base)
    :link ::link
    :meta ::meta
    :style ::style)
  #_{:description "metadata level elements, elements that only belong in the head element"})

(s/def ::body
  (s/or
    :map-form (st-ds/spec ::body
                          {:tag                 (tag :body)
                           (st-ds/opt :content) (s/cat :flow-element (s/+ ::flow-elements))})
    :vector-form (s/cat
                   :tag (tag :body)
                   :content (s/cat :flow-element (s/+ ::flow-elements)))))

(s/def ::section
  (s/or
    :map-form (st-ds/spec ::section {:tag (tag :section)})
    :vector-form (s/cat :tag (tag :section))))
(s/def ::nav
  (s/or
    :map-form (st-ds/spec ::nav {:tag (tag :nav)})
    :vector-form (s/cat :tag (tag :nav))))
(s/def ::article
  (s/or
    :map-form (st-ds/spec ::article {:tag (tag :article)})
    :vector-form (s/cat :tag (tag :article))))
(s/def ::aside
  (s/or
    :map-form (st-ds/spec ::aside {:tag (tag :aside)})
    :vector-form (s/cat :tag (tag :aside))))
(s/def ::h1
  (s/or
    :map-form (st-ds/spec ::h1 {:tag (tag :h1)})
    :vector-form (s/cat :tag (tag :h1))))
(s/def ::h2
  (s/or
    :map-form (st-ds/spec ::h2 {:tag (tag :h2)})
    :vector-form (s/cat :tag (tag :h2))))
(s/def ::h3
  (s/or
    :map-form (st-ds/spec ::h3 {:tag (tag :h3)})
    :vector-form (s/cat :tag (tag :h3))))
(s/def ::h4
  (s/or
    :map-form (st-ds/spec ::h4 {:tag (tag :h4)})
    :vector-form (s/cat :tag (tag :h4))))
(s/def ::h5
  (s/or
    :map-form (st-ds/spec ::h5 {:tag (tag :h5)})
    :vector-form (s/cat :tag (tag :h5))))
(s/def ::h6
  (s/or
    :map-form (st-ds/spec ::h6 {:tag (tag :h6)})
    :vector-form (s/cat :tag (tag :h6))))
(s/def ::hgroup
  (s/or
    :map-form (st-ds/spec ::hgroup {:tag (tag :hgroup)})
    :vector-form (s/cat :tag (tag :hgroup))))
(s/def ::header
  (s/or
    :map-form (st-ds/spec ::header {:tag (tag :header)})
    :vector-form (s/cat :tag (tag :header))))
(s/def ::footer
  (s/or
    :map-form (st-ds/spec ::footer {:tag (tag :footer)})
    :vector-form (s/cat :tag (tag :footer))))
(s/def ::address
  (s/or
    :map-form (st-ds/spec ::address {:tag (tag :address)})
    :vector-form (s/cat :tag (tag :address))))

(s/def ::sectioning-elements

  (s/alt
    :body ::body
    :section ::section
    :nav ::nav
    :article ::article
    :aside ::aside
    :h1 ::h1
    :h2 ::h2
    :h3 ::h3
    :h4 ::h4
    :h5 ::h5
    :h6 ::h6
    :hgroup ::hgroup
    :header ::header
    :footer ::footer
    :address ::address)
  #_{:description "section level elements, elements that define/create section breaks within a html document"})

(s/def ::p (st-ds/spec ::p {:tag (tag :p)}))
(s/def ::hr (st-ds/spec ::hr {:tag (tag :hr)}))
(s/def ::pre (st-ds/spec ::pre {:tag (tag :pre)}))
(s/def ::blockquote (st-ds/spec ::blockquote {:tag (tag :blockquote)}))
(s/def ::ol (st-ds/spec ::ol {:tag (tag :ol)}))
(s/def ::ul (st-ds/spec ::ul {:tag (tag :ul)}))
(s/def ::li (st-ds/spec ::li {:tag (tag :li)}))
(s/def ::dl (st-ds/spec ::dl {:tag (tag :dl)}))
(s/def ::dt (st-ds/spec ::dt {:tag (tag :dt)}))
(s/def ::dd (st-ds/spec ::dd {:tag (tag :dd)}))
(s/def ::figure (st-ds/spec ::figure {:tag (tag :figure)}))
(s/def ::fig-caption (st-ds/spec ::fig-caption {:tag (tag :fig-caption)}))
(s/def ::div (st-ds/spec ::div {:tag (tag :div)}))

(s/def ::main
  (s/or
    :map-form (st-ds/spec ::main
                          {:tag                 (tag :main)
                           (st-ds/opt :content) (s/cat :flow-element (s/+ ::flow-elements))})
    :vector-form (s/cat
                   :tag (tag :main)
                   :content (s/cat :flow-element (s/+ ::flow-elements)))))

(s/def ::grouping-elements

  (s/or
    :p ::p
    :hr ::hr
    :pre ::pre
    :blockquote ::blockquote
    :ol ::ol
    :ul ::ul
    :li ::li
    :dl ::dl
    :dt ::dt
    :dd ::dd
    :figure ::figure
    :fig-caption ::fig-caption
    :div ::div
    :main ::main)
  #_{:description "group level elements, elements used to group together other elements"})

(s/def ::ins (st-ds/spec ::ins {:tag (tag :ins)}))
(s/def ::del (st-ds/spec ::del {:tag (tag :del)}))

(s/def ::edit-elements
  (s/or
    :ins ::ins
    :del ::del))

(s/def ::img (st-ds/spec ::img {:tag (tag :img)}))
(s/def ::iframe (st-ds/spec ::iframe {:tag (tag :iframe)}))
(s/def ::embed (st-ds/spec ::embed {:tag (tag :embed)}))
(s/def ::object (st-ds/spec ::object {:tag (tag :object)}))
(s/def ::param (st-ds/spec ::param {:tag (tag :param)}))
(s/def ::video (st-ds/spec ::video {:tag (tag :video)}))
(s/def ::audio (st-ds/spec ::audio {:tag (tag :audio)}))
(s/def ::source (st-ds/spec ::source {:tag (tag :source)}))
(s/def ::track (st-ds/spec ::track {:tag (tag :track)}))
(s/def ::map (st-ds/spec ::map {:tag (tag :map)}))
(s/def ::area (st-ds/spec ::area {:tag (tag :area)}))
(s/def ::math (st-ds/spec ::math {:tag (tag :math)}))
(s/def ::svg (st-ds/spec ::svg {:tag (tag :svg)}))

(s/def ::embedded-content-elements

  (s/or
    :img ::img
    :iframe ::iframe
    :embed ::embed
    :object ::object
    :param ::param
    :video ::video
    :audio ::audio
    :source ::source
    :track ::track
    :map ::map
    :area ::area
    :math ::math
    :svg ::svg)
  #_{:description "embedded content elements, elements used to handle external content"})

(s/def ::table (st-ds/spec ::table {:tag (tag :table)}))
(s/def ::caption (st-ds/spec ::caption {:tag (tag :caption)}))
(s/def ::colgroup (st-ds/spec ::colgroup {:tag (tag :colgroup)}))
(s/def ::col (st-ds/spec ::col {:tag (tag :col)}))
(s/def ::tbody (st-ds/spec ::tbody {:tag (tag :tbody)}))
(s/def ::thead (st-ds/spec ::thead {:tag (tag :thead)}))
(s/def ::tfoot (st-ds/spec ::tfoot {:tag (tag :tfoot)}))
(s/def ::tr (st-ds/spec ::tr {:tag (tag :tr)}))
(s/def ::td (st-ds/spec ::td {:tag (tag :td)}))
(s/def ::th (st-ds/spec ::th {:tag (tag :th)}))

(s/def ::tabular-data-elements

  (s/or
    :table ::table
    :caption ::caption
    :colgroup ::colgroup
    :col ::col
    :tbody ::tbody
    :thead ::thead
    :tfoot ::tfoot
    :tr ::tr
    :td ::td
    :th ::th)
  #_{:description "tabular level elements, elements used to handle tabular content"})

(s/def ::form (st-ds/spec ::form {:tag (tag :form)}))
(s/def ::label (st-ds/spec ::label {:tag (tag :label)}))
(s/def ::input (st-ds/spec ::input {:tag (tag :input)}))
(s/def ::button (st-ds/spec ::button {:tag (tag :button)}))
(s/def ::select (st-ds/spec ::select {:tag (tag :select)}))
(s/def ::datalist (st-ds/spec ::datalist {:tag (tag :datalist)}))
(s/def ::optgroup (st-ds/spec ::optgroup {:tag (tag :optgroup)}))
(s/def ::option (st-ds/spec ::option {:tag (tag :option)}))
(s/def ::textarea (st-ds/spec ::textarea {:tag (tag :textarea)}))
(s/def ::keygen (st-ds/spec ::keygen {:tag (tag :keygen)}))
(s/def ::output (st-ds/spec ::output {:tag (tag :output)}))
(s/def ::progress (st-ds/spec ::progress {:tag (tag :progress)}))
(s/def ::meter (st-ds/spec ::meter {:tag (tag :meter)}))
(s/def ::fieldset (st-ds/spec ::fieldset {:tag (tag :fieldset)}))
(s/def ::legend (st-ds/spec ::legend {:tag (tag :legend)}))

(s/def ::form-elements

  (s/or
    :form ::form
    :label ::label
    :input ::input
    :button ::button
    :select ::select
    :datalist ::datalist
    :optgroup ::optgroup
    :options ::option
    :textarea ::textarea
    :keygen ::keygen
    :output ::output
    :progress ::progress
    :meter ::meter
    :fieldset ::fieldset
    :legend ::legend)
  #_{:description "form level elements, elements used for input"})

(s/def ::details (st-ds/spec ::details {:tag (tag :details)}))
(s/def ::summary (st-ds/spec ::summary {:tag (tag :summary)}))
(s/def ::menu (st-ds/spec ::menu {:tag (tag :menu)}))
(s/def ::menuitem (st-ds/spec ::menuitem {:tag (tag :menuitem)}))
(s/def ::dialog (st-ds/spec ::dialog {:tag (tag :dialog)}))

(s/def ::interactive-elements

  (s/alt
    :details ::details
    :summary ::summary
    :menu ::menu
    :menuitem ::menuitem
    :dialog ::dialog))

(s/def ::script (st-ds/spec ::script {:tag (tag :script)}))
(s/def ::noscript (st-ds/spec ::noscript {:tag (tag :noscript)}))
(s/def ::template (st-ds/spec ::template {:tag (tag ::template)}))
(s/def ::canvas (st-ds/spec ::canvas {:tag (tag :canvas)}))

(s/def ::scripting-elements

  (s/alt
    :script ::script
    :noscript ::noscript
    :template ::template
    :canvas ::canvas))

(s/def ::flow-elements

  (s/alt
    :a ::a
    :abbr ::abbr
    :address ::address
    :area ::area
    :article ::article
    :aside ::aside
    :audio ::audio
    :b ::b
    :bdo ::bdo
    :bdi ::bdi
    :blockquote ::blockquote
    :br ::br
    :buttno ::button
    :canvas ::canvas
    :cite ::cite
    :code ::code
    #_(comment :command ::command)
    :data ::data
    :datalist ::datalist
    :del ::del
    :details ::details
    :dfn ::dfn
    :div ::div
    :dl ::dl
    :em ::em
    :embed ::embed
    :fieldset ::fieldset
    :figure ::figure
    :footer ::footer
    :form ::form
    :h1 ::h1
    :h2 ::h2
    :h3 ::h3
    :h4 ::h4
    :h5 ::h5
    :h6 ::h6
    :header ::header
    :hgroup ::hgroup
    :hr ::hr
    :i ::i
    :iframe ::iframe
    :img ::img
    :input ::input
    :ins ::ins
    :kbd ::kbd
    :keygen ::keygen
    :label ::label
    :link ::link
    :main ::main
    :map ::map
    :mark ::mark
    :math ::math
    :menu ::menu
    :meta ::meta
    :meter ::meter
    :nav ::nav
    :noscript ::noscript
    :object ::object
    :ol ::ol
    :output ::output
    :p ::p
    :pre ::pre
    :progress ::progress
    :q ::q
    :ruby ::ruby
    :s ::s
    :samp ::samp
    :script ::script
    :section ::section
    :select ::select
    :small ::small
    :span ::span
    :strong ::strong
    :style ::style
    :sub ::sub
    :sup ::sup
    :svg ::svg
    :table ::table
    :template ::template
    :textarea ::textarea
    :time ::time
    :ul ::ul
    :var ::var
    :video ::video
    :wbr ::wbr))

(s/def ::phrasing-elements

  (s/alt
    :a ::a
    :abbr ::abbr
    :area ::area
    :audio ::audio
    :b ::b
    :bdo ::bdo
    :br ::br
    :button ::button
    :canvas ::canvas
    :cite ::cite
    :code ::code
    #_(comment :command ::command)
    :data ::data
    :datalist ::datalist
    :del ::del
    :dfn ::dfn
    :em ::em
    :embed ::embed
    :i ::i
    :iframe ::iframe
    :img ::img
    :input ::input
    :ins ::ins
    :kbd ::kbd
    :keygen ::keygen
    :label ::label
    :map ::map
    :mark ::mark
    :mark ::math
    :meta ::meta
    :meter ::meter
    :noscript ::noscript
    :object ::object
    :output ::output
    :progress ::progress
    :q ::q
    :ruby ::ruby
    :samp ::samp
    :script ::script
    :select ::select
    :small ::small
    :span ::span
    :strong ::strong
    :sub ::sub
    :sup ::sup
    :svg ::svg
    :textarea ::textarea
    :time ::time
    :var ::var
    :video ::video
    :wbr ::wbr))

(s/def ::void-elements

  (s/alt
    :area ::area
    :base ::base
    :br ::br
    :col ::col
    #_(comment :command ::command)
    :embed ::embed
    :hr ::hr
    :img ::img
    :input ::input
    :keygen ::keygen
    :link ::link
    :meta ::meta
    :param ::param
    :source ::source
    :track ::track
    :wbr ::wbr))

(s/def ::html
  (s/or
    :map-form (st-ds/spec ::html
                          {:tag     (tag :html)
                           #_((st-ds/opt :attrs) (s/* ::global-event-attributes))
                           :content (s/cat :head ::head :body ::body)})
    :vector-form (s/cat
                   :tag (tag :html)
                   ;;fixme attributes currently prevent conformance
                   #_(:attrs (s/* ::global-event-attributes))
                   :content (s/cat
                              :head (st-c/spec ::head)
                              :body (st-c/spec ::body)))))

#_(def-html-spec ::html [::head ::body])


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; FULL SPEC CONFORMANCE TEST
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(s/conform ::html
           [:html
            [:head
             [:title "title"]
             [:base]
             [:meta]
             [:meta]
             [:link]
             [:link]
             [:style]
             [:style]]
            [:body
             [:article]
             [:article]
             [:section]
             [:nav]
             [:aside]
             [:h1]
             [:h2]
             [:h3]
             [:h4]
             [:h5]
             [:h6]
             [:hgroup]
             [:header]
             [:footer]
             [:address]]])

(s/explain-data ::html
                [:html
                 [:head
                  [:title "title"]
                  [:base]
                  [:meta]
                  [:meta]
                  [:link]
                  [:link]
                  [:style]
                  [:style]]
                 [:body
                  [:article]
                  [:article]
                  [:section]
                  [:aside]
                  [:nav]
                  [:h1]
                  [:h2]
                  [:h3]
                  [:h4]
                  [:h5]
                  [:h6]
                  [:hgroup]
                  [:header]
                  [:footer]
                  [:address]]])

(s/conform ::html
           {:tag     :html
            :content [{:tag     :head
                       :content [{:tag     :title
                                  :content "title"}]}
                      {:tag     :body
                       :content [{:tag     :main
                                  :content [{:tag :h1}]}]}]})


(s/explain-data ::html
                {:tag     :html
                 :content [{:tag     :head
                            :content [{:tag     :title
                                       :content "title"}]}
                           {:tag     :body
                            :content [{:tag     :main
                                       :content [{:tag :h1}]}]}]})

(s/def ::elements
  (st-c/spec
    (s/or
      ::flow ::flow-elements
      ::phrasing ::phrasing-elements)))

#_(gen/generate (s/gen ::html))
(comment
  (s/form ::a)
  (s/form ::text-level-elements)
  (st-c/conform ::text-level-elements {:tag :em})
  (st-c/conform ::text-level-elements [:em])
  #_(st-c/explain-data ::text-level-elements [:em])

  (s/form ::meta-data-elements)
  (st-c/conform ::text "hello")
  (st-c/conform ::a {:tag :a :content ["" ""]})
  (st-c/explain-data ::a {:tag :a :content ""})
  (st-c/conform ::a [:a [:em]])
  (st-c/explain-data ::a [:a [:em]])
  #_(st-c/conform ::a [:a])
  (st-c/conform ::a {:tag :a})

  ;;valid
  (s/conform ::flow-elements {:tag :a :content ""})
  ;;invalid
  (s/conform ::flow-elements [:a])
  ;;invalid - if I use s/alt
  (s/conform ::elements :a)
  ;;valid - if I use s/alt
  (s/conform ::elements [:a])

  (st-c/explain ::elements [:a ""])

  (s/form ::flow-elements)
  (s/form ::elements)

  (keys (st-c/registry)))

(s/def ::odds-then-maybe-even (s/cat :odds (s/+ odd?)
                                     :even (s/? even?)))
(s/conform ::odds-then-maybe-even [1 3 5 100])

;;;;;;;;;;;;;;;;;;;;;
;;; work in progress
;;;;;;;;;;;;;;;;;;;;;

(defn full-element?
  [tag attrs content]
  (if (and (keyword? tag) (map? attrs) (not (empty? content)))
    true
    false))

(defn basic-element?
  [tag content]
  (if (and (keyword? tag) (not (empty? content)))
    true
    false))

(defn full-vector-element?
  [[tag attrs & content]]
  (full-element? tag attrs content))

(defn basic-vector-element?
  [[tag & content]]
  (basic-element? tag content))

(defn basic-void-element?
  [[tag & content]]
  (if (and (keyword? tag) (nil? content))
    true
    false))

(defn full-void-element? [[tag attrs & content]]
 (if (and (keyword? tag) (map? attrs) (nil? content))
   true
   false))

(defn full-map-element?
  [{:keys [tag attrs content]}]
  (full-element? tag attrs content))

(defn basic-map-element?
  [{:keys [tag content]}]
  (basic-element? tag content))

;; doesn't work the way I thought, it takes something that conforms to the spec
;; and outputs the expected spec in a different format
;; so this doesn't take a vector and turn it into a map that matches the spec but
;; rather it would take a vector that conforms to a spec and output it as a map

;; okay loading whole file works now?
;(defn vector->map
;  ;;TODO working on nesting
;  ([_ conformed-map coll]
;   (merge conformed-map {:content (vector->map _ coll)}))
;  ([_ coll]
;   (cond
;     (nil? (first coll)) ""
;     ;;FIXME string part needs some work
;     (string? (first coll)) (if (= (count coll) 1) (first coll) (map vector->map _ coll))
;     (coll? coll) (try
;                    (let [[tag & content] coll
;                          attrs (first content)]
;                      (if (map? attrs)
;                        (vector->map _ {:tag tag :attrs attrs} content)
;                        (vector->map _ {:tag tag :attrs {}} content)))
;                    (catch Exception _
;                      ::s/invalid)))))

(defn vector->map1
  ;;use pattern matching, core.match?
  ([_ coll]
   (try
     (let [[tag & content] coll
           attrs (first content)]
       (println content)
       ;(if (keyword? tag)
       ;  (if (map? attrs)
       ;    {:tag tag :attrs attrs :content (reduce #(vector->map1 _ %) (rest content))}
       ;    {:tag tag :attrs {} :content (reduce #(vector->map1 _ %) content)})))
       (cond
         (full-vector-element? coll) {:tag tag :attrs attrs :content (map #(vector->map1 _ %) (rest content))}
         (basic-vector-element? coll) {:tag tag :attrs {} :content (map #(vector->map1 _ %) content)}
         (full-void-element? coll) {:tag tag :attrs attrs :content [nil]}
         (basic-void-element? coll) {:tag tag :attrs {} :content [nil]}
         (full-map-element? coll) coll
         (basic-map-element? coll) coll
         (vector? coll) (map #(vector->map1 _ %) coll)
         (string? coll) coll
         :else :invalid))

     (catch Exception _
       :invalid))))

(def vector->map-conformer
  (st-c/type-conforming {:map vector->map1}))

(def fake-tag
  (st-ds/spec
    ::fake-tag
    ;more concise over (s/and keyword? #{:fake-tag})
    ; but can't be simplified to just #{:fake-tag)
    {:tag               (s/spec #{:fake-tag})
     (st-ds/opt :attrs) map?
     :content           [(s/nilable (s/or :string string? :element (s/spec fake-tag)))]}
    {:description "fake tag"
     :reason      "doesn't match format"}))

(def fake-tag2
  (st-ds/spec
    ::fake-tag2
    (s/cat
      :tag (s/spec #{:fake-tag})
      :attr map?
      :content (s/coll-of string?))
    {:description "fake tag"
     :reason      "doesn't match format"}))

(s/def ::fake-tag3 (s/alt :fake-tag fake-tag :fake-tag2 fake-tag2))

(comment
  ;;testing the WIP specs
  (st-c/explain-data fake-tag {:tag :fake-tag :content ["hello"]})
  (st-c/explain-data fake-tag2 [:fake-tag])

  ;;simple case, base?
  (vector->map1 nil [:fake-tag])
  (vector->map1 nil [:fake-tag "fake-tag"])
  (vector->map1 nil [:fake-tag {:id "fake-tag" :class "fake-tag"} "fake-tag"])
  ;;complex case
  (vector->map1 nil {:tag :fake-tag :content ["hello" {:tag :fake-tag :content ["hello"]}]})
  (vector->map1 nil [:fake-tag "fake-tag" [:fake-tag "fake-tag"]])
  (vector->map1 nil [:fake-tag {:id "fake-tag" :class "fake-tag"} "fake-tag" [:fake-tag "fake-tag"]])

  (st-c/conform fake-tag {:tag :fake-tag :content [nil]} vector->map-conformer)
  (st-c/conform fake-tag {:tag :fake-tag :content ["hello"]} vector->map-conformer)
  (st-c/conform fake-tag {:tag :fake-tag :content ["hello" {:tag :fake-tag :content ["hello"]}]} vector->map-conformer)
  (st-c/conform fake-tag [:fake-tag] vector->map-conformer)
  (st-c/conform fake-tag [:fake-tag "hello"] vector->map-conformer)
  (st-c/conform fake-tag [:fake-tag {:id "fake-tag" :class "fake-tag"} "fake-tag"] vector->map-conformer)

  (st-c/explain-data fake-tag [:fake-tag "fake-tag"] vector->map-conformer)
  (st-c/explain-data fake-tag {:tag :fake-tag :content ["hello" {:tag :fake-tag :content ["hello"]}]} vector->map-conformer)

  (s/exercise ::fake-tag3)
  (gen/generate (s/gen fake-tag)))