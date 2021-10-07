from Adafruit_IO import MQTTClient
from aio_config import *
import globals as g

client = MQTTClient(AIO_USERNAME, AIO_KEY)

def processData(data):

    global waiting_period, sending_mess_again, dataSave

    waiting_period = 3
    sending_mess_again = False
    dataSave = str(data)

    data = data.replace("!", "")
    data = data.replace("#", "")
    splitData = data.split(":")
    g.dataSave[0] = splitData[2]
    print(splitData)

    if splitData[1] == "TEMP":
        client.publish(AIO_FEED_DATA[0], splitData[2])
    elif splitData[1] == "HUMI":
        client.publish(AIO_FEED_DATA[1], splitData[2])
    else:
        pass