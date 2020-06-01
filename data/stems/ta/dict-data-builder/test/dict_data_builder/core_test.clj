(ns dict-data-builder.core-test
  (:require [clojure.test :refer :all]
            [clojure.zip :as z]
            [dict-data-builder.core :refer :all]
            [dict-data-builder.data :as data :refer [map->Verb]]
            [dict-data-builder.io :as io]
            [dict-data-builder.trie :as t]
            [dict-data-builder.verb :as verb])
  (:import dict_data_builder.data.Verb))

(deftest distinct-verbs-test
  (let [verbs [#dict_data_builder.data.Verb{:root "வில்", :verb-class "5"}
               #dict_data_builder.data.Verb{:root "வில்", :verb-class "5", :infinitive "விற்க", :avp "விற்று", :past-stem "விற்ற்", :present-stem "விற்கிற்", :future-stem "விற்ப்"}
               #dict_data_builder.data.Verb{:root "வில்", :verb-class "5"}]
        exp-distinct-verbs [#dict_data_builder.data.Verb{:root "வில்", :verb-class "5", :infinitive "விற்க", :avp "விற்று", :past-stem "விற்ற்", :present-stem "விற்கிற்", :future-stem "விற்ப்"}]]
    (is (= (distinct-verbs verbs)
           exp-distinct-verbs)))
  )
