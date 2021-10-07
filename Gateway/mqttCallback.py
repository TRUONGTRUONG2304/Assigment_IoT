import sys
from aio_config import *

def connected(client):
    print("Listening for all feed changes...")
    for FEED in AIO_FEED_BUTTON:
        client.subscribe(FEED)
    for FEED_DATA in AIO_FEED_DATA:
        client.subscribe(FEED_DATA)


def subscribe(client, userdata, mid, granted_qos):
    print("Subscribed successful!")


def disconnected(client):
    print("Disconnected from Adafruit IO!")
    sys.exit(1)


def message(client, feed_id, payload):

    if(splitData[1] == "TEMP"):
        splitData[1] = AIO_FEED_DATA[0]
    elif(splitData[1] == "HUMI"):
        splitData[1] = AIO_FEED_DATA[1]
    else:
        pass

    if feed_id == splitData[1] and str(payload) == str(splitData[2]):
        global waiting_period, sending_mess_again
        waiting_period = 0
        sending_mess_again = False
        print(feed_id + " received new value: " + payload)
        ser.write((str(payload) + "#").encode())

    if feed_id == "feed-led" or feed_id == "feed-fan":
        print(feed_id + " received new value: " + payload)
        ser.write((str(payload) + "#").encode())
def send(client, topic, payload, resend = False):
    print("-------------------")
    client.publish(topic, payload)
    if resend == True:
        print("Re-sent: " + str(payload))
    else:
        print("Sent: " + str(payload))
    pass