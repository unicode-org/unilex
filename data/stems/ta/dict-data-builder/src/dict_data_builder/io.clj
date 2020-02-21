(ns dict-data-builder.io
  (:require [clj-thamil.format :as fmt]
            [clojure.string :as string])
  (:import [com.ibm.icu.lang UCharacter$UnicodeBlock]))

;;
;; parsing of input stream and input word entries
;;

(defn is-tamil-char
  "Returns whether the input char (or int) is in the Tamil Unicode block."
  [char]
  (boolean
   (when char
     (let [char-int (int char)
           tamil-block (UCharacter$UnicodeBlock/TAMIL)]
       (= tamil-block (UCharacter$UnicodeBlock/of char-int))))))

(defn is-tamil-line
  "Returns whether a string represents an entry of a Tamil word and its frequency based on the first character in the line."
  [line]
  (let [first-char (first line)]
    (is-tamil-char first-char)))

(defn lines->word-freqs
  "Given a seq of lines (strings), returns a seq of 2-elem seqs containing a string (word) and its frequency count."
  [lines]
  (let [filtered-lines (filter is-tamil-line lines)
        word-freq-seq (->> filtered-lines
                           (map #(string/split % #"\s+")))]
    word-freq-seq))

(defn lines->phoneme-freq-trie
  [lines]
  (let [word-freq-seq (lines->word-freqs lines)
        phonemes-freq-seq (->> word-freq-seq
                               (map #(vector (-> (first %) fmt/str->phonemes) (Integer/parseInt (second %)))))
        phonemes-freq-map (into {} phonemes-freq-seq)
        phonemes-trie (fmt/make-trie phonemes-freq-map)]
    phonemes-trie))

(defn instream->lines
  "Converts an InputStream into a seq of lines"
  [instream]
  (line-seq (java.io.BufferedReader. instream)))
