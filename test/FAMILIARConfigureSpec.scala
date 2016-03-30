import com.ning.http.client.RequestBuilder
import controllers.FamiliarIDEController
import org.junit.runner.RunWith
import org.specs2.control.Debug
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.libs.json.Json
import play.api.mvc.Session
import play.api.test.Helpers._
import play.api.test.{Helpers, FakeRequest, WithApplication}


/**
 * Created by macher1 on 26/03/2016.
 */
@RunWith(classOf[JUnitRunner])
class FAMILIARConfigureSpec extends Specification with Debug {

  "Configurator" should {

    "returns a JSON" in new WithApplication {

      val s = "bar" -> "foo"
      val r = route(FakeRequest(GET, "/ide/familiar").withSession(s))
      status(r.get) must equalTo(OK)


      val data = "s" -> "fm1 = FM (A : [B] [C]; )\n"


      val execfm = route(FakeRequest(POST, "/interpret").withSession(s) // ?s="FM (A : [B] [C]; )"
        .withJsonBody(Json.toJson("fm1 = FM (A : [B] [C]; )\nfm2 = FM (A : [B] [C] E; E : (F|G); C : (X|Y)+; )")))

      println ("EXEC!" + contentAsString(execfm.get))
      status(execfm.get) must equalTo(OK)

      val conf = route(FakeRequest(GET, "/configure/fm1").withSession(s)).get
      status(conf) must equalTo(OK)
      println("CONF: " + contentAsString(conf))

      val conf2 = route(FakeRequest(GET, "/configure/fm2").withSession(s)).get
      status(conf2) must equalTo(OK)
      println("CONF2: " + contentAsString(conf2))

    }

    "send a bad configuration request (variable does not exist)" in new WithApplication {

      val conf = route(FakeRequest(GET, "/configure/fm3")) must beNone

    }
  }
}
