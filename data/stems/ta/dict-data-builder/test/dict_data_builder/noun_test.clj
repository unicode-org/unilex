(ns dict-data-builder.noun-test
  (:require [clj-thamil.format :as fmt]
            [clojure.test :refer :all]
            [dict-data-builder.noun :refer :all]
            [dict-data-builder.trie :as t]))


(deftest get-nouns-at-loc-test
  (testing "get-nouns-at-loc"
    (let [words-map {"ஆறு" nil
                     "ஆறினான்" nil
                     "ஆற்றுக்கு" nil}
          words-as-phonemes-map (reduce-kv #(assoc %1 (fmt/str->phonemes %2) %3) {} words-map)
          trie (fmt/make-trie words-as-phonemes-map)
          tz1 (t/trie-zipper trie)
          tz2 (t/descend-trie-zipper tz1 ["ஆ" "ற்" "உ"])
          tz3 (t/descend-trie-zipper tz1 ["ஆ" "ற்"])]
      (is (empty? (get-nouns-at-loc tz1)))
      (is (= #{"ஆறு"} (get-nouns-at-loc tz2)))
      (is (empty? (get-nouns-at-loc tz3))))))
