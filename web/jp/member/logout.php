<?php
  include("../init.php");
  page_open(array("sess"=>"sessionId", "auth"=>"PLM_Auth"));
  $sess->delete();
  include("./logout.ihtml");
  page_close();
?>