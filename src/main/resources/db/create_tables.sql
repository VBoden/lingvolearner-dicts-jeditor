
CREATE TABLE "words_category" ("id" integer NOT NULL PRIMARY KEY AUTOINCREMENT, "name" varchar(30) NOT NULL);

CREATE TABLE "words_language" ("code" varchar(2) NOT NULL PRIMARY KEY, "name" varchar(20) NOT NULL);

CREATE TABLE "words_dictionary" ("id" integer NOT NULL PRIMARY KEY AUTOINCREMENT, "name" varchar(30) NOT NULL, "language_from_id" varchar(2) NOT NULL REFERENCES "words_language" ("code") DEFERRABLE INITIALLY DEFERRED, "language_to_id" varchar(2) NOT NULL REFERENCES "words_language" ("code") DEFERRABLE INITIALLY DEFERRED);

CREATE INDEX "words_dictionary_language_from_id_e44f0bd9" ON "words_dictionary" ("language_from_id");
CREATE INDEX "words_dictionary_language_to_id_7bcc9605" ON "words_dictionary" ("language_to_id");

CREATE TABLE "words_word" ("id" integer NOT NULL PRIMARY KEY AUTOINCREMENT, "word" varchar(30) NOT NULL, "language_id" varchar(2) NOT NULL REFERENCES "words_language" ("code") DEFERRABLE INITIALLY DEFERRED, "notes" varchar(50) NULL);

CREATE INDEX "words_word_language_id_db78e29d" ON "words_word" ("language_id");

CREATE TABLE "words_word_category" ("id" integer NOT NULL PRIMARY KEY AUTOINCREMENT, "word_id" integer NOT NULL REFERENCES "words_word" ("id") DEFERRABLE INITIALLY DEFERRED, "category_id" integer NOT NULL REFERENCES "words_category" ("id") DEFERRABLE INITIALLY DEFERRED);

CREATE UNIQUE INDEX "words_word_category_word_id_category_id_45e3f970_uniq" ON "words_word_category" ("word_id", "category_id");
CREATE INDEX "words_word_category_word_id_db4bfb36" ON "words_word_category" ("word_id");
CREATE INDEX "words_word_category_category_id_223dd66b" ON "words_word_category" ("category_id");

CREATE TABLE "words_dictionaryentry" ("id" integer NOT NULL PRIMARY KEY AUTOINCREMENT, "transcription" varchar(30) NULL, "translation_id" integer NULL REFERENCES "words_word" ("id") DEFERRABLE INITIALLY DEFERRED, "word_id" integer NULL REFERENCES "words_word" ("id") DEFERRABLE INITIALLY DEFERRED);

CREATE INDEX "words_dictionaryentry_translation_id_ffce8365" ON "words_dictionaryentry" ("translation_id");
CREATE INDEX "words_dictionaryentry_word_id_040b0326" ON "words_dictionaryentry" ("word_id");

CREATE TABLE "words_dictionaryentry_dictionary" ("id" integer NOT NULL PRIMARY KEY AUTOINCREMENT, "dictionaryentry_id" integer NOT NULL REFERENCES "words_dictionaryentry" ("id") DEFERRABLE INITIALLY DEFERRED, "dictionary_id" integer NOT NULL REFERENCES "words_dictionary" ("id") DEFERRABLE INITIALLY DEFERRED);

CREATE UNIQUE INDEX "words_dictionaryentry_dictionary_dictionaryentry_id_dictionary_id_40be4077_uniq" ON "words_dictionaryentry_dictionary" ("dictionaryentry_id", "dictionary_id");
CREATE INDEX "words_dictionaryentry_dictionary_dictionaryentry_id_ac3a218f" ON "words_dictionaryentry_dictionary" ("dictionaryentry_id");
CREATE INDEX "words_dictionaryentry_dictionary_dictionary_id_a8bc9c21" ON "words_dictionaryentry_dictionary" ("dictionary_id");
