<?php
  include("./init.php");

  
  if ($_POST["action"] == 'step2') { step2($_POST); }
  elseif ($_POST["action"] == 'step3') { step3($_POST); }
  elseif ($_POST["action"] == 'cancel') { cancel($_POST); }
  elseif (empty($_GET["link_id"])) { error("エラー", "パラメータが足りません。"); }
  else { start($_GET); }


  /**
   * リンク情報をDBから取得して処理を振り分ける
   */
  function start($request) {
    global $sess;

    $response = array();
    $db = new db_handler;
    $linkInfo = $db->getLinkInfo($request["link_id"]);
    if (!$linkInfo->valid) {
      error('リンクエラー','仮登録ID(' . $linkInfo->link_id . ')が無効です。');
    }
      $response["link_id"]=$linkInfo->link_id;
      $response["parent_name"]=$linkInfo->parent_name;
      $response["relation"]=$linkInfo->relation;


    include("./link1.ihtml");
  }


  function cancel ($request) {
    global $sess;
    $db = new db_handler;

    if ($request["link_id"]) {
        $linkInfo = $db->getLinkInfo($request["link_id"]);
    } else {
        $linkInfo = new link_info;
    }
    
    
    $response["link_id"]=$linkInfo->link_id;
    $response["parent_name"]=$linkInfo->parent_name;
    $response["relation"]=$linkInfo->relation;
    include("./link_cancel.ihtml");
  }

  function step2 ($request) {
    global $sess;
    $db = new db_handler;

    if ($request["link_id"]) {
        $linkInfo = $db->getLinkInfo($request["link_id"]);
    } else {
        $linkInfo = new link_info;
    }

    if ($linkInfo->link_type == 0) {
      /*
       * 未登録者のリンク時はユーザー登録を行う
       */
      $response["link_id"]=$linkInfo->link_id;
      include("./link2_0.ihtml");
    } elseif ($linkInfo->link_type == 1) {

      /*
       * メンバー同士のリンク処理を行う
       * plm_member_relation テーブルへINSERT
       * (関係情報を登録する)
       * plm_group テーブルのggidをアップデート
       * (非紹介者のggidを紹介者と同じggidにする)
       */
   if (!$db->linkTrans($linkInfo)) {
     error('登録エラー', '大変申し訳ございません。登録処理に失敗しました。しばらくしてから、再度登録作業を行ってみて下さい。');
   }



      /*
       * リンク完了メール送信
       */
     global $BASE_URL;
     global $CONTACT_MAIL;
            
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

      include("./link2_1.ihtml");
    } else {
      error('リンクエラー','セッションが無効です。');
    }
  }



  function step3 ($request) {
    global $sess;
    $db = new db_handler;

    if ($request["link_id"]) {
        $linkInfo = $db->getLinkInfo($request["link_id"]);
    } else {
        $linkInfo = new link_info;
    }
    
    
    if ($linkInfo->link_type == 0) {
      $response["link_id"]=$linkInfo->link_id;
      include("./link3_0.ihtml");
    } else {
      error('リンクエラー','セッションが無効です。');
    }
    
    
  }









?>