import time
import sys
import serial.tools.list_ports
from Adafruit_IO import MQTTClient

AIO_USERNAME = "bombeoo"
AIO_KEY = ""

AIO_FEED_BUTTON = ["feed-led", "feed-fan"]
AIO_FEED_TEMP = "feed-temp"
AIO_FEED_HUMI = "feed-humi"

dataSave = "!::#"
waiting_period = 0
sending_mess_again = False


def connected(client):
    print("Listening for all feed changes...")
    for FEED in AIO_FEED_BUTTON:
        client.subscribe(FEED)
    client.subscribe(AIO_FEED_TEMP)
    client.subscribe(AIO_FEED_HUMI)


def subscribe(client, userdata, mid, granted_qos):
    print("Subscribed successful!")


def disconnected(client):
    print("Disconnected from Adafruit IO!")
    sys.exit(1)


def message(client, feed_id, payload):
    data = dataSave.replace("!", "")
    data = dataSave.replace("#", "")
    splitData = data.split(":")

    if(splitData[1] == "TEMP"):
        splitData[1] = AIO_FEED_TEMP
    elif(splitData[1] == "HUMI"):
        splitData[1] = AIO_FEED_HUMI
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


client = MQTTClient(AIO_USERNAME, AIO_KEY)

client.on_connect = connected
client.on_disconnect = disconnected
client.on_message = message
client.on_subscribe = subscribe

client.connect()
client.loop_background()


def getPort():
    ports = serial.tools.list_ports.comports()
    N = len(ports)
    commPort = "None"
    for i in range(0, N):
        port = ports[i]
        strPort = str(port)
        if "ELTIMA Virtual Serial Port" in strPort:
            splitPort = strPort.split(" ")
            commPort = (splitPort[0])
            break
    return commPort


isComConnect = False
if getPort() != "None":
    ser = serial.Serial(port=getPort(), baudrate=115200)
    print("Connected with " + getPort())
    isComConnect = True


def processData(data):

    global waiting_period, sending_mess_again, dataSave

    waiting_period = 3
    sending_mess_again = False
    dataSave = str(data)

    data = data.replace("!", "")
    data = data.replace("#", "")
    splitData = data.split(":")
    print(splitData)

    if splitData[1] == "TEMP":
        client.publish(AIO_FEED_TEMP, splitData[2])
    elif splitData[1] == "HUMI":
        client.publish(AIO_FEED_HUMI, splitData[2])
    else:
        pass


def readSerial():
    bytesToRead = ser.inWaiting()
    if (bytesToRead > 0):
        mess = ser.read(bytesToRead).decode("UTF-8")
        while ("#" in mess) and ("!" in mess):
            start = mess.find("!")
            end = mess.find("#")
            processData(mess[start:end+1])
            if (end == len(mess)):
                mess = ""
            else:
                mess = mess[end+1:]


counter = 0
loop = 0

while True:
    if isComConnect:
        readSerial()
        # if waiting_period > 0:
        #     waiting_period -= 1
        #     if waiting_period == 0:
        #         sending_mess_again = True
        # if sending_mess_again:
        #     if(counter < 3):
        #         counter += 1
        #         print("Send again")
        #         processData(str(dataSave))
        #     elif(counter >= 3):
        #         print("to dang di ngu roi :(((")
        #         counter += 1
        #         if(counter == 13):
        #             loop += 1
        #             counter = 0
        #     if(loop == 3):
        #         loop = 0
        #         waiting_period = 0
        #         sending_mess_again = False
        #         print("Can't send data !!!")
    else:
        print("None serial port !!!")

    time.sleep(1)
