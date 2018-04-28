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


## Pronunciation

In [pronunciation](data/pronunciation/), we collect phonemic
transcriptions of every word form to the [International Phonetic
Alphabet](https://en.wikipedia.org/wiki/International_Phonetic_Alphabet).

If a word has multiple pronunciations, we have separate entries.
If the pronunciation varies by part of speech or grammatical features,
we have a `PartOfSpeech` and `Features` column in the pronunciation
dictionary; see
[Bangla pronunciation](https://raw.githubusercontent.com/unicode-org/unilex/master/data/pronunciation/bn.txt) for an example. As
with everything in this project, the data format will evolve over time.

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


## Morphology

The data in [stems](data/stems/) is highly experimental and
particularly likely to change. Before settling on a format, we should
model a set of languages with more challenging morphologies.


## Part of Speech

Currently, we have no part-of-speech tags. Contributions are welcome.
Please [file an issue](https://github.com/unicode-org/unilex/issues)
to improve the current data, or to add additional data sets.
You’re also welcome to simply send [pull requests](https://help.github.com/categories/collaborating-with-issues-and-pull-requests/) via GitHub.
