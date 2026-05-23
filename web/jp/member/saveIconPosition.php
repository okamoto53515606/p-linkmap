<?php
  include("../init.php");
  page_open(array("sess"=>"sessionId", "auth"=>"PLM_Auth"));

  $response = new clientapli;



  /*
   * リクエストパラメータから位置情報を取得
   */
  $data = "";
  while ( list($key, $val) = each($_GET) ) { //loop
    if(ereg("^[0-9]+$", $key, $REGS)) {
       if ($val != '') {
         $data .= $key . "=" . $val . "&";
        }
    }
  } //loop




  if ($data != '') {
    $db = new db_handler;
    if (!$db->saveIconPositionTrans($auth->auth["uid"], $data)) {
      $response->printError('位置情報の保存に失敗しました。');
    }
  }


    $response->printSuccess();



  page_close();

?>