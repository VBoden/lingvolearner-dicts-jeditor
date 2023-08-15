from gtts import gTTS
import sys
import json
from os import listdir, rename
from os.path import isfile, join


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
    
    resyltFileName = fileName.replace(".vcb", ".mp3")
    with open(resyltFileName, 'wb') as f:
        for key in diction:
            gTTS(key, lang='es', slow=True).write_to_fp(f)
            gTTS(diction[key]+".", lang='uk', slow=False).write_to_fp(f)

print('cmd entry:', sys.argv[1])



def to_entry(line, dict):
    partes = line.replace('\n', '').split('|')
    word = partes[0]
    translation = partes[2]
    dict[word]=translation


def convert_line(dict, line):
    line = line.replace('\n', '')
    if(len(line)==0):
        return
    to_entry(line, dict)


from_path = sys.argv[1]
onlyfiles = [f for f in listdir(from_path) if isfile(join(from_path, f))]
print(onlyfiles)
for fileName in onlyfiles:
    print("fileName="+join(from_path, fileName))
    convertToMP3(join(from_path, fileName))


