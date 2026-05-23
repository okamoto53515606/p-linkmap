<?php

/**
 * DB操作クラス
 *
 */
class db_handler {

  /**
   * DB操作オブジェクト
   */
  var $db;


  /**
   * コンストラクタ
   * 
   */
  function db_handler () {
    $this->db = new PLM_DB_Sql;
  }

  /**
   * 日本語文字コードの指定(UNICODE or EUC-JP or SJIS)
   * @param $type 文字エンコード名
   */
   /*
  function encoding ($type = 'SJIS') {
    $this->db->query("SET CLIENT_ENCODING TO '$type'");
  }
  */
  
  
  /**
   * SQL文用のエスケープ(不正入力を防ぐ)
   * @param $string 文字列
   * @return 変換後の文字列
   *
   */
  function SQLEscape($string) {
    return addslashes($string);
  }


  /**
   * DB操作オブジェクトを返す
   * @return DB操作オブジェクト
  function getHandler () {
    return $this->db;
  }
   */






  /**
   * 指定したuidのマップ情報を返す。
   * リンクしていない、または削除フラグがTRUEの時は、空のデータを返す。
   * 
   * @param $uid 情報を取得するユーザーのuid
   * @param $auth 認証オブジェクト
   * @return 結果を格納した配列
   */
  function getMapData($uid, $auth) {

    $uid = sprintf("%d", $uid );
    $myggid = sprintf("%d", $auth->auth["ggid"] );
    $myuid = sprintf("%d", $auth->auth["uid"] );    
    $result = array();
    $member = new plm_member;
    $iconList = array();
    $positionArray = array();

    $sql = "SELECT " . 
           "PM.login_id, " . 
           "PM.passwd, " . 
           "PM.name, " . 
                      
           "PM.sex, " . 
           //"case " . 
           //" when PM.sex = 'man' then '男' " . 
           //" when PM.sex = 'woman' then '女' " . 
           //" else '' " . 
           //"end AS sex,  " . 
                      
           "PM.blood || '型' AS blood, " . 
           
           "PM.birthday, " . 
           //"TO_CHAR(PM.birthday, 'YYYY年MM月DD日') AS birthday, " . 
           
           "PM.prefectures, " . 
           "PM.job, " . 
           "PM.interest, " . 
           
           "PM.email, " .            
           //"case " . 
           //" when PM.email_flg then email " . 
           //" else '' " . 
           //"end AS email,  " . 
           
           "PM.email_flg, " .            
           //"case " . 
           //" when PM.email_flg then '公開' " . 
           //" else '非公開' " . 
           //"end AS email_flg,  " . 
           
           "PM.web, " . 
           "PM.delete_flg AS delete_flg " . 
           "FROM plm_member PM, plm_group PG " . 
           "WHERE PM.uid = $uid " . 
           "AND PG.gid = PM.gid " . 
           "AND PG.ggid = $myggid ";
    $this->db->query($sql);
    if ($this->db->next_record()) { //loop
      $member->uid = $uid;
      $member->valid = true;
      
      

      
      

      /* 削除フラグがTRUEの時のみ値をセット */
      if ($this->db->f('delete_flg') != 't') {
        $member->login_id = $this->db->f('login_id');
        $member->passwd = $this->db->f('passwd');
        $member->name = $this->db->f('name');
        $member->sex = $this->db->f('sex');
        $member->blood = $this->db->f('blood');
        $member->birthday = $this->db->f('birthday');
        $member->prefectures = $this->db->f('prefectures');
        $member->job = $this->db->f('job');
        $member->interest = $this->db->f('interest');
        $member->email = $this->db->f('email');
        $member->email_flg = $this->db->f('email_flg');
        $member->web = $this->db->f('web');
        
        
        //<追加 name="岡本" date="20021018">
        if ( $member->sex == 'man' ) { $member->sex = '男';  }
        elseif ( $member->sex == 'woman' ) { $member->sex =  '女';  }
        if ( $member->email_flg != 't' ) {  $member->email = '';  }
        if ( $member->email_flg == 't' ) { $member->email_flg = '公開';  }
        else { $member->email_flg = '非公開';  }
        //</追加>
        
      } else {
        $member->name = '';
      }
    } //loop


    if (!$member->valid) {
      $result["member"] = $member;
      $result["iconList"] = $iconList;
      return $result;
    }

      /* キーワードを取得 */
      $keywordList = array();
      $sql = "SELECT " . 
             "PMK.keyword AS keyword " . 
             "FROM plm_member_keyword PMK " . 
             "WHERE PMK.uid = $uid ";
      $this->db->query($sql);
      while ($this->db->next_record()) {
        $keywordList[] = $this->db->f('keyword');
      }
      if (count($keywordList) > 0) { $member->keyword .= join(' ', $keywordList); }

    /* アイコン位置情報を取得 */
    if ($uid == $myuid) {
      $sql = "SELECT " . 
           " position_list " . 
           "FROM icon_position " . 
           "WHERE " . 
           "uid = $uid ";
      $this->db->query($sql);
      $array = array();
      if ($this->db->next_record()) {

        $array = explode("&", $this->db->f('position_list'));
        for ($i=0; $i<count($array); $i++) {
         if ($array[$i] != '') {
           $array2 = explode("=", $array[$i]);
           $key = $array2[0]; 
           $positionArray[$key] = explode(",", $array2[1]);
         }
        }

      }

    }
    
      /* アイコンリストを取得 */
      $sql = "SELECT " . 
             "PM.uid AS uid, " . 
             "PM.name AS name, " . 
             "PM.delete_flg AS delete_flg, " . 
             "PMR.relation AS relation " . 
             "FROM plm_member PM, plm_member_relation PMR " . 
             "WHERE " . 
             "PMR.parent = $uid " . 
             "AND PMR.child = PM.uid ";
      $this->db->query($sql);
      while ($this->db->next_record()) { //loop
        $icon = new icon;
        $icon->uid = $this->db->f('uid');
        if ( isset($positionArray[$icon->uid]) ) {
           $icon->x = $positionArray[$icon->uid][0];
           $icon->y = $positionArray[$icon->uid][1];
           //$icon->active = $positionArray[$icon->uid][2];
        }
         /* 削除フラグがFALSEの時のみ個人情報をセットする */
        if ($this->db->f('delete_flg') == 'f') {
          $icon->name = $this->db->f('name');
          $icon->relation = $this->db->f('relation');
          if ($this->db->f('relation') == '') { $icon->relation = '?'; }
        } else {
          $icon->type = -1;
          $icon->name = '?';
          $icon->relation = '?';
        }
        $iconList[] = $icon;
      } //loop
      
      
      
      /* 仮登録アイコンリストを取得 */
      $sql = "SELECT " . 
             "child, relation, child_name " . 
             " FROM temp_plm_member_relation " . 
             " WHERE parent = $uid ";
      $this->db->query($sql);
      while ($this->db->next_record()) { //loop
        $icon = new icon;
        $icon->uid = $this->db->f('child');
        $icon->name = $this->db->f('child_name');
        $icon->relation = $this->db->f('relation');
        $icon->type = 1;
        $iconList[] = $icon;
      } //loop
      
      


    $result["member"] = $member;
    $result["iconList"] = $iconList;
    return $result;
  }



