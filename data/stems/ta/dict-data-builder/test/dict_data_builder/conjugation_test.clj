(ns dict-data-builder.conjugation-test
  (:require [clojure.test :refer :all]
            [dict-data-builder.conjugation :as cjg :refer :all]
            [dict-data-builder.data :as data :refer [map->Verb]])
  (:import dict_data_builder.data.Verb))

(def செய் (map->Verb {:root "செய்" :verb-class "1a"}))

(def மாள் (map->Verb {:root "மாள்" :verb-class "1b"}))

(def செல் (map->Verb {:root "செல்" :verb-class "1c"}))

(def சோர் (map->Verb {:root "சோர்" :verb-class "2"}))

(def ஊற்று (map->Verb {:root "ஊற்று" :verb-class "3"}))

(def போடு (map->Verb {:root "போடு" :verb-class "4"}))

(def எடு (map->Verb {:root "எடு" :verb-class "6"}))

(def கற (map->Verb {:root "கற" :verb-class "7"}))

(def கேள் (map->Verb {:root "கேள்" :verb-class "5"}))

(def வில் (map->Verb {:root "வில்" :verb-class "5"}))

(def தின் (map->Verb {:root "தின்" :verb-class "5"}))

(def உண் (map->Verb {:root "உண்" :verb-class "5"}))

(def போ (map->Verb {:root "போ" :verb-class "3"}))

(def சொல் (map->Verb {:root "சொல்" :verb-class "3"}))

(def ஆ (map->Verb {:root "ஆ" :verb-class "3"}))

(deftest past-stem-test
  (testing "past stem - class 1a"
    (is (= "செய்த்" (past-stem செய்))))
  (testing "past stem - class 1b"
    (is (= "மாண்ட்" (past-stem மாள்))))
  (testing "past stem - class 1c"
    (is (= "சென்ற்" (past-stem செல்))))
  (testing "past stem - class 2"
    (is (= "சோர்ந்த்" (past-stem சோர்))))
  (testing "past stem - class 3"
    (is (= "ஊற்றின்" (past-stem ஊற்று))))
  (testing "past stem - class 4"
    (is (= "போட்ட்" (past-stem போடு))))
  (testing "past stem - class 6"
    (is (= "எடுத்த்" (past-stem எடு))))
  (testing "past stem - class 7"
    (is (= "கறந்த்" (past-stem கற)))))

(deftest present-stem-test
  (testing "present stem - class 1a"
    (is (= "செய்கிற்" (present-stem செய்))))
  (testing "present stem - class 1b"
    (is (= "மாள்கிற்" (present-stem மாள்))))
  (testing "present stem - class 1c"
    (is (= "செல்கிற்" (present-stem செல்))))
  (testing "present stem - class 2"
    (is (= "சோர்கிற்" (present-stem சோர்))))
  (testing "present stem - class 3"
    (is (= "ஊற்றுகிற்" (present-stem ஊற்று))))
  (testing "present stem - class 4"
    (is (= "போடுகிற்" (present-stem போடு))))
  (testing "present stem - class 6"
    (is (= "எடுக்கிற்" (present-stem எடு))))
  (testing "present stem - class 7"
    (is (= "கறக்கிற்" (present-stem கற)))))

(deftest future-stem-test
  (testing "future stem - class 1a"
    (is (= "செய்வ்" (future-stem செய்))))
  (testing "future stem - class 1b"
    (is (= "மாள்வ்" (future-stem மாள்))))
  (testing "future stem - class 1c"
    (is (= "செல்வ்" (future-stem செல்))))
  (testing "future stem - class 2"
    (is (= "சோர்வ்" (future-stem சோர்))))
  (testing "future stem - class 3"
    (is (= "ஊற்றுவ்" (future-stem ஊற்று))))
  (testing "future stem - class 4"
    (is (= "போடுவ்" (future-stem போடு))))
  (testing "future stem - class 6"
    (is (= "எடுப்ப்" (future-stem எடு))))
  (testing "future stem - class 7"
    (is (= "கறப்ப்" (future-stem கற)))))

