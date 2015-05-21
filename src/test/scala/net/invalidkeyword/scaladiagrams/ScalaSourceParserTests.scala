package net.invalidkeyword.scaladiagrams

import org.scalatest.FlatSpec

class ScalaSourceParserTests extends FlatSpec {

  "The ScalaSourceParser" should
    "parse a class" in {
      val result = ScalaSourceParser.run("class bob")
      assert(result.successful == (true))
      assert(ScalaSourceParser.filter(result.get) == List(CLASS("bob", List())))
    }

  it should "should parse a trait" in {
    val result = ScalaSourceParser.run("trait Cat")
    assert(result.successful == true)
    assert(ScalaSourceParser.filter(result.get) == List(TRAIT("Cat", List())))
  }

  it should " fail to parse some other text" in {
    val result = ScalaSourceParser.run("bob is a cat")
    assert(ScalaSourceParser.filter(result.get) == List())
  }

  it should " parse a class with an with" in {
    val result = ScalaSourceParser.run("class bob with bill")
    assert(result.successful == true)
    assert(ScalaSourceParser.filter(result.get) == List(CLASS("bob", List(RELATED("bill")))))
  }

  it should " parse a class with an extends" in {
    val result = ScalaSourceParser.run("class bob extends bill")
    assert(result.successful == true)
    assert(ScalaSourceParser.filter(result.get) == List(CLASS("bob", List(RELATED("bill")))))
  }

  it should " parse a class with an extends and some withs" in {
    val result = ScalaSourceParser.run("class bob extends bill with peter with paul")
    assert(result.successful == true)
    assert(ScalaSourceParser.filter(result.get) == List(CLASS("bob", List(RELATED("bill"), RELATED("peter"), RELATED("paul")))))
  }

  it should " parse a class after some other text" in {
    val result = ScalaSourceParser.run("this is a test class bob")
    assert(result.successful == true)
    assert(ScalaSourceParser.filter(result.get) == List(CLASS("bob", List())))
  }

  it should " parse a class before some other text" in {
    val result = ScalaSourceParser.run("class bob and some more stuff")
    assert(result.successful == true)
    assert(ScalaSourceParser.filter(result.get) == List(CLASS("bob", List())))
  }

  it should " parse a class with some other text" in {
    val result = ScalaSourceParser.run("abc some class bob and some trait bill with peter more stuff")
    assert(result.successful == true)
    assert(ScalaSourceParser.filter(result.get) == List(CLASS("bob", List()), TRAIT("bill", List(RELATED("peter")))))
  }

  it should " parse a class with a self: " in {
    val result = ScalaSourceParser.run("class bob with peter { self: abc with xyz => ")
    assert(result.successful == true)
    assert(ScalaSourceParser.filter(result.get) == List(CLASS("bob", List(RELATED("peter", self = false), RELATED("abc", self = true), RELATED("xyz", self = true)))))
  }

  it should " parse a trait with a self: " in {
    val result = ScalaSourceParser.run("trait bob with peter { self: abc with xyz => ")
    assert(result.successful == true)
    assert(ScalaSourceParser.filter(result.get) == List(TRAIT("bob", List(RELATED("peter", self = false), RELATED("abc", self = true), RELATED("xyz", self = true)))))
  }

  it should " parse a case class with params" in {
    val result = ScalaSourceParser.run("case class bob (abc : String) extends bill")
    assert(result.successful == true)
    assert(ScalaSourceParser.filter(result.get) == List(CASE("bob", List(RELATED("bill")))))
  }

  it should " parse a package with a class" in {
    val result = ScalaSourceParser.run("package bill.peter\n class bob (abc : String) extends bill")
    assert(result.successful == true)
    assert(ScalaSourceParser.filter(result.get) == List(CLASS("bob", List(RELATED("bill")), "bill.peter")))
  }

  it should " a class should have a color" in {
    val cl = CLASS("abc", List())
    assert(cl.color == "darkorange")
  }

  //This test is based on the source code of ScalaSourceParser.scala so is likely to break when that file is changed...
  it should " parse a source file" in {
    val input = scala.io.Source.fromFile("src/main/scala/net/invalidkeyword/scaladiagrams/ScalaSourceParser.scala").mkString
    val result = ScalaSourceParser.run(input)
    assert(result.successful == true)
    assert(ScalaSourceParser.filter(result.get).head ==
      OBJECT("ScalaSourceParser", List(RELATED("RegexParsers"), RELATED("RunParser")), "net.invalidkeyword.scaladiagrams"))
  }

  it should " output an type in DOT format" in {
    val cl = CLASS("abc", List(RELATED("def")))
    assert(cl.toString() == "\"abc\" [style=filled, fillcolor=darkorange]\n  \"abc\" -> \"def\";\n")
  }

  it should " output a type in DOT format with a dashed line for self-types" in {
    val cl = CLASS("abc", List(RELATED("def", true)))
    assert(cl.toString() == "\"abc\" [style=filled, fillcolor=darkorange]\n  \"abc\" -> \"def\" [style=dashed];\n")
  }

}