(ns dict-data-builder.core
  (:require [clj-thamil.format :as fmt]
            [clojure.java.io :as jio]
            [clojure.string :as string]
            [clojure.tools.cli :as cli]
            [clojure.tools.reader :as rdr-tools]
            [clojure.zip :as z]
            [dict-data-builder.conjugation :as cjg]
            [dict-data-builder.data :as data :refer [map->Verb ->Verb]]
            [dict-data-builder.io :as io]
            [dict-data-builder.noun :as noun]
            [dict-data-builder.noun-stem :as noun-stem]
            [dict-data-builder.trie :as t]
            [dict-data-builder.verb :as verb]
            [dict-data-builder.verb-stem :as verb-stem]))

;;
;; remove non-Tamil content from input
;;

(defn read-input-print-only-tamil-words
  []
  (let [lines (io/instream->lines *in*)
        ta-lines (filter io/is-tamil-line lines)]
     (doseq [l ta-lines]
       (println l))))

;;
;; detection for all words (verbs, etc.)
;;

(defn find-words
  [phoneme-trie get-words-fn]
  (let [zipper (t/trie-zipper phoneme-trie)]
    (loop [z zipper
           word-z-seq #{}]
      (if (z/end? z)
        word-z-seq
        (let [new-word-z-seq (let [new-words (get-words-fn z)]
                               (if (seq new-words)
                                 (apply conj word-z-seq new-words)
                                 word-z-seq))
              new-z (z/next z)]
          (recur new-z new-word-z-seq))))))

(defn read-find-print-words
  [get-words-fn]
  ;; do something by passing in *in* as the input stream
  (let [lines (io/instream->lines *in*)
        trie (io/lines->phoneme-freq-trie lines)
        words (find-words trie get-words-fn)] 
    (doseq [w words]
      (prn w))))

;;
;; detection of verbs
;;

(defn distinct-verbs
  "Return a distinct seq of verbs, using the equality fn verb-equals?, but also preferring verbs with more fields. Order not preserved."
  [all-verbs]
  (letfn [(verb-group-by-fn
            [v]
            (select-keys v data/VERB-COMPARE-KEYS))]
    (let [verbs-map (group-by verb-group-by-fn all-verbs)]
      (for [[canonical-verb matching-verb-vals] verbs-map]
        (let [matching-verbs-by-field-count (sort-by count matching-verb-vals)
              preferred-verb (last matching-verbs-by-field-count)]
          preferred-verb)))))

(defn find-verbs
  "Convenience fn to help migrate verbs_test.clj code to use parameterized version of find-words fn"
  [phoneme-trie]
  (let [found-verbs-set (find-words phoneme-trie verb/get-verbs-at-loc)]
    found-verbs-set))

(defn find-verbs-with-adjustments
  [phoneme-trie]
  (let [initial-verb-set (find-verbs phoneme-trie)
        all-verbs (->> initial-verb-set
                       (remove verb/FALSE-POSITIVE-VERBS)
                       (concat verb/MANUAL-INCLUDES)
                       (distinct-verbs)
                       (sort data/verb-comparator))]
    all-verbs))

(defn read-find-print-verbs
  "Convenience fn to wrap find-verbs with the reading of input and printing of output"
  []
  ;; do something by passing in *in* as the input stream
  (let [lines (io/instream->lines *in*)
        trie (io/lines->phoneme-freq-trie lines)
        words (find-verbs-with-adjustments trie)] 
    (doseq [w words]
      (prn w))))

;;
;; stemming of verbs
;;

(defn read-verbs-print-stems
  "Read input, determine the verb stems, and print to output"
  []
  (let [lines (io/instream->lines *in*)
        verbs (map rdr-tools/read-string lines)
        ;;verb-stems (sort data/verb-comparator verbs)        
        verb-stems (->> (verb-stem/verbs->stems verbs)
                        (sort data/verb-comparator))]
    (doseq [v verb-stems]
      (prn v))))

;;
;; removing verbs from input
;;

(defn remove-verbs-from-words
  "With a list of the original words and a list of the stemmed verbs, return the words with all verbs and their conjugations removed"
  [words verbs]
  (let [all-conjugations (->> (map cjg/all-conjugations-with-verb-transform verbs)
                              (map #(map :conjugation %))
                              (apply concat)
                              (map (fn [s] (if (string? s) [s] s)))
                              (apply concat))
        conjugations-set (->> all-conjugations
                              (into #{}))
        words-without-verbs (remove conjugations-set words)]
    words-without-verbs))

(defn read-input-read-verbs-print-nonverbs
  [input-file-name verbs-file-name]
  (with-open [lines-rdr (jio/reader input-file-name)
              verbs-rdr (jio/reader verbs-file-name)]
    (let [lines (line-seq lines-rdr)
          verbs (->> (line-seq verbs-rdr)
                     (map rdr-tools/read-string))
          words (->> lines
                     io/lines->word-freqs
                     (map first))
          words-without-verbs (remove-verbs-from-words words verbs)]
      (doseq [w words-without-verbs]
        (println w)))))

;;
;; detection of nouns
;;

(defn read-nonverbs-print-nouns
  []
  (let [lines (io/instream->lines *in*)
        trie (->> lines
                  (map fmt/str->phonemes)
                  (map #(vector % nil))
                  (into {})
                  fmt/make-trie)
        words (find-words trie noun/get-nouns-at-loc)] 
    (doseq [w words]
      (prn w))))

;;
;; stemming of nouns
;;

(defn read-nouns-print-stems
  []
  (let [lines (io/instream->lines *in*)
        nouns (map rdr-tools/read-string lines)
        noun-stems (->> (noun-stem/nouns->stems nouns)
                        (sort fmt/word-comp))]
    (doseq [v noun-stems]
      (prn v))))

;;
;; main execution
;;

;; taken from clojure.tools.cli
(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn print-help-msg
  []
  (let [arg-usages {"input-ta-only" "cat ta.txt | lein run input-ta-no-eng > ta-no-eng.txt"
                    "verbs1" "cat ta-no-eng.txt | lein run verbs1 > verbs1.txt"
                    "verbs2" "cat verbs1.txt | lein run verbs2 > verbs2.txt"
                    "input-no-verbs" "lein run input-no-verbs ta-no-eng.txt verbs2.txt > no-verbs.txt"
                    "nouns1" "cat no-verbs.txt | lein run nouns1 > nouns1.txt"
                    "nouns2" "cat nouns1.txt | lein run nouns2 > nouns2.txt"}
        desc "Process list of Tamil words in stages into verbs and nouns"
        longest-command-length (->> arg-usages
                                    keys
                                    (apply max-key count)
                                    count)
        msg (->> (concat [desc
                          ""
                          "Usage:"]
                         (for [[cmd-name cmd-usage] arg-usages]
                           (str "    "
                                (format (str "%-" longest-command-length "s") cmd-name)
                                "    "
                                cmd-usage)))
                 (string/join "\n"))]
    (exit 1 msg)))

(defn get-cli-options
  [original-args]
  (let [cli-options-config [["-h" "--help"]]
        parsed-options-map (cli/parse-opts original-args cli-options-config :in-order true)]
    (when-not (pos? (count (:arguments parsed-options-map)))
      (print-help-msg))
    parsed-options-map))

(defn -main [& args]
  (let [parsed-options-map (get-cli-options args)
        parsed-args (:arguments parsed-options-map)]
    (let [first-arg (first parsed-args)]
      (case first-arg
        "input-ta-only" (read-input-print-only-tamil-words) ;; cat ta.txt | lein run input-ta-no-eng > ta-no-eng.txt
        "verbs1" (read-find-print-verbs) ;; cat ta-no-eng.txt | lein run verbs1 > verbs1.txt
        "verbs2" (read-verbs-print-stems) ;; cat verbs1.txt | lein run verbs2 > verbs2.txt
        "input-no-verbs" (apply read-input-read-verbs-print-nonverbs (rest parsed-args)) ;; lein run input-no-verbs ta-no-eng.txt verbs2.txt > no-verbs.txt
        "nouns1" (read-nonverbs-print-nouns) ;; cat no-verbs.txt | lein run nouns > nouns1.txt
        "nouns2" (read-nouns-print-stems) ;; cat nouns1.txt | lein run nouns2 > nouns2.txt
        (print-help-msg)))))


;; TODO:
;; - update the definition of Verb and conjugation logic to have enough info to fix conjugations like உண்டுகொளாமல்
;; - create record definitions for noun and noun inflections (similar to verb conjugations)
;;  * and also update the definiton for noun to support plurals like செங்கல் -> செங்கற்கள் (and not produce செங்கல்கள்)
;; - fix trie.clj to support making a trie zipper out of a trie made from a seq (instead of only supporting a map)
