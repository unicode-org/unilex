(ns dict-data-builder.io-test
  (:require [clojure.test :refer :all]
            [dict-data-builder.io :as io]))

(let [lines1 ["அகப்பட்ட	4662"
              "அகப்பட்டு	4662"]]
  (deftest input-and-parsing
    (testing "reading input file"
      (let [lines1b ["Form	Frequency"
                     ""
                     "# SPDX-License-Identifier: Unicode-DFS-2016"
                     "# Corpus-Size: 1286773"
                     "அகப்பட்ட	4662"
                     "அகப்பட்டு	4662"]]
        (testing "filtering only lines with Tamil words"
          (let [filtered-lines1 (filter io/is-tamil-line lines1)
                filtered-lines1b (filter io/is-tamil-line lines1b)]
            (is (= lines1 lines1))
            (is (= lines1 filtered-lines1b))))
        (testing "converting into a trie"
          (let [exp-trie1 {"அ"
                           {"க்"
                            {"அ"
                             {"ப்"
                              {"ப்"
                               {"அ"
                                {"ட்"
                                 {"ட்" 
                                  {"அ" {nil 4662},
                                   "உ" {nil 4662}}}}}}}}}}]
            (is (= exp-trie1 (io/lines->phoneme-freq-trie lines1)))))))))
