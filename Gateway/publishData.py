from aio_config import *


def publishData(data):
    data = data.replace("!", "")
    data = data.replace("#", "")
    splitData = data.split(":")

    if splitData[1] == "TEMP":
        client.publish(FEED_TEMP, splitData[2])
    elif splitData[1] == "HUMI":
        client.publish(FEED_HUMIDITY, splitData[2])
    else:
        pass
