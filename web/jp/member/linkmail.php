<?php
  include("../init.php");
  page_open(array("sess"=>"sessionId", "auth"=>"PLM_Auth"));
  if ($_POST["action"] == 'step2') { step2($_POST); }
  elseif ($_POST["action"] == 'step3') { step3($_POST); }
  elseif ($_POST["action"] == 'step4') { step4($_POST); }
  else { start(); }
  page_close();

  /**
   * フォーム1へ
   */
  function start() {
    global $auth;
    global $sess;
    $db = new db_handler;
    $count= $db->getLinkCount($auth->auth["uid"]);
    if ($count > 100) {
      error('リンク数オーバー', 'リンク数が上限(100人)をオーバーしています。残念ながらこれ以上はリンクすることはできません。');
    }
    include("./linkmail1.ihtml");
    
  }

  /**
   * メールアドレスを確認
   * フォーム2へ
   */
  function step2($request) {
    global $auth;
    global $sess;

    /*
     * 文頭、文末の空白を削除
     */
    while (list($key, $val) = each($request)) {
        $request[$key] = trim($val);
    }

   if ( empty($request["email"]) ) {
      error('入力エラー', 'E-Mailが未入力です。');
   } elseif ( !isEmail($request["email"]) ) {
      error('入力エラー', 'E-Mailの形式が違います。');
   }

    $db = new db_handler;
    $result= $db->checkLink($request["email"], $auth->auth["ggid"]);
    if ($result["link"]) {
      error('リンク済', 'すでにつながっています。');
    } else {
    
       if ($result["link_type"] == 1) {
          /* リンク先がメンバーの時はリンク数を確認する */
          $count= $db->getLinkCount($result["uid"]);
          if ($count > 100) {
           error('リンク数オーバー(リンク相手)', 'リンク先の方のリンク数が上限をオーバーしています。残念ながらこの方とはリンクすることはできません。');
          }
       }
    
       $response["email"] = $request["email"];
       $response["name"] = $result["name"];
       $response["link_type"] = $result["link_type"];
       $response["uid"] = $result["uid"];
    }

    include("./linkmail2.ihtml");
    
  }

  /**
   * 入力内容を確認
   * 確認画面へ
   */
  function step3($request) {
    global $auth;
    global $sess;

    /*
     * 文頭、文末の空白を削除
     */
    while (list($key, $val) = each($request)) {
        $request[$key] = trim($val);
    }

   if ( empty($request["email"]) ) {
      error('入力エラー', 'E-Mailが未入力です。');
   } elseif ( !isEmail($request["email"]) ) {
      error('入力エラー', 'E-Mailの形式が違います。');
   } elseif ( empty($request["name"]) ) {
      error('入力エラー', 'お名前が未入力です。');
   } elseif ( empty($request["relation"]) ) {
      error('入力エラー', '関係が未入力です。');
   } elseif ( empty($request["relation"]) ) {
      error('入力エラー', 'メッセージが未入力です。');
   }


    $response = array();
    $response["email"] = $request["email"];
    $response["link_type"] = $request["link_type"];
    $response["uid"] = $request["uid"];
    $response["name"] = $request["name"];
    $response["relation"] = $request["relation"];
    $response["message"] = $request["message"];
    $response["id"] = md5(uniqid("plm"));


    include("./linkmail3.ihtml");
    
  }

  /**
   * 入力内容を確認
   * DB登録
   * メール送信
   * 終了画面へ
   */
  function step4($request) {
    global $auth;
    global $sess;

    /*
     * 文頭、文末の空白を削除
     */
    while (list($key, $val) = each($request)) {
        $request[$key] = trim($val);
    }

    /*
     * リンク情報オブジェクトを作成
     */
    $linkInfo = new link_info;
    $linkInfo->parent_uid = $auth->auth["uid"];
    if ($request["uid"] != '') { $linkInfo->child_uid = $request["uid"]; }
    else { $linkInfo->child_uid = 'NULL'; }
    $linkInfo->child_name = $request["name"];
    $linkInfo->relation = $request["relation"];
    $linkInfo->link_type = $request["link_type"];
    $linkInfo->link_id = $request["id"];

  $db = new db_handler;
  if (!$db->linkmailTrans($linkInfo)) {
    error('仮登録エラー', '大変申し訳ございません。仮登録処理に失敗しました。しばらくしてから、再度登録作業を行ってみて下さい。');
  }



    /*
     * メール送信
     */
     global $BASE_URL;
     global $CONTACT_MAIL;

    $body = "　‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥[リンクメール]\n" . 
            "　 [P-linkmap] project　\n" . 
            "　http://www.p-linkmap.com\n" . 
            "　‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥\n" . 
            "　■P-linkmapへのお誘い\n" . 
            "\n" . 
            "　　" . $auth->auth["jpname"] . "さんから\n" . 
            "　　あなたへ、P-linkmapへのお誘いです。\n" . 
            "\n" . 
            "　　" . str_replace("\n" ,"\n　　", $request["message"]) . "\n" . 
            "　\n" . 
            "　↓こちらから簡単な登録作業を終えると\n" . 
            "　あなた専用の人間関係マップをつくることができます。（無料！）\n" . 
            "　\n" . 
            "" . $BASE_URL . "/jp/link.php?link_id=" . $request["id"] . "\n" . 
            "\n" . 
            "　------------------------------------------------------------\n" . 
            "　P-linkmapはWEB上のインタラクティブな人間関係地図です。\n" . 
            "　もちろん無料です。\n" . 
            "　どんどん、あなたのお友達をマッピングして\n" . 
            "　人間関係を視覚化してみましょう。\n" . 
            "\n" . 
            "　‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥\n" . 
            "　[登録内容変更]\n" . 
            "　-> " . $BASE_URL . "/jp/member/change.php\n" . 
            "\n" . 
            "　[質問・お問い合せ]\n" . 
            "　-> support@p-linkmap.com\n" . 
            "\n" . 
            "　[管理者へのメール]\n" . 
            "　-> webmaster@p-linkmap.com\n" . 
            "\n" . 
            "\n" . 
            "　Copyright (C) 2002 P-linkmap.All right reserved.\n" . 
            "　‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥\n" . 
            "\n\n";
     


      plm_mail($CONTACT_MAIL, $request["email"], '[P-linkmap]MAIL_P-linkmapへのお誘い', $body);
      include("./linkmail4.ihtml");
      
  }









?>