  /**
   * 検索マップデータを返す
   * 
   * @param $auth 認証オブジェクト
   * @param $searchParam 検索条件(配列)
   * @return 結果を格納した配列
   */
  function getSearchMapData($auth, $searchParam) {

    $myggid = sprintf("%d", $auth->auth["ggid"] );
    $myuid = sprintf("%d", $auth->auth["uid"] );
    $result = array();
    $member = new plm_member;
    $iconList = array();


    $member->valid = true;


    $keyword = $this->SQLEscape($searchParam["keyword"]);
    $prefectures = $this->SQLEscape($searchParam["prefectures"]);
    $keyword = implode("','", explode(" ", $keyword));

    if ($keyword != '') { //キーワード検索がある時
      $sql = " SELECT DISTINCT " . 
           " PM.uid AS uid, " . 
           " PM.name AS name, " . 
           " PM.delete_flg AS delete_flg " . 
           " FROM " . 
           " plm_member PM, " . 
           " plm_group PG, " . 
           " plm_member_keyword PMK " . 
           " WHERE " . 
           " PMK.keyword IN ('$keyword') " . 
           " AND PMK.uid = PM.uid " . 
           " AND PG.ggid = $myggid " . 
           " AND PM.gid = PG.gid ";
          if ($prefectures != '') {
            $sql .= " AND PM.prefectures = '$prefectures' ";
          }
    } else { //キーワード検索がない時
      $sql = " SELECT " . 
           " PM.uid AS uid, " . 
           " PM.name AS name, " . 
           " PM.delete_flg AS delete_flg " . 
           " FROM " . 
           " plm_member PM, " . 
           " plm_group PG " . 
           " WHERE " . 
           " PG.ggid = $myggid " . 
           " AND PM.gid = PG.gid ";
          if ($prefectures != '') {
            $sql .= " AND PM.prefectures = '$prefectures' ";
          }
    }
    $sql .= " LIMIT 100 ";
    $this->db->query($sql);
    while ($this->db->next_record()) { //loop
        $icon = new icon;
        $icon->uid = $this->db->f('uid');
        $icon->relation = "";
         /* 削除フラグがFALSEの時のみ個人情報をセットする */
        if ($this->db->f('delete_flg') == 'f') {
          $icon->name = $this->db->f('name');
          if ($this->db->f('uid') == $myuid) { $icon->name = 'あなた'; }
        } else {
          $icon->name = '?';
          $icon->relation = '?';
          $icon->type = -1;
        }
        $iconList[] = $icon;
    } //loop
    


    $result["member"] = $member;
    $result["iconList"] = $iconList;
    return $result;
  }



