
#----------------------------
#モジュールをロード
#----------------------------
use lib('/home/user/danzj000/perllib');
use DBUtil;
use DBI;
use strict;

#----------------------------
#グローバル変数
#----------------------------
my $TIME = time;
my $con = DBUtil->new()->getDBCONNECTION();
my $result;
my $sql = '';
my $oSTH;
my $nCnt = 0;

print '*** tpmr_delete.pl start ***' . "\n";

$sql = "DELETE FROM temp_plm_member_relation" . 
       " WHERE in_date < CURRENT_TIMESTAMP + '-1 months -1 day'";

eval {
$oSTH = $con->prepare($sql);
$nCnt = $oSTH->execute;
};
if ($@) {
  print 'error: ' . $@ . "\n";
  print 'sql: ' . $sql . "\n";
} else {
  print 'delete: ' . $nCnt . "\n";
}

$TIME = time - $TIME;
print 'time: ' . $TIME . "\n";
print '*** tpmr_delete.pl end   ***' . "\n";
exit(0);
