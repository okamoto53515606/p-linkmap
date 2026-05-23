<?php

/**
 * 登録者情報オブジェクト
 *
 */
class plm_member {

  /**
   * ユーザーID
   */
  var $uid;

  /**
   * ログインID
   */
  var $login_id;

  /**
   * パスワード
   */
  var $passwd;

  /**
   * 名前
   */
  var $name;

  /**
   * 性別
   */
  var $sex;

  /**
   * 血液型
   */
  var $blood;

  /**
   * 生年月日
   */
  var $birthday;

  /**
   * 都道府県
   */
  var $prefectures;

  /**
   * 職業
   */
  var $job;

  /**
   * 趣味
   */
  var $interest;

  /**
   * キーワード
   */
  var $keyword;

  /**
   * メールアドレス
   */
  var $email;

  /**
   * メール公開フラグ
   */
  var $email_flg;

  /**
   * ＨＰアドレス
   */
  var $web;

  /**
   * 利用可能かどうか判別するためのフラグ
   */
  var $valid;

  /**
   * NULLフラグ
   */
  var $nulls = array(
                     "uid" => false,
                     "login_id" => false,
                     "passwd" => false,
                     "name" => false,
                     "sex" => false,
                     "blood" => false,
                     "birthday" => false,
                     "prefectures" => false,
                     "job" => false,
                     "interest" => false,
                     "keyword" => false,
                     "email" => false,
                     "email_flg" => false,
                     "web" => true
                    );


  /**
   * コンストラクタ
   * 
   */
  function plm_member () {
    $this->uid = '';
    $this->login_id = '';
    $this->passwd = '';
    $this->name = '';
    $this->sex = '';
    $this->blood = '';
    $this->birthday = '';
    $this->prefectures = '';
    $this->job = '';
    $this->interest = '';
    $this->keyword = '';
    $this->email = '';
    $this->email_flg = '';
    $this->web = '';
    $this->valid = false;
  }


  /**
   * 列がNULLかどうかを返す
   * 
   */
  function isNull ($col) {
    if ($this->nulls[$col] == true) {
      return(true);
    } else {
      return(false);
    }
  }

  /**
   * 列にNULLをセットする
   * 
   */
  function setNull ($col) {
    $this->nulls[$col] = true;
  }

  /**
   * 列のNULLをリセットする
   * 
   */
  function resetNull ($col) {
    $this->nulls[$col] = false;
  }


  /**
   * デバッグ用
   * 
   */
  function debug () {
    print "uid = " . $this->uid . "\n";
    print "login_id = " . $this->login_id . "\n";
    print "passwd = " . $this->passwd . "\n";
    print "name = " . $this->name . "\n";
    print "sex = " . $this->sex . "\n";
    print "blood = " . $this->blood . "\n";
    print "birthday = " . $this->birthday . "\n";
    print "prefectures = " . $this->prefectures . "\n";
    print "job = " . $this->job . "\n";
    print "interest = " . $this->interest . "\n";
    print "keyword = " . $this->keyword . "\n";
    print "email = " . $this->email . "\n";
    print "email_flg = " . $this->email_flg . "\n";
    print "web = " . $this->web . "\n";
  }


}
?>