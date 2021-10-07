import time
from processData import *
from read_serial import *
from mqttCallback import *

dataSave = "!::#"
waiting_period = 0
sending_mess_again = False

client.on_connect = connected
client.on_disconnect = disconnected
client.on_message = message
client.on_subscribe = subscribe

client.connect()
client.loop_background()

while True:
    if isComConnect:
        if g.lastSentOK and intervalTimer == 0:
            g.lastPayload = random.randrange(20, 35)
            g.lastSentOK = False
            ackTimer = g.TIMEOUT_MS
            intervalTimer = g.INTERVAL_TIME_MS
            send(client, AIO_FEED_ID, g.lastPayload)

        if not g.lastSentOK and ackTimer == 0 and not goToSleep:
            ackTimer = g.TIMEOUT_MS
            send(client, AIO_FEED_ID, g.lastPayload, True)
            g.numOfAttempts += 1
            print("Attempt: " + str(g.numOfAttempts))

        if g.numOfAttempts >= g.MAX_NUM_OF_ATTEMPTS:
            print(str(g.numOfAttempts) + " attempts failed. Stop sending for 60s then try again.")
            longSleepTimer = g.LONG_SLEEP_MS
            g.numOfAttempts = 0
            goToSleep = True

        if longSleepTimer == 0:
            goToSleep = False

        if ackTimer > 0:
            ackTimer -= 1
        if intervalTimer > 0:
            intervalTimer -= 1
        if longSleepTimer > 0:
            longSleepTimer -= 1
        time.sleep(0.001)
    else:
        print("None serial port !!!")

    time.sleep(1)
