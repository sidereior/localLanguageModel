import requests
from IPython.display import Audio
import tempfile
from time import sleep

uberduck_auth = ("", "")
voicemodel_uuid = "778e27be-877f-4b61-aefc-4eb2ff88ec11"
text = "War is cruelty, and you cannot refine it; and those who brought war into our country deserve all the curses and maledictions a people can pour out. I know I had no hand in making this war, and I know I will make more sacrifices today than any of you to secure peace."

audio_uuid = requests.post(
    "https://api.uberduck.ai/speak",
    json=dict(speech=text, voicemodel_uuid=voicemodel_uuid),
    auth=uberduck_auth,
).json()["uuid"]

audio_url = None  # Initialize audio_url variable

for t in range(10):
    sleep(1)  # Check status every second for 10 seconds.
    output = requests.get(
        "https://api.uberduck.ai/speak-status",
        params=dict(uuid=audio_uuid),
        auth=uberduck_auth,
    ).json()
    if "path" in output:
        audio_url = output["path"]
        break

if audio_url:
    tf = tempfile.NamedTemporaryFile(suffix=".wav")
    r = requests.get(audio_url, allow_redirects=True)
    with open(tf.name, "wb") as f:
        f.write(r.content)
    Audio(tf.name)
else:
    print("Audio URL not found or request unsuccessful.")
