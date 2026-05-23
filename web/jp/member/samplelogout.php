<?php
  include("../init.php");
  page_open(array("sess"=>"sessionId"));
  $sess->delete();
  include("./logout.ihtml");
  page_close();
?>