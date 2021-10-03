import paho.mqtt.client as mqtt
from random import randrange
import time

mqttBroker = "mqtt.eclipseprojects.io"
client = mqtt.Client("NQT")
client.connect(mqttBroker)

while True:
    randNumber = randrange(10)
    client.publish("TEMPa", randNumber)
    print("Just published " + str(randNumber) + " to Topic TEMP")
    time.sleep(3)
