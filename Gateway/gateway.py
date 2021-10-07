import time
from readSerial import *
from mqttCallback import *
from aio_config import *
import globals as g


def main():
    client.on_connect = connected
    client.on_disconnect = disconnected
    client.on_message = message
    client.on_subscribe = subscribe
    client.connect()
    client.loop_background()

    while True:
        if g.isComConnect:
            readSerial()
        else:
            print("None serial port !!!")

        time.sleep(1)


if __name__ == "__main__":
    main()
