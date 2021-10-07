import sys
from aio_config import *


def connected(client):
    print("Listening for all feed changes...")
    client.subscribe(FEED_DOOR)
    client.subscribe(FEED_FAN)
    client.subscribe(FEED_HUMIDITY)
    client.subscribe(FEED_LIGHT)
    client.subscribe(FEED_REFRESHER)
    client.subscribe(FEED_TEMP)
    client.subscribe(FEED_WARNING)


def subscribe(client, userdata, mid, granted_qos):
    print("Subscribed successful!")


def disconnected(client):
    print("Disconnected from Adafruit IO!")
    sys.exit(1)


def message(client, feed_id, payload):
    if feed_id == FEED_REFRESHER or feed_id == FEED_LIGHT or feed_id == FEED_FAN:
        print(feed_id + " received new request: " + payload)

    if feed_id == FEED_TEMP or feed_id == FEED_HUMIDITY:
        print(feed_id + " send ACK: " + payload)
