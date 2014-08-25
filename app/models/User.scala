package models

class User(var login: String, var password: String) {  
 //@TODO : Add the user in the database  
  def getLogint(){
    return login
  }
  
  def getPass(){
    return password
  }
  
}

