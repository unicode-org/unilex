(ns dict-data-builder.noun
  (:require [clj-thamil.format :as fmt]
            [dict-data-builder.inflection :as infl]
            [dict-data-builder.trie :as t]))

(defn get-nouns-at-loc
  "return all the nouns detected at the current location in the zipper"
  [z]
  (let [phonemes (t/parent-path z)]
    (when (t/is-at-terminus z)
      (let [word (fmt/phonemes->str phonemes)
            all-noun-inflections (infl/all-inflections word)
            root-z (t/zipper-at-root-pos z)
            inflections-in-trie (filter #(t/contains-terminal-sub-paths root-z (vector (fmt/str->phonemes %))) all-noun-inflections)]
        (when (pos? (count inflections-in-trie))
          #{word})))))
