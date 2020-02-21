(ns dict-data-builder.verb
  (:require [clj-thamil.format :as fmt]
            [clj-thamil.மொழியியல் :as மொழி]
            [clojure.set :as set]
            [clojure.zip :as z]
            [dict-data-builder.data :as data :refer [map->Verb]]
            [dict-data-builder.trie :as t])
  (:import dict_data_builder.data.Verb))

;;
;; Tamil verb detection
;;

;; verb endings

(defn ends-in-verb-ending
  [z]
  (letfn [(ends-in-this-ending
            [ending-seq]
            (t/contains-terminal-sub-paths z (vector ending-seq)))]
    (let [verb-endings #{["ஏ" "ன்"]
                         ["ஆ" "ய்"]
                         ["ஈ" "ர்"]
                         ["ஈ" "ர்" "க்" "அ" "ள்"]
                         ["ஆ" "ன்"]
                         ["ஆ" "ள்"]
                         ["ஆ" "ர்"]
                         ["அ" "த்" "உ"]
                         ["ஓ" "ம்"]
                         ["ஆ" "ர்" "க்" "அ" "ள்"]}
          ends-in-some-ending (some ends-in-this-ending verb-endings)]
      (boolean ends-in-some-ending))))

;; weak verb

(defn can-infer-weak-verb-pres-fut-inf
  [z]
  (or (let [phonemes (t/parent-path z)
            last-phoneme (last phonemes)
            ;; handle verbs that might add ய் for infinitive
            adj-z (or (when (and (contains? #{"இ" "ஈ" "ஏ" "ஐ"} last-phoneme)
                                 (z/branch? z))
                        (t/descend-trie-zipper z ["ய்"]))
                      z)]
        ;; infinitive or future verbal adjective or inanimate future tense
        (or (t/contains-terminal-sub-paths adj-z [["அ"]])
            (t/contains-terminal-sub-paths adj-z [["உ" "ம்"]])))
      ;; verbs ending in -உ
      (let [phonemes (t/parent-path z)]
        (when (= "உ" (last phonemes))
          (or (t/contains-terminal-sub-paths z [["ம்"]])
              (t/contains-terminal-sub-paths (z/up z) [["அ"]]))))
      (some-> z
              (t/descend-trie-zipper ["க்" "இ" "ற்"])
              (ends-in-verb-ending))
      (some-> z 
              (t/descend-trie-zipper ["க்" "இ" "ன்" "ற்"])
              (ends-in-verb-ending))
      ;; in the case tha tclass 3 & 4 verbs may not have their
      ;; verb root (containing -உ) in the word list
      (some-> z
              (t/descend-trie-zipper ["உ" "வ்"])
              (ends-in-verb-ending))
      (some-> z
              (t/descend-trie-zipper ["வ்"])
              (ends-in-verb-ending))
      ;; handle the infinitive in the case of 1-syllable short CVC words (ex: செய், சொல், வெல், கொல், etc.)
      ;; and also any compound words based on them
      (let [phonemes (t/parent-path z)
            word (fmt/phonemes->str phonemes)
            all-letters (fmt/str->letters word)
            last-2-letters (take-last 2 all-letters)
            last-2-letter-phoneme-seq (-> (apply str last-2-letters)
                                          fmt/str->phonemes)
            letters last-2-letters]
        (and (or (= 2 (count all-letters))
                 (-> z z/root t/trie-zipper (t/contains-terminal-sub-paths [last-2-letter-phoneme-seq])))
             (மொழி/குறிலா? (first letters))
             (மொழி/மெய்யெழுத்தா? (second letters))
             (t/contains-terminal-sub-paths z [[(second letters) "அ"]])))))

;; strong verb

(defn can-infer-strong-verb-pres-fut-inf
  [z]
  (or (t/contains-terminal-sub-paths z [["க்" "க்" "அ"]])
      (some-> z
              (t/descend-trie-zipper ["க்" "க்" "உ" "ம்"])
              (ends-in-verb-ending))
      (some-> z
              (t/descend-trie-zipper ["க்" "க்" "இ" "ற்"])
              (ends-in-verb-ending))
      (some-> z
              (t/descend-trie-zipper ["க்" "க்" "இ" "ன்" "ற்"])
              (ends-in-verb-ending))
      (some-> z 
              (t/descend-trie-zipper ["ப்" "ப்"])
              (ends-in-verb-ending))))

;; verb class 1a

(defn can-infer-verb-class-1a-pres-fut-inf
  [z]
  (can-infer-weak-verb-pres-fut-inf z))

(defn can-infer-verb-class-1a-past
  [z]
  (or (t/contains-terminal-sub-paths z [["த்" "உ"]])
      (t/contains-terminal-sub-paths z [["த்" "அ"]])
      (and (some-> z
                   (t/descend-trie-zipper ["த்"])
                   (ends-in-verb-ending))
           ;; exclude the -தான் suffix because of 3rd person inanimate reflexive
           (not (t/contains-terminal-sub-paths z [["த்" "ஆ" "ன்"]])))))

(defn can-infer-verb-class-1a
  [z]
  (and (can-infer-verb-class-1a-pres-fut-inf z)
       (can-infer-verb-class-1a-past z)))

(defn is-verb-class-1a
  [z]
  (let [phonemes (t/parent-path z)]
    (and (= "ய்" (last phonemes))
         (can-infer-verb-class-1a z))))

(defn can-get-verb-class-1a-at-loc
  [z]
  (can-infer-verb-class-1a z))

;; verb class 1b

(defn can-infer-verb-class-1b-pres-fut-inf
  [z]
  (let [phonemes (t/parent-path z)]
    (and (= "ள்" (last phonemes)) 
         (can-infer-weak-verb-pres-fut-inf z))))

(defn can-infer-verb-class-1b-past
  [z]
  (or (t/contains-terminal-sub-paths z [["ண்" "ட்" "உ"]])
      (t/contains-terminal-sub-paths z [["ண்" "ட்" "அ"]])
      (some-> z
              (t/descend-trie-zipper ["ண்" "ட்"])
              (ends-in-verb-ending))))

(defn can-infer-verb-class-1b
  [z]
  (and (can-infer-verb-class-1b-pres-fut-inf z)
       (can-infer-verb-class-1b-past (z/up z))))

(defn can-get-verb-class-1b-at-loc
  [z]
  (can-infer-verb-class-1b z))

;; verb class 1c

(defn can-infer-verb-class-1c-pres-fut-inf
  [z]
  (let [phonemes (t/parent-path z)]
    (and (= "ல்" (last phonemes)) 
         (can-infer-weak-verb-pres-fut-inf z))))

(defn can-infer-verb-class-1c-past
  [z]
  (or (t/contains-terminal-sub-paths z [["ன்" "ற்" "உ"]])
      (t/contains-terminal-sub-paths z [["ன்" "ற்" "அ"]])
      (some-> z
              (t/descend-trie-zipper ["ன்" "ற்"])
              (ends-in-verb-ending))))

(defn can-infer-verb-class-1c
  [z]
  (and (can-infer-verb-class-1c-pres-fut-inf z)
       (can-infer-verb-class-1c-past (z/up z))))

(defn can-get-verb-class-1c-at-loc
  [z]
  (can-infer-verb-class-1c z))

;; verb class 2

(defn can-infer-verb-class-2-pres-fut-inf
  [z]
  (can-infer-weak-verb-pres-fut-inf z))

(defn can-infer-verb-class-2-past
  [z]
  (t/contains-sub-path z ["ந்" "த்"]))

(defn can-infer-verb-class-2
  [z]
  (and (can-infer-verb-class-2-pres-fut-inf z)
       (can-infer-verb-class-2-past z)))

(defn can-get-verb-class-2-at-loc
  [z]
  (can-infer-verb-class-2 z))

;; verb class 3

(defn can-infer-verb-class-3-pres-fut-inf
  [z]
  (if-let [verb-root-z (t/descend-trie-zipper z ["உ"])]
    (can-infer-weak-verb-pres-fut-inf verb-root-z)
    (t/contains-terminal-sub-paths z [["அ"]])))

(defn can-infer-verb-class-3-past
  [z]
  (or (t/contains-terminal-sub-paths z [["இ"]])
      (t/contains-terminal-sub-paths z [["இ" "ய்" "அ"]])
      (t/contains-terminal-sub-paths z [["இ" "ன்" "அ"]])
      (some-> z
              (t/descend-trie-zipper ["இ" "ய்"])
              (ends-in-verb-ending))
      (some-> z
              (t/descend-trie-zipper ["இ" "ன்"])
              (ends-in-verb-ending))))

(defn can-infer-verb-class-3
  [z]
  (and (can-infer-verb-class-3-pres-fut-inf z)
       (can-infer-verb-class-3-past z)))

(defn is-verb-class-3
  [z]
  (let [phonemes (t/parent-path z)]
    (and (= "உ" (last phonemes))
         (can-infer-verb-class-3 (z/up z)))))

(defn can-get-verb-class-3-at-loc
  [z]
  (or (is-verb-class-3 z)
      (can-infer-verb-class-3 z)))

;; verb class 4

(defn can-infer-verb-class-4-pres-fut-inf
  [z]
  (can-infer-weak-verb-pres-fut-inf z))

(defn can-infer-verb-class-4-past
  [z]
  (let [phonemes (t/parent-path z)
        last-phoneme (last phonemes)]
    ;; class 4 verb examples: போடு, பெறு, நகு
    (and (contains? #{"ட்" "ற்" "க்"} last-phoneme)
         (or (t/contains-terminal-sub-paths z [[last-phoneme "உ"]])
             (t/contains-terminal-sub-paths z [[last-phoneme "அ"]])
             (some-> z
                     (t/descend-trie-zipper [last-phoneme])
                     (ends-in-verb-ending))))))

(defn can-infer-verb-class-4
  [z]
  (and (can-infer-verb-class-4-pres-fut-inf z)
       (can-infer-verb-class-4-past z)))

(defn is-verb-class-4
  [z]
  (let [phonemes (t/parent-path z)]
    (and (= "உ" (last phonemes))
         (can-infer-verb-class-4 (z/up z)))))

(defn can-get-verb-class-4-at-loc
  [z]
  (or (is-verb-class-4 z)
      (can-infer-verb-class-4 z)))

;; verb class 5

(defn can-infer-verb-class-5-pres-fut-inf
  [z]
  (let [phonemes (t/parent-path z)
        last-phoneme (last phonemes)]
    (and (get #{"ற்" "ட்"} last-phoneme)
         (or (t/contains-terminal-sub-paths z [["க்" "அ"]])
             (t/contains-terminal-sub-paths z [["க்" "உ" "ம்"]])
             (some-> z
                     (t/descend-trie-zipper ["க்" "இ" "ற்"])
                     (ends-in-verb-ending))
             (some-> z 
                     (t/descend-trie-zipper ["க்" "இ" "ன்" "ற்"])
                     (ends-in-verb-ending))
             (some-> z
                     (t/descend-trie-zipper ["ப்"])
                     (ends-in-verb-ending))))))

(defn can-infer-verb-class-5-past
  [z]
  (let [phonemes (t/parent-path z)
        last-phoneme (last phonemes)]
    (and)
    (or (t/contains-sub-path z [last-phoneme "உ"])
        (t/contains-sub-path z [last-phoneme "அ"])
        (some-> z
              (t/descend-trie-zipper [last-phoneme])
              (ends-in-verb-ending)))))

(defn can-infer-verb-class-5
  [z]
  (and (can-infer-verb-class-5-pres-fut-inf z)
       (can-infer-verb-class-5-past z)))

(defn can-get-verb-class-5-at-loc
  [z]
  (can-infer-verb-class-5 z))

(defn transform-class-5-phonemes
  ;; adjust class 5 verbs to the correct verb root. this is not the
  ;; full list of known class 5 verbs.
  [phonemes]
  (letfn [(seq-suffix? [src tgt] (fmt/seq-prefix? (reverse src) (reverse tgt)))]
    (cond
      (seq-suffix? phonemes ["ஏ" "ற்"]) (concat (drop-last 2 phonemes) ["ஏ" "ல்"])
      (seq-suffix? phonemes ["வ்" "இ" "ற்"]) (concat (drop-last 3 phonemes) ["வ்" "இ" "ல்"])
      (seq-suffix? phonemes ["க்" "ஏ" "ட்"]) (concat (drop-last 3 phonemes) ["க்" "ஏ" "ள்"])
      (seq-suffix? phonemes ["க்" "அ" "ற்"]) (concat (drop-last 3 phonemes) ["க்" "அ" "ல்"])
      (seq-suffix? phonemes ["ம்" "ஈ" "ட்"]) (concat (drop-last 3 phonemes) ["ம்" "ஈ" "ள்"])
      :else phonemes)))

;; verb class 6

(defn can-infer-verb-class-6-pres-fut-inf
  [z]
  (can-infer-strong-verb-pres-fut-inf z))

(defn can-infer-verb-class-6-past
  [z]
  (or (t/contains-terminal-sub-paths z [["த்" "த்" "உ"]])
      (t/contains-terminal-sub-paths z [["த்" "த்" "அ"]])
      (some-> z
              (t/descend-trie-zipper ["த்" "த்"])
              (ends-in-verb-ending))))

(defn can-infer-verb-class-6
  [z]
  (and (can-infer-verb-class-6-pres-fut-inf z)
       (can-infer-verb-class-6-past z)))

(defn can-get-verb-class-6-at-loc
  [z]
  (can-infer-verb-class-6 z))

;; verb class 7

(defn can-infer-verb-class-7-pres-fut-inf
  [z]
  (can-infer-strong-verb-pres-fut-inf z))

(defn can-infer-verb-class-7-past
  [z]
  (and (or (t/contains-terminal-sub-paths z [["ந்" "த்" "உ"]])
           (t/contains-terminal-sub-paths z [["ந்" "த்" "அ"]])
           (some-> z
                   (t/descend-trie-zipper ["ந்" "த்"])
                   (ends-in-verb-ending)))
       ;; prevent mistakenly detecting classes 2, 6, and 7
       ;; in the case of a transitive/intransitive pair of
       ;; verbs in classes 2 & 6
       (not (can-infer-verb-class-6-past z))))

(defn can-infer-verb-class-7
  [z]
  (and (can-infer-verb-class-7-pres-fut-inf z)
       (can-infer-verb-class-7-past z)))

(defn can-get-verb-class-7-at-loc
  [z]
  (can-infer-verb-class-7 z))

;;
;; all verb classes' getters
;;
;; the reson we put them here is the check for inference for each verb
;; class is defined above and the getters start with the check for
;; inference, but sometimes the inference for one verb class should
;; depend on another
;;

(defn get-verb-class-3-at-loc
  [z]
  (when (can-get-verb-class-3-at-loc z)
    (let [phonemes (t/parent-path z)
          new-phonemes (cond
                         (= "உ" (last phonemes))
                         phonemes

                         (= "அ" (last phonemes))
                         (concat (butlast phonemes) ["உ"])

                         (= "இ" (last phonemes))
                         (concat (butlast phonemes) ["உ"])

                         :else
                         (concat phonemes ["உ"]))
          word (fmt/phonemes->str new-phonemes)]
      (map->Verb {:root word
                  :verb-class "3"}))))

(defn get-verb-class-4-at-loc
  [z]
  (when (and (can-get-verb-class-4-at-loc z)
             (not (can-get-verb-class-5-at-loc z)))
    (let [phonemes (t/parent-path z)
          new-phonemes (concat phonemes ["உ"])
          word (fmt/phonemes->str new-phonemes)]
      (map->Verb {:root word
                  :verb-class "4"}))))

(defn get-verb-class-5-at-loc
  [z]
  (when (can-get-verb-class-5-at-loc z)
    (let [phonemes (t/parent-path z)
          adjusted-phonemes (transform-class-5-phonemes phonemes)
          word (fmt/phonemes->str adjusted-phonemes)]
      (map->Verb {:root word
                  :verb-class "5"}))))


(defn get-verb-class-6-at-loc
  [z]
  (when (can-get-verb-class-6-at-loc z)
    (let [phonemes (t/parent-path z)
          new-phonemes (cond
                         (= ["த்" "த்"] (take-last 2 phonemes))
                         (drop-last 2 phonemes)

                         (= "த்" (last phonemes))
                         (butlast phonemes)

                         :else
                         phonemes)
          word (fmt/phonemes->str phonemes)]
      (map->Verb {:root word
                  :verb-class "6"}))))


;; TODO: handle irregular verbs (வா, போ, சொல், சா, ஆ)

;; all verb classes

(def ^{:doc "manually updated list of false positives. Many FPs occur due to the similarity between a pair of nouns in a way that matches the pattern of verb roots & conjugations. Other FPs are similarly caused by a noun-verb pair or a pair of nouns that exhibit patterns of verb conjugation without prior knowledge of language vocabulary."}
  FALSE-POSITIVE-VERBS
  #{(map->Verb {:root "", :verb-class "1a"})
    (map->Verb {:root "அகாலு", :verb-class "3"})
    (map->Verb {:root "அடிகு", :verb-class "4"})
    (map->Verb {:root "அடுத்தகடு", :verb-class "4"})
    (map->Verb {:root "அடுத்தக்கடு", :verb-class "4"})
    (map->Verb {:root "அதிகாரு", :verb-class "3"})
    (map->Verb {:root "அனுசரிகு", :verb-class "4"})
    (map->Verb {:root "அனுபவிகு", :verb-class "4"})
    (map->Verb {:root "அபகரிகு", :verb-class "4"})
    (map->Verb {:root "அமரிகு", :verb-class "4"})
    (map->Verb {:root "அமெரிகு", :verb-class "4"})
    (map->Verb {:root "அமைகு", :verb-class "4"})
    (map->Verb {:root "அரசு", :verb-class "3"})
    (map->Verb {:root "அரி", :verb-class "1a"})
    (map->Verb {:root "அறிவிகு", :verb-class "4"})
    (map->Verb {:root "அறு", :verb-class "3"})
    (map->Verb {:root "அறு", :verb-class "4"})
    (map->Verb {:root "அல்", :verb-class "1c"})
    (map->Verb {:root "அல்லு", :verb-class "3"})
    (map->Verb {:root "அளிகு", :verb-class "4"})
    (map->Verb {:root "அழிகு", :verb-class "4"})
    (map->Verb {:root "ஆ", :verb-class "1a"})
    (map->Verb {:root "ஆ", :verb-class "2"})
    (map->Verb {:root "ஆஃப்ரிகு", :verb-class "4"})
    (map->Verb {:root "ஆகு", :verb-class "4"})
    (map->Verb {:root "ஆடு", :verb-class "4"})
    (map->Verb {:root "ஆண்டு", :verb-class "3"})
    (map->Verb {:root "ஆத்மு", :verb-class "3"})
    (map->Verb {:root "ஆனந்து", :verb-class "3"})
    (map->Verb {:root "ஆனு", :verb-class "3"})
    (map->Verb {:root "ஆப்ரிகு", :verb-class "4"})
    (map->Verb {:root "ஆரம்பகடு", :verb-class "4"})
    (map->Verb {:root "இ", :verb-class "1a"})
    (map->Verb {:root "இ", :verb-class "2"})
    (map->Verb {:root "இடிகு", :verb-class "4"})
    (map->Verb {:root "இடு", :verb-class "3"})
    (map->Verb {:root "இணைகு", :verb-class "4"})
    (map->Verb {:root "இந்து", :verb-class "3"})
    (map->Verb {:root "இனு", :verb-class "3"})
    (map->Verb {:root "இன்று", :verb-class "3"})
    (map->Verb {:root "இப்போது", :verb-class "1a"})
    (map->Verb {:root "இரானு", :verb-class "3"})
    (map->Verb {:root "இருகு", :verb-class "4"})
    (map->Verb {:root "இருந்த்", :verb-class "1a"})
    (map->Verb {:root "இறுகு", :verb-class "4"})
    (map->Verb {:root "இறுதிகடு", :verb-class "4"})
    (map->Verb {:root "இலக்கு", :verb-class "3"})
    (map->Verb {:root "இல்", :verb-class "1c"})
    (map->Verb {:root "இல்லு", :verb-class "3"})
    (map->Verb {:root "இழகு", :verb-class "4"})
    (map->Verb {:root "இழப்பீடு", :verb-class "4"})
    (map->Verb {:root "இஸ்ரேலு", :verb-class "3"})
    (map->Verb {:root "ஈடுகடு", :verb-class "4"})
    (map->Verb {:root "உ", :verb-class "2"})
    (map->Verb {:root "உ", :verb-class "3"})
    (map->Verb {:root "உச்சரிகு", :verb-class "4"})
    (map->Verb {:root "உச்சு", :verb-class "3"})
    (map->Verb {:root "உயரு", :verb-class "3"})
    (map->Verb {:root "உள்", :verb-class "1b"})
    (map->Verb {:root "ஊக்குவிகு", :verb-class "4"})
    (map->Verb {:root "எடு", :verb-class "4"})
    (map->Verb {:root "எடுகு", :verb-class "4"})
    (map->Verb {:root "எடுத்துகொள்", :verb-class "1b"})
    (map->Verb {:root "எட்டா", :verb-class "1a"})
    (map->Verb {:root "எதிர்கு", :verb-class "4"})
    (map->Verb {:root "எதிர்ப்பார்க்கப்படு", :verb-class "4"})
    (map->Verb {:root "எதிர்பார்கு", :verb-class "4"})
    (map->Verb {:root "எளி", :verb-class "1a"})
    (map->Verb {:root "எழு", :verb-class "1a"})
    (map->Verb {:root "எழுத்", :verb-class "1a"})
    (map->Verb {:root "என்றழைக்கப்படு", :verb-class "4"})
    (map->Verb {:root "ஏது", :verb-class "3"})
    (map->Verb {:root "ஏறு", :verb-class "4"})
    (map->Verb {:root "ஒடு", :verb-class "4"})
    (map->Verb {:root "ஒத்", :verb-class "1a"})
    (map->Verb {:root "ஒத்து", :verb-class "3"})
    (map->Verb {:root "ஒன்று", :verb-class "3"})
    (map->Verb {:root "ஒலிகு", :verb-class "4"})
    (map->Verb {:root "ஒழிகு", :verb-class "4"})
    (map->Verb {:root "ஓடு", :verb-class "4"})
    (map->Verb {:root "கட்டுப்பாடு", :verb-class "4"})
    (map->Verb {:root "கடு", :verb-class "3"})
    (map->Verb {:root "கடு", :verb-class "4"})
    (map->Verb {:root "கடைப்பிடிகு", :verb-class "4"})
    (map->Verb {:root "கடைபிடி", :verb-class "6"})
    (map->Verb {:root "கடைபிடிக்கப்படு", :verb-class "4"})
    (map->Verb {:root "கணித்", :verb-class "1a"})
    (map->Verb {:root "கண்டு", :verb-class "3"})
    (map->Verb {:root "கத்தாரு", :verb-class "3"})
    (map->Verb {:root "கருத்", :verb-class "1a"})
    (map->Verb {:root "கருத்தரிகு", :verb-class "4"})
    (map->Verb {:root "கலந்துக்கொள்", :verb-class "1b"})
    (map->Verb {:root "கள்", :verb-class "1b"})
    (map->Verb {:root "கற்பிகு", :verb-class "4"})
    (map->Verb {:root "கனடு", :verb-class "3"})
    (map->Verb {:root "காக்கு", :verb-class "3"})
    (map->Verb {:root "காணு", :verb-class "3"})
    (map->Verb {:root "காண்பிகு", :verb-class "4"})
    (map->Verb {:root "காந்து", :verb-class "3"})
    (map->Verb {:root "கானு", :verb-class "3"})
    (map->Verb {:root "காலு", :verb-class "3"})
    (map->Verb {:root "கிடைகு", :verb-class "4"})
    (map->Verb {:root "கிட்டதடு", :verb-class "4"})
    (map->Verb {:root "கிட்டத்தடு", :verb-class "4"})
    (map->Verb {:root "கிராமு", :verb-class "3"})
    (map->Verb {:root "கு", :verb-class "2"})
    (map->Verb {:root "கு", :verb-class "3"})
    (map->Verb {:root "கு", :verb-class "4"})
    (map->Verb {:root "குடிகு", :verb-class "4"})
    (map->Verb {:root "குடியேறு", :verb-class "4"})
    (map->Verb {:root "குறு", :verb-class "4"})
    (map->Verb {:root "குவிகு", :verb-class "4"})
    (map->Verb {:root "கூடு", :verb-class "4"})
    (map->Verb {:root "கூரு", :verb-class "3"})
    (map->Verb {:root "கூறவேண்டு", :verb-class "3"})
    (map->Verb {:root "கூறு", :verb-class "4"})
    (map->Verb {:root "கேடு", :verb-class "4"})
    (map->Verb {:root "கேட்", :verb-class "5"})
    (map->Verb {:root "கேட்டு", :verb-class "3"})
    (map->Verb {:root "கை", :verb-class "1a"})
    (map->Verb {:root "கொண்டாடு", :verb-class "4"})
    (map->Verb {:root "கொடு", :verb-class "3"})
    (map->Verb {:root "கொடு", :verb-class "4"})
    (map->Verb {:root "கொடுகு", :verb-class "4"})
    (map->Verb {:root "சகோதரு", :verb-class "3"})
    (map->Verb {:root "சடு", :verb-class "4"})
    (map->Verb {:root "சது", :verb-class "3"}) 
    (map->Verb {:root "சத்", :verb-class "1a"}) 
    (map->Verb {:root "சமர்ப்பிகு", :verb-class "4"})
    (map->Verb {:root "சமு", :verb-class "3"})
    (map->Verb {:root "சம்பிகு", :verb-class "4"})
    (map->Verb {:root "சாரு", :verb-class "3"})
    (map->Verb {:root "சாஸ்திரு", :verb-class "3"})
    (map->Verb {:root "சி", :verb-class "6"})
    (map->Verb {:root "சிகு", :verb-class "4"})
    (map->Verb {:root "சித்", :verb-class "1a"})
    (map->Verb {:root "சித்து", :verb-class "3"})
    (map->Verb {:root "சிறி", :verb-class "1a"})
    (map->Verb {:root "சீரழி", :verb-class "7"})
    (map->Verb {:root "சுந்தரு", :verb-class "3"})
    (map->Verb {:root "சூடு", :verb-class "4"})
    (map->Verb {:root "சென்றிருகு", :verb-class "4"})
    (map->Verb {:root "செய்து", :verb-class "3"})
    (map->Verb {:root "செய்த்", :verb-class "1a"})
    (map->Verb {:root "செல்வு", :verb-class "3"})
    (map->Verb {:root "சேரு", :verb-class "3"})
    (map->Verb {:root "சேர்கு", :verb-class "4"})
    (map->Verb {:root "சொல்லியிருகு", :verb-class "4"})
    ;; சொல் is irregular
    (map->Verb {:root "சொல்லு", :verb-class "3"})
    (map->Verb {:root "ஜ", :verb-class "2"})
    (map->Verb {:root "ஜப்பானு", :verb-class "3"})
    (map->Verb {:root "டு", :verb-class "3"})
    (map->Verb {:root "த", :verb-class "2"})
    (map->Verb {:root "த", :verb-class "6"})
    (map->Verb {:root "தகு", :verb-class "4"})
    (map->Verb {:root "தடுகு", :verb-class "4"})
    (map->Verb {:root "தண்டிகு", :verb-class "4"})
    (map->Verb {:root "தந்திரு", :verb-class "2"})
    (map->Verb {:root "தந்து", :verb-class "3"})
    (map->Verb {:root "தனு", :verb-class "3"})    
    (map->Verb {:root "தப்பிகு", :verb-class "4"}) 
    (map->Verb {:root "தலைமையேற்", :verb-class "5"})
    (map->Verb {:root "தள்", :verb-class "1b"})
    (map->Verb {:root "தவிகு", :verb-class "4"})
    (map->Verb {:root "தானு", :verb-class "3"})
    (map->Verb {:root "தாரகு", :verb-class "4"})
    (map->Verb {:root "திடு", :verb-class "4"})
    (map->Verb {:root "திரடு", :verb-class "4"})
    (map->Verb {:root "திருடு", :verb-class "4"})
    (map->Verb {:root "தீவிரவாது", :verb-class "3"})
    (map->Verb {:root "து", :verb-class "3"})
    (map->Verb {:root "துகு", :verb-class "4"})
    (map->Verb {:root "தெரிவிகு", :verb-class "4"})
    (map->Verb {:root "தேசு", :verb-class "3"})
    (map->Verb {:root "தேர்தெடுக்கப்படு", :verb-class "4"})
    (map->Verb {:root "தேவு", :verb-class "3"})
    (map->Verb {:root "தொட", :verb-class "7"})
    (map->Verb {:root "தொல்", :verb-class "5"})
    (map->Verb {:root "தொறு", :verb-class "4"})
    (map->Verb {:root "தோறு", :verb-class "4"})
    (map->Verb {:root "தோற்கடிகு", :verb-class "4"})
    (map->Verb {:root "நட", :verb-class "6"})
    (map->Verb {:root "நடகு", :verb-class "4"})
    (map->Verb {:root "நடத்தவேண்டு", :verb-class "3"})
    (map->Verb {:root "நடந்திருகு", :verb-class "4"})
    (map->Verb {:root "நடிகு", :verb-class "4"})
    (map->Verb {:root "நட்த்தப்படு", :verb-class "4"})
    (map->Verb {:root "நரு", :verb-class "3"})
    (map->Verb {:root "நல்", :verb-class "1c"})
    (map->Verb {:root "நாடு", :verb-class "4"})
    (map->Verb {:root "நாற்", :verb-class "5"}) 
    (map->Verb {:root "நினைகு", :verb-class "4"})
    (map->Verb {:root "நிரூபிக்கவேண்டு", :verb-class "3"})
    (map->Verb {:root "நிர்வாகு", :verb-class "3"})
    (map->Verb {:root "நிறைவேறு", :verb-class "4"})
    (map->Verb {:root "நில்", :verb-class "1c"})
    (map->Verb {:root "நிவாரணு", :verb-class "3"})
    (map->Verb {:root "நீ", :verb-class "6"})
    (map->Verb {:root "நேபாளு", :verb-class "3"})
    (map->Verb {:root "ப", :verb-class "2"})
    (map->Verb {:root "ப", :verb-class "6"})
    (map->Verb {:root "பகு", :verb-class "4"})
    (map->Verb {:root "பங்கேற்", :verb-class "5"})
    (map->Verb {:root "படிகு", :verb-class "4"}) 
    (map->Verb {:root "படு", :verb-class "3"})
    (map->Verb {:root "பட்டு", :verb-class "3"})
    (map->Verb {:root "பணு", :verb-class "3"}) 
    (map->Verb {:root "பதடு", :verb-class "4"})
    (map->Verb {:root "பதவியேற்", :verb-class "5"})
    (map->Verb {:root "பயங்கரவாது", :verb-class "3"})
    (map->Verb {:root "பயணிகு", :verb-class "4"})
    (map->Verb {:root "பயணு", :verb-class "3"})
    (map->Verb {:root "பயன்பாடு", :verb-class "4"})
    (map->Verb {:root "பரி", :verb-class "2"})
    (map->Verb {:root "பரிமாறு", :verb-class "4"})
    (map->Verb {:root "பறு", :verb-class "4"})
    (map->Verb {:root "பலு" :verb-class "3"})
    (map->Verb {:root "பழகு", :verb-class "4"})
    (map->Verb {:root "பழு", :verb-class "1a"}) 
    (map->Verb {:root "பழு", :verb-class "3"})
    (map->Verb {:root "பாகிஸ்தானு", :verb-class "3"})
    (map->Verb {:root "பாடு", :verb-class "4"})
    (map->Verb {:root "பாதிகு", :verb-class "4"})
    (map->Verb {:root "பாதிக்கக்கூடு", :verb-class "3"})
    (map->Verb {:root "பாதிக்கப்படக்கூடு", :verb-class "3"})
    (map->Verb {:root "பாது", :verb-class "3"})
    (map->Verb {:root "பாரது", :verb-class "3"})
    (map->Verb {:root "பார்த்திருகு", :verb-class "4"})
    (map->Verb {:root "பாலத்தீனு", :verb-class "3"})
    (map->Verb {:root "பாலஸ்தீனு", :verb-class "3"})
    (map->Verb {:root "பாலு", :verb-class "3"}) 
    (map->Verb {:root "பிறகு", :verb-class "1a"})
    (map->Verb {:root "பிறகு", :verb-class "4"})
    (map->Verb {:root "பீடு", :verb-class "3"})
    (map->Verb {:root "பு", :verb-class "1a"})
    (map->Verb {:root "பு", :verb-class "3"})
    (map->Verb {:root "புகடு", :verb-class "4"})
    (map->Verb {:root "புகாரு", :verb-class "3"})
    (map->Verb {:root "புதி", :verb-class "1a"})
    (map->Verb {:root "புத்", :verb-class "1a"})
    (map->Verb {:root "புத்து", :verb-class "3"})
    (map->Verb {:root "புறு", :verb-class "4"})
    (map->Verb {:root "பெண்ணு", :verb-class "3"})
    (map->Verb {:root "பெரி", :verb-class "1a"})
    (map->Verb {:root "பெரு", :verb-class "2"})
    (map->Verb {:root "பெரு", :verb-class "3"}) 
    (map->Verb {:root "பெரு", :verb-class "6"})
    (map->Verb {:root "பெருகு", :verb-class "4"})
    (map->Verb {:root "பெற்றுகொள்", :verb-class "1b"})
    (map->Verb {:root "பேரு", :verb-class "2"})
    (map->Verb {:root "பேரு", :verb-class "3"})
    (map->Verb {:root "போ", :verb-class "1a"})
    (map->Verb {:root "போகு", :verb-class "4"})
    (map->Verb {:root "போட்டு", :verb-class "3"})
    (map->Verb {:root "போது", :verb-class "3"})
    (map->Verb {:root "போராடு", :verb-class "4"})
    (map->Verb {:root "போலு", :verb-class "3"})
    (map->Verb {:root "மடு", :verb-class "4"})
    (map->Verb {:root "மணு", :verb-class "3"})
    (map->Verb {:root "மதிகு", :verb-class "4"})
    (map->Verb {:root "மது", :verb-class "3"})
    (map->Verb {:root "மந்திரு", :verb-class "3"})
    (map->Verb {:root "மரு", :verb-class "1a"})
    (map->Verb {:root "மரு", :verb-class "2"})
    (map->Verb {:root "மரு", :verb-class "3"})
    (map->Verb {:root "மறு", :verb-class "4"})
    (map->Verb {:root "மறுகு", :verb-class "4"})
    (map->Verb {:root "மலையேறு", :verb-class "4"})
    (map->Verb {:root "மாணவு", :verb-class "3"})
    (map->Verb {:root "மாறக்கூடு", :verb-class "3"})
    (map->Verb {:root "மாறு", :verb-class "4"})
    (map->Verb {:root "மிகப்பெரு", :verb-class "3"})
    (map->Verb {:root "மிதவாது", :verb-class "3"})
    (map->Verb {:root "மீது", :verb-class "3"})
    (map->Verb {:root "மீத்", :verb-class "1a"})
    (map->Verb {:root "மீன்பிடிகு", :verb-class "4"})
    (map->Verb {:root "மீள்", :verb-class "1b"})
    (map->Verb {:root "மு", :verb-class "6"})
    (map->Verb {:root "முக்", :verb-class "1a"})
    (map->Verb {:root "முடிகு", :verb-class "4"})
    (map->Verb {:root "முதற்கடு", :verb-class "4"})
    (map->Verb {:root "முதல்கடு", :verb-class "4"})
    (map->Verb {:root "முன்னெடுகு", :verb-class "4"})
    (map->Verb {:root "முன்னேறு", :verb-class "4"})
    (map->Verb {:root "முன்வைகு", :verb-class "4"})
    (map->Verb {:root "மூல்", :verb-class "1c"})
    (map->Verb {:root "மூழ்கடிகு", :verb-class "4"})
    (map->Verb {:root "மெல்லு", :verb-class "3"})
    (map->Verb {:root "மொத்", :verb-class "1a"})
    (map->Verb {:root "யு", :verb-class "3"})
    (map->Verb {:root "யுத்", :verb-class "1a"})
    (map->Verb {:root "யோகு", :verb-class "3"})
    (map->Verb {:root "ரத்", :verb-class "1a"})
    (map->Verb {:root "ரத்து", :verb-class "3"})
    (map->Verb {:root "ராஜித்", :verb-class "1a"})
    (map->Verb {:root "ரு", :verb-class "3"})
    (map->Verb {:root "ரொகு", :verb-class "4"})
    (map->Verb {:root "லட்சு", :verb-class "3"})
    (map->Verb {:root "லு", :verb-class "3"})
    (map->Verb {:root "வகிகு", :verb-class "4"})
    (map->Verb {:root "வகுகு", :verb-class "4"})
    (map->Verb {:root "வங்கு", :verb-class "3"})
    (map->Verb {:root "வசந்து", :verb-class "3"})
    (map->Verb {:root "வசிகு", :verb-class "4"})
    (map->Verb {:root "வடு", :verb-class "4"})
    (map->Verb {:root "வட்டு", :verb-class "3"})
    (map->Verb {:root "வந்த்", :verb-class "1a"})
    (map->Verb {:root "வரலாறு", :verb-class "4"})
    (map->Verb {:root "வரவேண்டு", :verb-class "3"})
    (map->Verb {:root "வரவேறு", :verb-class "4"})
    (map->Verb {:root "வரவேற்", :verb-class "5"})
    (map->Verb {:root "வரிகடு", :verb-class "4"})
    (map->Verb {:root "வரு", :verb-class "2"})
    (map->Verb {:root "வரு", :verb-class "3"})
    (map->Verb {:root "வலி", :verb-class "2"})
    (map->Verb {:root "வளர்கு", :verb-class "4"})
    (map->Verb {:root "வளு", :verb-class "3"})
    (map->Verb {:root "வானு", :verb-class "3"})
    (map->Verb {:root "விசாரிகு", :verb-class "4"})
    (map->Verb {:root "விஞ்ஞானு", :verb-class "3"})
    (map->Verb {:root "விடுவிகு", :verb-class "4"})
    (map->Verb {:root "விண்ணப்பிகு", :verb-class "4"})
    (map->Verb {:root "விதிகு", :verb-class "4"})
    (map->Verb {:root "விது", :verb-class "3"})
    (map->Verb {:root "விமானு", :verb-class "3"})
    (map->Verb {:root "வியாபாரு", :verb-class "3"})
    (map->Verb {:root "விரடு", :verb-class "4"})
    (map->Verb {:root "விறு", :verb-class "4"})
    (map->Verb {:root "விலகு", :verb-class "4"})
    (map->Verb {:root "விளையாடு", :verb-class "4"})
    (map->Verb {:root "விளைவிகு", :verb-class "4"})
    (map->Verb {:root "விழு", :verb-class "3"})
    (map->Verb {:root "விவசாயு", :verb-class "3"})
    (map->Verb {:root "வீடு", :verb-class "4"})
    (map->Verb {:root "வீது", :verb-class "3"})
    (map->Verb {:root "வு", :verb-class "3"})
    (map->Verb {:root "வெடிகு", :verb-class "4"})
    (map->Verb {:root "வெடு", :verb-class "4"})
    (map->Verb {:root "வெறு", :verb-class "3"})
    (map->Verb {:root "வெறு", :verb-class "4"}) 
    (map->Verb {:root "வெள்ளு", :verb-class "3"})
    (map->Verb {:root "வைகு", :verb-class "4"})
    (map->Verb {:root "வைத்திருகு", :verb-class "4"})    
    (map->Verb {:root "வைத்து", :verb-class "3"})})

(def ^{:doc "Add back in verbs that are irregular and probably aren't worth fixing up the existing code too much more (at least for now)"}
  MANUAL-INCLUDES
  (concat
   #{(map->Verb {:root "இடி", :verb-class "2"})
     (map->Verb {:root "அசை", :verb-class "2"})
     (map->Verb {:root "கட", :verb-class "6"})
     (map->Verb {:root "திரி", :verb-class "6"})
     (map->Verb {:root "நட", :verb-class "7"})
     (map->Verb {:root "நிறை", :verb-class "6"})
     (map->Verb {:root "நுழை", :verb-class "6"})
     (map->Verb {:root "நெருக்கு", :verb-class "3"})
     (map->Verb {:root "வலி", :verb-class "6"})}
   (->> data/CLASS-5-VERBS
        vals
        (map map->Verb))
   (->> data/IRREGULAR-VERBS
        vals
        (map map->Verb))))

(defn is-invalid-verb
  "Return whether the Verb record matches any known false positive patterns"
  [verb]
  (or (மொழி/பின்னொட்டா? (:root verb) "உஉ")
      (and (= "4" (:verb-class verb))
           (fmt/suffix? (:root verb) "ரிகு"))
      (and (= "4" (:verb-class verb))
           (fmt/suffix? (:root verb) "ருகு"))
      (and (= "4" (:verb-class verb))
           (fmt/suffix? (:root verb) "கு")
           (not= "நகு"  (:root verb)))
      (empty? (:root verb))
      ))

(defn can-get-verb-at-loc
  [z]
  (let [verb-classes-get-fns [can-get-verb-class-1a-at-loc
                              can-get-verb-class-1b-at-loc
                              can-get-verb-class-1c-at-loc
                              can-get-verb-class-2-at-loc
                              can-get-verb-class-3-at-loc
                              can-get-verb-class-4-at-loc
                              can-get-verb-class-5-at-loc
                              can-get-verb-class-6-at-loc
                              can-get-verb-class-7-at-loc]
        can-get-verb-class-results ((apply juxt verb-classes-get-fns) z)
        can-get-some-verb (some identity can-get-verb-class-results)]
    can-get-some-verb))

(defn get-verbs-at-loc
  "return all the verbs detected at the current location in the zipper"
  [z]
  (letfn [(default-verb
            [z] 
            (-> z t/parent-path fmt/phonemes->str))]
    (let [verb-class-fns {"1a" {:pred can-get-verb-class-1a-at-loc
                               :getter default-verb}
                          "1b" {:pred can-get-verb-class-1b-at-loc
                               :getter default-verb}
                          "1c" {:pred can-get-verb-class-1c-at-loc
                               :getter default-verb}
                          "2" {:pred can-get-verb-class-2-at-loc
                               :getter default-verb} 
                          "3" {:pred can-get-verb-class-3-at-loc
                               :getter get-verb-class-3-at-loc}
                          "4" {:pred can-get-verb-class-4-at-loc
                               :getter get-verb-class-4-at-loc}
                          "5" {:pred can-get-verb-class-5-at-loc
                               :getter get-verb-class-5-at-loc}
                          "6" {:pred can-get-verb-class-6-at-loc
                               :getter get-verb-class-6-at-loc}
                          "7" {:pred can-get-verb-class-7-at-loc
                               :getter default-verb}}
          found-verbs (for [[verb-class {:keys [pred getter]}] verb-class-fns]
                        (when (pred z)
                          (let [verb (getter z)]
                            (if (record? verb)
                                verb
                                (map->Verb {:root (getter z)
                                            :verb-class verb-class})))))
          found-verbs-no-nils (keep identity found-verbs)
          found-verbs-valid (->> found-verbs-no-nils
                                 (remove FALSE-POSITIVE-VERBS)
                                 (remove is-invalid-verb)
                                 ;;(concat MANUAL-INCLUDES)
                                 )]
      found-verbs-valid)))
