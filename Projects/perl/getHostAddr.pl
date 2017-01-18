	
#!/usr/bin/perl 

#---------------------------------------------------------------------------------------
#This routine fetches the Host address
#---------------------------------------------------------------------------------------

use Socket;
use Sys::Hostname;
my $host = hostname();
my $addr = inet_ntoa(scalar(gethostbyname($host)));
my $addr2 = inet_ntoa(scalar(gethostbyname("localhost")));

printf "Hostname=$host\n"; 
printf "ADDR=$addr";
printf "ADDR_LOCALHOST=$addr2";