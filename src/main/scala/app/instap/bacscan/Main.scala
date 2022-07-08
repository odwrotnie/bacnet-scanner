package app.instap.bacscan

object Main {
  val ARG_IP_ADDRESS = "ip"
  val ARG_BROADCAST = "broadcast"
  val ARG_DEVICE_ID = "device"
  val ARG_TIMEOUT = "timeout"

  private val usage =
    s"Usage: java -jar bacscan.jar" +
      s" --$ARG_IP_ADDRESS ip" +
      s" --$ARG_BROADCAST broadcast" +
      s" --$ARG_DEVICE_ID device" +
      s" --$ARG_TIMEOUT timeout"

  def main(args: Array[String]): Unit = {

    if (args.length == 0) println(usage)
    else {
      val options = nextArg(Map(), args.toList)
      options foreach {
        case (k, v) => println(s" - $k: $v")
      }

      val ip = options.get(ARG_IP_ADDRESS).get
      val broadcast = options.get(ARG_BROADCAST).get
      val deviceId = options.get(ARG_DEVICE_ID).get.toInt
      val timeout = options.get(ARG_TIMEOUT).get.toInt

      val csv = new CSV("bacscan.csv",
        "property-name", "property-id", "property-type", "property-units",
        "device-id", "device-name", "device-network", "device-address", "device-vendor", "device-model",
        "value")
      val c = Client.apply(ip = ip, broadcast = broadcast, deviceId = deviceId, timeout = timeout)
      c.start()
      c.stream.foreach {
        case (dd, pd, value) =>
          println(s"Device data: $dd, property data: $pd, value: $value")
          csv.write(Map(
            "device-id" -> dd.id.getOrElse(""),
            "device-name" -> dd.name.getOrElse(""),
            "device-network" -> dd.network.getOrElse(""),
            "device-address" -> dd.address.getOrElse(""),
            "device-vendor" -> dd.vendor.getOrElse(""),
            "device-model" -> dd.model.getOrElse(""),
            "property-id" -> pd.id.getOrElse(""),
            "property-name" -> pd.name.getOrElse(""),
            "property-type" -> pd.tpe.getOrElse(""),
            "property-units" -> pd.units.getOrElse(""),
            "value" -> value
          ))
      }
      c.stop()
      csv.close()
    }
  }

  private def nextArg(map: Map[String, Any], list: List[String]): Map[String, String] =
    list match {
      case Nil => map.view.mapValues(_.toString).toMap
      case s"--$ARG_IP_ADDRESS" :: value :: tail =>
        nextArg(map ++ Map(ARG_IP_ADDRESS -> value), tail)
      case s"--$ARG_BROADCAST" :: value :: tail =>
        nextArg(map ++ Map(ARG_BROADCAST -> value), tail)
      case s"--$ARG_DEVICE_ID" :: value :: tail =>
        nextArg(map ++ Map(ARG_DEVICE_ID -> value.toInt), tail)
      case s"--$ARG_TIMEOUT" :: value :: tail =>
        nextArg(map ++ Map(ARG_TIMEOUT -> value.toInt), tail)
      case string :: Nil =>
        nextArg(map ++ Map("filename" -> string), list.tail)
      case unknown :: _ =>
        println("Unknown option " + unknown)
        throw new IllegalArgumentException
    }
}
