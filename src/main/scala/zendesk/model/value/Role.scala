package zendesk.model.value

import io.circe.{Decoder, Encoder}


sealed case class Role(name: String)

object Admin extends Role("admin")
object EndUser extends Role("end-user")
object Agent extends Role("agent")

object Role {
  val all = Admin :: EndUser :: Agent :: Nil

  implicit val encoderEvent: Encoder[Role] = {
    Encoder[String].contramap( _.name )
  }

  implicit val decoderEvent: Decoder[Role] = {
    Decoder[String].map { name  =>
      all.find( _.name == name ).getOrElse( Role( name ) )
    }
  }
}