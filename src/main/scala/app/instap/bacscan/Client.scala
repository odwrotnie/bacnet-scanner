package app.instap.bacscan

case class Client(
    ip: String,
    broadcast: String,
    deviceId: String,
  ) {
  val client = new BacNetIpClient(ip, broadcast, deviceId)
  client.start()
  client.discoverDevices(5000) foreach { device =>
    println(device)

    client.getDeviceProperties(device) foreach { property =>
      println(property.getName() + " " + client.getPropertyValue(property));
    }
  }

  client.stop();
}
