# Unilex Properties

All files in [data](data/) are tab-separated plaintext files in
UTF-8 encoding. The file names are [Unicode locale
identifiers](http://unicode.org/reports/tr35/#Unicode_locale_identifier)
in IETF BCP47 syntax; for example, `vec-u-sd-itpd.txt` contains
data for the Venetian language as used in the Padua subdivision of Italy.

If you’d like to contribute data, please
[tell us by filing a GitHub issue](https://github.com/unicode-org/unilex/issues).

For more background, see the main [Unilex description](https://github.com/unicode-org/unilex/blob/master/README.md).


## Word frequency

In [frequency](data/frequency/), we collect data how often each word
form appears per billion tokens in a language corpus. (Our corpora are
actually much smaller than a billion tokens, but their size varies a lot
depending on the language; so we scale the numbers to a hypothetical
total of one billion tokens per language). Currently, we use Google’s
[Corpus Crawler](https://github.com/googlei18n/corpuscrawler) project
to build language corpora. We’re computing our word frequencies on these
crawled corpora, but we’re open to accepting other contributions.

**Noise:** We are just getting started, so there will be some noise in
the word frequency data. For example, there may be odd words resulting
from quoting a foreign language, or words representing model numbers
(“A3”). So people should use it with that in mind. That being said, it
should be usable enough to advance the quality for languages that
otherwise have little available data.

**Segmentation:** The crawled data uses the word-break algorithm of
the ICU library. For many languages, this corresponds to what people
think of as “space-delimited” words. For languages that don’t
typically use spaces, and are not supported by ICU’s word break, the
mechanism doesn’t yet work. We’ll need to address this in the future.

**N-Grams:** Currently, we have no n-gram data available for our
datasets. However, you can run [Corpus
Crawler](https://github.com/googlei18n/corpuscrawler) to crawl
plaintext corpora yourself, and extract n-grams from the crawled
content.

**Adding more data:** To add more data to a language requires
modifying the code of
[Corpus Crawler](https://github.com/googlei18n/corpuscrawler)
for that language. The changes are to 
fetch additional URLs, and to extract text from the crawled
content. For more information, see
[Corpus Crawler](https://github.com/googlei18n/corpuscrawler).


## Pronunciation

In [pronunciation](data/pronunciation/), we collect phonemic
transcriptions of every word form to the [International Phonetic
Alphabet](https://en.wikipedia.org/wiki/International_Phonetic_Alphabet).

**File format:** The columns are identified by their headers
in the TSV file. Additional columns may be added over time.

* `Form` is the surface form to be pronounced. There may be
multiple rows for the same form in case it varies by part of
speech or grammatical features.

* `Pronunciation` is a phonemic
transcription in [IPA](https://en.wikipedia.org/wiki/International_Phonetic_Alphabet).

* `PartOfSpeech` and `Features` are optional fields, used to distinguish
cases where the same form has multiple pronunciations. Currently, this
is only used for [Bangla pronunciations](https://raw.githubusercontent.com/unicode-org/unilex/master/data/pronunciation/bn.txt) but we anticipate that other
languages will need the same. As identifiers, we use the
[part of speech tags](http://universaldependencies.org/u/pos/)
and [lexical features](http://universaldependencies.org/u/feat/index.html)
from the [Universal Dependencies Project](http://universaldependencies.org/).
When there’s no information available, we use `*`.

**Adding more data:** We’re soliciting contributions.
Please [file an issue](https://github.com/unicode-org/unilex/issues)
to improve the current data, or to add additional data sets.
You’re also welcome to simply send [pull requests](https://help.github.com/categories/collaborating-with-issues-and-pull-requests/) via GitHub.


## Hyphenation

In [hyphenation](data/hyphenation/), we collect hyhenated words.
An entry `uit➊spra➋ken` means that it’s better to hyphenate
`uit-spraken` than `uitspra-ken`.

At some point, we’ll need to
model hyphenations that modify the letter sequence,
but our current data doesn't yet need the additional structure.
For example,
in German traditional orthography (de-1901), the word `Beckenbruch`
is hyphenated as `Bek-ken-bruch`.
One way we‘ve considered expressing this is as `Be⟨ck|k➋k⟩en➊bruch`.

**Adding more data:** We’re soliciting contributions. Again,
please [file an issue](https://github.com/unicode-org/unilex/issues)
to improve the current data, or to add additional data sets.
You’re also welcome to simply send [pull requests](https://help.github.com/categories/collaborating-with-issues-and-pull-requests/) via GitHub.


## Experimental

### Morphology and Grammar

In the long term, it would be good to model morphological and grammatical features.
Currently, however, we’re not sure how to do this. The current data in
[stems](data/experimental/stems/) is highly experimental and particularly likely to change. Before settling on a format, we should
model a set of languages with more challenging morphologies.
