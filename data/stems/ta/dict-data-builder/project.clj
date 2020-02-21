(defproject dict-data-builder "0.1.0"
  :description "Derive POS-tagged lists of words (verbs/nouns) for Tamil from an input list of words from the Unilex project."
  :license {:name "Unicode License"
            :url "https://www.unicode.org/license.html"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/tools.cli "0.4.2"]
                 [clj-thamil "0.2.0"]
                 [com.ibm.icu/icu4j "60.1"]
                 [org.flatland/ordered "1.5.7"]]
  :main dict-data-builder.core)
