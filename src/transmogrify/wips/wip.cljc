(ns transmogrify.wips.wip
  (:require [transmogrify.internal :refer :all]
            #_[transmogrify.specs.html_spec :as html]
            [transmogrify.specs.html :as html1]
            [clojure.spec.alpha :as spec]
            [clojure.pprint :as pp]
            [clojure.string :as str]
            [clojure.test :as test]))

(comment
  1. want to check if the tag is voidable
  2. check if it has attributes
  3. check if it has content
  
  ;;TODO escape characters -> http://www.theukwebdesigncompany.com/articles/entity-escape-characters.php
  
  ;;FIXME require a spec for void tags such that if it has content produce an error along the lines of no content allowed and end completion
  
  (if (nil? content
         ">"
         (str ">" 
           ;;FIXME issues to do with arguments
           (str/join (map #(html-tag->str %) content))
           "</"(name tag)">"))))

;; FIXME name transmogrification instead of transmogrifier
(defmethod transmogrifier :html
  [_ data]
  #_{:pre [(spec/valid? :transmogrify.html/element data)]}
  (if-let [output (spec/conform :transmogrify.html/element data) #_data]
    output
    (spec/explain-data :transmogrify.html/element data)))

(transmogrifier :html [:html [:body [:p "hello"]]])
#_(transmogrifier :html [:html {} [:head [:title {}] [:body {} [:img {} ] [:p]]]])

(comment
  (defmethod transmogrifier ::html
    [_ & data]
    {:pre [#_(spec/valid? :transmogrify.html/element data)]
     :post [#_(spec/valid? string? %)]}
    
    (comment "first look up spec"
      (let [to-html (fn [element] (str "<" element "/>"))]
        (apply str "<!DOCTYPE html>"
          (if (map? (first data))
            (let [options (first data)
                  data (second data)]
              (comment "do something if options are present")
              (str options " "
                (map to-html data))) 
            (map to-html data)))))
    
    (let [data (if (map? (first data))
                 {:options (first data)
                  :data (second data)}
                 {:data (first data)})
          element (spec/conform :transmogrify.html/element (:data data))]
      (pp/pprint data)
      (pp/pprint element)
      
      (let [styles-map->str (fn [styles] 
                              (apply str 
                                (map (fn [[k v]] 
                                       (str (name k) ":" v ";")) 
                                  styles)))
            
            attrs-map->str (fn [attrs] 
                             (apply str " "
                               (map (fn [[k v]] 
                                      (str (str/lower-case (name k)) "=\"" 
                                        (cond 
                                          (map? v) (styles-map->str v)
                                          (sequential? v) (str/join " " v)
                                          :else v)
                                        "\" ")) 
                                 attrs)))
            
            voidable-tag? (fn [tag]
                            (let [tag-type (spec/or 
                                             :void :transmogrify.html/void-tags 
                                             :unescapable :transmogrify.html/unescapable-tags)
                                  conformed-tag (spec/conform tag-type tag)]
                              (if (spec/valid? tag-type tag)
                                (case (first conformed-tag)
                                  :void true
                                  :unescapable false)
                                false)))]
        
        ;;TODO unform/normalise element
        (let [unformed-element (spec/unform :transmogrify.html/element element)
              unformed-tag (:tag unformed-element)
              tag (name (second (:tag element)))
              attrs (:attrs element)
              content (:content element)]
          (str "\n<"tag
            (attrs-map->str attrs)
            (if (voidable-tag? tag)
              (if (nil? content)
                "/>"
                (str ">" 
                  #_(map #(transmogrifier ::html %) content)
                  element
                  unformed-element
                  content
                  "</"tag">"))
              (str "> </" tag ">"))))
        
        #_(if (= element :clojure.spec/invalid))
        (spec/explain-data :transmogrify.html/element element)
        element))))

;;;;;;; REMOVE ;;;;;;;


#_(do
    (transmogrifier ::html {:allow-custom true
                            :allow-any    false
                            :validate     true} [:html [:head] [:body {} ""]])

    (spec/valid? :transmogrify.html/element [:html [:head] [:body]]))


;;;;;;;;;;;;;;;;;;;;;

(defmethod transmogrifier :fuse
  [_ & data]
  (str data " fusing it up"))

(defmethod transmogrifier :java-fxml
  [_ & data]
  (comment "need to deal with java fxml imports")
  (str data " FX-ing things"))

(defmethod transmogrifier :default 
  [language & data] 
  (println "Do not recongise the language" language " or it lacks a transformation function implementation.") 
  data)


#_(transmogrifier {:language ::html :data "hello"})
#_(transmogrifier ::html5 "hello" "world")