  /**
   * 指定したuidの登録者情報を返す。
   * 登録者情報の変更時に使用する。
   *
   * @param $auth 認証オブジェクト
   * @return 結果を格納した配列
   */
  function getFormData($auth) {
    $myuid = sprintf("%d", $auth->auth["uid"]);
    $myggid = sprintf("%d", $auth->auth["ggid"]);
    
    $result = array();
    $member = new plm_member;
    $iconList = array();
    
    $sql = "SELECT " . 
           "PM.login_id, " . 
           "PM.passwd, " . 
           "PM.name, " . 
           "PM.sex,  " . 
           "PM.blood, " . 
           "PM.birthday, " . 
           //"TO_CHAR(PM.birthday, 'YYYYMMDD') AS birthday, " . 
           "PM.prefectures, " . 
           "PM.job, " . 
           "PM.interest, " . 
           "PM.email,  " . 
           "PM.email_flg, " . 
           "PM.web, " . 
           "PM.delete_flg AS delete_flg " . 
           "FROM plm_member PM, plm_group PG " . 
           "WHERE PM.uid = $myuid " . 
           "AND PG.gid = PM.gid " . 
           "AND PG.ggid = $myggid ";
    $this->db->query($sql);
    if ($this->db->next_record()) {
      $member->uid = $myuid;
      $member->valid = true;

      /* 削除フラグがTRUEの時は空のオブジェクトを返す */
      if ($this->db->f('delete_flg') == 't') { return $member; }

      $member->login_id = $this->db->f('login_id');
      $member->passwd = $this->db->f('passwd');
      $member->name = $this->db->f('name');
      $member->sex = $this->db->f('sex');
      $member->blood = $this->db->f('blood');
      $member->birthday = $this->db->f('birthday');
      $member->prefectures = $this->db->f('prefectures');
      $member->job = $this->db->f('job');
      $member->interest = $this->db->f('interest');
      $member->email = $this->db->f('email');
      $member->email_flg = $this->db->f('email_flg');
      $member->web = $this->db->f('web');
    } else {
      return $member;
    }


    if (!$member->valid) {
      $result["member"] = $member;
      $result["iconList"] = $iconList;
      return $result;
    }

    /*
     * キーワードを取得
     */
    $keywordList = array();
    $sql = "SELECT " . 
           "PMK.keyword AS keyword " . 
           "FROM plm_member_keyword PMK " . 
           "WHERE PMK.uid = $myuid ";
    $this->db->query($sql);
    while ($this->db->next_record()) {
      $keywordList[] = $this->db->f('keyword');
    }
    if (count($keywordList) > 0) { $member->keyword .= join(' ', $keywordList); }


      /* アイコンリストを取得 */
      $sql = "SELECT " . 
             "PM.uid AS uid, " . 
             "PM.name AS name, " . 
             "PM.delete_flg AS delete_flg, " . 
             "PMR.relation AS relation " . 
             "FROM plm_member PM, plm_member_relation PMR " . 
             "WHERE " . 
             "PMR.parent = $myuid " . 
             "AND PMR.child = PM.uid ";
      $this->db->query($sql);
      while ($this->db->next_record()) { //loop
        $icon = new icon;
        $icon->uid = $this->db->f('uid');
         /* 削除フラグがFALSEの時のみ個人情報をセットする */
        if ($this->db->f('delete_flg') == 'f') {
          $icon->name = $this->db->f('name');
          $icon->relation = $this->db->f('relation');
          $iconList[] = $icon;
        }
        // else {
        //  $icon->name = '?';
        //  $icon->relation = '?';
        //  $icon->type = -1;
       // }
        
        
        
      } //loop


    $result["member"] = $member;
    $result["iconList"] = $iconList;
    return $result;
  }









