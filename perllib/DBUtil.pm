package DBUtil;

use DBI;

sub new {
  my $class = shift;
  my $self = bless {
      DBCONNECTION => '',
      }, $class;
  $self->_initialize();
  $self;
}

sub setDBCONNECTION  {
  my $self = shift;
  if (@_) { $self->{DBCONNECTION} = shift; }
  return $self->{DBCONNECTION};
}

sub getDBCONNECTION {
  my $self = shift;
  return $self->{DBCONNECTION};
}


sub _initialize {
  my $self = shift;
  # 
  # DB CONNECTION PARAMETER
  # 
  my $DB_SERVER_NAME = "";
  my $DB_SERVER_PORT = "";
  my $DB_NAME = "";
  my $DB_USER = "";
  my $DB_PASSWORD = "";

  $self->{DBCONNECTION} = DBI->connect("dbi:Pg:dbname=$DB_NAME", 
							"$DB_USER", 
							"", 
							{ PrintError=>1, RaiseError=>1, AutoCommit=>1 });
}


# ---------------------
# Example for use
# ---------------------
# use DBUtil;
# my $con = DBUtil->new()->getDBCONNECTION();
# 





return 1;

