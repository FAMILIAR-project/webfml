package models

/**
 * class to create user
 */
class User(var login: String, var password: String) {  
 
  /**
   * Return the login of the user
   */
  def getLogint():String={
    return login
  }
  /**
   * Return the pass of the user
   */
  def getPass():String ={
    return password
  }
  
}