  /**
   *  E-MailとユーザーIDの重複をチェックする
   *
   * @param $email E-Mail
   * @param $login_id ユーザーID
   * @return 結果を格納した配列
   */
  function checkDuplication($email, $login_id) {
    $result = array("email"=>0, "login_id"=>0);
    if ($email == '' && $login_id == '') { return $result; }
    $email = $this->SQLEscape($email);
    $login_id = $this->SQLEscape($login_id);
    $sql = "SELECT " . 
           " email, login_id " . 
           "FROM plm_member " . 
           "WHERE " . 
           " email = '$email' OR login_id = '$login_id' ";
    $this->db->query($sql);
    while ($this->db->next_record()) {
        if ($this->db->f('email') == $email) { $result["email"] = 1; }
        if ($this->db->f('login_id') == $login_id) { $result["login_id"] = 1; }
    }
    return $result;
  }




  /**
   * 直接つながっている人数を返す(仮登録も含む)
   * @param $uid uid
   * @return 直接つながっている人数
   */
  function getLinkCount($uid) {
    $count = 0;
    $uid = sprintf("%d", $uid );
    $sql = "SELECT " . 
           "COUNT(child) AS count " .  
           "FROM plm_member_relation " . 
           " WHERE parent = $uid ";
    $this->db->query($sql);
    if ( $this->db->next_record() ) {
      $count += $this->db->f("count");
    }

    $sql = "SELECT " . 
           "COUNT(parent) AS count " .  
           "FROM temp_plm_member_relation " . 
           " WHERE parent = $uid ";
    $this->db->query($sql);
    if ( $this->db->next_record() ) {
      $count += $this->db->f("count");
    }

    return $count;
  }






  /**
   * リンク状態をチェックする(リンクメール送信時)
   * @param $email リンクメール送信アドレス
   * @param $myggid 自身のggid
   * @return 結果を格納した配列
   *
   */
  function checkLink($email, $myggid) {
    $myggid = sprintf("%d", $myggid );
    $result = array("link" => FALSE, 
                    "name" => '', 
                    "link_type" => -1,
                    "uid" => '', 
                    "gid" => '');

    $sql = "SELECT " . 
           "PG.ggid, " .  
           "PM.name, " . 
           "PM.uid, " . 
           "PM.gid " . 
           "FROM plm_member PM, plm_group PG " . 
           "WHERE PM.email = '$email' " . 
           "AND PG.gid = PM.gid ";

    $this->db->query($sql);
    if ( $this->db->next_record() ) {
      if ($this->db->f("ggid") == $myggid ){ //つながっている
        $result["link"] = true;
      } else { //つながっていない
        $result["name"] = $this->db->f("name");
        $result["link_type"] = 1; //リンクタイプは1
        $result["uid"] = $this->db->f("uid"); 
        $result["gid"] = $this->db->f("gid"); 
      }
    } else {
      $result["link_type"] = 0; //リンクタイプは0
    }

    return $result;
  }


  /**
   * リンク情報オブジェクトを返す
   * @param $id 仮登録ID
   * @return リンク情報オブジェクト
   */
  function getLinkInfo($id) {

    $result = new link_info;
    if ($id == '') { return $result; }
    $result->link_id = $id;

    $sql = "SELECT " . 
           "PM.name, " . 
           "PM.email, " . 
           "PM.uid, " . 
           "PM.gid, " . 
           "PM.delete_flg, " . 
           "TPMR.link_type, " .  
           "TPMR.child, " . 
           "TPMR.child_name, " .  
           "TPMR.relation, " .  
           "TPMR.link_type " .  
           "FROM plm_member PM, temp_plm_member_relation TPMR " . 
           "WHERE TPMR.id = '$id' " . 
           "AND PM.uid = TPMR.parent ";

    $this->db->query($sql);
    if ( $this->db->next_record() ) {
       if ($this->db->f("delete_flg") == 't') { return $result; }
         $result->parent_name = $this->db->f("name");
         $result->parent_gid = $this->db->f("gid");
         $result->parent_uid = $this->db->f("uid");
         $result->parent_email = $this->db->f("email");
         $result->child_uid = $this->db->f("child");
         $result->child_name = $this->db->f("child_name");
         $result->relation = $this->db->f("relation");
         $result->link_type = $this->db->f("link_type");
         $result->valid = true;
    }
    return $result;
  }






