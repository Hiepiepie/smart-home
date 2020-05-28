namespace java ThriftAPI

service DataSender{
  void ping(),
  string getSensorData(1: string sensorType)
}
