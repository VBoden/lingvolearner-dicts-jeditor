from gtts import gTTS
import sys
import json


print('cmd entry:', sys.argv[1])



def to_entry(line, dict):
    partes = line.replace('\n', '').split('|')
    word = partes[0]
    translation = partes[2]
    print(word+":"+translation)
    dict[word]=translation
#     if(' (' in partes[2]):
#         trans = partes[2].split(' (')
#         translation = trans[0]
#         notes = trans[1][:-1]
#     else:
#         translation = partes[2]
#         notes = None


def convert_line(dict, line):
    line = line.replace('\n', '')
    if(len(line)==0):
        return
    to_entry(line, dict)

diction={}
with open(sys.argv[1], "r") as f:
    for line in f:
        print(line)
        if '%' in line:
            lines = line.split('%')
            for l in lines:
                convert_line(diction, l)
        else:
            convert_line(diction, line)

print(diction)
resyltFileName = sys.argv[1].replace(".vcb",".mp3")
# s=sys.argv[1].replace("\\","").replace("_"," ")[1:-1]
# print(s)
# d=json.loads(s)

with open(resyltFileName, 'wb') as f:
    for key in diction:
	    gTTS(key, lang='es', slow=True).write_to_fp(f)
	    gTTS(diction[key], lang='uk', slow=False).write_to_fp(f)