  /**
   * メンバーの新規登録処理
   * リンク処理も含む
   * @param $request HTTPリクエストパラメータ
   * @param $linkInfo リンク情報オブジェクト
   * @return 結果の真偽(boolean)
   *
   */
  function registTrans($request, $linkInfo) {
    /*
     * トランザクションを開始
     */
    $this->db->query("BEGIN");

    /*
     * gid,uidを取得
     */
    $sql = "SELECT nextval('plm_group_seq') AS gid, nextval('plm_member_seq') AS uid ";
    $this->db->query($sql);
    if ($this->db->next_record()) {
      $gid = $this->db->f("gid");
      $uid = $this->db->f("uid");
    }

    /*
     * リンク情報が存在する場合、gidは紹介者のgidにする
     */
    if ($linkInfo->valid) {
      $gid = $linkInfo->parent_gid;
    } else {
      /*
       * plm_groupテーブルにINSERT
       */
      $sql = sprintf("INSERT INTO plm_group " . 
             " ( gid, ggid ) " . 
             " VALUES " . 
             " ( %d, %d ) " 
             , $gid, $gid );
       $this->db->query($sql);
      if (!$this->db->affected_rows()) {
        $this->db->query("rollback");
        writeLog("rollback : " . $sql);
        return FALSE;
      }
    }
    /*
     * plm_memberテーブルにINSERT
     */
    $login_id = $this->SQLEscape($request["login_id"]);
    $passwd = $this->SQLEscape($request["passwd"]);
    $name = $this->SQLEscape($request["name"]);
    $sex = $this->SQLEscape($request["sex"]);
    $blood = $this->SQLEscape($request["blood"]);
    $birthday = $this->SQLEscape($request["birthday"]);
    $prefectures = $this->SQLEscape($request["prefectures"]);
    $job = $this->SQLEscape($request["job"]);
    $interest = $this->SQLEscape($request["interest"]);
    $email = $this->SQLEscape($request["email"]);
    $email_flg = $this->SQLEscape($request["email_flg"]);
    $web = $this->SQLEscape($request["web"]);

    $sql = sprintf("INSERT INTO plm_member " . 
                   " (uid, login_id, passwd, name, sex, blood, birthday, prefectures, job, interest, email, email_flg, web, gid) " . 
                   " VALUES " . 
                   " ( %d, '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', %s, '%s', %d ) " 
                   ,$uid, $login_id, $passwd, $name, $sex, $blood, $birthday, $prefectures, $job, $interest, $email, $email_flg, $web, $gid );
     $this->db->query($sql);
    if (!$this->db->affected_rows()) {
      $this->db->query("rollback");
      writeLog("rollback : " . $sql);
      return FALSE;
    }


    /*
     * plm_member_keyword(keyword)テーブルにINSERT
     */
    $keywordList = array();
    $sqlList = array();

    for($i=1; $i<9; $i++) { //loop
      $key = "keyword" . $i;
      if ($request[$key] != '') {
        ${$key} = $this->SQLEscape($request[$key]);
        $keywordList[] = ${$key};
      }
    } //loop
    /* SQL文を作成 */
    $keywordList = array_unique($keywordList); //add 20020825
    for($i=0; $i<count($keywordList); $i++) { //loop
        $sqlList[$i] = sprintf("INSERT INTO plm_member_keyword " . 
                    " (uid, keyword) " . 
                    " VALUES " . 
                    " (%d, '%s') ", 
                     $uid, $keywordList[$i] );
    } //loop


    /* キーワードの登録 */
    for($i=0; $i<count($sqlList); $i++) { //loop
      if ($sqlList[$i] != '') {
        
        $this->db->query($sqlList[$i]);
        if (!$this->db->affected_rows()) {
          $this->db->query("rollback");
          writeLog("rollback : " . $sql);
          return FALSE;
        }
      }
    } //loop


    /*
     * リンク情報が存在する場合、リンク情報を登録する
     */
    if ($linkInfo->valid) { //リンク処理
        /*
         * plm_member_relationテーブルにINSERT
         */
          $parent_gid=$this->SQLEscape($gid);
          $parent_uid=$this->SQLEscape($linkInfo->parent_uid);
          $child_uid=$this->SQLEscape($uid);
          $relation=$this->SQLEscape($linkInfo->relation);
          $link_id=$this->SQLEscape($linkInfo->link_id);

          /* 紹介者から参照される関係を登録 */
          $sql = sprintf("INSERT INTO plm_member_relation (parent,child,relation) VALUES (%d, %d, '%s')" , $parent_uid, $child_uid, $relation );
     $this->db->query($sql);
      if (!$this->db->affected_rows()) {
          $this->db->query("rollback");
          writeLog("rollback : " . $sql);
          return FALSE;
      }

          /* 非紹介者から参照される関係を登録 */
          $sql = sprintf("INSERT INTO plm_member_relation (parent,child,relation) VALUES (%d, %d, '%s')" ,$child_uid , $parent_uid, $relation );
     $this->db->query($sql);
      if (!$this->db->affected_rows()) {
          $this->db->query("rollback");
          writeLog("rollback : " . $sql);
          return FALSE;
      }

         /* 仮登録情報を削除 */
        $sql = sprintf("DELETE FROM temp_plm_member_relation WHERE id = '%s'", $link_id );
        $this->db->query($sql);
    } //リンク処理


    /*
     * トランザクションをコミット
     */
    $this->db->query("COMMIT");

    return TRUE;
  }



