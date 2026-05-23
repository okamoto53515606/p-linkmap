<?php
  include("../init.php");
  page_open(array("sess"=>"sessionId", "auth"=>"PLM_Auth2"));
  
  writeLog('#samplemap#' . $auth->auth["jpname"] . "(uid=" . $auth->auth["uid"] . ")");
  
  include("./samplemap.ihtml");
  
  page_close();
?>