package app.instap.bacscan

import app.instap.bacscan.model.{DeviceData, PropertyData}
import org.code_house.bacnet4j.wrapper.ip.BacNetIpClient

import scala.jdk.CollectionConverters._
import org.code_house.bacnet4j.wrapper.api.{BypassBacnetConverter, Device, Property}

import scala.util.Try

final case class Client(
    ip: String,
    broadcast: String,
    deviceId: Int,
    timeout: Int,
    empty: String = "n/a"
  ) {
  private val converter = new BypassBacnetConverter()
  private val client = new BacNetIpClient(ip, broadcast, deviceId)

  println(s"Starting the Client - $this")

  def start(): Unit = {
    client.start()
  }

  def stop(): Unit = {
    client.stop()
  }

  def stream: LazyList[(DeviceData, PropertyData, Any)] = {
    for {
      device: Device <- Try(client.discoverDevices(timeout).asScala).toOption.toList.flatten.to(LazyList)
      property: Property <- Try(client.getDeviceProperties(device).asScala).toOption.toList.flatten.to(LazyList)
    } yield {
      val dd = DeviceData(device)
      val pd = PropertyData(property)
      val value = Try(client.getPropertyValue(property, converter)).toOption.getOrElse(empty)
      (dd, pd, value)
    }
  }

//  client.start()
//
//  client.discoverDevices(timeout).asScala foreach { device =>
//    println(s"Discovered device: $device")
//    try
//      client.getDeviceProperties(device).asScala foreach { property =>
//        val name = Try(property.getName()).toOption.getOrElse("n/a")
//        val value = Try(client.getPropertyValue(property, converter)).toOption.getOrElse("n/a")
//        println(name + ": " + value);
//        println(s" - Property '$name': '$value'")
//      }
//    catch {
//      case e: Exception =>
//        println(s"Error while getting properties for device $device: $e")
//    }
//  }
//
//  client.stop();
}
