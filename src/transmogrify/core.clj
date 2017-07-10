(ns transmogrify.core
  (:require [transmogrify.internal :refer :all]
            [transmogrify.wip :refer :all]
            #_[clojure.spec.alpha :as spec]
            #_[clojure.spec.test.alpha :as stest]
            [spec-tools.spec :as st]))

(spec/fdef 
  transmogrify 
  :args (spec/cat :language keyword? :data coll?)
  :ret str)

(defn transmogrify 
  "(transmogrify ::html [:html [:body [:p \"hello\"]]])
    => <!DOCTYPE html>
       <html>
         <body>
           <p>hello</p>
         </body>
       </html>"
  [language & data]
  (comment 
    "from the data we want an element of which is a tag and content a.k.a data/element"
    "we recurively transform the data and generate the apprioate file with contents"
    "try aim for multiple file transformation/optimisation outputs?"
    "take a leading tag as file name in datastructure?"
    (let [transformed-data (let [element (first data)
                                 tag (first element)
                                 content (second element)]
                             (if (!= content element?)
                               (transfrom-to-language tag content)
                               (recur language content)))]
      (output-to-file language transformed-data))) 
  
  (comment "using language argument lookup the respective algorithm"
    "the actual work is done by the transmogrifier")
  (transmogrifier language data))
  

(transmogrify ::html [:html])


#_(defn -main []
              (println "Hello, World!"))

(stest/check `transmogrify)
