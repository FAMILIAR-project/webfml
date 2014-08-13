package models.ide.familiar

import fr.familiar.interpreter.FMLBasicInterpreter

class ConcreteFamiliarIDEFactory extends FamiliarIDEFactory{
	 
	def createInterpreter() : FMLBasicInterpreter = {
	   println("I create a new Interpreter")
	   var myInterpreter = new FMLBasicInterpreter()
	   return myInterpreter
	 }
	 
	 def createSession() : Session={
	   println("I'm creating a new session")
	   var mySession = new Session()
	   return mySession
	 }
}