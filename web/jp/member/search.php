<?php
  include("../init.php");
  page_open(array("sess"=>"sessionId", "auth"=>"PLM_Auth"));
  
  //writeLog("検索 " . "(uid=" . $auth->auth["uid"] . ")");
  
  
  /*
   * セッション変数
   */
   $searchParam = array();
   
  
  if ($_POST["action"] == 'step2') { step2($_POST); }
  else { start(); }
  page_close();




  /**
   * 検索フォームへ
   */
  function start() {
    global $auth;
    global $sess;
    
    include("./search1.ihtml");
  }

  /**
   * 検索パラメータを確認し、
   * マップへ
   */
  function step2($request) {
    global $auth;
    global $sess;
    global $searchParam;
    
    
    
    
    
    /*
     * 文頭、文末の空白を削除
     */
    while (list($key, $val) = each($request)) {
        $request[$key] = trim($val);
    }

   //if (empty($request["prefectures"]) && empty($request["keyword"])) {
   //   error('入力エラー', '検索条件がありません。');
   //}

  /*
   * セッションスコープで検索条件をセットする
   */
      $sess->register("searchParam");
    //$sess->register("cart"); $sess->unregister("cart");
   $searchParam = array(
                        "prefectures" => $request["prefectures"],
                        "keyword" => $request["keyword"]
                        );

   
   
   
  /*
   * HTMLページの表示
   */
   //$response = array(
   //                  "DO_SEARCH" => "TRUE"
   //                 );
    include("./search2.ihtml");

   
   
  }

   
   
?>