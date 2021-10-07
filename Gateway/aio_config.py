from Adafruit_IO import MQTTClient
AIO_USERNAME = "iotg06"
AIO_KEY = "aio_pZLL43LGAxfNGAXoiLtYy6LCOd0X"

FEED_DOOR = "door"
FEED_FAN = "fan"
FEED_HUMIDITY = "humidity"
FEED_LIGHT = "light"
FEED_REFRESHER = "refresher"
FEED_TEMP = "temp"
FEED_WARNING = "warning"

client = MQTTClient(AIO_USERNAME, AIO_KEY)
