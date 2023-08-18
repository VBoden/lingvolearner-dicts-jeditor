from gtts import gTTS
import sys
import json
import os
from os import listdir, rename
from os.path import isfile, join, exists
import re

# out_paths = "out/words_mp3"
out_paths = "/Disk_A/Words_mp3"

use_splited = False


def get_word_file_name(word, language):
    word_file_name = word.lower().strip().replace('/', '_').replace('\\', '_')
    if word_file_name.endswith('.'):
        word_file_name = word_file_name[:-1]
    first_letter = word_file_name[:1]
    letters_mappings = {}
    letters_mappings['ó'] = 'o'
    letters_mappings['á'] = 'a'
    letters_mappings['í'] = 'i'
    letters_mappings['é'] = 'e'
    letters_mappings['ú'] = 'u'
    if first_letter in letters_mappings:
        first_letter = letters_mappings[first_letter]
    folder_name = join(out_paths, language, first_letter)
    resultFileName = join(folder_name, word_file_name + ".mp3")
    return folder_name, resultFileName


def write_word_to_file(f, word, language, slow_param=False):
    folder_name, resultFileName = get_word_file_name(word, language)
    if not exists(folder_name):
        print("Folder " + folder_name + " not exists. Creating...")
        os.makedirs(folder_name)
 #   print("resultFileName=" + resultFileName)
    if not exists(resultFileName):
        print("File " + resultFileName + " not exists. Creating...")
        with open(resultFileName, 'wb') as f:
            gTTS(word, lang=language, slow=slow_param).write_to_fp(f)


def split_words(word):
    if use_splited:    
        new_word = re.sub(r"[<>\[\]{}/\\()?!.,:;]+", " ", word)
        new_word = new_word.replace('  ', ' ')
        return new_word.split()
    else:
        return [word]


def append_to_file(f, values, value_value, lang):
    fold, resultFileName2 = get_word_file_name(value_value + ".", lang)
    with open(resultFileName2, 'rb') as f3:
        start = 256 if value_value != values[0] else 0
        if use_splited and value_value != values[-1]:
            f.write(f3.read()[start:-512])
        elif use_splited and value_value == values[-1]:
            f.write(f3.read()[start:])
        else:
            f.write(f3.read())


def convertToMP3(fileName):
    diction = {}
    with open(fileName, "r") as f:
        for line in f:
            if '%' in line:
                lines = line.split('%')
                for l in lines:
                    convert_line(diction, l)
            else:
                convert_line(diction, line)
    
    for key in diction:
        keys = split_words(key)
        values = split_words(diction[key])
        for key_value in keys:
            write_word_to_file(f, key_value, 'es', True)
        for value_value in values:
            write_word_to_file(f, value_value, 'uk', False)
    resyltFileName = fileName.replace(".vcb", ".mp3")
    with open(resyltFileName, 'wb') as f:
        for key in diction:
            keys = split_words(key)
            values = split_words(diction[key])
            for key_value in keys:
#                 fold, resultFileName1 = get_word_file_name(key, 'es')
                append_to_file(f, keys, key_value, 'es')
#                 fold, resultFileName1 = get_word_file_name(key_value, 'es')
#                 with open(resultFileName1, 'rb') as f2:
#                     f.write(f2.read())
            for value_value in values:
#                 fold, resultFileName2 = get_word_file_name(diction[key] + ".", 'uk')
                append_to_file(f, values, value_value, 'uk')
        
#             gTTS(diction[key]+".", lang='uk', slow=False).write_to_fp(f)


print('cmd entry:', sys.argv[1])


def to_entry(line, dict):
    partes = line.replace('\n', '').split('|')
    word = partes[0]
    translation = partes[2]
    dict[word] = translation


def convert_line(dict, line):
    line = line.replace('\n', '')
    if(len(line) == 0):
        return
    to_entry(line, dict)


from_path = sys.argv[1]
onlyfiles = [f for f in listdir(from_path) if isfile(join(from_path, f))]
print(onlyfiles)
for fileName in onlyfiles:
    if os.path.basename(fileName).endswith('.vcb'):
        print("fileName=" + join(from_path, fileName))
        convertToMP3(join(from_path, fileName))

