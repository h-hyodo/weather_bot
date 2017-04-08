package example

import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._
import skinny.http._

object Weatherbot extends Greeting with App {

  implicit val formats = DefaultFormats
  case class PinpointLocations(link: String, name: String)
  case class Dscription(text: String, publicTime: String)
  case class Wather(publicTime: String, title: String, description: Dscription, pinpointLocations : List[PinpointLocations])

  var res = HTTP.get("http://weather.livedoor.com/forecast/webservice/json/v1", "city" -> 130010)
  var json = parse(res.asString)
  val watcher = json.extract[Wather]

  val sendJson : String = compact(render(("text" -> ("渋江だよ" + "\n\n" + watcher.title + "\n\n" + watcher.description.text))))
  //val sendJson : String = compact(render(("text" -> ("https://suzumi.github.io/all-archives/"))))

  val req = Request("https://hooks.slack.com/services/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").body(sendJson.getBytes, "application/json")
  HTTP.post(req)

}

trait Greeting {
  lazy val greeting: String = "hello"
}
