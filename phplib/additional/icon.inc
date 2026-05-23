<?php

/**
 * アイコン情報オブジェクト
 *
 */
class icon {

  /**
   * ユーザーID
   */
  var $uid;

  /*
   * 名前
   */
  var $name;

  /**
   * 関係
   */
  var $relation;

  /**
   * X座標
   */
  var $x;

  /**
   * Y座標
   */
  var $y;

  /**
   * 状態
   * -1 : 削除ユーザーのアイコン
   *  0 : 普通のアイコン
   *  1 : 仮登録者のアイコン
   */
  var $type;

  /**
   * コンストラクタ
   * 
   */
  function icon () {
    $this->uid = '';
    $this->name = '';
    $this->ralation = '';
    $this->x = '';
    $this->y = '';
    $this->type = 0;
  }



  /**
   * デバッグ用
   * 
   */
  function debug () {
    print "uid = " . $this->uid . " ";
    print "name = " . $this->name . " ";
    print "relation = " . $this->relation  . " ";
    print "x = " . $this->x  . " ";
    print "y = " . $this->y  . "\n";
    print "type = " . $this->active  . "\n";
  }


}
?>