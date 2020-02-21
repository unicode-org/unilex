(ns dict-data-builder.verb-test
  (:require [clojure.test :refer :all]
            [clojure.data :refer [diff]]
            [clojure.zip :as z]
            [dict-data-builder.core :refer :all]
            [dict-data-builder.data :as data :refer [map->Verb]]
            [dict-data-builder.io :as io]
            [dict-data-builder.trie :as t]
            [dict-data-builder.verb :as verb])
  (:import dict_data_builder.data.Verb))

(deftest verb-detection-test
  (testing "can find a class 1a verb"
    (let [lines1a ["செய் 1"
                   "உறுதிசெய்து	3108"
                   "உறுதிசெய்ய	5439"
                   "உறுதிசெய்யவில்லை	2331"
                   "உறுதிசெய்யும்	3885"]
          trie1a (io/lines->phoneme-freq-trie lines1a)
          tz1 (t/trie-zipper trie1a)
          tz2 (t/descend-trie-zipper tz1 ["உ" "ற்" "உ" "த்" "இ" "ச்" "எ" "ய்"])]
      (is (verb/can-get-verb-class-1a-at-loc tz2)))
    (let [lines1a ["செய்து 1"
                   "செய்ய 1"]
          trie1a (io/lines->phoneme-freq-trie lines1a)
          tz1 (t/trie-zipper trie1a)
          tz2 (t/descend-trie-zipper tz1 ["ச்" "எ" "ய்"])]
      (is (verb/can-get-verb-class-1a-at-loc tz2))))
  (testing "can find a class 1b verb" 
    (let [lines1b ["ஆண்டார்கள் 1"
                   "ஆளும் 1"]
          trie1b (io/lines->phoneme-freq-trie lines1b)
          tz1 (t/trie-zipper trie1b)
          tz2 (t/descend-trie-zipper tz1 ["ஆ" "ள்"])]
      (is (verb/can-get-verb-class-1b-at-loc tz2))))
  (testing "can find a class 1c verb" 
    (let [lines1c ["சென்ற 1"
                   "செல்கின்றோம் 1"]
          trie1c (io/lines->phoneme-freq-trie lines1c)
          tz1 (t/trie-zipper trie1c)
          tz2 (t/descend-trie-zipper tz1 ["ச்" "எ" "ல்"])]
      (is (verb/can-get-verb-class-1c-at-loc tz2))))
  (testing "can find a class 2 verb"
    (let [lines2 ["அமர்ந்த	6994"
                  "அமர்ந்தது	3885"
                  "அமர்ந்தனர்	3108"
                  "அமர்ந்திருக்க	2331"
                  "அமர்ந்திருக்கும்	6994"
                  "அமர்ந்திருந்த	11657"
                  "அமர்ந்திருந்தனர்	3885"
                  "அமர்ந்திருந்தார்	5439"
                  "அமர்ந்து	48959"
                  "அமர்ந்துள்ளனர்	2331"
                  "அமர்நாத்	40411"
                  "அமர்வதற்கு	3108"
                  "அமர்வதும்	2331"
                  "அமர்வதை	2331"
                  "அமர்வில்	31085"
                  "அமர்வின்	15542"
                  "அமர்வு	127450"
                  "அமர்வுக்கு	3885"
                  "அமர்வுகள்	4662"
                  "அமர்வை	3108"
                  "அமர	10879"
                  "அமரதுங்க	2331"
                  "அமரதேவா	5439"
                  "அமரல்	2331"
                  "அமரவீர	3108"
                  "அமரிக்க	2331"
                  "அமருங்கள்	4662"
                  "அமரும்	4662"]
          trie2 (io/lines->phoneme-freq-trie lines2)
          tz1 (t/trie-zipper trie2)
          tz2 (t/descend-trie-zipper tz1 ["அ" "ம்" "அ" "ர்"])]
      (is (verb/can-get-verb-class-2-at-loc tz2))))
  (testing "can find a class 3 verb"
    (let [lines3a ["அகற்ற	52845"
                   "அகற்றக்	3108"
                   "அகற்றத்	3108"
                   "அகற்றப்பட்ட	15542"
                   "அகற்றப்பட்டதாக	2331"
                   "அகற்றப்பட்டது	6217"
                   "அகற்றப்பட்டதை	3108"
                   "அகற்றப்பட்டன	3108"
                   "அகற்றப்பட்டார்	3108"
                   "அகற்றப்பட்டு	8548"
                   "அகற்றப்பட	3108"
                   "அகற்றப்படும்	13211"
                   "அகற்றம்	3885"
                   "அகற்றி	10879"
                   "அகற்றிடும்	2331"
                   "அகற்றிய	3108"
                   "அகற்றிவிட்டு	5439"
                   "அகற்றிவிட	5439"
                   "அகற்றிவிடும்	2331"
                   "அகற்றினர்	3108"
                   "அகற்றினார்	2331"
                   "அகற்று	2331"
                   "அகற்றுதல்	3108"
                   "அகற்றும்	34971"
                   "அகற்றும்படி	3108"
                   "அகற்றுமாறு	3108"
                   "அகற்றுவதற்காக	3108"
                   "அகற்றுவதற்கான	3108"
                   "அகற்றுவதற்கு	6994"
                   "அகற்றுவது	34194"
                   "அகற்றுவேன்	2331"]
          trie3a (io/lines->phoneme-freq-trie lines3a)
          tz1 (t/trie-zipper trie3a)
          tz2 (t/descend-trie-zipper tz1 ["அ" "க்" "அ" "ற்" "ற்" "உ"])]
      (is (verb/is-verb-class-3 tz2))
      (is (not (verb/can-infer-verb-class-3 tz2)))
      (is (verb/can-get-verb-class-3-at-loc tz2)))
    (let [lines3b ["கலக்கிய	4662"
                   "கலக்கும்	3885"
                   "கலக்கும்போது	2331"]
          trie3b (io/lines->phoneme-freq-trie lines3b)
          tz1 (t/trie-zipper trie3b)
          tz2 (t/descend-trie-zipper tz1 ["க்" "அ" "ல்" "அ" "க்" "க்" "உ"])]
      (is (verb/is-verb-class-3 tz2))
      (is (not (verb/can-infer-verb-class-3 tz2)))
      (is (verb/can-get-verb-class-3-at-loc tz2))))
  (testing "can find a class 4 verb"
    (let [;; markers of past tense of போடு - past tense verbal adjective and past tense verbal noun
          lines4-part1 ["போட்ட	14765"
                        "போட்டது	3885"]
          ;; words not related to past tense of போடு, even though they contain போட்ட் as a prefix in terms of phonemes
          lines4-part2 ["போட்டி	244798"
                        "போட்டிக்காக	4662"
                        "போட்டிக்கான	10102"
                        "போட்டிக்கு	23314"
                        "போட்டிக்குப்	2331"
                        "போட்டிகள்	94810"
                        "போட்டிகளில்	111907"
                        "போட்டிகளிலும்	14765"
                        "போட்டிகளின்	8548"
                        "போட்டிகளுக்காக	3885"
                        "போட்டிகளுக்கு	21759"
                        "போட்டிகளை	85485"
                        "போட்டிகளையும்	2331"
                        "போட்டிச்	2331"
                        "போட்டித்	6217"
                        "போட்டிதானா	3108"
                        "போட்டிப்	6217"
                        "போட்டியாக	17097"
                        "போட்டியாகும்	4662"
                        "போட்டியாளர்	17097"
                        "போட்டியாளர்கள்	10879"
                        "போட்டியாளர்களான	2331"
                        "போட்டியாளர்களை	2331"
                        "போட்டியாளராக	7771"
                        "போட்டியாளரான	13211"
                        "போட்டியானது	2331"
                        "போட்டியிட்ட	46628"
                        "போட்டியிட்டது	5439"
                        "போட்டியிட்டன	3885"
                        "போட்டியிட்டார்	11657"
                        "போட்டியிட்டால்	3108"
                        "போட்டியிட்டு	23314"
                        "போட்டியிட	48959"
                        "போட்டியிடப்	5439"
                        "போட்டியிடப்போவதாக	4662"
                        "போட்டியிடவில்லை	2331"
                        "போட்டியிடவும்	3885"
                        "போட்டியிடவுள்ள	3885"
                        "போட்டியிடுகிறார்	5439"
                        "போட்டியிடுகின்றனர்	5439"
                        "போட்டியிடும்	36525"
                        "போட்டியிடுவதற்கான	2331"
                        "போட்டியிடுவதற்கு	5439"
                        "போட்டியிடுவதாக	5439"
                        "போட்டியிடுவதில்	2331"
                        "போட்டியிடுவது	7771"
                        "போட்டியிடுவார்	12434"
                        "போட்டியில்	356706"
                        "போட்டியிலிருந்து	5439"
                        "போட்டியிலும்	4662"
                        "போட்டியிலேயே	5439"
                        "போட்டியின்	35748"
                        "போட்டியின்போது	3108"
                        "போட்டியும்	2331"
                        "போட்டியை	39634"
                        "போட்டியைக்	3108"]
          ;; AvP of போடு - in other words, marker of past tense
          lines4-part3 ["போட்டு	34971"
                        "போட்டுக்	8548"
                        "போட்டுடைத்த	3885"
                        "போட்டுவிடுவேன்	6217"
                        "போட்டுள்ளார்	2331"
                        "போட்ஸ்வானா	3885"]
          ;; infinitive of போடு
          lines4-part4 ["போட	19428"]
          lines4 (concat lines4-part1 lines4-part2 lines4-part3 lines4-part4)]
      (let [trie4-part1 (io/lines->phoneme-freq-trie (concat lines4-part1 lines4-part2))
            tz1 (t/trie-zipper trie4-part1)
            tz2 (t/descend-trie-zipper tz1 ["ப்" "ஓ" "ட்"])]
        (is (not (verb/can-get-verb-class-4-at-loc tz2))))
      (let [trie4-part2 (io/lines->phoneme-freq-trie (concat lines4-part2
                                                          lines4-part3))
            tz1 (t/trie-zipper trie4-part2)
            tz2 (t/descend-trie-zipper tz1 ["ப்" "ஓ" "ட்"])]
        (is (not (verb/can-get-verb-class-4-at-loc tz2))))
      (let [trie4-part2 (io/lines->phoneme-freq-trie (concat lines4-part1
                                                          lines4-part4))
            tz1 (t/trie-zipper trie4-part2)
            tz2 (t/descend-trie-zipper tz1 ["ப்" "ஓ" "ட்"])]
        (is (verb/can-get-verb-class-4-at-loc tz2)))
      (let [trie4-part2 (io/lines->phoneme-freq-trie (concat lines4-part3
                                                          lines4-part4))
            tz1 (t/trie-zipper trie4-part2)
            tz2 (t/descend-trie-zipper tz1 ["ப்" "ஓ" "ட்"])]
        (is (verb/can-get-verb-class-4-at-loc tz2)))
      (let [trie4 (io/lines->phoneme-freq-trie lines4)
            tz1 (t/trie-zipper trie4)
            tz2 (t/descend-trie-zipper tz1 ["ப்" "ஓ" "ட்"])]
        (is (verb/can-get-verb-class-4-at-loc tz2)))))
  (testing "can find a class 5 verb" 
    (let [lines5 ["கேட்டு 1"
                  "கேட்கும் 1"]
          trie5 (io/lines->phoneme-freq-trie lines5)
          tz1 (t/trie-zipper trie5)
          tz2 (t/descend-trie-zipper tz1 ["க்" "ஏ" "ட்"])]
      (is (verb/can-get-verb-class-5-at-loc tz2)))
    (let [lines5 ["விற்றாள் 1"
                  "விற்போம் 1"]
          trie5 (io/lines->phoneme-freq-trie lines5)
          tz1 (t/trie-zipper trie5)
          tz2 (t/descend-trie-zipper tz1 ["வ்" "இ" "ற்"])]
      (is (verb/can-get-verb-class-5-at-loc tz2))))
  (testing "can find a class 6 verb"
    (let [lines6a ["ஆதரவையும்	9325"
                   "ஆதரவோடு	24091"
                   "ஆதரிக்க	12434"
                   "ஆதரிக்கவில்லை	7771"
                   "ஆதரிக்கவும்	2331"
                   "ஆதரிக்கிறதா	2331"
                   "ஆதரிக்கிறது	3108"
                   "ஆதரிக்கிறார்	2331"
                   "ஆதரிக்கிறேன்	2331"
                   "ஆதரிக்கின்றன	2331"
                   "ஆதரிக்கின்றனர்	3108"
                   "ஆதரிக்கும்	24868"
                   "ஆதரித்த	2331"
                   "ஆதரித்தது	3108"
                   "ஆதரித்தன	3108"
                   "ஆதரித்தார்	3108"
                   "ஆதரித்து	20982"
                   "ஆதரிப்பதற்கு	2331"]
          trie6a (io/lines->phoneme-freq-trie lines6a)
          tz1 (t/trie-zipper trie6a)
          tz2 (t/descend-trie-zipper tz1 ["ஆ" "த்" "அ" "ர்" "இ"])]
      (is (verb/can-get-verb-class-6-at-loc tz2)))
    (let [lines6b ["உயர்	349712"
                   "உயர்த்த	15542"
                   "உயர்த்தி	14765"
                   "உயர்த்துகிறது	3108"
                   "உயர்த்தும்	4662"]
          trie6b (io/lines->phoneme-freq-trie lines6b)
          tz1 (t/trie-zipper trie6b)
          tz2 (t/descend-trie-zipper tz1 ["உ" "ய்" "அ" "ர்"])
          tz3 (t/descend-trie-zipper tz2 ["த்" "த்"])
          tz4 (t/descend-trie-zipper tz2 ["த்" "த்" "உ"])]
      (is (not (verb/can-get-verb-class-6-at-loc tz2)))
      (is (verb/can-infer-verb-class-3 tz3))
      (is (not (verb/is-verb-class-3 tz3)))
      (is (verb/can-get-verb-class-3-at-loc tz3))
      (is (verb/is-verb-class-3 tz4))
      (is (verb/can-get-verb-class-3-at-loc tz4))))
  (testing "can find a class 7 verb"
    (let [lines7-part1 ["இருந்துகொண்டு	3885"
                        "இருந்துதான்	9325"
                        "இருந்தும்	56731"
                        "இருந்துவந்தது	6994"
                        "இருந்துவந்தார்	4662"
                        "இருந்துவருகிறது	4662"
                        "இருந்துவருகிறார்	3108"
                        "இருந்துவரும்	4662"
                        "இருந்துவிட்டு	3108"
                        "இருந்துள்ளது	17097"
                        "இருந்துள்ளன	9325"
                        "இருந்துள்ளனர்	4662"
                        "இருந்துள்ளார்	11657"
                        "இருந்தே	37302"
                        "இருந்தேன்	30308"
                        "இருந்தோம்	12434"
                        "இருநாட்டு	12434"
                        "இருநாட்டுப்	2331"
                        "இருநாடுகள்	2331"
                        "இருநாடுகளுக்கு	4662"
                        "இருநாடுகளும்	5439"
                        "இருநூற்று	2331"
                        "இருநூற்றுக்கும்	5439"
                        "இருநூறு	6217"]
          lines7-part2 ["இருப்பதற்காக	8548"
                        "இருப்பதற்கான	27976"
                        "இருப்பதற்கு	29531"
                        "இருப்பதன்	7771"
                        "இருப்பதனால்	2331"
                        "இருப்பதாக	540110"
                        "இருப்பதாகக்	46628"
                        "இருப்பதாகச்	6994"
                        "இருப்பதாகத்	18651"
                        "இருப்பதாகப்	4662"
                        "இருப்பதாகவும்	162421"
                        "இருப்பதாகவே	8548"
                        "இருப்பதால்	188067"
                        "இருப்பதால்தான்	3108"
                        "இருப்பதாலும்	7771"
                        "இருப்பதாலேயே	3885"
                        "இருப்பதில்	7771"
                        "இருப்பதில்லை	12434"
                        "இருப்பது	248684"
                        "இருப்பதுடன்	3885"
                        "இருப்பதுதான்	10102"
                        "இருப்பதுபோல்	3108"
                        "இருப்பதுபோல	4662"
                        "இருப்பதும்	16319"
                        "இருப்பதே	13211"]
          lines7-part3 ["இருவர்	123564"
                        "இருவரது	3885"]]
      (let [trie7 (io/lines->phoneme-freq-trie (concat lines7-part1 lines7-part2))
            tz1 (t/trie-zipper trie7)
            tz2 (t/descend-trie-zipper tz1 ["இ" "ர்" "உ"])]
        (is (not (verb/can-get-verb-class-2-at-loc tz2)))
        (is (verb/can-get-verb-class-7-at-loc tz2)))
      (let [trie7 (io/lines->phoneme-freq-trie (concat lines7-part1 lines7-part3))
            tz1 (t/trie-zipper trie7)
            tz2 (t/descend-trie-zipper tz1 ["இ" "ர்" "உ"])]
        (is (not (verb/can-get-verb-class-2-at-loc tz2)))
        (is (not (verb/can-get-verb-class-7-at-loc tz2)))))))