  /*
   * メンバー同士のリンク処理を行う
   * plm_member_relation テーブルへINSERT(関係情報を登録する)
   * plm_group テーブルのggidをアップデート(非紹介者のggidを紹介者と同じggidにする)
   * 仮登録データを削除
   * @param $linkInfo リンク情報オブジェクト
   * @return 結果の真偽(boolean)
   *
   */
  function linkTrans($linkInfo) {
      $this->db->query("BEGIN");

    /*
     * plm_member_relationテーブルにINSERT
     */
      $parent_gid=$this->SQLEscape($linkInfo->parent_gid);
      $parent_uid=$this->SQLEscape($linkInfo->parent_uid);
      $child_uid=$this->SQLEscape($linkInfo->child_uid);
      $relation=$this->SQLEscape($linkInfo->relation);
      $link_id=$this->SQLEscape($linkInfo->link_id);

      /* 紹介者から参照される関係を登録 */
      $sql = sprintf("INSERT INTO plm_member_relation (parent,child,relation) VALUES (%d, %d, '%s')" , $parent_uid, $child_uid, $relation );
     $this->db->query($sql);
    if (!$this->db->affected_rows()) {
      $this->db->query("rollback");
      return FALSE;
    }

     /* 非紹介者から参照される関係を登録 */
      $sql = sprintf("INSERT INTO plm_member_relation (parent,child,relation) VALUES (%d, %d, '%s')" ,$child_uid , $parent_uid, $relation );
     $this->db->query($sql);
    if (!$this->db->affected_rows()) {
      $this->db->query("rollback");
      return FALSE;
    }


    /*
     * plm_groupテーブルをアップデート
     * 非紹介者のggidをもつ全てのグループのggidを紹介者のggidにアップデート
     */
    //****************************************
    // サーバーを変更後、PostgreSQLのバージョンが下がったため、
    // UPDATE の SET句に副問い合わせを使用できなくなったため、変更
    //****************************************
    //<変更前 日付="2002/11/02">
      //     $sql = sprintf("UPDATE plm_group SET ggid = (SELECT ggid FROM plm_group WHERE gid = %d) " . 
      //            "  , up_date = now() " . 
      //     " WHERE ggid = (SELECT ggid FROM plm_group PG, plm_member PM WHERE PM.uid = %d AND PG.gid = PM.gid)" 
      // , $parent_gid, $child_uid );
      //
      //     $this->db->query($sql);
      //    if (!$this->db->affected_rows()) {
      //      $this->db->query("rollback");
      //      return FALSE;
      //    }
    //</変更前>
    
    //</変更後 変更者="岡本">
    
      //テーブルロック(plm_group)
      $this->db->query("LOCK TABLE plm_group IN SHARE ROW EXCLUSIVE MODE");
      
      //紹介者のggidを求める      
      $sql = sprintf("SELECT ggid FROM plm_group WHERE gid = %d " , $parent_gid ); 
      $this->db->query($sql);
      if ( $this->db->next_record() ) {
        $parent_ggid = $this->db->f('ggid');
      } else {
        $this->db->query("rollback");
        return FALSE;
      }
      
      //更新SQL
      //UPDATE plm_group SET ggid = (紹介者のggid) WHERE ggid = (非紹介者のggid)
       $sql = sprintf("UPDATE plm_group SET ggid = %d " . 
                      "  , up_date = now() " . 
                      " WHERE ggid = (SELECT ggid FROM plm_group PG, plm_member PM WHERE PM.uid = %d AND PG.gid = PM.gid)" 
              , $parent_ggid, $child_uid );
    
       $this->db->query($sql);
      if (!$this->db->affected_rows()) {
        $this->db->query("rollback");
        return FALSE;
      }
    //</変更後>






      /*
       * 仮登録情報を削除
       */
     $sql = sprintf("DELETE FROM temp_plm_member_relation WHERE id = '%s'", $link_id );
     $this->db->query($sql);


    $this->db->query("COMMIT");

   return TRUE;

  }


  /**
   * つながり情報の仮登録処理(リンクメール時)
   * @param $linkInfo リンク情報オブジェクト
   * @return 結果の真偽(boolean)
   */
  function linkmailTrans($linkInfo) {
    /*
     * temp_plm_member_relationテーブルにINSERT
     */
    $link_id = $this->SQLEscape($linkInfo->link_id);
    $parent = $this->SQLEscape($linkInfo->parent_uid);
    $child = $this->SQLEscape($linkInfo->child_uid);
    $relation = $this->SQLEscape($linkInfo->relation);
    $child_name = $this->SQLEscape($linkInfo->child_name);
    $link_type = $this->SQLEscape($linkInfo->link_type);

    $sql = sprintf("INSERT INTO temp_plm_member_relation " . 
                   " (id,parent,child,relation,child_name,link_type) " . 
                   " VALUES " . 
                   " ( '%s',%d,%d,'%s','%s',%d ) " 
                   ,$link_id,$parent,$child,$relation,$child_name,$link_type );
     $this->db->query($sql);
    if (!$this->db->affected_rows()) {
      $this->db->query("rollback");
      return FALSE;
    }

    return TRUE;
  }


