<?php
  include("../init.php");
  
  page_open(array("sess"=>"sessionId", "auth"=>"PLM_Auth"));
  if ($_POST["action"] == 'step2') { step2($_POST); }
  elseif ($_POST["action"] == 'step3') { step3($_POST); }
  else { start(); }
  page_close();


  /**
   * DBから登録情報を取得
   * フォームへ
   */
  function start() {
    global $auth;
    global $sess;


     /* DBから登録情報を取得 */
    $db = new db_handler;
    $result = $db->getFormData($auth);
    $member = $result["member"];
    $iconList = $result["iconList"];
    
    if (!$member->valid) {
      error("内部エラー", "登録情報の読込中にエラーが発生しました。");
    }


     /* 画面出力 */
     $response = array();
     $response["name"] = htmlspecialchars($member->name);
     $response["sex"] = htmlspecialchars($member->sex);
     
     list($y,$m,$d)= split("-", $member->birthday, 3);  
     $response["year"] = sprintf("%d",$y);
     $response["month"] = sprintf("%d",$m);
     $response["day"] = sprintf("%d",$d);
     //$response["year"] = substr($member->birthday,0,4);
     //$response["month"] = sprintf("%d",substr($member->birthday,4,2));
     //$response["day"] = sprintf("%d",substr($member->birthday,6,2));
     
     
     
     $response["blood"] = htmlspecialchars($member->blood);
     $response["prefectures"] = htmlspecialchars($member->prefectures);
     $response["job"] = htmlspecialchars($member->job);
     $response["interest"] = htmlspecialchars($member->interest);
     $formArray = explode(" ", $member->keyword);
     for ($i=0; $i<8; $i++) {
       $j = $i + 1;
       if (isset($formArray[$i])) { $val = $formArray[$i]; }
       else { $val = ""; }
       $response["keyword" . $j] = htmlspecialchars($val);
     }
    $response["email"] = htmlspecialchars($member->email);
    $response["email_flg"] = htmlspecialchars($member->email_flg);    
    $response["hidden"] .= '<input type="hidden" name=old_email value="' . htmlspecialchars($member->email) . '">';
    $response["hidden"] .= '<input type="hidden" name=old_login_id value="' . htmlspecialchars($member->login_id) . '">';
    $response["web"] = htmlspecialchars($member->web);
    if ($member->web == '') { $response["web"] = "http://"; }
    $response["login_id"] = htmlspecialchars($member->login_id);
    $response["passwd"] = htmlspecialchars($member->passwd);
    $response["passwd_confirm"] = htmlspecialchars($member->passwd);
    $response["iconList"] = $iconList;
    $response["db"] = $db;
    unset($request);

    include("./change1.ihtml");
  }

  /**
   * 入力内容を確認
   * 確認画面へへ
   */
  function step2($request) {
    global $auth;
    global $sess;

    /* 空白などを削除 */
    $request = initRequest($request);

     /* つながり情報を取得 */
    $linkInfo = getLinkInfo($request);

    /* 入力内容のチェック */
    checkPlm_member($request);
    checkLinkInfo($linkInfo);

     /* 画面出力 */
    $response = getStep2Response($request, $linkInfo);
    unset($request);

    include("./change2.ihtml");
  }

  /**
   * 入力内容を確認
   * DB登録
   * 終了画面へ
   */
  function step3($request) {
    global $auth;
    global $sess;

    $db = new db_handler;

    /* 空白などを削除 */
    $request = initRequest($request);

     /* つながり情報を取得 */
    $linkInfo = getLinkInfo($request);

    /* DBへ登録 */
    if (!$db->changeTrans($request, $linkInfo, $auth)) {
     error('登録エラー', '大変申し訳ございません。登録処理に失敗しました。しばらくしてから、再度登録作業を行ってみて下さい。');
    }
    
    //-----------------------
    //岡本追加 (2002/08/17) start
    //-----------------------
    $auth->auth["jpname"] = $request["name"];
    //-----------------------
    //end
    //-----------------------
    
    
    
    include("./change3.ihtml");
  }




  function getLinkInfo($request) {
    $linkInfo = array();
    while ( list($key, $val) = each($request) ) { //loop
      if (ereg("link_uid([0-9]+)", $key, $REGS)) {
        $icon = new icon;
        $icon->uid = $val;
        $icon->name = $request["link_name" . $REGS[1]];
        $icon->relation = $request["link_relation" . $REGS[1]];
        $linkInfo[] = $icon;
      }
    } //loop
    return $linkInfo;
  }



  /**
   * つながり情報のチェック
   */
  function checkLinkInfo($linkInfo) {
     for($i=0; $i<count($linkInfo); $i++) { //loop
        if ($linkInfo[$i]->relation == '' || $linkInfo[$i]->name == '') {
          error('入力エラー', 'つながり情報に入力漏れがあります。');
        }
     } //loop
  }


  /**
   * ユーザー情報の登録、変更内容のチェック
   */
  function checkPlm_member($request) {
    /*
     * 未入力のチェック
     * HPに関しては未入力を許可
     */
    if ( empty($request["name"]) ) {
      error('入力エラー', 'お名前が未入力です。');
    } elseif ( empty($request["login_id"]) ) {
      error('入力エラー', 'ユーザーIDが未入力です。');
    } elseif ( empty($request["passwd"]) ) {
      error('入力エラー', 'パスワードが未入力です。');
    } elseif ( empty($request["passwd_confirm"]) ) {
      error('入力エラー', '確認用パスワードが未入力です。');
    } elseif ( empty($request["sex"]) ) {
      error('入力エラー', '性別が未選択です。');
    } elseif ( empty($request["blood"]) ) {
      error('入力エラー', '血液型が未選択です。');
    } elseif ( empty($request["year"]) || 
               empty($request["month"]) || 
               empty($request["day"]) ) {
      error('入力エラー', '生年月日に入力漏れがあります。');
    } elseif ( empty($request["prefectures"]) ) {
      error('入力エラー', '都道府県が未選択です。');
    } elseif ( empty($request["job"]) ) {
      error('入力エラー', '社会的な区分が未選択です。');
    } elseif ( empty($request["interest"]) ) {
      error('入力エラー', '趣味が未入力です。');
    } elseif ( empty($request["keyword1"]) &&
               empty($request["keyword2"]) &&
               empty($request["keyword3"]) &&
               empty($request["keyword4"]) &&
               empty($request["keyword5"]) &&
               empty($request["keyword6"]) &&
               empty($request["keyword7"]) &&
               empty($request["keyword8"]) ) {
      error('入力エラー', 'キーワードが未入力です。一つ以上のキーワードを入力して下さい。');
    } elseif ( empty($request["email"]) ) {
      error('入力エラー', 'E-Mailが未入力です。');
    } elseif ( empty($request["email_flg"]) ) {
      error('入力エラー', 'E-Mailの公開が未選択です。');
    }


    /*
     * 文字長さのチェック
     */
    if (strlen($request["name"]) > 32) {
      error('入力エラー', 'お名前の文字数が制限をオーバーしています。文字数を減らして下さい。');
    } elseif (strlen($request["login_id"]) > 32) {
      error('入力エラー', 'ユーザーIDの文字数が制限をオーバーしています。文字数を減らして下さい。');
    } elseif (strlen($request["passwd"]) > 32) {
      error('入力エラー', 'パスワードの文字数が制限をオーバーしています。文字数を減らして下さい。');    } elseif (strlen($request["passwd"]) < 6) {
      error('入力エラー', 'パスワードの文字数が短すぎます。6文字以上にして下さい。');
    } elseif (strlen($request["interest"]) > 128 ) {
      error('入力エラー', '趣味の文字数が制限をオーバーしています。文字数を減らして下さい。');
    } elseif (strlen($request["keyword1"]) > 128 ||
              strlen($request["keyword2"]) > 128 ||
              strlen($request["keyword3"]) > 128 ||
              strlen($request["keyword4"]) > 128 ||
              strlen($request["keyword5"]) > 128 ||
              strlen($request["keyword6"]) > 128 ||
              strlen($request["keyword7"]) > 128 ||
              strlen($request["keyword8"]) > 128 ) {
      error('入力エラー', '長すぎるキーワードがあります。キーワードの文字数を減らして下さい。');
    } elseif (strlen($request["email"]) > 128) {
      error('入力エラー', 'E-Mailの文字数が制限をオーバーしています。文字数を減らして下さい。');
    } elseif (strlen($request["web"]) > 128) {
      error('入力エラー', 'URLの文字数が制限をオーバーしています。文字数を減らして下さい。');
    }


    /*
     * 文字形式のチェック
     */
     if ( !isEmail($request["email"]) ) {
       error('入力エラー', 'E-Mailの形式が違います。');
     } elseif ( !ereg("[0-9][0-9][0-9][0-9]", $request["year"], $REGS) ) {
       error('入力エラー', '生年月日[年]は半角数字4桁で入力して下さい。');
     } elseif (!isASII($request["login_id"])) {
       error('入力エラー', 'ユーザーIDは半角英数字で入力して下さい。');
     } elseif (!isASII($request["passwd"])) {
       error('入力エラー', 'パスワードは半角英数字で入力して下さい。');
     }


     /*
      * 確認入力のチェック
      */
    if ( $request["passwd"] != $request["passwd_confirm"]) {
      error('入力エラー', '確認用パスワードが一致しません。');
    }


      $db = new db_handler;
     /*
      * E-Mail,ユーザーIDが既に登録済でないかどうかチェック
      */
      $check = $db->checkDuplication($request["email"], $request["login_id"]);
     if ($check["email"] == 1 && $request["old_email"] != $request["email"]) {
       error('重複エラー', '同じE-Mailアドレスが既に登録されています。');
     }
     if ($check["login_id"] == 1 && $request["old_login_id"] != $request["login_id"]) {
       error('重複エラー', '同じユーザIDが既に登録されています。');
     }
  }






  /**
   * step2用のレスポンスを返す
   */
  function getStep2Response($request, $linkInfo) {
     $response = array();
     $response["name"] = htmlspecialchars($request["name"]) . 
                         '<input type="hidden" name="name" value=' . htmlspecialchars($request["name"]) . ' >';

     $response["login_id"] = htmlspecialchars($request["login_id"]) . 
                             '<input type="hidden" name="login_id" value="' . htmlspecialchars($request["login_id"]) . '" >';
     $response["sex"] = htmlspecialchars($request["sex"]) . 
                        '<input type="hidden" name="sex" value=' . htmlspecialchars($request["sex"]) . ' >';

     $response["birthday"] = $request["year"] . "-" . $request["month"] . "-" . $request["day"];
     $response["birthday"] = htmlspecialchars($response["birthday"]) . 
                             '<input type="hidden" name="birthday" value=' . htmlspecialchars($response["birthday"]) . ' >';

     $response["blood"] = htmlspecialchars($request["blood"]) . 
                          '<input type="hidden" name="blood" value=' . htmlspecialchars($request["blood"]) . ' >';
     $response["prefectures"] = htmlspecialchars($request["prefectures"]) . 
                                '<input type="hidden" name="prefectures" value="' . htmlspecialchars($request["prefectures"]) . '">';
     $response["job"] = htmlspecialchars($request["job"]) . 
                        '<input type="hidden" name="job" value="' . htmlspecialchars($request["job"]) . '">';
     $response["interest"] = htmlspecialchars($request["interest"]) . 
                             '<input type="hidden" name="interest" value=' . htmlspecialchars($request["interest"]) . ' >';
     for ($i=1; $i<9; $i++) {
       $key = "keyword" . $i;
       if ($request[$key] != '') {
         $response["keyword"] .= htmlspecialchars($request[$key]) . 
                           '<input type="hidden" name="' . $key . '" value="' . htmlspecialchars($request[$key]) . '">' . "<br>\n";
       }
     }
     $response["email"] = $request["email"] . 
                          '<input type="hidden" name="email" value=' . $request["email"] . ' >';

     $email_flg = array( "TRUE"=>"する", "FALSE"=>"しない" );
     $response["email_flg"] = $email_flg[$request["email_flg"]] . 
                              '<input type="hidden" name="email_flg" value=' . htmlspecialchars($request["email_flg"]) . ' >';
     if ($request["web"] == 'http://') { $request["web"] = ''; }
     $response["web"] = htmlspecialchars($request["web"]) . 
                        '<input type="hidden" name="web" value=' . htmlspecialchars($request["web"]) . ' >';


     $response["passwd"] = htmlspecialchars($request["passwd"]) . 
                           '<input type="hidden" name="passwd" value="' . htmlspecialchars($request["passwd"]) . '" >';


    /*
     * つながり情報
     */
       $response["iconList"] = $linkInfo;
       






    return $response;
  }

  /**
   * 文頭、文末の空白を削除
   */
  function initRequest($request) {
    while (list($key, $val) = each($request)) {
        $request[$key] = trim($val);
    }
    return $request;
  }

?>