(deftest verb-class-fully-conjugated-verbs
  (testing "inferring verb classes with fully conjugated verbs"
    (testing "class 1a"
      (let [lines ["செய்தீர்கள் 1"
                   "செய்கின்றீர்கள் 1"]
            trie (io/lines->phoneme-freq-trie lines)
            tz1 (t/trie-zipper trie)
            tz2 (t/descend-trie-zipper tz1 ["ச்" "எ" "ய்"])]
        (is (verb/can-get-verb-class-1a-at-loc tz2))))
    (testing "class 2"
      (let [lines ["வளர்ந்தேன் 1"
                   "வளர்வேன் 1"]
            trie (io/lines->phoneme-freq-trie lines)
            tz1 (t/trie-zipper trie)
            tz2 (t/descend-trie-zipper tz1 ["வ்" "அ" "ள்" "அ" "ர்"])]
        (is (verb/can-get-verb-class-2-at-loc tz2))))
    (testing "class 3"
      (let [lines ["வாங்கினாய் 1"
                   "வாங்குகிறாய் 1"]
            trie (io/lines->phoneme-freq-trie lines)
            tz1 (t/trie-zipper trie)
            tz2 (t/descend-trie-zipper tz1 ["வ்" "ஆ" "ங்" "க்" "உ"])
            tz3 (t/descend-trie-zipper tz1 ["வ்" "ஆ" "ங்" "க்"])]
        (is (verb/can-get-verb-class-3-at-loc tz2))
        (is (verb/can-get-verb-class-3-at-loc tz3)))
      (let [lines ["அடக்கினீர்கள் 1"
                   "அடக்கம் 1"]
            trie (io/lines->phoneme-freq-trie lines)
            tz1 (t/trie-zipper trie)
            tz2 (t/descend-trie-zipper tz1 ["அ" "ட்" "அ" "க்" "க்"])]
        (is (not (verb/can-get-verb-class-3-at-loc tz2)))))
    (testing "class 4"
      (let [lines ["இட்டேன் 1"
                   "இடுவேன் 1"]
            trie (io/lines->phoneme-freq-trie lines)
            tz1 (t/trie-zipper trie)
            tz2 (t/descend-trie-zipper tz1 ["இ" "ட்"])
            tz3 (t/descend-trie-zipper tz1 ["இ" "ட்" "உ"])]
        (is (verb/can-get-verb-class-4-at-loc tz2))
        (is (verb/can-get-verb-class-4-at-loc tz3)))
      (let [lines ["நட்டார் 1"
                   "நடுகல் 1"]
            trie (io/lines->phoneme-freq-trie lines)
            tz1 (t/trie-zipper trie)
            tz2 (t/descend-trie-zipper tz1 ["ந்" "அ" "ட்" "உ"])]
        (is (not (verb/can-get-verb-class-4-at-loc tz2)))))
    (testing "class 6"
      (let [lines ["எடுத்தோம் 1"
                   "எடுப்போம் 1"]
            trie (io/lines->phoneme-freq-trie lines)
            tz1 (t/trie-zipper trie)
            tz2 (t/descend-trie-zipper tz1 ["எ" "ட்" "உ"])]
        (is (verb/can-get-verb-class-6-at-loc tz2))))
    (testing "class 7"
      (let [lines ["வளர்ந்தார்கள் 1"
                   "வளர்ப்பார்கள் 1"]
            trie (io/lines->phoneme-freq-trie lines)
            tz1 (t/trie-zipper trie)
            tz2 (t/descend-trie-zipper tz1 ["வ்" "அ" "ள்" "அ" "ர்"])]
        (is (verb/can-get-verb-class-7-at-loc tz2))))))

