(ns dict-data-builder.verb-stem-test
  (:require [clojure.test :refer :all]
            [dict-data-builder.data :as data :refer [map->Verb]]
            [dict-data-builder.verb-stem :as vs :refer :all])
  (:import dict_data_builder.data.Verb))

(def இடம்பெறு (map->Verb {:root "இடம்பெறு" :verb-class "4"}))

(def இடம்பெற்றிரு (map->Verb {:root "இடம்பெற்றிரு" :verb-class "7"}))

(def இடம்பெற்றுக்கொள் (map->Verb {:root "இடம்பெற்றுக்கொள்" :verb-class "1b"}))

(def இடம்பெறப்படு (map->Verb {:root "இடம்பெறப்படு" :verb-class "4"}))

(def இடம்பெற்றுக்கொண்டிரு (map->Verb {:root "இடம்பெற்றுக்கொண்டிரு" :verb-class "7"}))

(deftest verb-derivation-map
  (testing "இடம்பெறு"
    (is (= (verb->derivation-map இடம்பெறு)
           {#dict_data_builder.data.Verb{:root "இடம்பெறவேண்டு", :verb-class "3"}
            #dict_data_builder.data.Verb{:root "இடம்பெறு", :verb-class "4"},
            #dict_data_builder.data.Verb{:root "இடம்பெற்றுக்கொள்",
                                         :verb-class "1b"}
            #dict_data_builder.data.Verb{:root "இடம்பெறு", :verb-class "4"},
            #dict_data_builder.data.Verb{:root "இடம்பெற்றிரு", :verb-class "7"}
            #dict_data_builder.data.Verb{:root "இடம்பெறு", :verb-class "4"},
            #dict_data_builder.data.Verb{:root "இடம்பெறப்பட்டிரு",
                                         :verb-class "7"}
            #dict_data_builder.data.Verb{:root "இடம்பெறு", :verb-class "4"},
            #dict_data_builder.data.Verb{:root "இடம்பெறப்படு", :verb-class "4"}
            #dict_data_builder.data.Verb{:root "இடம்பெறு", :verb-class "4"},
            #dict_data_builder.data.Verb{:root "இடம்பெற்றுக்கொள்ளப்படு",
                                         :verb-class "4"}
            #dict_data_builder.data.Verb{:root "இடம்பெறு", :verb-class "4"},
            #dict_data_builder.data.Verb{:root "இடம்பெறக்கூடு", :verb-class "3"}
            #dict_data_builder.data.Verb{:root "இடம்பெறு", :verb-class "4"},
            #dict_data_builder.data.Verb{:root "இடம்பெற்றுவிடு",
                                         :verb-class "4"}
            #dict_data_builder.data.Verb{:root "இடம்பெறு", :verb-class "4"},
            #dict_data_builder.data.Verb{:root "இடம்பெற்றுக்கொண்டிரு",
                                         :verb-class "7"}
            #dict_data_builder.data.Verb{:root "இடம்பெறு", :verb-class "4"}}))))

