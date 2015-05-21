package net.invalidkeyword.scaladiagrams

import org.scalatest.FlatSpec

class NodeSelectorTests extends FlatSpec {

  "The NodeSelector" should
    "find a node if one exists if sent a String" in {
      val nodes = List(CLASS("myClass", List()), CLASS("anotherClass", List()))
      val ns = new NodeSelector(nodes)
      assert(ns.findNode("myClass") == (Some(CLASS("myClass", List()))))
    }

  it should "should find a node if one exists if being sent a WITH" in {
    val nodes = List(CLASS("myClass", List()), CLASS("anotherClass", List()))
    val ns = new NodeSelector(nodes)
    assert(ns.findNode(RELATED("myClass")) == (Some(CLASS("myClass", List()))))
  }

  it should "should not find node if it does not exist" in {
    val nodes = List(CLASS("myClass", List()), CLASS("anotherClass", List()))
    val ns = new NodeSelector(nodes)
    assert(ns.findNode(RELATED("abc")) == (None))
  }

  it should "should return a set of all child nodes for a node" in {
    val nodes = List(CLASS("myClass", List(RELATED("anotherClass"))), CLASS("anotherClass", List(RELATED("third"))), CLASS("unused", List()), CLASS("third", List()))
    val ns = new NodeSelector(nodes)
    val node = ns.findNode("myClass").get
    assert(ns.selectChildNodes(node) == (Set(CLASS("third", List()), CLASS("myClass", List(RELATED("anotherClass"))), CLASS("anotherClass", List(RELATED("third"))))))

  }

}