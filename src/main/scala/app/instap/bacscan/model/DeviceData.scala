package app.instap.bacscan.model

import com.serotonin.bacnet4j.`type`.primitive.OctetString
import com.serotonin.bacnet4j.util.sero.ArrayUtils
import org.code_house.bacnet4j.wrapper.api.Device

import scala.util.Try

case class DeviceData(name: Option[String],
                      network: Option[String],
                      id: Option[String],
                      address: Option[String],
                      model: Option[String],
                      vendor: Option[String],
                      macAddress: Option[String])

object DeviceData {
  def apply(device: Device): DeviceData = {

    val deviceAddress = Try {
      val a = device.getAddress.splitAt(4)
      val ip = a._1.map(_ & 0xff).mkString(".")
      val port = (a._2(0) << 8 | a._2(1) & 0xff).toString
      s"$ip:$port"
    }.toOption

    val deviceName = Try(device.getName).toOption
    val deviceNetwork = Try(device.getNetworkNumber.toString).toOption
    val deviceId = Try(device.getInstanceNumber.toString).toOption
    val deviceModel = Try(device.getModelName).toOption
    val deviceVendor = Try(device.getVendorName).toOption
    val macAddress = Try(device.getBacNet4jAddress.getMacAddress.toString).toOption

    DeviceData(
      name = deviceName,
      network = deviceNetwork,
      id = deviceId,
      address = deviceAddress,
      model = deviceModel,
      vendor = deviceVendor,
      macAddress = macAddress)
  }
}
