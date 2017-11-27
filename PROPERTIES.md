# Unilex Properties

All files in [data](data/) are tab-separated plaintext files in
Unicode encoding. The file names are [Unicode language
identfiers](http://unicode.org/reports/tr35/#Unicode_language_identifier)
in IETF BCP47 syntax; for example, `vec-u-sd-itpd.txt` contains
data for the Venetian language as used in the Padua subdivision of Italy.


## Word frequency

In [frequency](frequency/), we collect data how often each word form
appears per million tokens in a language corpus. Currently, we use
Google’s [Corpus Crawler](https://github.com/googlei18n/corpuscrawler)
project to build language corpora from we’re computing word frequencies,
but we’re open to accepting other contributions.


## Pronunciation

In [pronunciation](pronunciation/), we’ll collect phonemic transcriptions
of every word form to the
[International Phonetic Alphabet](https://en.wikipedia.org/wiki/International_Phonetic_Alphabet).
