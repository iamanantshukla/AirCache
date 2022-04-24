#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <aREST.h>
#include <Servo.h>

WiFiClient espClient;
PubSubClient client(espClient);
Servo myservo;

aREST arestVar = aREST(client);

/*Enter an Unique ID to identify the device for arest.io
  A Maximum of 6 characters are supported. Must be unique.*/

char* device_id = "dev334"; // Do not use this device_id. Create your own id.

/* Enter SSID and Password of your
  WiFi Network*/
const char* ssid = "johnwick"; // Name of WiFi Network.
const char* password = "12345678"; // Password of your WiFi Network.
int stateCurr = 0;
int pos = 0;
void callback(char* topic, byte* payload, unsigned int length);

void setup(void)
{
  myservo.attach(4);
  Serial.begin(115200);
  pinMode(2, OUTPUT);

  client.setCallback(callback);

  // Give name and ID to device
  arestVar.set_id(device_id);
  arestVar.set_name("MyESP");

  // Connect to WiFi
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED)
  {
    delay(500);
    Serial.print("*");
  }
  Serial.println("");
  Serial.println("WiFi connected");

  String out_topic = arestVar.get_topic();
}

void loop()
{
    int stateNew = digitalRead(2);
    if(stateNew!=stateCurr){
      if(stateNew)
      for(int i=0;i<=180;i+=1)
      myservo.write(i);
      else
      for(int i=180;i>=0;i-=1)
      myservo.write(i);
    }
    stateCurr=stateNew;
  arestVar.loop(client);
}

void callback(char* topic, byte* payload, unsigned int length)
{
  arestVar.handle_callback(client, topic, payload, length);
}
