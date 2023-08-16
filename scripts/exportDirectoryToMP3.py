from gtts import gTTS
import sys
import json
import os
from os import listdir, rename
from os.path import isfile, join, exists

out_paths = "out/words_mp3"


def write_word_to_file(f, word, language):
    word_file_name = word.strip().replace('/', '_').replace('\\', '_')
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
    if not exists(folder_name):
        print("Folder " + folder_name + " not exists. Creating...")
        os.makedirs(folder_name)
    resultFileName = join(folder_name, word_file_name + ".mp3")
    print("resultFileName=" + resultFileName)
    if not exists(resultFileName):
        print("File " + resultFileName + " not exists. Creating...")
        with open(resultFileName, 'wb') as f:
            gTTS(word, lang=language, slow=True).write_to_fp(f)


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
    
#     resyltFileName = fileName.replace(".vcb", ".mp3")
#     with open(resyltFileName, 'wb') as f:
    for key in diction:
        write_word_to_file(f, key, 'es')
        write_word_to_file(f, diction[key] + ".", 'uk')
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
    print("fileName=" + join(from_path, fileName))
    convertToMP3(join(from_path, fileName))