(deftest find-verb-any-class
  (let [lines1 ["கலந்த	37302"
                "கலந்தது	3108"]
        lines2 ["உயர்	349712"
                "உயர்த்த	15542"]
        lines3 ["உயர்	349712"
                "உயர்த்த	15542"
                "உயர்த்தி	14765"
                "உயர்த்துகிறது	3108"
                "உயர்த்தும்	4662"
                "உயர்ந்த	40411"
                "உயர்ந்தது	10102"
                "உயர்ந்து	9325"]
        lines4 ["உயர்	349712"
                "உயர்த்த	15542"
                "உயர்த்தி	14765"
                "உயர்த்துகிறது	3108"
                "உயர்த்தும்	4662"
                "உயர்ந்த	40411"
                "உயர்ந்தது	10102"
                "உயர்ந்து	9325"
                "உயர	13211"]
        t1 (-> lines1 io/lines->phoneme-freq-trie)
        t2 (-> lines2 io/lines->phoneme-freq-trie)
        t3 (-> lines3 io/lines->phoneme-freq-trie)
        t4 (-> lines4 io/lines->phoneme-freq-trie)
        tz1 (-> t1 t/trie-zipper)
        tz2 (-> t2 t/trie-zipper)
        tz3 (-> t3 t/trie-zipper)
        tz4 (-> t4 t/trie-zipper)
        tz1-guided (-> tz1 (t/descend-trie-zipper ["க்" "அ" "ல்" "அ"]))
        tz2-guided (-> tz2 (t/descend-trie-zipper ["உ" "ய்" "அ" "ர்"]))
        tz3-guided (-> tz3 (t/descend-trie-zipper ["உ" "ய்" "அ" "ர்"]))
        tz4-guided (-> tz4 (t/descend-trie-zipper ["உ" "ய்" "அ" "ர்"]))]
    (testing "guided detection of verbs"
      (is (not (verb/can-get-verb-at-loc tz1-guided)))
      (is (not (verb/can-get-verb-at-loc tz2-guided)))
      (is (not (verb/can-get-verb-at-loc tz3-guided)))
      (is (verb/can-get-verb-at-loc (-> tz3-guided
                                   (t/descend-trie-zipper ["த்" "த்" "உ"]))))
      (is (verb/can-get-verb-at-loc tz4-guided)))
    (testing "automatic detection of verbs"
      (is (empty? (find-verbs t1)))
      (is (empty? (find-verbs t2)))
      (is (= (find-verbs t3) #{(map->Verb {:root "உயர்த்து"
                                           :verb-class "3"})}))
      (is (= (find-verbs t4) #{(map->Verb {:root "உயர்த்து"
                                           :verb-class "3"}),
                               (map->Verb {:root "உயர்"
                                           :verb-class "2"})}))))
  (testing "more verb class logic examples using automatic detection"
    (testing "verb class 1a"
      (let [ex-lines1 ["செய்ய 1"
                       "செய்து 1"]
            t1 (-> ex-lines1 io/lines->phoneme-freq-trie)
            exp-verbs #{(map->Verb {:root "செய்"
                                   :verb-class "1a"})}]
        (is (= exp-verbs (find-verbs t1)))))
    (testing "verb class 1b"
      (let [ex-lines1 ["ஆள 1"
                       "ஆண்டு 1"]
            t1 (-> ex-lines1 io/lines->phoneme-freq-trie)
            exp-verbs #{(map->Verb {:root "ஆள்"
                                   :verb-class "1b"})}]
        (is (= exp-verbs (find-verbs t1)))))
    (testing "verb class 1c"
      (let [ex-lines1 ["வெல்ல 1"
                       "வென்று 1"]
            t1 (-> ex-lines1 io/lines->phoneme-freq-trie)
            exp-verbs #{(map->Verb {:root "வெல்"
                                   :verb-class "1c"})}]
        (is (= exp-verbs (find-verbs t1)))))
    (testing "verb class 2"
      (let [ex-lines1 ["வளர 1"
                       "வளர்ந்து 1"]
            t1 (-> ex-lines1 io/lines->phoneme-freq-trie)
            exp-verbs #{(map->Verb {:root "வளர்"
                                   :verb-class "2"})}]
        (is (= exp-verbs (find-verbs t1)))))
    (testing "verb class 3"
      (let [ex-lines1 ["வாங்க 1"
                       "வாங்கி 1"]
            t1 (-> ex-lines1 io/lines->phoneme-freq-trie)
            exp-verbs #{(map->Verb {:root "வாங்கு"
                                    :verb-class "3"})}]
        (is (= exp-verbs (find-verbs t1))))
      (let [ex-lines1 ["வாங்கும் 1"
                       "வாங்கி 1"]
            t1 (-> ex-lines1 io/lines->phoneme-freq-trie)
            exp-verbs #{(map->Verb {:root "வாங்கு"
                                    :verb-class "3"})}]
        (is (= exp-verbs (find-verbs t1)))))
    (testing "verb class 4"
      (let [ex-lines1 ["பட 1"
                       "பட்டு 1"]
            t1 (-> ex-lines1 io/lines->phoneme-freq-trie)
            exp-verbs #{(map->Verb {:root "படு"
                                    :verb-class "4"})}]
        (is (= exp-verbs (find-verbs t1)))))
    (testing "verb class 6"
      (let [ex-lines1 ["வளர்க்க 1"
                       "வளர்த்து 1"]
            t1 (-> ex-lines1 io/lines->phoneme-freq-trie)
            exp-verbs #{(map->Verb {:root "வளர்"
                                    :verb-class "6"})}]
        (is (= exp-verbs (find-verbs t1)))))
    (testing "verb class 7"
      (let [ex-lines1 ["இருக்க 1"
                       "இருந்து 1"]
            t1 (-> ex-lines1 io/lines->phoneme-freq-trie)
            exp-verbs #{(map->Verb {:root "இரு"
                                   :verb-class "7"})}]
        (is (= exp-verbs (find-verbs t1))))))
  (testing "detecing multiple verbs at the same position"
    (let [ex-lines1 ["உயர்	349712"
                     "உயர்த்த	15542"
                     "உயர்த்தி	14765"
                     "உயர்த்துகிறது	3108"
                     "உயர்த்தும்	4662"
                     "உயர்ந்த	40411"
                     "உயர்ந்தது	10102"
                     "உயர்ந்து	9325"
                     "உயர 1"]
          t1 (-> ex-lines1 io/lines->phoneme-freq-trie)
          exp-verbs #{(map->Verb {:root "உயர்"
                                  :verb-class "2"})
                      (map->Verb {:root "உயர்த்து"
                                  :verb-class "3"})}]
      (is (= exp-verbs (find-verbs t1))))
    (let [ex-lines2 ["வளர்த்து 1"
                     "வளர்ந்து 1"
                     "வளர 1"
                     "வளர்க்க 1"]
          t2 (-> ex-lines2 io/lines->phoneme-freq-trie)
          exp-verbs #{(map->Verb {:root "வளர்"
                                  :verb-class "2"})
                      (map->Verb {:root "வளர்"
                                  :verb-class "6"})}]
      (is (= exp-verbs (find-verbs t2))))
    (let [ex-lines3 ["அமைத்து 1"
                     "அமைந்து 1"
                     "அமைய 1"
                     "அமைக்க 1"]
          t3 (-> ex-lines3 io/lines->phoneme-freq-trie)
          exp-verbs #{(map->Verb {:root "அமை"
                                  :verb-class "2"})
                      (map->Verb {:root "அமை"
                                  :verb-class "6"})}]
      (is (= exp-verbs (find-verbs t3))))))

