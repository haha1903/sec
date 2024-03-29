import scala.slick.driver.JdbcDriver.backend.Database
import Database.dynamicSession
import scala.slick.jdbc.{GetResult, StaticQuery => Q}
import Q.interpolation

object A extends App {
  case class Supplier(id: Int, name: String, street: String, city: String, state: String, zip: String)
  case class Coffee(name: String, supID: Int, price: Double, sales: Int, total: Int)

  Database.forURL("jdbc:h2:mem:test1", driver = "org.h2.Driver") withDynSession {
    // Create the tables, including primary and foreign keys
    Q.updateNA("create table suppliers(" +
      "id int not null primary key, " +
      "name varchar not null, " +
      "street varchar not null, " +
      "city varchar not null, " +
      "state varchar not null, " +
      "zip varchar not null)").execute
    Q.updateNA("create table coffees(" +
      "name varchar not null, " +
      "sup_id int not null, " +
      "price double not null, " +
      "sales int not null, " +
      "total int not null, " +
      "foreign key(sup_id) references suppliers(id))").execute
    // Insert some suppliers
    (Q.u + "insert into suppliers values(101, 'Acme, Inc.', '99 Market Street', 'Groundsville', 'CA', '95199')").execute
    (Q.u + "insert into suppliers values(49, 'Superior Coffee', '1 Party Place', 'Mendocino', 'CA', '95460')").execute
    (Q.u + "insert into suppliers values(150, 'The High Ground', '100 Coffee Lane', 'Meadows', 'CA', '93966')").execute

    def insert(c: Coffee) = (Q.u + "insert into coffees values (" +? c.name +
      "," +? c.supID + "," +? c.price + "," +? c.sales + "," +? c.total + ")").execute

    // Insert some coffees
    Seq(
      Coffee("Colombian", 101, 7.99, 0, 0),
      Coffee("French_Roast", 49, 8.99, 0, 0),
      Coffee("Espresso", 150, 9.99, 0, 0),
      Coffee("Colombian_Decaf", 101, 8.99, 0, 0),
      Coffee("French_Roast_Decaf", 49, 9.99, 0, 0)
    ).foreach(insert)

    // Result set getters
    implicit val getSupplierResult = GetResult(r =>
      Supplier(r.nextInt, r.nextString, r.nextString,
        r.nextString, r.nextString, r.nextString))
    implicit val getCoffeeResult = GetResult(r => Coffee(r.<<, r.<<, r.<<, r.<<, r.<<))

    Q.queryNA[Coffee]("select * from coffees") foreach { c =>
      println("  " + c.name + "\t" + c.supID + "\t" + c.price + "\t" + c.sales + "\t" + c.total)
    }

    def coffeeByName(name: String) = sql"select * from coffees where name = $name".as[Coffee]
    println("Coffee Colombian: " + coffeeByName("Colombian").firstOption)

    def deleteCoffee(name: String) = sqlu"delete from coffees where name = $name".first
    val rows = deleteCoffee("Colombian")
    println(s"Deleted $rows rows")
  }
}
