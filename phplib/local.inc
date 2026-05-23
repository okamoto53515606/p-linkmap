<?php


class PLM_DB_Sql extends DB_Sql {
  var $Host = "";
  var $Database = "danzj000"; 
  var $User     = "danzj000"; 
  var $Password = ""; 
  var $Auto_Free = 1;
  
  
  function connect() {
    if ( 0 == $this->Link_ID ) {
      $cstr = "dbname=".$this->Database.
      $this->ifadd($this->Host, "host=").
      $this->ifadd($this->Port, "port=").
      $this->ifadd($this->User, "user=").
      $this->ifadd($this->Password, "password=");
      $this->Link_ID=pg_connect($cstr);


      if (!$this->Link_ID) {
        $this->halt("Link-ID == false, pg_connect failed");
      }
      //pg_Exec($this->Link_ID, "SET CLIENT_ENCODING TO 'SJIS'");
      pg_Exec($this->Link_ID, "SET CLIENT_ENCODING TO 'EUC_JP'");
      pg_Exec($this->Link_ID, "SET DATESTYLE TO 'ISO'");
    }
  }

}


/*
class PLM_CT_File extends CT_File {
  var $file_path = "/home/user/danzj000/phplib/session_files/";
}
*/

class PLM_CT_Sql extends CT_Sql {
  var $database_class = "PLM_DB_Sql";          ## Which database to connect...
  var $database_table = "active_sessions"; ## and find our session data in this table.
}



class sessionId extends Session {
  var $classname = "sessionId";

  var $cookiename     = "";                ## defaults to classname
  var $magic          = "X8)V&9ey";      ## ID seed
  var $mode           = "cookie";          ## We propagate session IDs with cookies
  var $fallback_mode  = "get";
  var $lifetime       = 0;                 ## 0 = do session cookies, else minutes
  var $that_class     = "PLM_CT_Sql"; ## name of data storage container
  var $gc_probability = 5;  
  var $allowcache = "no";        ## "passive", "no", "private", "public"
}



class PLM_Auth extends Auth {

  var $classname      = "PLM_Auth";

  var $lifetime       =  0; 

  var $magic          = "X8)V&9ey";  ## Challenge seed
  var $database_class = "PLM_DB_Sql";
  var $database_table = "plm_member"; 

  function auth_loginform() {
    global $sess;
    global $challenge;
    global $regist_id;

    include("/home/user/danzj000/web/jp/member/login.ihtml");
  }


  function auth_validatelogin() {
    global $username, $password, $challenge, $response;

    $this->auth["uname"]=$username;        ## This provides access for "loginform.ihtml"
    
    $this->db->query(sprintf("SELECT PM.uid AS uid, PM.name AS name, PM.passwd AS passwd, PG.ggid AS ggid, PM.delete_flg AS delete_flag ".
                " FROM %s PM, plm_group PG WHERE PM.login_id = '%s' AND PG.gid = PM.gid ",
                          $this->database_table,
                          addslashes($username)));

    while($this->db->next_record()) {
      if ( $this->db->f("delete_flg") == 't' ) { return false; }
      $jpname = $this->db->f("name");
      $uid = $this->db->f("uid");
      $ggid = $this->db->f("ggid");
      $pass = $this->db->f("passwd");   ## Password is stored as a plain text
    }



      if ($password != $pass) {       ## md5 hash for non-JavaScript browsers
        return false;
      } else {
        $this->auth["jpname"] = $jpname;
        $this->auth["ggid"] = $ggid;
        return $uid;
      }
  }
}



/*
 * for sample map
 */
class PLM_Auth2 extends PLM_Auth {
  var $classname      = "PLM_Auth2";
  function auth_loginform() {
    global $sess;
    global $challenge;
    global $regist_id;
    include("/home/user/danzj000/web/jp/member/samplelogin.ihtml");
  }
}



?>