(deftest find-verb-any-class-bug-fixes
  (testing "mistaken class 3 inference from non-verb words that differ -அ/-இ"
    (let [lines ["பல	2238934"
                 "பலகை	9325"
                 "பலகைகள்	7771" 
                 "பலி	321735"
                 "பலிக்கவில்லை	2331"
                 "பலுச்சிஸ்தான்	2331"
                 "பலுவானா	2331"
                 "பலூச்	3885"
                 "பலூசிஸ்தான்	3108"
                 "பலூசிஸ்தானில்	3108"
                 "பலூன்	4662"
                 "பலூன்கள்	3108"]
          trie (-> lines io/lines->phoneme-freq-trie)
          exp-verbs #{}]
      (is (= exp-verbs (find-verbs trie)))))
  (testing "-து/-வது diff between Class 1a past/future verbal noun matches எட்டு tenseless verbal neg adj/ordinal number"
    (let [lines ["எட்டாத	3108"
                 "எட்டாம்	13988"
                 "எட்டாயிரம்	3108"
                 "எட்டாவது	6217"]
          trie (-> lines io/lines->phoneme-freq-trie)
          exp-verbs #{}]
      (is (= exp-verbs (find-verbs trie)))))
  (testing "-படு/-படுத்து verb pairs shouldn't produce -படுஉ"
    (let [lines ["பயன்பட்ட	3108"
                 "பயன்படலாம்	2331"
                 "பயன்படாத	2331"                 
                 "பயன்படுகிறது	2331"
                 "பயன்படுகின்றன	4662"
                 "பயன்படுத்த	125119"
                 "பயன்படுத்தக்	10879"
                 "பயன்படுத்தத்	8548"
                 "பயன்படுத்தலாம்	26422"
                 "பயன்படுத்தலாமா	5439"
                 "பயன்படுத்தலாமே	2331"
                 "பயன்படுத்தவில்லை	5439"
                 "பயன்படுத்தவும்	5439"
                 "பயன்படுத்தாமல்	3885"
                 "பயன்படுத்தி	214490"
                 "பயன்படுத்திக்	34971"
                 "பயன்படுத்திய	59839"
                 "பயன்படுத்தியதற்கான	2331"
                 "பயன்படுத்தியதாக	15542"
                 "பயன்படுத்தியதாகவும்	2331"
                 "பயன்படுத்தியதால்	7771"
                 "பயன்படுத்தியதில்	2331"
                 "பயன்படுத்தியது	7771"
                 "பயன்படுத்தியதை	4662"
                 "பயன்படுத்தியவர்கள்	3108"
                 "பயன்படுத்தியிருக்கிறது	3108"
                 "பயன்படுத்தியிருக்கிறார்	3108"
                 "பயன்படுத்தியும்	2331"
                 "பயன்படுத்தியுள்ளது	4662"
                 "பயன்படுத்தியுள்ளனர்	5439"
                 "பயன்படுத்தியுள்ளார்	3108"
                 "பயன்படுத்தினர்	8548"
                 "பயன்படுத்தினார்	10879"
                 "பயன்படுத்தினார்கள்	4662"
                 "பயன்படுத்தினால்	7771"
                 "பயன்படுத்துகிற	3885"
                 "பயன்படுத்துகிறது	6217"
                 "பயன்படுத்துகிறார்	2331"
                 "பயன்படுத்துகிறார்கள்	5439"
                 "பயன்படுத்துகிறோம்	4662"
                 "பயன்படுத்துகின்ற	3108"
                 "பயன்படுத்துகின்றன	6217"
                 "பயன்படுத்துகின்றனர்	13211"
                 "பயன்படுத்துங்கள்	3885"
                 "பயன்படுத்துதல்	3885"
                 "பயன்படுத்துபவர்கள்	3885"
                 "பயன்படுத்தும்	82376"
                 "பயன்படுத்தும்போது	7771"
                 "பயன்படுத்துமாறு	3108"
                 "பயன்படுத்துவதற்காக	3108"
                 "பயன்படுத்துவதற்கான	3108"
                 "பயன்படுத்துவதற்கு	17097"
                 "பயன்படுத்துவதற்கும்	2331"
                 "பயன்படுத்துவதன்	3108"
                 "பயன்படுத்துவதாக	11657"
                 "பயன்படுத்துவதாகவும்	6217"
                 "பயன்படுத்துவதால்	3108"
                 "பயன்படுத்துவதில்	10102"
                 "பயன்படுத்துவதில்லை	5439"
                 "பயன்படுத்துவது	50513"
                 "பயன்படுத்துவதை	18651"
                 "பயன்படுத்துவதையும்	3108"
                 "பயன்படுத்துவர்	2331"
                 "பயன்படுத்துவார்கள்	3885"
                 "பயன்படுத்துவோர்	6994"
                 "பயன்படும்	27199"]
          trie (-> lines io/lines->phoneme-freq-trie)
          exp-verbs #{(map->Verb {:root "பயன்படு", :verb-class "4"})
                      (map->Verb {:root "பயன்படுத்து", :verb-class "3"})}]
      (is (= exp-verbs (find-verbs trie)))))
  (testing "degenerate example: கு class 3"
    (let [lines ["க 0"
                 "கி 0"]
          trie (-> lines io/lines->phoneme-freq-trie)
          exp-verbs #{}]
      (is (= exp-verbs (find-verbs trie)))))
  (testing "tricky example: பழு class 3"
    (let [lines ["பழ 0"
                 "பழி 0"]
          trie (-> lines io/lines->phoneme-freq-trie)
          exp-verbs #{}]
      (is (= exp-verbs (find-verbs trie)))))
  (testing "class 1a -தான்/-உம் split also works for nouns ending in -து"
    (let [lines ["இப்போது	341940"
                 "இப்போதுதான்	9325"
                 "இப்போதும்	34971"
                 "இப்போதுள்ள	3885"
                 "இப்போதே	7771"
                 "இப்போதைக்கு	3885"
                 "இப்போதைய	10879"]
          trie (-> lines io/lines->phoneme-freq-trie)
          exp-verbs #{}]
      (is (= exp-verbs (find-verbs trie))))
    (let [lines ["பிறகு	1681726"
                 "பிறகுதான்	45851"
                 "பிறகும்	69165"]
          trie (-> lines io/lines->phoneme-freq-trie)
          exp-verbs #{}]
      (is (= exp-verbs (find-verbs trie)))))
  (testing "class 1a -து/-அ AvP/infinitive split can be caused from related noun on common prefix non-word"
    (let [lines ["எழுத்து	31085"
                 "எழுத்துக்கள்	3108"
                 "எழுத்துக்களில்	2331"
                 "எழுத்துக்களை	2331"
                 "எழுத்துச்	6217"
                 "எழுத்துப்	2331"
                 "எழுத்துப்பூர்வமாக	2331"
                 "எழுத்துரு	7771"
                 "எழுத	36525"
                 "எழுதத்	3885"]
          trie (-> lines io/lines->phoneme-freq-trie)
          exp-verbs #{}]
      (is (= exp-verbs (find-verbs trie)))))
  (testing "class 4 -ட்டு/-உம் AvP/non-human future verbal adj split can be caused from noun ending in -டு"
    (let [lines ["இழப்பீட்டு	2331"
                 "இழப்பீட்டுத்	2331"
                 "இழப்பீட்டை	3108"
                 "இழப்பீடாக	7771"
                 "இழப்பீடாவும்	2331"
                 "இழப்பீடு	48182"
                 "இழப்பீடும்	6217"]
                 trie (-> lines io/lines->phoneme-freq-trie)
          exp-verbs #{}]
      (is (= exp-verbs (find-verbs trie))))) 
  (testing "class 5 return values need cleanup"
    (let [lines ["விற்க	55176"
                 "விற்ற	5439"
                 "விற்று	13988"]
                 trie (-> lines io/lines->phoneme-freq-trie)
          exp-verbs #{(map->Verb {:root "வில்", :verb-class "5"})}]
      (is (= exp-verbs (find-verbs trie)))))
  (testing "more class 5 return value cleanup"
      (let [lines ["பங்கேற்க	45073"
                   "பங்கேற்கச்	2331"
                   "பங்கேற்கத்	2331"
                   "பங்கேற்கப்	2331"
                   "பங்கேற்கவில்லை	10102"
                   "பங்கேற்காது	3108"
                   "பங்கேற்கிறது	11657"
                   "பங்கேற்கிறார்கள்	2331"
                   "பங்கேற்கின்றனர்	4662"
                   "பங்கேற்கும்	28754"
                   "பங்கேற்குமாறு	3885"
                   "பங்கேற்பதற்காக	2331"
                   "பங்கேற்பதாக	3108"
                   "பங்கேற்பதில்	2331"
                   "பங்கேற்பது	10102"
                   "பங்கேற்பதை	3885"
                   "பங்கேற்பார்	4662"
                   "பங்கேற்பார்கள்	3885"
                   "பங்கேற்பாளர்கள்	2331"
                   "பங்கேற்பாளர்களை	3108"
                   "பங்கேற்பு	10879"
                   "பங்கேற்ற	69942"
                   "பங்கேற்றதாக	5439"]
            trie (-> lines io/lines->phoneme-freq-trie)
            exp-verbs #{(map->Verb {:root "பங்கேல்", :verb-class "5"})}]
        (is (= exp-verbs (find-verbs trie)))))
  (testing "class 3 -இய/-உம் past verbal adj/non-human future verbal adj from plain adj"
    (let [lines ["பெரிய	744498"                 
                 "பெரும்	560316"]
          trie (-> lines io/lines->phoneme-freq-trie)
          exp-verbs #{}]
      (is (= exp-verbs (find-verbs trie)))))
  (testing "class 3 coincidence of noun as adj and place name proper noun"
    (let [lines ["இல்ல	5439"
                 "இல்லம்	50513"
                 "இல்லினாய்	2331"
                 "இல்லினோய்	2331"]
          trie (-> lines io/lines->phoneme-freq-trie)
          exp-verbs #{}]
      (is (= exp-verbs (find-verbs trie)))))
  (testing "class 3 coincidence of person name proper nouns"
    (let [lines ["சுந்தரம்	19428"
                 "சுந்தரி	2331"
                 "சுந்தரும்	2331"]
          trie (-> lines io/lines->phoneme-freq-trie)
          exp-verbs #{}]
      (is (= exp-verbs (find-verbs trie)))))
  (testing "class 7 inference from class verb's noun form and common misspelling of past tense"
    (let [lines ["தொடக்கம்	71496"
                 "தொடக்க	80822"                 
                 "தொடந்து	10879"
                 "தொடர்ந்து	1301705"]
          trie (-> lines io/lines->phoneme-freq-trie)
          exp-verbs #{}]
      (is (= exp-verbs (find-verbs trie)))))
  (testing "class 4 needs full verb root or present/future/infinitive"
    (let [lines ["வெட்	2331"
                 "வெட்ட	7771"]
          trie (-> lines io/lines->phoneme-freq-trie)
          exp-verbs #{}]
      (is (= exp-verbs (find-verbs trie)))))
  (testing "class 1a from a single-letter class 6 verb"
    (let [lines ["ஒத்த	6217"
                 "ஒத்துக்	2331"
                 "ஒத்துப்	2331"]
          trie (-> lines io/lines->phoneme-freq-trie)
          exp-verbs #{}]
      (is (= exp-verbs (find-verbs trie)))))
  (testing "class 3 -அ/-இய split from noun/class 2 verb infinitive and other class 2 verb infinitive (root1+இ=root2)"
    (let [lines ["அற	2331" 
                 "அறம்	2331"
                 "அறவழிப்	8548"
                 "அறவழியில்	2331"
                 "அறவே	2331" 
                 "அறிய	104913"] 
          trie (-> lines io/lines->phoneme-freq-trie)
          exp-verbs #{}]
      (is (= exp-verbs (find-verbs trie)))))
  (testing "class 4 tricky instance from class 2 infinitive and class 3 verb"
    (let [lines ["அற்ற	8548"
                 "அற	2331"]
          trie (-> lines io/lines->phoneme-freq-trie)
          exp-verbs #{}]
      (is (= exp-verbs (find-verbs trie))))) 
  (testing "class 4 from -ட்ட்-/-ட்- class 3 verbs trans/intrans split"
    (let [lines ["கூட்டும்	5439"
                 "கூட	369917"]
          trie (-> lines io/lines->phoneme-freq-trie)
          exp-verbs #{}]
      (is (= exp-verbs (find-verbs trie))))) 
  (testing "class 3 coincidence of noun and class 1a AvP"
    (let [lines ["செய்தி	750715" 
                 "செய்த	461619"]
          trie (-> lines io/lines->phoneme-freq-trie)
          exp-verbs #{}]
      (is (= exp-verbs (find-verbs trie)))))
  (testing "class 5 coincidence from 2 nouns"
    (let [lines ["நாற்பது	23314"
                 "நாற்றம்	2331"]
          trie (-> lines io/lines->phoneme-freq-trie)
          exp-verbs #{}]
      (is (= exp-verbs (find-verbs trie)))))
  (testing "class 1a coincidence from 2 different verbs"
    (let [lines ["எழுகிறது	18651" 
                 "எழுத	36525"]
          trie (-> lines io/lines->phoneme-freq-trie)
          exp-verbs #{}]
      (is (= exp-verbs (find-verbs trie)))))
  (testing "class 4 from just a class 6 infinitive and non-human future tense"
    (let [lines ["புதுப்பிக்க	3108"
                 "புதுப்பிக்கத்தக்க	11657"
                 "புதுப்பிக்கப்பட்ட	4662"
                 "புதுப்பிக்கும்	4662"
                 "புதுப்பித்து	2331"]
          trie (-> lines io/lines->phoneme-freq-trie)
          exp-verbs #{(map->Verb {:root "புதுப்பி", :verb-class "6"})}]
      (is (= exp-verbs (find-verbs trie)))))
  (testing "class 3 coincidence from two nouns???"
    (let [lines ["அகால	3108"
                 "அகாலி	5439"]
          trie (-> lines io/lines->phoneme-freq-trie)
          exp-verbs #{}]
      (is (= exp-verbs (find-verbs trie))))) 
  (testing "class 3 coincidence from two nouns"
    (let [lines ["வெறி	3108"
                 "வெறும்	131336"]
          trie (-> lines io/lines->phoneme-freq-trie)
          exp-verbs #{}]
      (is (= exp-verbs (find-verbs trie))))) 
  (testing "class 3 coincidence from two nouns???"
    (let [lines ["ஆன	27976" 
                 "ஆனி	3108"]
          trie (-> lines io/lines->phoneme-freq-trie)
          exp-verbs #{}]
      (is (= exp-verbs (find-verbs trie)))))
  (testing "class 1a coincidence from simple adjective"
    (let [lines ["புதிது	2331"
                 "புதிய	1661520"]
          trie (-> lines io/lines->phoneme-freq-trie)
          exp-verbs #{}]
      (is (= exp-verbs (find-verbs trie))))) 
  (testing "ensure class 3 -ற்ற்-/-ற்- trans/intrans verb split isn't overcorrected"
    (let [lines ["ஏற	9325" 
                 "ஏறி	15542"
                 "ஏறிய	11657"
                 "ஏறினார்	3885"
                 "ஏறினால்	2331"
                 "ஏறு	3885"]
          trie (-> lines io/lines->phoneme-freq-trie)
          exp-verbs #{(map->Verb {:root "ஏறு", :verb-class "3"})}]
      (is (= exp-verbs (find-verbs trie))))
    (let [lines ["ஏற்ற	42742"
                 "ஏற்றக்	2331" 
                 "ஏற்றலாம்	2331"
                 "ஏற்றி	29531"]
          trie (-> lines io/lines->phoneme-freq-trie)
          exp-verbs #{(map->Verb {:root "ஏற்று", :verb-class "3"})}]
      (is (= exp-verbs (find-verbs trie)))))
  (testing "class 1a coincidence from simple adjective"
    (let [lines ["யோக	2331"
                 "யோகன்	3108"
                 "யோகா	57508"
                 "யோகாசன	5439"
                 "யோகி	79268"]
          trie (-> lines io/lines->phoneme-freq-trie)
          exp-verbs #{}]
      (is (= exp-verbs (find-verbs trie)))))


  

  )