  /**
   * マップのアイコンの位置情報をDBに保存する
   * @param $uid 保存対象のマップの中心のuid
   * @param $data 位置情報データ(uid1=x,y&uid2=x,y&uid3=x,y&)
   * @return 結果の真偽(boolean)
   */
  function saveIconPositionTrans($uid, $data) {
     $uid = $this->SQLEscape(sprintf("%d", $uid));
     $data = $this->SQLEscape($data);


    $updateFlag = false;
     $sql = sprintf("SELECT COUNT(*) AS count FROM icon_position " . 
                   " WHERE uid = %d" ,$uid);
    $this->db->query($sql);
    if ($this->db->next_record()) {
       if ($this->db->f('count') > 0) { $updateFlag = TRUE; }
    }

    if ($updateFlag) { //更新
      $sql = sprintf("UPDATE icon_position  SET  " . 
                     " position_list = '%s', " . 
                     " up_date = NOW() " . 
                     " WHERE " . 
                     "  uid = %d  " ,$data, $uid );
    } else {//挿入
      $sql = sprintf("INSERT INTO icon_position " . 
                     " (uid, position_list) " . 
                     " VALUES " . 
                     " ( %d, '%s' ) " ,$uid, $data);
    }

     $this->db->query($sql);
    if (!$this->db->affected_rows()) {
      $this->db->query("rollback");
      return FALSE;
    }

  return TRUE;
  }




  /**
   * 登録データの変更処理
   * リンクデータも含む
   * @param $request HTTPリクエストパラメータ
   * @param $linkInfo リンク情報オブジェクト
   * @param $auth 認証オブジェクト
   * @return 結果の真偽(boolean)
   *
   */
  function changeTrans($request, $linkInfo, $auth) {

    /*
     * トランザクションを開始
     */
    $this->db->query("BEGIN");
    $uid = $auth->auth["uid"];


    /*
     * plm_memberテーブルをUPDATE
     */
    $uid = $this->SQLEscape($uid);
    $login_id = $this->SQLEscape($request["login_id"]);
    $passwd = $this->SQLEscape($request["passwd"]);
    $name = $this->SQLEscape($request["name"]);
    $sex = $this->SQLEscape($request["sex"]);
    $blood = $this->SQLEscape($request["blood"]);
    $birthday = $this->SQLEscape($request["birthday"]);
    $prefectures = $this->SQLEscape($request["prefectures"]);
    $job = $this->SQLEscape($request["job"]);
    $interest = $this->SQLEscape($request["interest"]);
    $email = $this->SQLEscape($request["email"]);
    $email_flg = $this->SQLEscape($request["email_flg"]);
    $web = $this->SQLEscape($request["web"]);
    $sql = sprintf("UPDATE plm_member SET " . 
                   " login_id = '%s'," . 
                   " passwd = '%s', " . 
                   " name = '%s', " . 
                   " sex = '%s', " . 
                   " blood = '%s', " . 
                   "birthday = '%s', " . 
                   "prefectures = '%s', " . 
                   "job = '%s', " . 
                   "interest = '%s', " . 
                   "email = '%s', " . 
                   "email_flg = '%s', " . 
                   "web = '%s' " . 
                   " WHERE uid = %d " , $login_id, $passwd, $name, $sex, $blood, $birthday, $prefectures, $job, $interest, $email, $email_flg, $web, $uid );
     $this->db->query($sql);

    if (!$this->db->affected_rows()) {
      $this->db->query("rollback");
      writeLog("rollback : " . $sql);
      return FALSE;
    }

    /*
     * plm_member_keyword(keyword)テーブルから削除
     */
    $sql = sprintf("DELETE FROM plm_member_keyword WHERE uid = %d ", $uid);
     
     $this->db->query($sql);
    if (!$this->db->affected_rows()) {
      $this->db->query("rollback");
      writeLog("rollback : " . $sql);
      return FALSE;
    }

    /*
     * plm_member_keyword(keyword)テーブルにINSERT
     */
    $keywordList = array();
    $sqlList = array();

    for($i=1; $i<9; $i++) { //loop
      $key = "keyword" . $i;
      if ($request[$key] != '') {
        ${$key} = $this->SQLEscape($request[$key]);
        $keywordList[] = ${$key};
      }
    } //loop
    /* SQL文を作成 */
    $keywordList = array_unique($keywordList); //add 20020825
    for($i=0; $i<count($keywordList); $i++) { //loop
        $sqlList[$i] = sprintf("INSERT INTO plm_member_keyword " . 
                    " (uid, keyword) " . 
                    " VALUES " . 
                    " (%d, '%s') ", 
                     $uid, $keywordList[$i] );
    } //loop


    /* キーワードの登録 */
    for($i=0; $i<count($sqlList); $i++) { //loop
      if ($sqlList[$i] != '') {
        
        $this->db->query($sqlList[$i]);
        if (!$this->db->affected_rows()) {
          $this->db->query("rollback");
          writeLog("rollback : " . $sql);
          return FALSE;
        }
      }
    } //loop


    /*
     * つながり情報を更新(plm_member_relationテーブルをUPDATE)
     */
      for ($i=0; $i<count($linkInfo); $i++) { //loop
        $icon = $linkInfo[$i];
        $parent = $uid;
        $child=$this->SQLEscape($icon->uid);
        $relation=$this->SQLEscape($icon->relation);

          $sql = sprintf("UPDATE plm_member_relation SET " . 
                         "relation = '%s' " . 
                         " WHERE parent = %d " . 
                          " AND child = %d " , $relation, $parent, $child );
   
         $this->db->query($sql);
         if (!$this->db->affected_rows()) {
            $this->db->query("rollback");
            writeLog("rollback : " . $sql);
            return FALSE;
         }
    } //loop


    /*
     * トランザクションをコミット
     */
    $this->db->query("COMMIT");


    return TRUE;
}


