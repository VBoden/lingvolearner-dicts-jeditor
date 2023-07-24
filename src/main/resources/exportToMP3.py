from gtts import gTTS
import sys
import json

print('cmd entry:', sys.argv[1])

s=sys.argv[1].replace("\\","").replace("_"," ")[1:-1]
print(s)
d=json.loads(s)

with open(sys.argv[2], 'wb') as f:
    for key in d:
	    gTTS(key, lang='es', slow=True).write_to_fp(f)
	    gTTS(d[key], lang='uk', slow=True).write_to_fp(f)
