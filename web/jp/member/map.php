<?php
  include("../init.php");
  page_open(array("sess"=>"sessionId", "auth"=>"PLM_Auth"));
  
  writeLog($auth->auth["jpname"] . "(uid=" . $auth->auth["uid"] . ")");
  
  
  include("./map.ihtml");
  page_close();
?>