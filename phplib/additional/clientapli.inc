<?php

/**
 * クライアントアプリケーション(APLLET or FLASH)
 * に指定形式のデータを返すクラス
 */
class clientapli {


  /**
   * コンストラクタ
   * 
   */
  function clientapli () {
  }



  /**
   * 正常に終了したことを通知する
   * 
   */
  function printSuccess() {
    $data =<<<EOS
<STATUS>
code	TRUE
message	
</STATUS>

EOS;

    $this->printHeader();
    print $data;
  }



  /**
   * エラー出力
   * 
   */
  function printError($message) {

    $message = $this->getJpString($message);

    $data =<<<EOS
<STATUS>
code	FALSE
message	$message
</STATUS>

EOS;

    $this->printHeader();
    print $data;

  }



  function getMapData ($member, $iconList) {
  
    $uid = $this->getJpString($member->uid);
    $login_id = $this->getJpString($member->login_id);
    $passwd = $this->getJpString($member->passwd);
    $name = $this->getJpString($member->name);
    $sex = $this->getJpString($member->sex);
    $blood = $this->getJpString($member->blood);
    $birthday = $this->getJpString($member->birthday);
    $prefectures = $this->getJpString($member->prefectures);
    $job = $this->getJpString($member->job);
    $interest = $this->getJpString($member->interest);
    $keyword = $this->getJpString($member->keyword);
    $email = $this->getJpString($member->email);
    $email_flg = $this->getJpString($member->email_flg);
    $web = $this->getJpString($member->web);
    $gid = $this->getJpString($member->gid);
    $in_date = $this->getJpString($member->in_date);
    $up_date = $this->getJpString($member->up_date);

    $data =<<<EOS
<STATUS>
code	TRUE
message	
</STATUS>
<USERINFO>
name	$name
sex	$sex
blood	$blood
birthday	$birthday
prefectures	$prefectures
job	$job
interest	$interest
keyword	$keyword
email	$email
web	$web
</USERINFO>
<ICONLIST>

EOS;


    for ($i=0;$i<count($iconList);$i++) { //loop
      $icon = $iconList[$i];

      $uid = $icon->uid;
      $name = $this->getJpString($icon->name);
      $relation = $this->getJpString($icon->relation);
      $x = $icon->x;
      $y = $icon->y;
      $type = $icon->type;


    $data .=<<<EOS
$uid	$name	$relation	$type	$x	$y

EOS;
    } //loop

    $data .=<<<EOS
</ICONLIST>

EOS;

    return $data;
  }







  /**
   * マップデータ出力
   * 
   */
  function printMapData($member, $iconList) {
    $this->printHeader();
    $data = $this->getMapData($member, $iconList);
    print $data;
  }

  /**
   * 検索マップデータ出力
   * 
   */
  function printSearchMapData($member, $iconList, $searchParam) {
    $this->printHeader();
    $data = $this->getMapData($member, $iconList);
    
    
    
    
    $jokenList = array();
    if ($searchParam["prefectures"] != "") {
      $jokenList[] = $searchParam["prefectures"];
    }
    if ($searchParam["keyword"] != "") {
      $jokenList[] = $searchParam["keyword"];
    }
    
    $JOKEN = join(" ", $jokenList);
    $JOKEN = $this->getJpString($JOKEN);
    
    $data .=<<<EOS
<SEARCHPARAM>
JOKEN	$JOKEN
</SEARCHPARAM>

EOS;
    
    
    print $data;
    
  }









  function printHeader() {
    //$header = "Content-type: text/plain\n" . 
    //          "Expires: Sat, 05 May 2001 16:00:00:00 GMT\n" . 
    //          "Cache-Control: \"no-cache\"\n" . 
    //          "\n";
    $header = "Content-type: text/plain\n" . 
              "\n";
    Header($header);
  }




  function euc2sjis($string) {
    return mb_convert_encoding($string, "SJIS", "EUC-JP");
  }


  function getJpString($string) {
    return $string;
    //return $this->euc2sjis($string);
  }









}

?>