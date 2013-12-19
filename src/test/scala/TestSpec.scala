import com.datayes.pms.sec._
import com.datayes.pms.sec.model._
import org.apache.commons.dbcp.BasicDataSource
import org.specs2.mutable._

object TestSpec extends Specification {
  implicit val ds = new BasicDataSource
  ds.setDriverClassName("com.mysql.jdbc.Driver")
  ds.setUrl("jdbc:mysql://localhost/security")
  ds.setUsername("root")
  ds.setPassword("")
  implicit val ls = BasicLookupStrategy
  "AclService" should {
    "read acl" in {
      val acls = AclService.readAclsById(ObjectIdentity(1, "com.datayes.paas.Foo") :: Nil, Sid("haha") :: Nil)
      println(acls)
      acls must be size (1)
    }
  }
}
