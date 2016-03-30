package controllers

import fr.familiar.interpreter.FMLBasicInterpreter
import fr.familiar.parser.ConfigurationVariableBDDImpl
import fr.familiar.variable.{FeatureModelVariable, ConfigurationVariable}

/**
 * Created by macher1 on 30/03/2016.
 */
object ConfigurationManager {


  var mapConfigurations : Map [(String, FMLBasicInterpreter), ConfigurationVariable] = Map()

  /**
   * Associate a configuration to a configuration identifier and an interpreter (for users' session)
   * @param i
   * @param cf
   */
  def storeConfiguration(i: FMLBasicInterpreter,cf: ConfigurationVariableBDDImpl) = {
    mapConfigurations += ((cf.getIdentifier, i) -> cf)
  }

  /**
   *
   * @param i the FML interpreter (we know it)
   * @param fmid the feature model variable identifier
   * @return a new configuration (side effect is that the configuration can be retrieved in this controller
   *         with the configuration id)
   */
  def mkConfiguration(i: FMLBasicInterpreter, fmid: String) : Option[ConfigurationVariable] = {
    val v = i.eval(fmid)
    if (v.isInstanceOf[FeatureModelVariable]) {
      val fmv = v.asInstanceOf[FeatureModelVariable]
      val cf = new ConfigurationVariableBDDImpl(fmv, "")
      storeConfiguration(i, cf)
      Some (cf)
    }
    else // feature model does not exist!
      None

  }


  /**
   * retrieve the configuration
   * @param i
   * @param cid
   * @return
   */
  def getConfiguration(i:  FMLBasicInterpreter, cid: String) : Option[ConfigurationVariable] = {
    mapConfigurations.get((cid, i))
  }


}
