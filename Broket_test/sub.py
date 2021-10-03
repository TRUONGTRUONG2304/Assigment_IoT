import paho.mqtt.client as mqtt
import time


def on_message(client, userdata, message):
    print("Received message: ", str(message.payload.decode("utf-8")))


mqttBroker = "mqtt.eclipseprojects.io"
client = mqtt.Client("NQTT")
client.connect(mqttBroker)

client.loop_start()
client.subscribe("TEMPa")
client.on_message = on_message
# time.sleep(30)
while True:
    pass
# client.loop_stop()
