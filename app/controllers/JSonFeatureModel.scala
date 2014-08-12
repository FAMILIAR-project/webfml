package controllers

import play.api.libs.json.Json
import play.api.libs.json.JsString
import scala.collection.immutable.HashMap
import scala.collection.immutable.HashSet
import gsd.synthesis.FeatureNode
import fr.familiar.variable.FeatureModelVariable
import scala.collection.JavaConversions._
import fr.familiar.fm.FeatureNodeUtils
import fr.familiar.fm.converter.FeatureModelUtil
import gsd.synthesis.FeatureEdge

class JSonFeatureModel (fmv: FeatureModelVariable) {
  
  
  def toJSon() = {
    
        
    
    
    Json.toJson(JsString(""))
    
       
  }
  
  def _toJSon() = {
    
    
  }
  
 

	val ftToId = Map[String, Integer]()



	/**
	 * increment for id generator
	 */
	var id = 0

	/**
	 * the feature model to process internally (a clone/copy)
	 */
	val fm = fmv.getFm()
	
	val fg = fmv.getFm().getDiagram()


	/*
	def transformFMLConstraintstoSPLOT() = {
		val cnfs = Set[String];

		val constraints = _fm.getConstraints();
		// convert each expression to CNF expression
		for (expression <- constraints) {
			// _LOGGER.debug("expression=" +
			// expression);
			val cnf = FMLExpressionUtil.toCNF(expression);

			val cnfsSplitted = ExpressionUtil
					.splitConjunction(cnf);
			for (cnfSplitted <- cnfsSplitted) {
				cnfs.add(cnfToString(cnfSplitted));
			}

		}

		return cnfs;
	}*/

	/*
	def cnfToString(cnf: Expression[String]) {

		val type = cnf.getType();
		switch (type) {
		case FEATURE:
			return featureID(cnf.getFeature());
		case TRUE:
			return featureID(_fmv.root().name());
		case FALSE:
			return "~" + featureID(_fmv.root().name());
		case NOT:
			return "~" + cnfToString(cnf.getLeft());
		default:
			StringBuffer sb = new StringBuffer();
			sb.append(cnfToString(cnf.getLeft()));
			sb.append(" " + typeValue(type) + " ");
			sb.append(cnfToString(cnf.getRight()));
			return sb.toString();
		}

	}*/

	/**
	 * supposed to be in conjonctive normal form
	 * 
	 * @param type
	 * @return
	 */
	/*
	def typeValue(t: ExpressionType) = {
		assert (t == ExpressionType.AND || t == ExpressionType.OR);
		
		t == ExpressionType.AND ? "and"
				: t == ExpressionType.OR ? "or" : t.toString();
	}*/

	/**
	 * Inspired from FeatureModelSerializer
	 * 
	 * @return a FML, string-based representation of a feature model
	 */
	
	/*
	def transformFMLtoSPLOT() = {

		// transform Mutex-groups in the feature model
		normalizeMutexGroups()
		val fg = _fm.getDiagram()

		// _LOGGER.debug("After normalization (Mutex/splot):\nfm="
		// + _fm + "\nfg=" + fg);

		val sb = new StringBuilder()

		if (fg.isTop())
			return FeatureGraphFactory.DEFAULT_TOP_STRING;
		else if (fg.isBottom())
			return FeatureGraphFactory.DEFAULT_BOTTOM_STRING;

		// starting from the root
		val rootName = _fmv.root().getFtName();
		val rootNode = fg.findVertex(rootName);
		sb.append(processFt(rootNode, fg, ""));

		return sb.toString();
	}*/

	def processFt(v: FeatureNode[String]) = {

		val sb = new StringBuilder();

		/*
		if (v.equals(_fg.getTopVertex())) {
		  
		}			
		else if (FeatureVariable.isRoot(v, _fmv)) {
			
		}

		// process groups
		for (FeatureEdge e : fg.incomingEdges(v)) {
			if (e.getType() == FeatureEdge.MUTEX)
				throw new UnsupportedOperationException(
						"Does not support MUTEX group -- refactoring needed !");
			val sources = fg.getSources(e);

			if (sources.size() == 1) {
				
				// opt or mand
				FeatureNode<String> childFt = sources.iterator().next();
				if (FeatureNodeUtils.isMandatory(fg, childFt)
						&& e.getType() != FeatureEdge.HIERARCHY) {
					
				} else if (FeatureNodeUtils.isOptional(fg, childFt)) {
					
				} else {
					
				}

			}

			switch (e.getType()) {
			case FeatureEdge.XOR:
				
				for (source <- sources) {
					
				}
				
			case FeatureEdge.OR:
				for (source <- sources) {
					
				}
			}

		}*/

		sb.toString();
	}

	def ftName(v: FeatureNode[String]) = {
		v.getFeature();
	}

	def featureID(ftName: String) : String = {
	   ftToId + (ftName -> id) 
	   "_r" + ftToId.get(ftName).toString()
	}

	def featureID(v: FeatureNode[String]) : String = {
		featureID (v.getFeature())
	}

	/**
	 * normalize the feature model _fm (no mutex-groups)
	 */
	def normalizeMutexGroups() {

		for (ft <- fg.features()) {

			val v = fg.findVertex(ft);
	
			assert (v != null);
			if (FeatureNodeUtils.hasMutexGroup(fg, v)) {
				FeatureModelUtil.unfoldGroup(fg, v, FeatureEdge.MUTEX);
			}

		}
	}






}