<?php

/**
 * リンク情報オブジェクト
 *
 */
class link_info {

  /**
   * 紹介者の名前
   */
  var $parent_name;
  /**
   * 紹介者のgid
   */
  var $parent_gid;
  /**
   * 紹介者のuid
   */
  var $parent_uid;
  /**
   * 非紹介者のuid
   */
  var $child_uid;
  /**
   * 非紹介者のuid
   */
  var $child_uid;
  /**
   * 非紹介者の名前
   */
  var $child_name;
  /**
   * 紹介者のE-Mail
   */
  var $parent_email;
  /**
   * 関係
   */
  var $relation;
  /**
   * リンクの種類
   */
  var $link_type;

  /**
   * 利用可能かどうか判別するためのフラグ
   */
  var $valid;
  /**
   * 仮登録ID
   */
  var $link_id;

  /**
   * コンストラクタ
   * 
   */
  function link_info () {
    $this->parent_name = '';
    $this->parent_gid = -1;
    $this->parent_uid = -1;
    $this->child_uid = -1;
    $this->child_name = '';
    $this->relation = '';
    $this->link_type = -1;
    $this->valid = false;
    $this->link_id = '';
    $this->parent_email = '';
  }




}
?>