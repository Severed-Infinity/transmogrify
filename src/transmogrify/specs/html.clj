(ns transmogrify.specs.html
  (:require #_[clojure.spec.alpha :as spec] [clojure.spec.alpha :as spec]))


;;;;;;;;; HTML TAGS LISTING ;;;;;;;;;

(spec/def :transmogrify.html/void-tags
  #{:area 
    :base 
    :br 
    :col 
    :command 
    :embed 
    :hr 
    :img 
    :input 
    :keygen 
    :link 
    :meta
    :param 
    :source 
    :track 
    :wbr})

(spec/def :transmogrify.html/unescapable-tags #{:script :style})

(spec/def :transmogrify.html/basic-tags
  #{:html
    :title
    :body
    :head
    :h1
    :h2
    :h3
    :h4
    :h5
    :h6
    :p
    :br
    :hr})

(spec/def :transmogrify.html/formatting-tags
  #{:abbr
    :address
    :b
    :bdi
    :bdo
    :blockqoute
    :cite
    :code
    :del
    :dfn
    :em
    :ins
    :kbd
    :mark
    :meter
    :pre
    :progress
    :q
    :rp
    :rt
    :ruby
    :s
    :samp
    :small
    :strong
    :sub
    :sup
    :time
    :u
    :var
    :wbr})

(spec/def :transmogrify.html/forms-and-input-tags
  #{:form
    :input
    :textarea
    :button
    :select
    :optgroup
    :option
    :label
    :fieldset
    :legend
    :datalist
    :keygen
    :output})

(spec/def :transmogrify.html/frame-tags
  #{:iframe})

(spec/def :transmogrify.html/image-tags
  #{:img
    :map
    :area
    :canvas
    :figcaption
    :figure
    :picture})

(spec/def :transmogrify.html/audio-video-tags
  #{:audio
    :source
    :track
    :video})

(spec/def :transmogrify.html/link-tags
  #{:a
    :link
    :nav})

(spec/def :transmogrify.html/list-tags
  #{:ul
    :ol
    :li
    :dl
    :dt
    :dd
    :menu
    :menuitem})

(spec/def :transmogrify.html/table-tags
  #{:table
    :caption
    :th
    :tr
    :td
    :thead
    :tbody
    :tfoot
    :col
    :colgroup})

(spec/def :transmogrify.html/style-and-semantic-tags
  #{:style
    :dic
    :span
    :header
    :footer
    :main
    :section
    :article
    :aside
    :details
    :dialog
    :summary
    :data})

(spec/def :transmogrify.html/meta-tags
  #{:head
    :meta
    :base})
    
(spec/def :transmogrify.html/programming-tags
  #{:script
    :noscript
    :embed
    :object
    :param})

(spec/def :transmogrify.html/tags
  (spec/alt
    :basic :transmogrify.html/basic-tags
    :fomatting :transmogrify.html/formatting-tags
    :forms-and-input :transmogrify.html/forms-and-input-tags
    :frames :transmogrify.html/frame-tags
    :images :transmogrify.html/image-tags
    :audio-video :transmogrify.html/audio-video-tags
    :links :transmogrify.html/link-tags
    :list :transmogrify.html/list-tags
    :table :transmogrify.html/table-tags
    :style-and-semantic :transmogrify.html/style-and-semantic-tags
    :meta :transmogrify.html/meta-tags
    :programming :transmogrify.html/programming-tags))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;;;;;;;; ATTRS LISTING ;;;;;;;;;

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(spec/def :transmogrify.html/tag :transmogrify.html/tags)
(spec/def :transmogrify.html/attrs (spec/? map?))
(spec/def :transmogrify.html/content (spec/* (spec/or 
                                               :content string?
                                               :element :transmogrify.html/element)))
             
(spec/def :transmogrify.html/element (spec/cat 
                                       :tag :transmogrify.html/tag
                                       :attrs :transmogrify.html/attrs
                                       :content :transmogrify.html/content))
    
;;; HTML SPEC TESTING ;;;
(def testing [:html {} [:head [:title "hello"]]])
(spec/valid? :transmogrify.html/element testing)
(spec/explain-data :transmogrify.html/element testing)

(spec/conform :transmogrify.html/element testing)
