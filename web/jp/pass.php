<?php
  include("./init.php");

  if ($_POST["action"] == 'step2') { step2($_POST); }
  else { start($_POST); }



  /**
   * フォームへ
   */
  function start($request) {
    global $auth;
    global $sess;

    $response["db"] = new db_handler;
    include("./pass1.ihtml");
  }


  /**
   * 入力内容を確認
   * DB検索
   * メール送信
   * 終了画面へ
   */
  function step2($request) {
    global $auth;
    global $sess;

    /* 空白などを削除 */
    $request = initRequest($request);

    /* 入力内容のチェック */
    checkRequestParam($request);

    $db = new db_handler;
    /* DBへ登録 */
//   if (!$db->getID_PW($request["email"])) {
//     error('エラー', '大変申し訳ございません。処理に失敗しました。しばらくしてから、再度行ってみて下さい。');
//   }
    $res = array();
    $res = $db->getID_PW($request["email"]);

    /*
     * 該当なし
     */
    if (empty($res["id"])) {
      error('エラー', '該当するメールアドレスがありません。');
    }

    /* E-Mailの送信 */
     global $BASE_URL;
     global $CONTACT_MAIL;

    $body = "　‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥[再発行メール]\n" . 
            "　 [P-linkmap] project　\n" . 
            "　http://www.p-linkmap.com\n" . 
            "　‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥\n" . 
            "　■ID・パスワード再発行\n" . 
            "\n" . 
            "　P-linkmap　をご利用いただきありがとうございます。\n" . 
            "　" . $res["name"] . "さんのID、及びパスワードです。\n" . 
            "　\n" . 
            "　ID: " . $res["id"] . "\n" . 
            "　パスワード: " . $res["pw"] . "\n" . 
            "　\n" . 
            "　あなたのMAPはこちらです。\n" . 
            "　" . $BASE_URL . "/jp/member/map.php\n" . 
            "　------------------------------------------------------------\n" . 
            "　[このメールは保管することをオススメします。]\n" . 
            "\n" . 
            "　P-linkmapはWEB上のインタラクティブな人間関係地図です。\n" . 
            "　もちろん無料です。\n" . 
            "　どんどん、あなたのお友達をマッピングして\n" . 
            "　人間関係を視覚化してみましょう。\n" . 
            "\n" . 
            "　登録に身に覚えのない方、また登録削除したい方は\n" . 
            "　お手数ですがsupport@p-linkmap.comまでメールをお願いします。\n" . 
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

     plm_mail($CONTACT_MAIL, $request["email"], '[P-linkmap]MAIL_ID・パスワード', $body);

    /* 終了画面の表示 */
    include("./pass2.ihtml");
  }



  /**
   * ユーザー情報の登録、変更内容のチェック
   */
  function checkRequestParam($request) {
    /*
     * 未入力のチェック
     * HPに関しては未入力を許可
     */
    if ( empty($request["email"]) ) {
      error('入力エラー', 'メールアドレスを入力してください。');
	}
    /*
     * 文字長さのチェック
     */
    if (strlen($request["email"]) > 128) {
      error('入力エラー', 'E-Mailの文字数が制限をオーバーしています。文字数を減らして下さい。');
    }


    /*
     * 文字形式のチェック
     */
     if ( !isEmail($request["email"]) ) {
       error('入力エラー', 'E-Mailの形式が違います。');
     }


     /*
      * 確認入力のチェック
      */

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