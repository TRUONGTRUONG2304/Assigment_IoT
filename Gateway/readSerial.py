import serial.tools.list_ports
import globals as g
from publishData import *


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


if getPort() != "None":
    ser = serial.Serial(port=getPort(), baudrate=115200)
    print("Connected with " + getPort())
    g.isComConnect = True


def readSerial():
    bytesToRead = ser.inWaiting()
    if (bytesToRead > 0):
        mess = ser.read(bytesToRead).decode("UTF-8")
        while ("#" in mess) and ("!" in mess):
            start = mess.find("!")
            end = mess.find("#")
            publishData(mess[start:end+1])
            if (end == len(mess)):
                mess = ""
            else:
                mess = mess[end+1:]
