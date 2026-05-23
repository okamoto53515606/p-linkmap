<?php



/*
 * 共通で利用するグローバル変数
 */
$BASE_URL = "http://www.p-linkmap.com";
$CONTACT_MAIL = "support@p-linkmap.com";
$USER_HOME = "/home/user/danzj000";

/**
 * エラー画面の表示
 */
function error($title, $message) {
  global $_ENV;
  global $USER_HOME;
  $response = array();
  $response["title"] = $title;
  $response["message"] = $message;
  include($USER_HOME . "/web/jp/error.ihtml");
  exit;
}

/**
 * ASII文字の判別
 */
function isASII($string) {
  //$b = unpack("C*", $string);
  //$len = count($b);
  //for ($i=0; $i<$len; $i++) {
  //  if ($b[$i] > 0x7E) { return FALSE; }
  //}
  if (!ereg("[\x80-\xFF]", $string)) return TRUE;
  return FALSE;
}

/**
 * E-Mailの形式チェック
 */
function isEmail($string) {

//ereg("^[0-9a-zA-Z\.\-\_]+\@[0-9a-zA-Z\.\-]+\.[0-9a-zA-Z\-]*$", $string, $REGS)

  if (ereg("^.+\@.+\..*$", $string, $REGS)) {
    return TRUE;
  } else {
    return FALSE;
  }
  
}


/**
 * E-Mailの送信
 */
function plm_mail($from, $to, $subject, $body) {

  $xtra = "From: " . $from . "\r\n";

  //mb_language("Japanese");
  mb_send_mail($to, $subject, $body, $xtra);
  //mb_send_mail("webmaster@p-linkmap.com", "デバッグメール : " . $subject, $body, $xtra);
  
}

/**
 * デバッグ用出力
 */
function debug($string) {
  // echo $string . "\n";
}

/**
 * ログファイルに記録する
 */
function writeLog($string) {
  global $USER_HOME;
  global $_ENV;
  global $_SERVER;
  
  $env = $_SERVER;

  $string = "DATE=" . date("Y/m/d(D) H:i:s") . "\n" . 
            "HTTP_REFERER=" . $env["HTTP_REFERER"] . "\n" . 
            "HTTP_USER_AGENT=" . $env["HTTP_USER_AGENT"] . "\n" . 
            "REMOTE_ADDR=" . $env["REMOTE_ADDR"] . "\n" . 
            "HTTP_COOKIE=" . $env["HTTP_COOKIE"] . "\n" . 
            "REQUEST_METHOD=" . $env["REQUEST_METHOD"] . "\n" . 
            "REQUEST_URI=" . $env["REQUEST_URI"] . "\n" . 
            "--------------------------------\n" . 
            $string . "\n" . 
            "--------------------------------\n";

  $fileName = date("Ym") . ".phplog";
  $fp = fopen($USER_HOME . "/applog/" . $fileName, "a");

  flock($fp, LOCK_EX);
  fputs($fp, $string);
  fclose($fp);

}


?>