(deftest verbs-transformed-verb-derivation-map
  (testing "இடம்பெறு"
    (is (= (verbs->transformed-verb-derivation-map [இடம்பெறு])
           {#dict_data_builder.data.Verb{:root "இடம்பெறவேண்டு", :verb-class "3"}
            #dict_data_builder.data.Verb{:root "இடம்பெறு", :verb-class "4"},
            #dict_data_builder.data.Verb{:root "இடம்பெற்றுக்கொள்",
                                         :verb-class "1b"}
            #dict_data_builder.data.Verb{:root "இடம்பெறு", :verb-class "4"},
            #dict_data_builder.data.Verb{:root "இடம்பெற்றிரு", :verb-class "7"}
            #dict_data_builder.data.Verb{:root "இடம்பெறு", :verb-class "4"},
            #dict_data_builder.data.Verb{:root "இடம்பெறப்பட்டிரு",
                                         :verb-class "7"}
            #dict_data_builder.data.Verb{:root "இடம்பெறு", :verb-class "4"},
            #dict_data_builder.data.Verb{:root "இடம்பெறப்படு", :verb-class "4"}
            #dict_data_builder.data.Verb{:root "இடம்பெறு", :verb-class "4"},
            #dict_data_builder.data.Verb{:root "இடம்பெற்றுக்கொள்ளப்படு",
                                         :verb-class "4"}
            #dict_data_builder.data.Verb{:root "இடம்பெறு", :verb-class "4"},
            #dict_data_builder.data.Verb{:root "இடம்பெறக்கூடு", :verb-class "3"}
            #dict_data_builder.data.Verb{:root "இடம்பெறு", :verb-class "4"},
            #dict_data_builder.data.Verb{:root "இடம்பெற்றுவிடு",
                                         :verb-class "4"}
            #dict_data_builder.data.Verb{:root "இடம்பெறு", :verb-class "4"},
            #dict_data_builder.data.Verb{:root "இடம்பெற்றுக்கொண்டிரு",
                                         :verb-class "7"}
            #dict_data_builder.data.Verb{:root "இடம்பெறு", :verb-class "4"}})))
  (testing "இடம்பெறு + derivatives"
    (is (= (verbs->transformed-verb-derivation-map [இடம்பெறு
                                                    இடம்பெற்றிரு])
           {#dict_data_builder.data.Verb{:root "இடம்பெறவேண்டு", :verb-class "3"}
            #dict_data_builder.data.Verb{:root "இடம்பெறு", :verb-class "4"},
            
            #dict_data_builder.data.Verb{:root "இடம்பெற்றுக்கொள்",
                                         :verb-class "1b"}
            #dict_data_builder.data.Verb{:root "இடம்பெறு", :verb-class "4"},
            
            #dict_data_builder.data.Verb{:root "இடம்பெற்றிரு", :verb-class "7"}
            #dict_data_builder.data.Verb{:root "இடம்பெறு", :verb-class "4"},

            #dict_data_builder.data.Verb{:root "இடம்பெறப்பட்டிரு",
                                         :verb-class "7"}
            #dict_data_builder.data.Verb{:root "இடம்பெறு", :verb-class "4"},

            #dict_data_builder.data.Verb{:root "இடம்பெறப்படு", :verb-class "4"}
            #dict_data_builder.data.Verb{:root "இடம்பெறு", :verb-class "4"},

            #dict_data_builder.data.Verb{:root "இடம்பெற்றுக்கொள்ளப்படு",
                                         :verb-class "4"}
            #dict_data_builder.data.Verb{:root "இடம்பெறு", :verb-class "4"},

            #dict_data_builder.data.Verb{:root "இடம்பெறக்கூடு", :verb-class "3"}
            #dict_data_builder.data.Verb{:root "இடம்பெறு", :verb-class "4"},

            #dict_data_builder.data.Verb{:root "இடம்பெற்றுவிடு",
                                         :verb-class "4"}
            #dict_data_builder.data.Verb{:root "இடம்பெறு", :verb-class "4"},

            #dict_data_builder.data.Verb{:root "இடம்பெற்றுக்கொண்டிரு",
                                         :verb-class "7"}
            #dict_data_builder.data.Verb{:root "இடம்பெறு", :verb-class "4"}



            #dict_data_builder.data.Verb{:root "இடம்பெற்றிருக்கவேண்டு", :verb-class "3"}
            #dict_data_builder.data.Verb{:root "இடம்பெற்றிரு", :verb-class "7"},

            #dict_data_builder.data.Verb{:root "இடம்பெற்றிருந்துகொள்",
                                         :verb-class "1b"}
            #dict_data_builder.data.Verb{:root "இடம்பெற்றிரு", :verb-class "7"},

            #dict_data_builder.data.Verb{:root "இடம்பெற்றிருந்திரு", :verb-class "7"}
            #dict_data_builder.data.Verb{:root "இடம்பெற்றிரு", :verb-class "7"},

            #dict_data_builder.data.Verb{:root "இடம்பெற்றிருக்கப்பட்டிரு",
                                         :verb-class "7"}
            #dict_data_builder.data.Verb{:root "இடம்பெற்றிரு", :verb-class "7"},

            #dict_data_builder.data.Verb{:root "இடம்பெற்றிருக்கப்படு", :verb-class "4"}
            #dict_data_builder.data.Verb{:root "இடம்பெற்றிரு", :verb-class "7"},

            #dict_data_builder.data.Verb{:root "இடம்பெற்றிருந்துகொள்ளப்படு",
                                         :verb-class "4"}
            #dict_data_builder.data.Verb{:root "இடம்பெற்றிரு", :verb-class "7"},

            #dict_data_builder.data.Verb{:root "இடம்பெற்றிருக்கக்கூடு", :verb-class "3"}
            #dict_data_builder.data.Verb{:root "இடம்பெற்றிரு", :verb-class "7"},

            #dict_data_builder.data.Verb{:root "இடம்பெற்றிருந்துவிடு",
                                         :verb-class "4"}
            #dict_data_builder.data.Verb{:root "இடம்பெற்றிரு", :verb-class "7"},

            #dict_data_builder.data.Verb{:root "இடம்பெற்றிருந்துகொண்டிரு",
                                         :verb-class "7"}
            #dict_data_builder.data.Verb{:root "இடம்பெற்றிரு", :verb-class "7"}

            }))))

(deftest verbs-to-stems-test
  (is (= #{இடம்பெறு} (verbs->stems [இடம்பெறு
                                   இடம்பெற்றிரு])))
  (is (= #{இடம்பெறு} (verbs->stems [இடம்பெறு
                                   இடம்பெற்றிரு
                                   இடம்பெற்றுக்கொள்
                                   இடம்பெறப்படு
                                   இடம்பெற்றுக்கொண்டிரு]))))
