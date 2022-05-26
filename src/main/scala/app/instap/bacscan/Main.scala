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

  def main(args: Array[String]): Unit =
    if (args.length == 0) println(usage)
    else {
      val options = nextArg(Map(), args.toList)
      println(options)
    }

  private def nextArg(map: Map[String, Any], list: List[String]): Map[String, Any] =
    list match {
      case Nil => map
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
