#!/usr/bin/perl 

#---------------------------------------------------------------------------------------
#This script provides various file IO Operations
#1.readLinesFromFile :
#               Usage: readLinesFromFile(file)
#				Reads the file and returns the file lines as a array
#
#2.writeLinesToFile :
#               Usage: writeLinesToFile(targetFile,lineArray)
#				Writes the passed lines to the specified file
#
#3.replaceToken :
#               Usage: replaceToken(targetFile,tokenValue,tokenRegex)
#				Reads a file , finds the specified regex and replaces it with the passed value
#				Example: replaceToken($targetFile,"123","AAA") :will replace all occurrence of AAA with 123
#
#4.deleteFile :
#               Usage: deleteFile(targetFile)
#				Deletes the specified file
#				Returns 1 if successful ,0 if failed
#
#5.renameFile :
#               Usage: renameFile(oldFile,newFile)
#				Renames the file
#
#---------------------------------------------------------------------------------------



sub readLinesFromFile{

  my $file=shift;
  open (IN, $file) || die "Cannot open file ".$file." for read";     
  my @lines=<IN>;
  close IN;
  
  return @lines;  
}


sub writeLinesToFile{
  my $fileName     = shift;
  my @lines        = @{$linesRef};
  
  open (OUT, ">", $fileName) || die "Cannot open file ".$fileName." for write";
  foreach my $line (@lines){  
    print OUT $line;
  }
  close OUT;
}


sub replaceToken{
  my $file         = shift;
  my $tokenValue   = shift;
  my $tokenRegex   = shift;

  open (IN, $file) || die "Cannot open file ".$file." for read";     
  my @lines=<IN>;
  close IN;
  
  open (OUT, ">", $file) || die "Cannot open file ".$file." for write";
  foreach my $line (@lines)
  {  
       $line =~ s/$tokenRegex/$tokenValue/;
    print OUT $line;
  }
  close OUT;
}


sub deleteFile{
  my $file =shift;
  if(-e $file) 
  {  
    unlink $file;
	print "Deleted file : $file";
	return 1;
  }
  
  print "Specified file: $file does not exist";
  return 0;
}


sub renameFile{
  my $currentfile=shift;
  my $newFile=shift;
  
  rename $currentfile, $newFile;
}
