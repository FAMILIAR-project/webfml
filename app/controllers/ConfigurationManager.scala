package controllers

import fr.familiar.interpreter.FMLBasicInterpreter
import fr.familiar.parser.ConfigurationVariableBDDImpl
import fr.familiar.variable.{FeatureModelVariable, ConfigurationVariable}
import play.Logger

/**
 * Created by macher1 on 30/03/2016.
 */
object ConfigurationManager {


  var mapConfigurations : Map [(String, FMLBasicInterpreter), ConfigurationVariable] = Map()


  var conf = 0

  /**
   * Associate a configuration to a configuration identifier and an interpreter (for users' session)
   * @param i
   * @param cf
   */
  def storeConfiguration(i: FMLBasicInterpreter, cf: ConfigurationVariableBDDImpl, cid: String) = {
    mapConfigurations += ((cid, i) -> cf)
  }

  def mkConfIdentifier() = {
     conf = conf + 1
     "conf" + conf
  }


  /**
   *
   * @param i the FML interpreter (we know it)
   * @param fmid the feature model variable identifier
   * @return a new configuration (side effect is that the configuration can be retrieved in this controller
   *         with the configuration id)
   */
  def mkConfiguration(i: FMLBasicInterpreter, fmid: String) : Option[(ConfigurationVariable, String)] = {
    val v = i.eval(fmid)
    if (v.isInstanceOf[FeatureModelVariable]) {
      val fmv = v.asInstanceOf[FeatureModelVariable]
      // TODO calling getIdentifier multiple times is dangerous
      // when the variable identifier is not fixed (due to "res" increment: very bad implementation, see bug)
      // it explains why we are pro-active in the generation of identifier
      val cid =  mkConfIdentifier()
      val cf = new ConfigurationVariableBDDImpl(fmv, cid)
      storeConfiguration(i, cf, cid)
      Some (cf, cid)
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