(deftest infinitive-test
  (testing "infinitive - class 1a"
    (is (= "செய்ய" (infinitive செய்))))
  (testing "infinitive - class 1b"
    (is (= "மாள" (infinitive மாள்))))
  (testing "infinitive - class 1c"
    (is (= "செல்ல" (infinitive செல்))))
  (testing "infinitive - class 2"
    (is (= "சோர" (infinitive சோர்))))
  (testing "infinitive - class 3"
    (is (= "ஊற்ற" (infinitive ஊற்று))))
  (testing "infinitive - class 4"
    (is (= "போட" (infinitive போடு))))
  (testing "infinitive - class 6"
    (is (= "எடுக்க" (infinitive எடு))))
  (testing "infinitive - class 7"
    (is (= "கறக்க" (infinitive கற)))))

(deftest weak-verb-infinitive-test
  (testing "2 short letter weak verbs do not use சந்தி rules for infinitive"
    (is (= "விழ" (infinitive (map->Verb {:root "விழு" :verb-class "2"}))))
    (is (= "அழ" (infinitive (map->Verb {:root "அழு" :verb-class "1a"})))))
  (testing "other weak verbs still use சந்தி rules for infinitive"
    (is (= "சொல்ல" (infinitive (map->Verb {:root "சொல்" :verb-class "3"}))))))

(deftest avp-test
  (testing "avp - class 1a"
    (is (= "செய்து" (avp செய்))))
  (testing "avp - class 1b"
    (is (= "மாண்டு" (avp மாள்))))
  (testing "avp - class 1c"
    (is (= "சென்று" (avp செல்))))
  (testing "avp - class 2"
    (is (= "சோர்ந்து" (avp சோர்))))
  (testing "avp - class 3"
    (is (= "ஊற்றி" (avp ஊற்று))))
  (testing "avp - class 4"
    (is (= "போட்டு" (avp போடு))))
  (testing "avp - class 6"
    (is (= "எடுத்து" (avp எடு))))
  (testing "avp - class 7"
    (is (= "கறந்து" (avp கற)))))

(deftest negative-avp-test
  (testing "special case - இரு^7"
    (is (= "இல்லாமல்" (negative-avp (map->Verb {:root "இரு" :verb-class "7"})))))
  (testing "negative avp - class 1a"
    (is (= "செய்யாமல்" (negative-avp செய்))))
  (testing "negative avp - class 1b"
    (is (= "மாளாமல்" (negative-avp மாள்))))
  (testing "negative avp - class 1c"
    (is (= "செல்லாமல்" (negative-avp செல்))))
  (testing "negative avp - class 2"
    (is (= "சோராமல்" (negative-avp சோர்))))
  (testing "negative avp - class 3"
    (is (= "ஊற்றாமல்" (negative-avp ஊற்று))))
  (testing "negative avp - class 4"
    (is (= "போடாமல்" (negative-avp போடு))))
  (testing "negative avp - class 6"
    (is (= "எடுக்காமல்" (negative-avp எடு))))
  (testing "negative avp - class 7"
    (is (= "கறக்காமல்" (negative-avp கற)))))

