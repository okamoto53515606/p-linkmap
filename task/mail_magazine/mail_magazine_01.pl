
#----------------------------
#�⥸�塼��������
#----------------------------

use lib('/home/sites/site4/perllib');
use DBUtil;
use Pg;
use strict;
use Jcode;
use Net::SMTP;
#use MIME::Entity;

require '/home/sites/site4/perllib/mimew.pl';


print "stop! \n";
exit(0);

#----------------------------
#�������Х��ѿ�
#----------------------------
my $con = DBUtil->new()->getDBCONNECTION();
my ($result);
my $TIME = time;
my $send_num = 0;
my $send_error_num = 0;
my $send_success_num = 0;

my $body = '
���ššššššššššššššššššššššššššššš�

   [P-linkmap] project

��http://www.p-linkmap.com

���ššššššššššššššššššššššššššššš�
����NEWS
   +  9/18 ��ǽ�ɲá��ǥ������˥塼���롪�ץ쥪���ץ�

���ššššššššššššššššššššššššššššš�
��P-linkmap�Τ����Ѥ��꤬�Ȥ��������ޤ���
��Plm�ץ��������ȳ��Ϥ�����3ǯ�ܡ�
�����褤�衢�ץ��ȥ����פǤ��ä��С�����󤫤�
��Plm�⾯���ŤĤǤϤ���ޤ��������ʤ�Ϥ���ޤ���

��[����Υ�˥塼����Point]
��*4�Ͱʾ�Τ�ͧã�ȤĤʤ���褦�ˤʤ�ޤ�����
��*�ޥåפΥ��������ư�����ơ����֤���¸�Ǥ���褦�ˤʤ�ޤ�����
��*�ޥåפ�é�äƤ��ä����򤬻Ĥ�ޤ�����
��*ID��ͳ������Ǥ���褦�ˤʤ�ޤ�����

  �����ʤ���map�ϥ����餫��
  http://www.p-linkmap.com/jp/member/map.php

���ššššššššššššššššššššššššššššš�
��[��Ͽ�����ѹ�]
��-> http://www.p-linkmap.com/jp/member/change.php

��[���䡦���䤤�礻]
��-> support@p-linkmap.com

��[�����ԤؤΥ᡼��]
��-> webmaster@p-linkmap.com


��Copyright (C) 2002 P-linkmap.All right reserved.
���ššššššššššššššššššššššššššššš�
';
my $subject = '[P-linkmap]MAIL_NEWS_00001';
$body = Jcode->new($body, 'euc')->jis;
$subject = &mimeencode($subject);
my $from = 'support@p-linkmap.com';






if ( -f "/home/www/danzj000/task/mail_magazine_01.log" ) {
  print "log file exist. \n";
  exit(0);
}


open(LOG, "> /home/www/danzj000/task/mail_magazine_01.log");


$result = $con->exec("SELECT name, email  FROM plm_member  WHERE delete_flg = false AND uid < 709 OFFSET 44 ");
&SelectErrorCheck($result,"SELECT name, email  FROM plm_member  WHERE delete_flg = false AND uid < 709 OFFSET 44 ");
$send_num = $result->ntuples;

my $oSmtp = Net::SMTP->new('localhost',
                           Debug   => 0,
                          );

for ( my $j = 0; $j < $result->ntuples; ++$j) { #loop1

	$oSmtp->mail($from);

	my $name = $result->getvalue($j,0) . ' ��';
	my $email = $result->getvalue($j,1);
	
	if ( !&isEmail($email) ) {
	  print LOG ("$j . $email -> skip \n");
	  next;
	}
	
	
	print $email . "\n";	

	my $DATA = "From: $from\n"
	         . "To: $email\n"
	         . "Subject: $subject\n"
	         . "MIME-Version: 1.0\n"
	         . "Content-type: text/plain; charset=\"ISO-2022-JP\"\n"
	         . "Content-Transfer-Encoding: 7bit\n"
	         . "\n"
	         . "$body\n";


	my $send_error_flag = '';	
	if (!$oSmtp->to($email)) { $send_error_flag++; }
	if (!$oSmtp->data()) { $send_error_flag++; }
	if (!$oSmtp->datasend($DATA)) { $send_error_flag++; }
	if (!$oSmtp->dataend()) { $send_error_flag++; }

	if ($send_error_flag) {
		$send_error_num++;
		print LOG ("$j . $email -> send error \n");
		
		
		$oSmtp->quit;
		$oSmtp = Net::SMTP->new('localhost',
                           Debug   => 0,
                          );
		
		
	} else {
		$send_success_num++;
		print LOG ("$j . $email -> send ok \n");
	}


	sleep(0.5);
	
	
	#if ($j == 5) { last; }		
	#if (($j % 100) == 0 && $j > 0) {
	#	sleep(10*1); #10�õٷ�
	#}
} #loop1
$oSmtp->quit;





$TIME = time - $TIME;
print LOG ("-------------------------- \n");
print LOG ("TIME = $TIME \n");
print LOG ("send num = $send_num \n");
print LOG ("success = $send_success_num \n");
print LOG ("error = $send_error_num \n");


print "-------------------------- \n";
print "TIME = $TIME \n";
print "send num = $send_num \n";
print "success = $send_success_num \n";
print "error = $send_error_num \n";


close(LOG);
exit(0);















sub isEmail {
    my $string = "";
    if (@_) { $string = shift @_; }
    else { return 0; }
    
    if ($string =~ /.+\@p-linkmap\.com/) { return 0;  }
    
    if ($string =~ /\b[-\w.]+@[-\w.]+\.[-\w]+\b/) {
        return 1;
    }
    return 0;
}

sub SelectErrorCheck() { #FUNCTION START

	my $Query_ID = shift;
	my $SQL = shift;
	if ($Query_ID->resultStatus ne PGRES_TUPLES_OK) {
		print "Query_ID->resultStatus ne PGRES_TUPLES_OK \n";
		print "$SQL \n";
		exit;
	}
	if ($Query_ID->ntuples == 0) {
		print "Query_ID->ntuples == 0 \n";
		print "$SQL \n";
		exit;
	}

} #FUNCTION END



__END__

