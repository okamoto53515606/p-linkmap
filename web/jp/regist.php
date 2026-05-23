<?php
  include("./init.php");


  /**
   * リンク情報オブジェクト
   * リンクIDがPOSTされた時はユーザー登録処理に加えてリンク関連の処理も追加する
   */
  $linkInfo;
  if ($_POST["link_id"] != '') {
    $db = new db_handler;
    $linkInfo = $db->getLinkInfo($_POST["link_id"]);
    $linkInfo->valid = TRUE;
  } else {
    $linkInfo = new link_info;
    $linkInfo->valid = FALSE;
  }



  if ($_POST["action"] == 'step2') { step2($_POST); }
  elseif ($_POST["action"] == 'step3') { step3($_POST); }
  else { start($_POST); }



  /**
   * フォームへ
   */
  function start($request) {
    global $auth;
    global $sess;
    global $linkInfo;

    
    if ($linkInfo->valid) {
      $response["link_id"]=$linkInfo->link_id;
    }

    $response["db"] = new db_handler;
    include("./regist1.ihtml");
  }


  /**
   * 入力内容を確認
   * 確認画面へ
   */
  function step2($request) {
    global $auth;
    global $sess;
    global $linkInfo;

    /* 空白などを削除 */
    $request = initRequest($request);

    /* 入力内容のチェック */
    checkRequestParam($request);

     /* 画面出力 */
    $response = getStep2Response($request);
    if ($linkInfo->valid) {
      $response["link_id"]=$linkInfo->link_id;
    }
    unset($request);
    include("./regist2.ihtml");
  }




  /**
   * 入力内容を確認
   * DB登録
   * メール送信
   * 終了画面へ
   */
  function step3($request) {
    global $auth;
    global $sess;
    global $linkInfo;

    $gid = '';
    $uid = '';

    /* 空白などを削除 */
    $request = initRequest($request);



    $db = new db_handler;
    /* DBへ登録 */
   if (!$db->registTrans($request, $linkInfo)) {
     error('登録エラー', '大変申し訳ございません。登録処理に失敗しました。しばらくしてから、再度登録作業を行ってみて下さい。');
   }

    /* E-Mailの送信 */
     global $BASE_URL;
     global $CONTACT_MAIL;

    $body = "　‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥[登録完了メール]\n" . 
            "　 [P-linkmap] project　\n" . 
            "　http://www.p-linkmap.com\n" . 
            "　‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥\n" . 
            "　■登録完了\n" . 
            "\n" . 
            "　P-linkmap　へのご登録ありがとうございます。\n" . 
            "　" . $request["name"] . "さんのID、及びパスワードです。\n" . 
            "　\n" . 
            "　ID: " . $request["login_id"] . "\n" . 
            "　パスワード: " . $request["passwd"] . "\n" . 
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

     plm_mail($CONTACT_MAIL, $request["email"], '[P-linkmap]MAIL_登録完了', $body);

    /*
     * リンク情報が存在する場合、紹介者にリンク完了メールを送信する
     */
    if ($linkInfo->valid) {
          /*
           * リンク完了メール送信
           */
           
    $body = "　‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥[マッピング完了メール]\n" . 
            "　 [P-linkmap] project　\n" . 
            "　http://www.p-linkmap.com\n" . 
            "　‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥‥\n" . 
            "　■マッピング完了\n" . 
            "\n" . 
            "　あなたのMAPに\n" . 
            "　" . $linkInfo->child_name . "さんがマッピングされました。\n" . 
            "\n" . 
            "　\n" . 
            "　↓mapはコチラ\n" . 
            "　" . $BASE_URL . "/jp/member/map.php\n" . 
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
           
           
                
                
         plm_mail($CONTACT_MAIL, $linkInfo->parent_email, '[P-linkmap]MAIL_マッピング完了', $body);
    }

    /* 終了画面の表示 */
    include("./regist3.ihtml");
  }












  /**
   * ユーザー情報の登録、変更内容のチェック
   */
  function checkRequestParam($request) {
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
    if ( $request["email"] != $request["email_confirm"]) {
      error('入力エラー', '確認用E-Mailが一致しません。');
    } elseif ( $request["passwd"] != $request["passwd_confirm"]) {
      error('入力エラー', '確認用パスワードが一致しません。');
    }

      $db = new db_handler;
     /*
      * E-Mail,ユーザーIDが既に登録済でないかどうかチェック
      */
      $check = $db->checkDuplication($request["email"], $request["login_id"]);
     if ($check["email"] == 1) {
       error('重複エラー', '同じE-Mailアドレスが既に登録されています。');
     }
     if ($check["login_id"] == 1) {
       error('重複エラー', '同じユーザIDが既に登録されています。');
     }
  }







  /**
   * step2用のレスポンスを返す
   */
  function getStep2Response($request) {
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
                           '<input type="hidden" name="' . $key . '" value=' . htmlspecialchars($request[$key]) . '>' . "<br>\n";
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
     
     $response["db"] = new db_handler;
        
        
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