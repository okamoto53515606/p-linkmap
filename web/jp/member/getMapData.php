<?php
  include("../init.php");
  page_open(array("sess"=>"sessionId", "auth"=>"PLM_Auth"));
  

  //include("/home/user/danzj000/phplib/additional/clientapli2.inc");
  $response = new clientapli;
  $request = array();

  if ( isset($_POST["uid"]) ) {
    $request = $_POST;
  } elseif ( isset($_GET["uid"]) ) {
    $request = $_GET;
  } else {
    $response->printError('パラメータが無効です。');
    page_close(); exit;
  }



  /*
   * セッション変数のチェック
   */
  if(empty($auth->auth["uid"])) {
    $response->printError('セッションが無効です。');
    page_close(); exit;
  }



  /*
   * マップデータの表示
   */
  $db = new db_handler;





  if ( isset($request['DO_SEARCH']) ) { //検索モード
  
      if ( !isset($searchParam) ) {
        $searchParam = array(
                         "prefectures" => "",
                         "keyword" => ""                         
                       );
      } else {
        ///while (list($key, $val) = each($searchParam)) {
        //  print "$key = $val \n";
        //}
      }
      
      
      
      $result = $db->getSearchMapData($auth, $searchParam);
      $member = $result["member"];
      $iconList = $result["iconList"];
      if ($member->valid) {
          $response->printSearchMapData($member, $iconList, $searchParam);
      } else {
          writeLog("(uid=" . $auth->auth["uid"] . "):getSearchMapData:");
          $response->printError('つながっていない人のマップを表示しようとしています。');
      }
      
      /*
       * セッションから検索パラメータを削除
       */
      $sess->unregister("$searchParam");
      
      
  } else { //通常モード
  
      $result = $db->getMapData($request["uid"], $auth);
      $member = $result["member"];
      $iconList = $result["iconList"];
      if ($member->valid) {
          $response->printMapData($member, $iconList);
      } else {
           writeLog("(uid=" . $auth->auth["uid"] . "):gethMapData:request[uid]=" . $request["uid"]);
          $response->printError('つながっていない人のマップを表示しようとしています。');
      }
      
  }






  page_close();










/*
  function dump($string) {
      if ($string == '') { return; }
        $debug = "prefectures_dump : \n";
        $b = unpack("C*", $string);
        for ($i = 1; $i <= count($b); ++$i) { $debug .= sprintf("0x%02X\n", $b[$i]); }
        $debug .= "prefectures_encode : " . mb_detect_encoding($string, "SJIS,UTF-8,EUC-JP,JIS,ASCII") . "\n";
        $debug .= "prefectures : " . $string . "\n";
        $debug .= "convert_SJIS_prefectures : " . mb_convert_encoding($string, "SJIS", "SJIS,UTF-8,EUC-JP,JIS,ASCII") . "\n";
        writeLog($debug);
  }
*/








?>