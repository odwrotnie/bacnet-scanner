package app.instap.bacscan

import org.code_house.bacnet4j.wrapper.ip.BacNetIpClient
import scala.jdk.CollectionConverters._
import org.code_house.bacnet4j.wrapper.api.BypassBacnetConverter

final case class Client(
    ip: String,
    broadcast: String,
    deviceId: Int,
    timeout: Int,
  ) {
  private val converter = new BypassBacnetConverter()
  private val client = new BacNetIpClient(ip, broadcast, deviceId)
  client.start()
  client.discoverDevices(timeout).asScala foreach { device =>
    println(device)
    client.getDeviceProperties(device).asScala foreach { property =>
      println(property.getName() + " " + client.getPropertyValue(property, converter));
    }
  }

  client.stop();
}
