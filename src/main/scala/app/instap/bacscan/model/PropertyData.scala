package app.instap.bacscan.model

import org.code_house.bacnet4j.wrapper.api.{BypassBacnetConverter, Property}

import scala.util.Try

case class PropertyData(
    id: Option[Int],
    name: Option[String],
    tpe: Option[String],
    units: Option[String]) {

  val idOrEmpty = id.getOrElse("")
  val nameOrEmpty = name.getOrElse("")
  val tpeOrEmpty = tpe.getOrElse("")
  val unitsOrEmpty = units.getOrElse("")

  override def toString: String = {
    s"PropertyData(id=$id, name=$name, tpe=$tpe, units=$units)"
  }
}

object PropertyData {
  private val converter = new BypassBacnetConverter()
  def apply(property: Property): PropertyData = {
    PropertyData(
      id = Try(property.getId).toOption,
      name = Try(property.getName).toOption,
      tpe = Try(property.getType.toString).toOption,
      units = Try(property.getUnits).toOption
    )
  }
}
