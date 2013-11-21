package controllers

trait VariableHelper {
  
    
  
    def mkVariableURL(varID : String) = {
     val urlID = "variable/" + varID
     val callBack = "displayVariable(" + "\"" + varID + "\"" + ");"
      
       <div class="dropdown">
  <button class="btn dropdown-toggle sr-only" type="button" id="dropdownMenu1" data-toggle="dropdown">
    {varID}
    <span class="caret pull-right"></span>
  </button>
  <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
    <li role="presentation"><a role="menuitem" tabindex="-1" onclick={callBack}>Display</a></li>
    <li role="presentation"><a role="menuitem" tabindex="-1" href="#">Edit</a></li>
    <li role="presentation"><a role="menuitem" tabindex="-1" href="#">Remove</a></li>
  </ul>
</div>
<script>  
  
</script> 
   }
    
  

}