(deftest negative-verb-conjugation-past-present-test
  (testing "negative verb conjugation - past/present - class 1a"
    (is (= #{"செய்யவில்லை"} (negative-verb-conjugation-past-present செய்))))
  (testing "negative verb conjugation - past/present - class 1b"
    (is (= #{"மாளவில்லை"} (negative-verb-conjugation-past-present மாள்))))
  (testing "negative verb conjugation - past/present - class 1c"
    (is (= #{"செல்லவில்லை"} (negative-verb-conjugation-past-present செல்))))
  (testing "negative verb conjugation - past/present - class 2"
    (is (= #{"சோரவில்லை"} (negative-verb-conjugation-past-present சோர்))))
  (testing "negative verb conjugation - past/present - class 3"
    (is (= #{"ஊற்றவில்லை"} (negative-verb-conjugation-past-present ஊற்று))))
  (testing "negative verb conjugation - past/present - class 4"
    (is (= #{"போடவில்லை"} (negative-verb-conjugation-past-present போடு))))
  (testing "negative verb conjugation - past/present - class 6"
    (is (= #{"எடுக்கவில்லை"} (negative-verb-conjugation-past-present எடு))))
  (testing "negative verb conjugation - past/present - class 7"
    (is (= #{"கறக்கவில்லை"} (negative-verb-conjugation-past-present கற))))
  (testing "2 short letter weak verbs do not use சந்தி rules for infinitive"
    (is (= #{"விழவில்லை"} (negative-verb-conjugation-past-present (map->Verb {:root "விழு" :verb-class "2"}))))
    (is (= #{"அழவில்லை"} (negative-verb-conjugation-past-present (map->Verb {:root "அழு" :verb-class "1a"}))))))

(deftest negative-verb-future-stem-test
  (testing "negative verb - past/present - class 1a"
    (is (= "செய்யமாட்ட்" (negative-verb-future-stem செய்))))
  (testing "negative verb - past/present - class 1b"
    (is (= "மாளமாட்ட்" (negative-verb-future-stem மாள்))))
  (testing "negative verb - past/present - class 1c"
    (is (= "செல்லமாட்ட்" (negative-verb-future-stem செல்))))
  (testing "negative verb - past/present - class 2"
    (is (= "சோரமாட்ட்" (negative-verb-future-stem சோர்))))
  (testing "negative verb - past/present - class 3"
    (is (= "ஊற்றமாட்ட்" (negative-verb-future-stem ஊற்று))))
  (testing "negative verb - past/present - class 4"
    (is (= "போடமாட்ட்" (negative-verb-future-stem போடு))))
  (testing "negative verb - past/present - class 6"
    (is (= "எடுக்கமாட்ட்" (negative-verb-future-stem எடு))))
  (testing "negative verb - past/present - class 7"
    (is (= "கறக்கமாட்ட்" (negative-verb-future-stem கற))))
  (testing "2 short letter weak verbs do not use சந்தி rules for infinitive"
    (is (= "விழமாட்ட்" (negative-verb-future-stem (map->Verb {:root "விழு" :verb-class "2"}))))
    (is (= "அழமாட்ட்" (negative-verb-future-stem (map->Verb {:root "அழு" :verb-class "1a"}))))))

(deftest conditional-test
  (testing "conditional - class 1a"
    (is (= "செய்தால்" (conditional செய்))))
  (testing "conditional - class 1b"
    (is (= "மாண்டால்" (conditional மாள்))))
  (testing "conditional - class 1c"
    (is (= "சென்றால்" (conditional செல்))))
  (testing "conditional - class 2"
    (is (= "சோர்ந்தால்" (conditional சோர்))))
  (testing "conditional - class 3"
    (is (= "ஊற்றினால்" (conditional ஊற்று))))
  (testing "conditional - class 4"
    (is (= "போட்டால்" (conditional போடு))))
  (testing "conditional - class 6"
    (is (= "எடுத்தால்" (conditional எடு))))
  (testing "conditional - class 7"
    (is (= "கறந்தால்" (conditional கற))))
  (testing "conditional - class 5 examples"
    (is (= "கேட்டால்" (conditional கேள்)))
    (is (= "விற்றால்" (conditional வில்)))
    (is (= "தின்றால்" (conditional தின்)))
    (is (= "உண்டால்" (conditional உண்)))))

(deftest negative-conditional-test
  (testing "negative conditional - class 1a"
    (is (= "செய்யாவிட்டால்" (negative-conditional செய்))))
  (testing "negative conditional - class 1b"
    (is (= "மாளாவிட்டால்" (negative-conditional மாள்))))
  (testing "negative conditional - class 1c"
    (is (= "செல்லாவிட்டால்" (negative-conditional செல்))))
  (testing "negative conditional - class 2"
    (is (= "சோராவிட்டால்" (negative-conditional சோர்))))
  (testing "negative conditional - class 3"
    (is (= "ஊற்றாவிட்டால்" (negative-conditional ஊற்று))))
  (testing "negative conditional - class 4"
    (is (= "போடாவிட்டால்" (negative-conditional போடு))))
  (testing "negative conditional - class 6"
    (is (= "எடுக்காவிட்டால்" (negative-conditional எடு))))
  (testing "negative conditional - class 7"
    (is (= "கறக்காவிட்டால்" (negative-conditional கற))))
  (testing "negative conditional - class 5 examples"
    (is (= "கேட்காவிட்டால்" (negative-conditional கேள்)))
    (is (= "விற்காவிட்டால்" (negative-conditional வில்)))
    (is (= "தின்னாவிட்டால்" (negative-conditional தின்)))
    (is (= "உண்ணாவிட்டால்" (negative-conditional உண்)))))

(deftest perfect-tense-verb-test
  (testing "perfect-tense-verb - class 1a"
    (is (= (map->Verb {:root "செய்திரு" :verb-class "7"})
           (perfect-tense-verb செய்))))
  (testing "perfect-tense-verb - class 1b"
    (is (= (map->Verb {:root "மாண்டிரு" :verb-class "7"})
           (perfect-tense-verb மாள்))))
  (testing "perfect-tense-verb - class 1c"
    (is (= (map->Verb {:root "சென்றிரு" :verb-class "7"})
           (perfect-tense-verb செல்))))
  (testing "perfect-tense-verb - class 2"
    (is (= (map->Verb {:root "சோர்ந்திரு" :verb-class "7"})
           (perfect-tense-verb சோர்))))
  (testing "perfect-tense-verb - class 3"
    (is (= (map->Verb {:root "ஊற்றியிரு" :verb-class "7"})
           (perfect-tense-verb ஊற்று))))
  (testing "perfect-tense-verb - class 4"
    (is (= (map->Verb {:root "போட்டிரு" :verb-class "7"})
           (perfect-tense-verb போடு))))
  (testing "perfect-tense-verb - class 6"
    (is (= (map->Verb {:root "எடுத்திரு" :verb-class "7"})
           (perfect-tense-verb எடு))))
  (testing "perfect-tense-verb - class 7"
    (is (= (map->Verb {:root "கறந்திரு" :verb-class "7"})
           (perfect-tense-verb கற))))
  (testing "class 5 verb"
    (is (= (map->Verb {:root "கேட்டிரு" :verb-class "7"})
           (perfect-tense-verb கேள்)))
    (is (= (map->Verb {:root "விற்றிரு" :verb-class "7"})
           (perfect-tense-verb வில்)))
    (is (= (map->Verb {:root "தின்றிரு" :verb-class "7"})
           (perfect-tense-verb தின்)))
    (is (= (map->Verb {:root "உண்டிரு" :verb-class "7"})
           (perfect-tense-verb உண்)))))

(deftest completion-விடு-verb-test
  (testing "completion-விடு-verb - class 1a"
    (is (= (map->Verb {:root "செய்துவிடு" :verb-class "4"})
           (completion-விடு-verb செய்))))
  (testing "completion-விடு-verb - class 1b"
    (is (= (map->Verb {:root "மாண்டுவிடு" :verb-class "4"})
           (completion-விடு-verb மாள்))))
  (testing "completion-விடு-verb - class 1c"
    (is (= (map->Verb {:root "சென்றுவிடு" :verb-class "4"})
           (completion-விடு-verb செல்))))
  (testing "completion-விடு-verb - class 2"
    (is (= (map->Verb {:root "சோர்ந்துவிடு" :verb-class "4"})
           (completion-விடு-verb சோர்))))
  (testing "completion-விடு-verb - class 3"
    (is (= (map->Verb {:root "ஊற்றிவிடு" :verb-class "4"})
           (completion-விடு-verb ஊற்று))))
  (testing "completion-விடு-verb - class 4"
    (is (= (map->Verb {:root "போட்டுவிடு" :verb-class "4"})
           (completion-விடு-verb போடு))))
  (testing "completion-விடு-verb - class 6"
    (is (= (map->Verb {:root "எடுத்துவிடு" :verb-class "4"})
           (completion-விடு-verb எடு))))
  (testing "completion-விடு-verb - class 7"
    (is (= (map->Verb {:root "கறந்துவிடு" :verb-class "4"})
           (completion-விடு-verb கற))))
  (testing "class 5 verb"
    (is (= (map->Verb {:root "கேட்டுவிடு" :verb-class "4"})
           (completion-விடு-verb கேள்)))
    (is (= (map->Verb {:root "விற்றுவிடு" :verb-class "4"})
           (completion-விடு-verb வில்)))
    (is (= (map->Verb {:root "தின்றுவிடு" :verb-class "4"})
           (completion-விடு-verb தின்)))
    (is (= (map->Verb {:root "உண்டுவிடு" :verb-class "4"})
           (completion-விடு-verb உண்)))))

(deftest reflexive-verb-test
  (testing "reflexive-verb - class 1a"
    (is (= (map->Verb {:root "செய்துகொள்" :verb-class "1b"})
           (reflexive-verb செய்))))
  (testing "reflexive-verb - class 1b"
    (is (= (map->Verb {:root "மாண்டுகொள்" :verb-class "1b"})
           (reflexive-verb மாள்))))
  (testing "reflexive-verb - class 1c"
    (is (= (map->Verb {:root "சென்றுகொள்" :verb-class "1b"})
           (reflexive-verb செல்))))
  (testing "reflexive-verb - class 2"
    (is (= (map->Verb {:root "சோர்ந்துகொள்" :verb-class "1b"})
           (reflexive-verb சோர்))))
  (testing "reflexive-verb - class 3"
    (is (= (map->Verb {:root "ஊற்றிக்கொள்" :verb-class "1b"})
           (reflexive-verb ஊற்று))))
  (testing "reflexive-verb - class 4"
    (is (= (map->Verb {:root "போட்டுக்கொள்" :verb-class "1b"})
           (reflexive-verb போடு))))
  (testing "reflexive-verb - class 6"
    (is (= (map->Verb {:root "எடுத்துக்கொள்" :verb-class "1b"})
           (reflexive-verb எடு))))
  (testing "reflexive-verb - class 7"
    (is (= (map->Verb {:root "கறந்துகொள்" :verb-class "1b"})
           (reflexive-verb கற))))
  (testing "class 5 verb"
    (testing "double strong letter"
      (is (= (map->Verb {:root "கேட்டுக்கொள்" :verb-class "1b"})
             (reflexive-verb கேள்)))
      (is (= (map->Verb {:root "விற்றுக்கொள்" :verb-class "1b"})
           (reflexive-verb வில்))))
    (testing "do not double strong letter"
      (is (= (map->Verb {:root "தின்றுகொள்" :verb-class "1b"})
             (reflexive-verb தின்)))
      (is (= (map->Verb {:root "உண்டுகொள்" :verb-class "1b"})
           (reflexive-verb உண்))))))

(deftest continuous-tense-verb-test
  (testing "continuous-tense-verb - class 1a"
    (is (= (map->Verb {:root "செய்துகொண்டிரு" :verb-class "7"})
           (continuous-tense-verb செய்))))
  (testing "continuous-tense-verb - class 1b"
    (is (= (map->Verb {:root "மாண்டுகொண்டிரு" :verb-class "7"})
           (continuous-tense-verb மாள்))))
  (testing "continuous-tense-verb - class 1c"
    (is (= (map->Verb {:root "சென்றுகொண்டிரு" :verb-class "7"})
           (continuous-tense-verb செல்))))
  (testing "continuous-tense-verb - class 2"
    (is (= (map->Verb {:root "சோர்ந்துகொண்டிரு" :verb-class "7"})
           (continuous-tense-verb சோர்))))
  (testing "continuous-tense-verb - class 3"
    (is (= (map->Verb {:root "ஊற்றிக்கொண்டிரு" :verb-class "7"})
           (continuous-tense-verb ஊற்று))))
  (testing "continuous-tense-verb - class 4"
    (is (= (map->Verb {:root "போட்டுக்கொண்டிரு" :verb-class "7"})
           (continuous-tense-verb போடு))))
  (testing "continuous-tense-verb - class 6"
    (is (= (map->Verb {:root "எடுத்துக்கொண்டிரு" :verb-class "7"})
           (continuous-tense-verb எடு))))
  (testing "continuous-tense-verb - class 7"
    (is (= (map->Verb {:root "கறந்துகொண்டிரு" :verb-class "7"})
           (continuous-tense-verb கற))))
  (testing "class 5 verb"
    (testing "double strong letter"
      (is (= (map->Verb {:root "கேட்டுக்கொண்டிரு" :verb-class "7"})
             (continuous-tense-verb கேள்)))
      (is (= (map->Verb {:root "விற்றுக்கொண்டிரு" :verb-class "7"})
           (continuous-tense-verb வில்))))
    (testing "do not double strong letter"
      (is (= (map->Verb {:root "தின்றுகொண்டிரு" :verb-class "7"})
             (continuous-tense-verb தின்)))
      (is (= (map->Verb {:root "உண்டுகொண்டிரு" :verb-class "7"})
           (continuous-tense-verb உண்))))))

(deftest passive-verb-test
  (testing "passive-verb - class 1a"
    (is (= (map->Verb {:root "செய்யப்படு" :verb-class "4"})
           (passive-verb செய்))))
  (testing "passive-verb - class 3"
    (is (= (map->Verb {:root "ஊற்றப்படு" :verb-class "4"})
           (passive-verb ஊற்று))))
  (testing "passive-verb - class 4"
    (is (= (map->Verb {:root "போடப்படு" :verb-class "4"})
           (passive-verb போடு))))
  (testing "passive-verb - class 6"
    (is (= (map->Verb {:root "எடுக்கப்படு" :verb-class "4"})
           (passive-verb எடு))))
  (testing "passive-verb - class 7"
    (is (= (map->Verb {:root "கறக்கப்படு" :verb-class "4"})
           (passive-verb கற))))
  (testing "class 5 verb"
    (testing "double strong letter"
      (is (= (map->Verb {:root "கேட்கப்படு" :verb-class "4"})
             (passive-verb கேள்)))
      (is (= (map->Verb {:root "விற்கப்படு" :verb-class "4"})
           (passive-verb வில்))))
    (testing "do not double strong letter"
      (is (= (map->Verb {:root "தின்னப்படு" :verb-class "4"})
             (passive-verb தின்)))
      (is (= (map->Verb {:root "உண்ணப்படு" :verb-class "4"})
           (passive-verb உண்))))))

(deftest present-conjugation-அவை-test
  (testing "present-conjugation-அவை - class 1a"
    (is (= #{"செய்கின்றன"} (present-conjugation-அவை செய்))))
  (testing "present-conjugation-அவை - class 1b"
    (is (= #{"மாள்கின்றன"} (present-conjugation-அவை மாள்))))
  (testing "present-conjugation-அவை - class 1c"
    (is (= #{"செல்கின்றன"} (present-conjugation-அவை செல்))))
  (testing "present-conjugation-அவை - class 2"
    (is (= #{"சோர்கின்றன"} (present-conjugation-அவை சோர்))))
  (testing "present-conjugation-அவை - class 3"
    (is (= #{"ஊற்றுகின்றன"} (present-conjugation-அவை ஊற்று))))
  (testing "present-conjugation-அவை - class 4"
    (is (= #{"போடுகின்றன"} (present-conjugation-அவை போடு))))
  (testing "present-conjugation-அவை - class 6"
    (is (= #{"எடுக்கின்றன"} (present-conjugation-அவை எடு))))
  (testing "present-conjugation-அவை - class 7"
    (is (= #{"கறக்கின்றன"} (present-conjugation-அவை கற))))
  (testing "present-conjugation-அவை - class 5 examples"
    (is (= #{"கேட்கின்றன"} (present-conjugation-அவை கேள்)))
    (is (= #{"விற்கின்றன"} (present-conjugation-அவை வில்)))
    (is (= #{"தின்கின்றன"} (present-conjugation-அவை தின்)))
    (is (= #{"உண்கின்றன"} (present-conjugation-அவை உண்)))))

(deftest future-conjugation-அது-அவை-test
  (testing "future-conjugation-அது-அவை - class 1a"
    (is (= #{"செய்யும்"} (future-conjugation-அது-அவை செய்))))
  (testing "future-conjugation-அது-அவை - class 1b"
    (is (= #{"மாளும்"} (future-conjugation-அது-அவை மாள்))))
  (testing "future-conjugation-அது-அவை - class 1c"
    (is (= #{"செல்லும்"} (future-conjugation-அது-அவை செல்))))
  (testing "future-conjugation-அது-அவை - class 2"
    (is (= #{"சோரும்"} (future-conjugation-அது-அவை சோர்))))
  (testing "future-conjugation-அது-அவை - class 3"
    (is (= #{"ஊற்றும்"} (future-conjugation-அது-அவை ஊற்று))))
  (testing "future-conjugation-அது-அவை - class 4"
    (is (= #{"போடும்"} (future-conjugation-அது-அவை போடு))))
  (testing "future-conjugation-அது-அவை - class 6"
    (is (= #{"எடுக்கும்"} (future-conjugation-அது-அவை எடு))))
  (testing "future-conjugation-அது-அவை - class 7"
    (is (= #{"கறக்கும்"} (future-conjugation-அது-அவை கற))))
  (testing "future-conjugation-அது-அவை - class 5 examples"
    (is (= #{"கேட்கும்"} (future-conjugation-அது-அவை கேள்)))
    (is (= #{"விற்கும்"} (future-conjugation-அது-அவை வில்)))
    (is (= #{"தின்னும்"} (future-conjugation-அது-அவை தின்)))
    (is (= #{"உண்ணும்"} (future-conjugation-அது-அவை உண்)))))

(deftest past-conjugation-அது-test
  (testing "past-conjugation-அது - class 1a"
    (is (= #{"செய்தது"} (past-conjugation-அது செய்))))
  (testing "past-conjugation-அது - class 1b"
    (is (= #{"மாண்டது"} (past-conjugation-அது மாள்))))
  (testing "past-conjugation-அது - class 1c"
    (is (= #{"சென்றது"} (past-conjugation-அது செல்))))
  (testing "past-conjugation-அது - class 2"
    (is (= #{"சோர்ந்தது"} (past-conjugation-அது சோர்))))
  (testing "past-conjugation-அது - class 3"
    (is (= #{"ஊற்றியது" "ஊற்றிற்று"} (past-conjugation-அது ஊற்று))))
  (testing "past-conjugation-அது - class 4"
    (is (= #{"போட்டது"} (past-conjugation-அது போடு))))
  (testing "past-conjugation-அது - class 6"
    (is (= #{"எடுத்தது"} (past-conjugation-அது எடு))))
  (testing "past-conjugation-அது - class 7"
    (is (= #{"கறந்தது"} (past-conjugation-அது கற))))
  (testing "past-conjugation-அது - class 5 examples"
    (is (= #{"கேட்டது"} (past-conjugation-அது கேள்)))
    (is (= #{"விற்றது"} (past-conjugation-அது வில்)))
    (is (= #{"தின்றது"} (past-conjugation-அது தின்)))
    (is (= #{"உண்டது"} (past-conjugation-அது உண்))))
  (testing "irregular verbs"
    (is (= #{"போனது" "போயிற்று"} (past-conjugation-அது போ)))
    (is (= #{"சொன்னது" "சொல்லிற்று"} (past-conjugation-அது சொல்)))
    (is (= #{"ஆனது" "ஆயிற்று"} (past-conjugation-அது ஆ)))))

(deftest past-conjugation-அவை-test
  (testing "past-conjugation-அவை - class 1a"
    (is (= #{"செய்தன"} (past-conjugation-அவை செய்))))
  (testing "past-conjugation-அவை - class 1b"
    (is (= #{"மாண்டன"} (past-conjugation-அவை மாள்))))
  (testing "past-conjugation-அவை - class 1c"
    (is (= #{"சென்றன"} (past-conjugation-அவை செல்))))
  (testing "past-conjugation-அவை - class 2"
    (is (= #{"சோர்ந்தன"} (past-conjugation-அவை சோர்))))
  (testing "past-conjugation-அவை - class 3"
    (is (= #{"ஊற்றின"} (past-conjugation-அவை ஊற்று))))
  (testing "past-conjugation-அவை - class 4"
    (is (= #{"போட்டன"} (past-conjugation-அவை போடு))))
  (testing "past-conjugation-அவை - class 6"
    (is (= #{"எடுத்தன"} (past-conjugation-அவை எடு))))
  (testing "past-conjugation-அவை - class 7"
    (is (= #{"கறந்தன"} (past-conjugation-அவை கற))))
  (testing "past-conjugation-அவை - class 5 examples"
    (is (= #{"கேட்டன"} (past-conjugation-அவை கேள்)))
    (is (= #{"விற்றன"} (past-conjugation-அவை வில்)))
    (is (= #{"தின்றன"} (past-conjugation-அவை தின்)))
    (is (= #{"உண்டன"} (past-conjugation-அவை உண்))))
  (testing "irregular verbs"
    (is (= #{"போயின"} (past-conjugation-அவை போ)))
    (is (= #{"சொல்லின"} (past-conjugation-அவை சொல்)))
    (is (= #{"ஆயின"} (past-conjugation-அவை ஆ)))))

(deftest verb-transformers-test
  (testing "செய்"
    (is (= (verb-with-transformed-verbs செய்) #{(map->Verb {:root "செய்துகொண்டிரு", :verb-class "7"})
                                               (map->Verb {:root "செய்துவிடு", :verb-class "4"})
                                               (map->Verb {:root "செய்யப்படு", :verb-class "4"})
                                               (map->Verb {:root "செய்", :verb-class "1a"})
                                               (map->Verb {:root "செய்துகொள்", :verb-class "1b"})
                                               (map->Verb {:root "செய்திரு", :verb-class "7"})})))
  (testing "மாள்"
    (is (= (verb-with-transformed-verbs மாள்) #{(map->Verb {:root "மாண்டுகொண்டிரு", :verb-class "7"})
                                               (map->Verb {:root "மாண்டுவிடு", :verb-class "4"})
                                               (map->Verb {:root "மாளப்படு", :verb-class "4"})
                                               (map->Verb {:root "மாள்", :verb-class "1b"})
                                               (map->Verb {:root "மாண்டுகொள்", :verb-class "1b"})
                                               (map->Verb {:root "மாண்டிரு", :verb-class "7"})})))
  (testing "போடு"
    (is (= (verb-with-transformed-verbs போடு) #{(map->Verb {:root "போட்டுக்கொண்டிரு", :verb-class "7"})
                                                (map->Verb {:root "போட்டுவிடு", :verb-class "4"})
                                                (map->Verb {:root "போடப்படு", :verb-class "4"})
                                                (map->Verb {:root "போடு", :verb-class "4"})
                                                (map->Verb {:root "போட்டுக்கொள்", :verb-class "1b"})
                                                (map->Verb {:root "போட்டிரு", :verb-class "7"})})))
  (testing "கற"
    (is (= (verb-with-transformed-verbs கற) #{(map->Verb {:root "கறந்துகொண்டிரு", :verb-class "7"})
                                              (map->Verb {:root "கறந்துவிடு", :verb-class "4"})
                                              (map->Verb {:root "கறக்கப்படு", :verb-class "4"})
                                              (map->Verb {:root "கற", :verb-class "7"})
                                              (map->Verb {:root "கறந்துகொள்", :verb-class "1b"})
                                              (map->Verb {:root "கறந்திரு", :verb-class "7"})})))
  (testing "எடு"
    (is (= (verb-with-transformed-verbs எடு) #{(map->Verb {:root "எடுத்துக்கொண்டிரு", :verb-class "7"})
                                               (map->Verb {:root "எடுத்துவிடு", :verb-class "4"})
                                               (map->Verb {:root "எடுக்கப்படு", :verb-class "4"})
                                               (map->Verb {:root "எடு", :verb-class "6"})
                                               (map->Verb {:root "எடுத்துக்கொள்", :verb-class "1b"})
                                               (map->Verb {:root "எடுத்திரு", :verb-class "7"})})))
  (testing "தோல்"
    (let [தோல் (map->Verb {:root "தோல்", :verb-class "5", :infinitive "தோற்க", :avp "தோற்று", :past-stem "தோற்ற்", :present-stem "தோற்கிற்", :future-stem "தோற்ப்"})]
      (is (= (verb-with-transformed-verbs தோல்) #{(map->Verb {:root "தோற்றுக்கொண்டிரு", :verb-class "7"})
                                                 (map->Verb {:root "தோற்றுவிடு", :verb-class "4"})
                                                 (map->Verb {:root "தோற்கப்படு", :verb-class "4"})
                                                 தோல்
                                                 (map->Verb {:root "தோற்றுக்கொள்", :verb-class "1b"})
                                                 (map->Verb {:root "தோற்றிரு", :verb-class "7"})})))))