  /**
   * つながっているすべての人のuidのリストを返す(デバッグ用)
   * @param $ggid 自身のggid
   * @return uidのリスト
   */
  function getLinkUidList($ggid) {
    $ggid = sprintf("%d", $ggid );
    $linkUidList = array();
    $sql = " SELECT " . 
           " PM.uid AS uid " . 
           " FROM " . 
           "plm_member PM, " . 
           "plm_group PG " . 
           " WHERE " . 
           " PG.ggid = $ggid " . 
           " AND PM.gid = PG.gid ";
    $this->db->query($sql);
    while ($this->db->next_record()) {
      $linkUidList[] = $this->db->f('uid');
    }
    return $linkUidList;
  }


  /**
   * ID・パスワード検索
   * @param email
   * @return login_id,passwd
   */
  function getID_PW($input_email) {
  
    $input_email = $this->SQLEscape($input_email); //岡本追加 20020917
    
    $result = array();
    $sql = " SELECT " . 
           " name,login_id,passwd " . 
           " FROM " . 
           "plm_member " . 
           " WHERE " . 
           " email = '$input_email' ";
    $this->db->query($sql);
    if ($this->db->next_record()) {
      $result["name"] = $this->db->f('name');
      $result["id"] = $this->db->f('login_id');
      $result["pw"] = $this->db->f('passwd');
    }
    return $result;
  }

  function getSexMaster() {  
    return array("man"=>"男",
                 "woman"=>"女");
  }


  function getBloodMaster() {  
    return array("A",
                 "B", 
                 "O", 
                 "AB");  
  }


  function getJobMaster() {  
    return array(
                 "社会人・OL", 
                 "主婦", 
                 "大学・専門学校", 
                 "中・高生", 
                 "小学生", 
                 "その他");
  }



  function getPrefecturesMaster() {  
    return array(
                   "北海道", 
                   "青森県", 
                   "岩手県", 
                   "宮城県", 
                   "秋田県", 
                   "山形県", 
                   "福島県", 
                   "茨城県", 
                   "栃木県", 
                   "群馬県", 
                   "埼玉県", 
                   "千葉県", 
                   "東京都", 
                   "神奈川県", 
                   "新潟県", 
                   "富山県", 
                   "石川県", 
                   "福井県", 
                   "山梨県", 
                   "長野県", 
                   "岐阜県", 
                   "静岡県", 
                   "愛知県", 
                   "三重県", 
                   "滋賀県", 
                   "京都府", 
                   "大阪府", 
                   "兵庫県", 
                   "奈良県", 
                   "和歌山県", 
                   "鳥取県", 
                   "島根県", 
                   "岡山県", 
                   "広島県", 
                   "山口県", 
                   "徳島県", 
                   "香川県", 
                   "愛媛県", 
                   "高知県", 
                   "福岡県", 
                   "佐賀県", 
                   "長崎県", 
                   "熊本県", 
                   "大分県", 
                   "宮崎県", 
                   "鹿児島県", 
                   "沖縄県");
  }






}
?>