package models.ide.familiar

import fr.familiar.interpreter.FMLBasicInterpreter

trait FamiliarIDEFactory {
	def createInterpreter() : FMLBasicInterpreter
	def createSession() : Session
}