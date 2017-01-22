
resource "aws_instance" "inst" {
  ami           = "${var.aws_base_ami_id}"
  instance_type = "${var.aws_base_instance_type}"
  key_name 		= "keyName"
  }

output "instance_id" {
  value = "${aws_instance.inst.id}"
}

	
	

